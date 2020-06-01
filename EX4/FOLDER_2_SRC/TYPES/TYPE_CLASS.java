package TYPES;

import java.util.*;

public class TYPE_CLASS extends TYPE
{

	/*********************************************************************/
	/* If this class does not extend a father class this should be null  */
	/*********************************************************************/
	public TYPE_CLASS father;

	/**************************************************/
	/* Gather up all data members in one place        */
	/* Note that data members coming from the AST are */
	/* packed together with the class methods         */
	/**************************************************/
	public TYPE_CLASS_VAR_DEC_LIST dataMembers; 
	
    /**************************************************/
    /* Gather up all methods in one place        */
    /* Note that data members coming from the AST are */
    /* packed together with the class methods         */
    /**************************************************/
    public TYPE_LIST methods;

    //Added
    public String vTableLabel;
    public String varInitLabel;
    public String classInitLabel;
    public List<String> varInitValues;

	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_CLASS(
        TYPE_CLASS father, 
        String name,
        TYPE_CLASS_VAR_DEC_LIST dataMembers, 
        TYPE_LIST methods,
        String vTableLabel, 
        String varInitLabel, 
        String classInitLabel,
        List<String> varInitValues) 
	{
		this.name = name;
		this.father = father;
		this.dataMembers = dataMembers;
        this.methods = methods;
        this.vTableLabel = vTableLabel;
        this.varInitLabel = varInitLabel;
        this.classInitLabel = classInitLabel;
        this.varInitValues = varInitValues;
	}
    
    public boolean isClass() {
        return true;
    }
    
    /******************************************************************/
    /* 	      Checks if otherClass is a parent of this class.         */
    /******************************************************************/
    public boolean checkInheritance(String otherClass) {
        TYPE_CLASS currClass = this;
        while (currClass != null) {
            if (currClass.name.equals(otherClass)) {
                return true;
            }
            currClass = currClass.father;
        }
        return false;
    }
    
    /* Adds the new data member to the class's data members list */
    public void AddDataMember(TYPE_CLASS_VAR_DEC dataMember) {
        this.dataMembers = new TYPE_CLASS_VAR_DEC_LIST(dataMember, dataMembers);
    }
    
    /* Adds the new method to the class's methods list */
    public void AddMethod(TYPE method) {
        TYPE_LIST newTypeList = new TYPE_LIST(method, null);
        if(this.methods==null)
            this.methods = newTypeList;
        else {
            TYPE_LIST ptr = this.methods;
            while(ptr.tail!=null)
                ptr=ptr.tail;
            ptr.tail = newTypeList;
        }
    }
    
    /******************************************************************/
    /* searches for the data member in the class's data members list. */
    /* If the data member is not found, then we search for it in the  */
    /* parent class's data members list.                              */
    /******************************************************************/
    public TYPE findDataMember(String name) {
        return  findDataMember(name, true, false);
    }

    /******************************************************************/
    /* searches for the data member in the class's data members list. */
    /* If the data member is not found, then we search for it in the  */
    /* parent class's data members list.                              */
    /******************************************************************/
    public TYPE findDataMember(String name, boolean t_isNeeded, boolean head_isNeeded) {
        TYPE_CLASS currClass = this;
        while (currClass != null) {
            for (TYPE_CLASS_VAR_DEC_LIST list = currClass.dataMembers; list != null; list = list.tail) {
                if (list.head.name.equals(name)) {
                    if(t_isNeeded)
                        return list.head.t;
                    else
                        return list.head;
                }
            }
            currClass = currClass.father;
        }
        return null;
    }
    
    /*************************************************************/
    /* searches for the method in the class's methods list.      */
    /* If the method is not found, then we search for it in the  */
    /* parent class's methods list.                              */
    /*************************************************************/
    public TYPE_FUNCTION findMethod(String name) {
        TYPE_CLASS currClass = this;
        while (currClass != null) {
            for (TYPE_LIST list = currClass.methods; list != null; list = list.tail) {
                if (list.head.name.equals(name)) {
                    return (TYPE_FUNCTION) list.head;
                }
            }
            currClass = currClass.father;
        }
        return null;
    }
    
}

