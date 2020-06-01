/***********/
/* PACKAGE */
/***********/
package SYMBOL_TABLE;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


/*******************/
/* PROJECT IMPORTS */
/*******************/
import TYPES.*;

/****************/
/* SYMBOL TABLE */
/****************/
public class SYMBOL_TABLE
{
	private int hashArraySize = 50;
	
	/**********************************************/
	/* The actual symbol table data structure .../*/
	/**********************************************/
	private SYMBOL_TABLE_ENTRY[] table = new SYMBOL_TABLE_ENTRY[hashArraySize];
	private SYMBOL_TABLE_ENTRY top;
	private int top_index = 0;
	private TYPE_CLASS currParentClass = null;
	private TYPE currentReturnType = null;
	private int scope_level=0; //starts at 0 - global level scope
	private Set<String> keywordsForClassOrArrayType = new HashSet<String>(Arrays.asList("class", "nil", "array", "while", "extends", "return", "new", "if", "string", "int", "void", "PrintInt", "PrintString", "PrintTrace"));
	private Set<String> keywords = new HashSet<String>(Arrays.asList("class", "nil", "array", "while", "extends", "return", "new", "if", "string", "int", "void"));

	public boolean isKeyword (String word,boolean isClassOrArrayTypeName)

	{
		if(isClassOrArrayTypeName)
			return keywordsForClassOrArrayType.contains(word);
		return keywords.contains(word);
	}

	public void setCurrParentClass(TYPE_CLASS currParentClass)
	{
        	this.currParentClass = currParentClass;
	}
    
	public TYPE_CLASS getCurrParentClass() 
	{
		return currParentClass;
	}	
	
	public void setCurrnetReturnType(TYPE currentType) 
	{
		this.currentReturnType = currentType;
	}

	public TYPE getCurrnetReturnType()
	{
		return currentReturnType;
	}		
		
	public int hash(String s) {
		final int prime = 263;
		int hashedString = 0;
		if (s!=null) hashedString = s.hashCode();
		return Math.abs((prime + hashedString) % hashArraySize);
	}

	/****************************************************************************/
	/* Enter a variable, function, class type or array type to the symbol table */
	/****************************************************************************/
	public void enter(String name,TYPE t)
	{
		/*************************************************/
		/* [1] Compute the hash value for this new entry */
		/*************************************************/
		int hashValue = hash(name);

		/******************************************************************************/
		/* [2] Extract what will eventually be the next entry in the hashed position  */
		/*     NOTE: this entry can very well be null, but the behaviour is identical */
		/******************************************************************************/
		SYMBOL_TABLE_ENTRY next = table[hashValue];
	
		/**************************************************************************/
		/* [3] Prepare a new symbol table entry with name, type, next and prevtop */
		/**************************************************************************/
		SYMBOL_TABLE_ENTRY e = new SYMBOL_TABLE_ENTRY(name,t,hashValue,next,top,top_index++,this.scope_level);

		/**********************************************/
		/* [4] Update the top of the symbol table ... */
		/**********************************************/
		top = e;
		
		/****************************************/
		/* [5] Enter the new entry to the table */
		/****************************************/
		table[hashValue] = e;
		
		/**************************/
		/* [6] Print Symbol Table */
		/**************************/
		PrintMe();
	}

	/***********************************************/
	/* Find the inner-most scope element with name */
	/***********************************************/
	public TYPE find(String name)
	{
		//Find the inner-most scope element with name
		SYMBOL_TABLE_ENTRY e;
		TYPE nameInParent = null;
		
		for (e = table[hash(name)]; e != null; e = e.next)
		{
			if (name.equals(e.name))
			{
				break;
			}
		}
	
		TYPE nameTypeInParent = null;
		if((currParentClass!=null && e==null) || (currParentClass!=null && e.entry_scope_level==0))
			nameTypeInParent = findNameInParentClass(name);

		if(nameTypeInParent!=null)
			return nameTypeInParent;
		
		if(e==null)
			return null;
		
		return e.type;
	}
	
	private TYPE findNameInParentClass(String name)
	{
		if(currParentClass.findDataMember(name)!=null)
			return currParentClass.findDataMember(name);

		if(currParentClass.findMethod(name)!=null)
			return currParentClass.findMethod(name);		
		
		return null;
	}


	/***********************************************/
	/* Find is this new variable name */
	/***********************************************/	
	public boolean IsVarNameNew(String name, boolean isClassOrArrayTypeName) {
		if(isKeyword(name,isClassOrArrayTypeName))
			return false;        	
		SYMBOL_TABLE_ENTRY currEntry = table[hash(name)];
		while(currEntry!=null)
		{
			if(currEntry.name.equals(name))
			{
				//two variables with the same name in the same scope level - illegal
				if(currEntry.entry_scope_level == scope_level)
					return false;
				//different scope levels - can't  				
				if(currEntry.type.isClass() || currEntry.type.isArray())
				{
					if(currEntry.type.name.equals(name))
					{
						return false;
					}
				}
			}
			currEntry = currEntry.next;			
		}
        	return true;	
	}
	
