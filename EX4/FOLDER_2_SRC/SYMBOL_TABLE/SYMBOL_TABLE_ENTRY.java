/***********/
/* PACKAGE */
/***********/
package SYMBOL_TABLE;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TYPES.*;
import CONTEXT.*;
/**********************/
/* SYMBOL TABLE ENTRY */
/**********************/
public class SYMBOL_TABLE_ENTRY
{
	/*********/
	/* index */
	/*********/
	int index;
	
	/********/
	/* name */
	/********/
	public String name;

	/******************/
	/* TYPE value ... */
	/******************/
	public TYPE type;

	/*********************************************/
	/* prevtop and next symbol table entries ... */
	/*********************************************/
	public SYMBOL_TABLE_ENTRY prevtop;
	public SYMBOL_TABLE_ENTRY next;

	/****************************************************/
	/* The prevtop_index is just for debug purposes ... */
	/****************************************************/
	public int prevtop_index;
	

	/****************************************************/
	/* entry scope level                            ... */
	/****************************************************/    	
	public int entry_scope_level;

	public Context context;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public SYMBOL_TABLE_ENTRY(
		String name,
		TYPE type,
		int index,
		SYMBOL_TABLE_ENTRY next,
		SYMBOL_TABLE_ENTRY prevtop,
		int prevtop_index, 
		int entry_scope_level,
		Context context) //todo - NEW. each SymbolTable entry now has Context field which holds offset data
	{
		this.index = index;
		this.name = name;
		this.type = type;
		this.next = next;
		this.prevtop = prevtop;
		this.prevtop_index = prevtop_index;
		this.entry_scope_level=entry_scope_level;
		this.context = context;
	}
}