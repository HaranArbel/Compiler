package TYPES;

public class TYPE_STRING extends TYPE
{
	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static TYPE_STRING instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected TYPE_STRING() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static TYPE_STRING getInstance()
	{
		if (instance == null)
		{
			instance = new TYPE_STRING();
			instance.name = "string";
		}
		return instance;
	}
	
	public boolean isString() {
		return true;
	}

	/******************************/
	/* TYPE STRING IS PRIMITIVE ... */
	/******************************/
	public boolean isPrimitive() {
		return true;
	}
}
