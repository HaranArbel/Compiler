package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;
import MIPS.sir_MIPS_a_lot;

import java.util.ArrayList;
import java.util.List;


public class AST_DEC_CLASSDEC extends AST_DEC
{
    public String parentName;
    public String idName;
    public AST_C_FIELD_LIST dataMembers;
    public AST_C_FIELD_LIST methods;
    public AST_C_FIELD_LIST fields;
    private SYMBOL_TABLE st;
    public int lineNumber;

    public String vTableLabel;
    public List<String> vTable;
    public List<String> varDefaults;
    private TYPE_CLASS classType;
    public static int fieldsCounter = -1;
    public String varInitLabel;
    public String classInitLabel;


    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_DEC_CLASSDEC(String idName, String parentName, AST_C_FIELD_LIST fields, int lineNumber) {

        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();
        
        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== dec -> classDec\n");
        
        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.parentName = parentName;
        this.idName = idName;
        this.fields = fields;
        this.st = SYMBOL_TABLE.getInstance();
        this.lineNumber = lineNumber;

        this.vTable = new ArrayList<>();
        this.varDefaults = new ArrayList<>();

        separateDataMembersAndMethods(fields);
    }

    public Boolean isDataMember() {
        return true;
    }



    /* The default message for an dec classDec AST node */

