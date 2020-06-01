package TYPES;

public abstract class TYPE
{
	/******************************/
	/*  Every type has a name ... */
	/******************************/
	public String name;

	/*************/
	/* isClass() */
	/*************/
	public boolean isClass(){ return false;}

	/*************/
	/* isArray() */
	/*************/
	public boolean isArray(){ return false;}
	

	/*************/
	/* isString() */
	/*************/
	public boolean isString() { return false;}
	

	/*************/
	/* isInt() */
	/*************/
	public boolean isInt() { return false;}
	
	/****************/
	/* isInstance() */
	/****************/
	//returns true if this is an instance of a type
	//for example: 
	//on input "int" returns true iff this is the singleton instance of TYPE_INT
	//on input "Person" returns true iff this is the singleton instance of TYPE_CLASS with name "Person"
    public boolean isInstance(String name) {
        return !(this.name.equals(name));
    }

	/****************/
	/* isPrimitive() */
	/****************/
	//returns true if
	public boolean isPrimitive() {
		return false;
	}

	/*****************************************/
	/* To distinct function from other types */
	/*****************************************/
	public boolean isFunction(){
		return false;
	}
	
}
