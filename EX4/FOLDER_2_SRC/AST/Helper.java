package AST;
import TYPES.*;

public class Helper
{
	//returns true iff the two types are identical
	public static boolean compareTypes(TYPE type1, TYPE type2) {

		if (type1.isPrimitive() && type2.isPrimitive()){ //if type is primitive getInstance always returns the same instance.
			return type1 == type2;
		}
		if ((type1.isArray() && type2.isArray()) || (type1.isClass() && type2.isClass())){ // if type class or array then name determines the type
			return type1.name.equals(type2.name);
		}
		return false;
	}

	
	public static boolean isValidType(TYPE type, String typeName) {
        
		if (type.isInstance(typeName))
			return false;
		
		else if (type.isArray() || type.isClass() || type == TYPE_STRING.getInstance() || type == TYPE_INT.getInstance()) 
			return true;
        
        return false;
	}
	

	//Called From AST_EXP_BINOP
	public static boolean isEqualityValid(TYPE t1, TYPE t2)
	{
		return  (isAssignmentValid(t1, t2, false) || isAssignmentValid(t2, t1, false) || (t1==TYPE_NIL.getInstance() && t2==TYPE_NIL.getInstance()) );
	}

	//Called From AST_STMT_RETURN
	public static boolean currentReturnIsValid(TYPE expected_type, TYPE returned_type)
	{
		return isAssignmentValid(expected_type, returned_type, false);
	}
	
	/*
		returns true iff assignment left :=right is legal
		Called from:
		AST_STNT_ASSIGN: var := Exp/newExp 
		AST_V_DEC: vartype var := Exp/newExp
	*/
	public static boolean isAssignmentValid(TYPE left, TYPE right, boolean isRightNewExp)
	{
		//class := nil     or     array := nil
		if ((left.isArray() || left.isClass()) && (right == TYPE_NIL.getInstance()))
		{
		return true;
		}
		//class := class
		if(left.isClass() && right.isClass())
		{
			return ((TYPE_CLASS)right).checkInheritance(left.name);
		}

		//primitive := primitive
		if(left.isPrimitive()&&right.isPrimitive()) {
			return left == right; //check if it's exactly the same instance (singlelton)
		}
		//array := array
		if(left.isArray()&&right.isArray())
		{
			if(isRightNewExp)
			{
				return ((TYPE_ARRAY)left).type.name.equals(((TYPE_ARRAY)right).type.name);			
			}
			else
			{
				return left.name.equals(right.name);
			}
			
		}
		return false;
	}
  


}