    /***********************************************/
    public void PrintMe()
    {
        /************************************/
        /* AST NODE TYPE = DEC CLASSDEC AST NODE */
        /************************************/
        System.out.print("AST NODE DEC CLASSDEC\n");

        /*****************************/
        /* RECURSIVELY PRINT classDec ... */
        /*****************************/
        if (dataMembers != null) dataMembers.PrintMe();
        if (methods != null) methods.PrintMe();

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        if (parentName!=null)
        {
            AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("CLASS %s EXTENDS %s",idName,parentName));
        }
        else{
            AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("CLASS %s",idName));
        }
        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/

        if (dataMembers != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, dataMembers.SerialNumber);
        if (methods != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, methods.SerialNumber);
    }



    /***********************************************/
    /********************************************/
    public TYPE SemantMe() throws SemantMeException{

        /// find all classes methods in the parents to set vTableOffset
        List<TYPE_CLASS> parentClasses = new ArrayList<>();


        
        /* [0] Make sure that the class is declared for the first time */
        if(!this.st.IsVarNameNew(idName,true))
            throw new SemantMeException(lineNumber,"The name "+idName+" aleady exists in the symbol table!\n");


        /* [1] Make sure the class extends a previously defined class */
        TYPE parent = null;
        if (parentName != null) {
            parent = this.st.find(parentName);
            if (parent == null || !parent.isClass()) {
                String info = (parent == null) ? "Parent name is not declared" : "Parent name is not a class";
                throw new SemantMeException(lineNumber, info);
            }
        }

        this.fieldsCounter = 0;

        
        TYPE_CLASS parentClass = (TYPE_CLASS) parent;
        TYPE_CLASS grandParent = parentClass;
        
        while (grandParent != null) {
            parentClasses.add(0, grandParent);
            grandParent = grandParent.father;
        }

        final int parentScopeCount = parentClasses.size();
        
        for (TYPE_CLASS curr : parentClasses) {
            st.beginScope();

            /// dataMembers
            TYPE_CLASS_VAR_DEC_LIST members = curr.dataMembers;
            while (members != null) {
                TYPE_CLASS_VAR_DEC member = (TYPE_CLASS_VAR_DEC) members.head;
                st.enter(member.name, member.t, member.context);
                fieldsCounter++;
                members = members.tail;
            }

            /// methods
            TYPE_LIST methods = curr.methods;
            while (methods != null) {
                TYPE_FUNCTION method = (TYPE_FUNCTION) methods.head;
                st.enter(method.name, method);

                // add all fathers methods, override if needed
                if (method.vTableOffset == vTable.size()) {
                    vTable.add(method.startLabel);
                } else {
                    vTable.set(method.vTableOffset, method.startLabel);
                }
                methods = methods.tail;
            }
        }


        /* [2] Begin Class Scope */
        this.st.beginScope();
        this.st.setCurrParentClass(parentClass);

        this.varInitLabel = IRcommand.getFreshLabel(String.format("%s_fields", this.idName));
        this.vTableLabel = IRcommand.getFreshLabel(String.format("%s_funcs", this.idName));
        this.classInitLabel = IRcommand.getFreshLabel(String.format("%s", this.idName));
  

        /* [3] Enter current class for recursive references. Will be removed when scope ends.  */
        /* For example - when implemnting a comparator between two instances of the same class */
        TYPE_CLASS currClass = new TYPE_CLASS(parentClass, idName, null, null, vTableLabel, varInitLabel,
                classInitLabel, varDefaults);

        this.classType = currClass;


        this.st.enter(idName, currClass);



        /* [4] Semant data members */
        if (dataMembers != null) {

            AST_C_FIELD_LIST dataMembers_pointer = dataMembers;

            //will be used for appending TYPE_CLASS.DATAMEMBER at the end
            TYPE_CLASS_VAR_DEC_LIST fieldsTail = null;

            do{
                AST_C_FIELD_VARDEC fieldVar = (AST_C_FIELD_VARDEC) dataMembers_pointer.head;
                TYPE_CLASS_VAR_DEC fieldMember  = fieldVar.SemantBody();

                // Now we add its initial value to the list: 0 is default for INT and NIL
                String initialValue = "0";
                AST_EXP initExp = fieldVar.vDec.exp;
                if (initExp != null) {

                    // Can only be nil, string or constant int.
                    if (initExp instanceof AST_EXP_STRING) {
                        initialValue = ((AST_EXP_STRING) initExp).str_label;

                    } else if (initExp instanceof AST_EXP_INT) {
                        initialValue = String.valueOf(((AST_EXP_INT) initExp).value);
                    }
                }
                varDefaults.add(initialValue);
                TYPE_CLASS_VAR_DEC_LIST newNode = new TYPE_CLASS_VAR_DEC_LIST(fieldMember, null);
                if (currClass.dataMembers == null) {
                    currClass.dataMembers = newNode;
                } else {
                    fieldsTail.tail = newNode;
                }
                fieldsTail = newNode;



                dataMembers_pointer = dataMembers_pointer.tail;
            } while ( dataMembers_pointer != null );
        }


        /*[4.5] semant methods*/

        /* Semant Signature */
        if (methods != null) {

            TYPE_FUNCTION function = (TYPE_FUNCTION) methods.head.SemantSignature();
            function.belongsToClass = classType;
            classType.AddMethod(function);

            TYPE_FUNCTION parentFunc = null;
            if (parentClass != null){
                parentFunc = st.getCurrParentClass().findMethod(function.name);
                if (parentFunc != null){ //override!
                    function.vTableOffset = parentFunc.vTableOffset;
                    vTable.set(function.vTableOffset, function.startLabel);
                }
            }
            if (parentClass == null || parentFunc == null) { //no override
                function.vTableOffset = vTable.size();
                vTable.add(function.startLabel);
            }

            for (AST_C_FIELD_LIST list = methods.tail; list != null; list = list.tail) {
                // Call semantMe on current C_FIELD and store its returned type in class's methods list.
                function = (TYPE_FUNCTION) list.head.SemantSignature();
                function.belongsToClass = classType;
                classType.AddMethod(function);
            

                parentFunc = null;
                if (parentClass != null){
                    parentFunc = st.getCurrParentClass().findMethod(function.name);
                    if (parentFunc != null){ //override!
                        function.vTableOffset = parentFunc.vTableOffset;
                        vTable.set(function.vTableOffset, function.startLabel);
                    }
                }
                if (parentClass == null || parentFunc == null) { //no override
                    function.vTableOffset = vTable.size();
                    vTable.add(function.startLabel);
                }
            }
        }
        // Semant Body
        if (methods != null) {
            methods.head.SemantBody();
            for (AST_C_FIELD_LIST list = methods.tail; list != null; list = list.tail) {
                // Call semantMe on current C_FIELD and store its returned type in class's methods list.
                list.head.SemantBody();
            }
        }




    /* [7] End Scope */
        this.st.setCurrParentClass(null);
        this.st.endScope();
        fieldsCounter = -1; // init for next class
        
        // end father scopes
        for (int i = 0; i < parentScopeCount; i++) {
            st.endScope();
        }

        /* [8] Enter the Class Type to the Symbol Table */
        this.st.enter(idName, currClass);

        sir_MIPS_a_lot.vtables.put(this.vTableLabel, vTable);

        if (parentClass != null) {
            varDefaults.addAll(0, parentClass.varInitValues);
        }
        sir_MIPS_a_lot.classInitValues.put(varInitLabel, varDefaults);

        return currClass; 
    }


    ////////////////////////////////

    private void separateDataMembersAndMethods(AST_C_FIELD_LIST fields){
        AST_C_FIELD_LIST lastDataMember = null;
        AST_C_FIELD_LIST lastMethod = null;
        AST_C_FIELD_LIST list = fields;
        
        while(list != null){
            if (list.head.isDataMember()){
                if (lastDataMember == null){
                    lastDataMember = new AST_C_FIELD_LIST(list.head, null, list.lineNumber);
                    this.dataMembers = lastDataMember;
                }
                else{
                    lastDataMember.tail = new AST_C_FIELD_LIST(list.head, null, list.lineNumber);
                    lastDataMember = lastDataMember.tail;
                }
            }
            else{
                if (lastMethod == null){
                    lastMethod = new AST_C_FIELD_LIST(list.head, null, list.lineNumber);
                    this.methods = lastMethod;
                }
                else{
                    lastMethod.tail = new AST_C_FIELD_LIST(list.head, null, list.lineNumber);
                    lastMethod = lastMethod.tail;
                }
            }
            list = list.tail;
        }
    }

    public TEMP IRme() 
    {
        //IR methods
        AST_C_FIELD_LIST methods = this.methods;       
        while (methods != null) 
        {
            methods.head.IRme();
            methods = methods.tail;
        }

        Runnable irBody = () -> { IR.getInstance().Add_IRcommand(new IRcommand_Constructor(classInitLabel + "_end", classType)); };
        AST_F_DEC.IRFuncDec(irBody, classInitLabel, 0, classInitLabel + "_end", false, false);

        return null;
    }
    
}

