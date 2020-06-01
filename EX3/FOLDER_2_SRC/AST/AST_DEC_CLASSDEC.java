package AST;
//import ERRORS.SemantMeException;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;


public class AST_DEC_CLASSDEC extends AST_DEC
{
    public String parentName;
    public String idName;
    public AST_C_FIELD_LIST dataMembers;
    public AST_C_FIELD_LIST methods;
    public AST_C_FIELD_LIST fields;
    private SYMBOL_TABLE st;
    public int lineNumber;

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

        separateDataMembersAndMethods(fields);
    }

    public Boolean isDataMember() {
        return true;
    }


    /***********************************************/
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
    
    public TYPE SemantMe() throws SemantMeException{

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
        TYPE_CLASS parentClass = (TYPE_CLASS) parent;
        
        /* [2] Begin Class Scope */
        this.st.beginScope();
        this.st.setCurrParentClass(parentClass);
        
        /* [3] Enter current class for recursive references. Will be removed when scope ends.  */
        /* For example - when implemnting a comparator between two instances of the same class */
        TYPE_CLASS currClass = new TYPE_CLASS(parentClass, idName, null, null);
        this.st.enter(idName, currClass);
        
        /* [4] Semant data members */
        if (dataMembers != null) {
            currClass.AddDataMember((TYPE_CLASS_VAR_DEC) dataMembers.head.SemantBody());
            for (AST_C_FIELD_LIST list = dataMembers.tail; list != null; list = list.tail) {
                // Call semantMe on current C_FIELD and store its returned type in class's dataMembers list.
                currClass.AddDataMember((TYPE_CLASS_VAR_DEC) list.head.SemantBody());
            }
        }
        
        /* Semant Signature */
        if (methods != null) {
            currClass.AddMethod(methods.head.SemantSignature());
            for (AST_C_FIELD_LIST list = methods.tail; list != null; list = list.tail) {
                // Call semantMe on current C_FIELD and store its returned type in class's methods list.
                currClass.AddMethod(list.head.SemantSignature());
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
        
        /* [8] Enter the Class Type to the Symbol Table */
        this.st.enter(idName, currClass);

        /* [9] Return value is irrelevant for class declarations */
        return null;
        
        
    }
    
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
    
    
}

