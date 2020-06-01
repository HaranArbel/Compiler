   
import java.io.*;
import java.io.PrintWriter;

import java_cup.runtime.Symbol;
   
public class Main
{
	static public void main(String argv[])
	{
		Lexer l;
		Symbol s;
		FileReader file_reader;
		PrintWriter file_writer;
		String inputFilename = argv[0], outputFilename = argv[1];
		int id;
		
		try	{
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

			/***********************/
			/* [4] Read next token */
			/***********************/
			s = l.next_token();

			/********************************/
			/* [5] Main reading tokens loop */
			/********************************/
			while (s.sym != TokenNames.EOF)
			{
				id = s.sym;
				
				/* INT boundries checking  */
				if( id==1  ) throw new Error() ;
				
				
				/************************/
				/* [6] Print to console */
				/************************/
				System.out.print( l.printToken(id) );
				if( id==2 || id==3 || id==4 ) System.out.print("(" + s.value + ")");
				System.out.print("[" +l.getLine()+ "," +l.getTokenStartPosition()+ "]\n");
				

				/*********************/
				/* [7] Print to file */
				/*********************/
				file_writer.print(l.printToken(id) ); 
				if( id==2 || id==3 || id==4 ) file_writer.print("(" + s.value +")");
				file_writer.print("[" +l.getLine()+ "," +l.getTokenStartPosition()+ "]\n");
				
				/***********************/
				/* [8] Read next token */
				/***********************/
				s = l.next_token();
			}
			
			/******************************/
			/* [9] Close lexer input file */
			/******************************/
			l.yyclose();

			/**************************/
			/* [10] Close output file */
			/**************************/
			file_writer.close();
    	} 
		catch (Error er){   /* Token raise Error  */
			try{
				file_writer = new PrintWriter(outputFilename);
				file_writer.print("ERROR");
				file_writer.close();
			}catch (Exception e){ /* IO Exception  */
				e.printStackTrace();
			}
			er.printStackTrace();
		} 
		catch (Exception ex){   /* IO Exception  */
			ex.printStackTrace();
		}
	}
}


