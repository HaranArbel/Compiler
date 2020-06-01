package TYPES;

public class TYPE_ARRAY extends TYPE
{
	public TYPE type;

	/****************/
	/* CTROR    ... */
	/****************/
	public TYPE_ARRAY(String name, TYPE type)
	{
		this.name = name;
		this.type = type;
	}

	public boolean isArray() 
	{
	        return true;
    }
}
