package TYPES;

import CONTEXT.*;

public class TYPE_CLASS_VAR_DEC extends TYPE
{
	public TYPE t;
	public String name;
	public Context context;
	

	//class variable should now contain also his context,
	//In generak, context is a nice way to save info on variables, 
	//such ass offset, if the variable is local/global/ in function/ a class field, etc..
	public TYPE_CLASS_VAR_DEC(TYPE t,String name, Context context)
	{
		this.t = t;
		this.name = name;
		this.context = context;
	}
}
