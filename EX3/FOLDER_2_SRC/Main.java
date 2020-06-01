   
import java.io.*;
import java.io.PrintWriter;
import java_cup.runtime.Symbol;
import AST.*;

public class Main
{
	static public void main(String argv[])
	{
		Lexer l = null;
		Parser p = null ;
		Symbol s = null;
		AST_PROG AST = null;
		FileReader file_reader = null;
		PrintWriter file_writer = null;
		String inputFilename = argv[0];
		String outputFilename = argv[1];
		
		try
		{
			/********************************/
			/* [1] Initialize a file reader */
			/********************************/
			file_reader = new FileReader(inputFilename);

			/********************************/
			/* [2] Initialize a file writer */
			/********************************/
			file_writer = new PrintWriter(outputFilename);
			
			/******************************/	
			/* [3] Initialize a new lexer */
			/******************************/
			l = new Lexer(file_reader);
			
			/*******************************/
			/* [4] Initialize a new parser */
			/*******************************/
			p = new Parser(l);
		}
		catch (Exception e)
		{
			System.out.println("Error, parsing have not started!!!");
			System.exit(0);
		}
		
		
		try
		{	
			/***********************************/
			/* [5] 3 ... 2 ... 1 ... Parse !!! */
			/***********************************/
			
			AST = (AST_PROG) p.parse().value;
			
		}
		catch(Throwable e)
		{	
			System.out.println("caught throwable e\n");		
			file_writer.write("ERROR("+l.getLine()+")\n");
			System.out.println("wrote to file = " + l.getLine() + "\n");
			file_writer.close();
			System.exit(0);
		}

		try
		{
			/*************************/
			/* [6] Print the AST ... */
			/*************************/
			AST.PrintMe();
		}
		catch (Exception e)
		{
			System.out.println("Error in printing AST");
			e.printStackTrace();

		}

		try
		{
			/**************************/
			/* [7] Semant the AST ... */
			/**************************/
		
			AST.SemantMe();
		}
		catch(Exception e)
		{
			System.out.println("Error in semanting AST");	
			if (e instanceof SemantMeException) 
			{
				if(((SemantMeException)e).errorDesc!=null)
                	System.err.println("SemantMeException:"+((SemantMeException)e).errorDesc);
            }

			file_writer.write(e.toString());
			e.printStackTrace();
			file_writer.close();
			System.exit(0);
		}


		try{
			file_writer.write("OK\n");
		}
		catch (Exception e)
		{
			System.out.println("Error, could not write ok");
			System.exit(0);
		}

		
			/*************************/
			/* [8] Close output file */
			/*************************/
			file_writer.close();
			
			/*************************************/
			/* [9] Finalize AST GRAPHIZ DOT file */
			/*************************************/
			AST_GRAPHVIZ.getInstance().finalizeFile();
			

			System.out.println("exiting main\n");
	}
}


