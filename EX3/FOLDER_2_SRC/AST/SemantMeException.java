package AST;

public class SemantMeException extends Exception
{
	public String errorDesc = null;

	public SemantMeException(int lineNumber, String errorDesc) 
	{
		super(""+lineNumber);
		this.errorDesc=errorDesc;
	}

	@Override
	public String toString() 
	{
		return "ERROR("+getMessage()+")\n";
	}
}