	/***************************************************************************/
	/* begine scope = Enter the <SCOPE-BOUNDARY> element to the data structure */
	/***************************************************************************/
	public void beginScope()
	{
		/************************************************************************/
		/* Though <SCOPE-BOUNDARY> entries are present inside the symbol table, */
		/* they are not really types. In order to be ablt to debug print them,  */
		/* a special TYPE_FOR_SCOPE_BOUNDARIES was developed for them. This     */
		/* class only contain their type name which is the bottom sign: _|_     */
		/************************************************************************/
		enter(
			"SCOPE-BOUNDARY",
			new TYPE_FOR_SCOPE_BOUNDARIES("NONE"));

		/*********************************************/
		/* Print the symbol table after every change */
		/*********************************************/
		PrintMe();
		this.scope_level++;
	}

	/********************************************************************************/
	/* end scope = Keep popping elements out of the data structure,                 */
	/* from most recent element entered, until a <NEW-SCOPE> element is encountered */
	/********************************************************************************/
	public void endScope()
	{
		/**************************************************************************/
		/* Pop elements from the symbol table stack until a SCOPE-BOUNDARY is hit */		
		/**************************************************************************/
		while (top.name != "SCOPE-BOUNDARY")
		{
			table[top.index] = top.next;
			top_index = top_index-1;
			top = top.prevtop;
		}
		/**************************************/
		/* Pop the SCOPE-BOUNDARY sign itself */		
		/**************************************/
		table[top.index] = top.next;
		top_index = top_index-1;
		top = top.prevtop;

		/*********************************************/
		/* Print the symbol table after every change */		
		/*********************************************/
		PrintMe();
		
		this.scope_level--;
	}
	
	public static int n=0;
	
	public void PrintMe()
	{
		int i=0;
		int j=0;
		String dirname="./FOLDER_5_OUTPUT/";
		String filename=String.format("SYMBOL_TABLE_%d_IN_GRAPHVIZ_DOT_FORMAT.txt",n++);

		try
		{
			/*******************************************/
			/* [1] Open Graphviz text file for writing */
			/*******************************************/
			PrintWriter fileWriter = new PrintWriter(dirname+filename);

			/*********************************/
			/* [2] Write Graphviz dot prolog */
			/*********************************/
			fileWriter.print("digraph structs {\n");
			fileWriter.print("rankdir = LR\n");
			fileWriter.print("node [shape=record];\n");

			/*******************************/
			/* [3] Write Hash Table Itself */
			/*******************************/
			fileWriter.print("hashTable [label=\"");
			for (i=0;i<hashArraySize-1;i++) { fileWriter.format("<f%d>\n%d\n|",i,i); }
			fileWriter.format("<f%d>\n%d\n\"];\n",hashArraySize-1,hashArraySize-1);
		
			/****************************************************************************/
			/* [4] Loop over hash table array and print all linked lists per array cell */
			/****************************************************************************/
			for (i=0;i<hashArraySize;i++)
			{
				if (table[i] != null)
				{
					/*****************************************************/
					/* [4a] Print hash table array[i] -> entry(i,0) edge */
					/*****************************************************/
					fileWriter.format("hashTable:f%d -> node_%d_0:f0;\n",i,i);
				}
				j=0;
				for (SYMBOL_TABLE_ENTRY it=table[i];it!=null;it=it.next)
				{
					/*******************************/
					/* [4b] Print entry(i,it) node */
					/*******************************/
					fileWriter.format("node_%d_%d ",i,j);
					fileWriter.format("[label=\"<f0>%s|<f1>%s|<f2>prevtop=%d|<f3>next\"];\n",
						it.name,
						it.type.name,
						it.prevtop_index);

					if (it.next != null)
					{
						/***************************************************/
						/* [4c] Print entry(i,it) -> entry(i,it.next) edge */
						/***************************************************/
						fileWriter.format(
							"node_%d_%d -> node_%d_%d [style=invis,weight=10];\n",
							i,j,i,j+1);
						fileWriter.format(
							"node_%d_%d:f3 -> node_%d_%d:f0;\n",
							i,j,i,j+1);
					}
					j++;
				}
			}
			fileWriter.print("}\n");
			fileWriter.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}		
	}
	
	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static SYMBOL_TABLE instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected SYMBOL_TABLE() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static SYMBOL_TABLE getInstance()
	{
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new SYMBOL_TABLE();

			/**************************************************/
			/* [1] Enter primitive types int, string and void */
			/**************************************************/
			instance.enter("int",   TYPE_INT.getInstance());
			instance.enter("string",TYPE_STRING.getInstance());
			instance.enter("void",TYPE_VOID.getInstance());

			/***************************************/
			/* [2] Enter library function PrintInt */
			/***************************************/
			instance.enter(
				"PrintInt",
				new TYPE_FUNCTION(
					TYPE_VOID.getInstance(),
					"PrintInt",
					new TYPE_LIST(
						TYPE_INT.getInstance(),
						null)));


			/*****************************************/
			/* [3] Enter library function PrintSring */
			/*****************************************/
			instance.enter(
				"PrintString",
				new TYPE_FUNCTION(
					TYPE_VOID.getInstance(),
					"PrintString",
					new TYPE_LIST(
						TYPE_STRING.getInstance(),
						null)));

			/*****************************************/
			/* [4] Enter library function PrintTrace */
			/*****************************************/
			instance.enter(
				"PrintTrace",
				new TYPE_FUNCTION(
					TYPE_VOID.getInstance(),
					"PrintTrace",
					null));
		
		}
		return instance;
	}
}
