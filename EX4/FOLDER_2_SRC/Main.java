import java.io.*;
import java.io.PrintWriter;
import java.util.Map;
import IR.IR;
import CONTROL_FLOW.CFG;
import MIPS.sir_MIPS_a_lot;
import java_cup.runtime.Symbol;
import AST.*;

public class Main {

    public static Lexer l;

    public static void main(String argv[]) {
        Parser p;
        Symbol s;
        AST_PROG prog;
        FileReader file_reader;
        String inputFilename = argv[0];
        String outputFilename = argv[1];

        try {
            /********************************/
            /* [1] Initialize a file reader */
            /********************************/
            file_reader = new FileReader(inputFilename);

            /******************************/
			/* [3] Initialize a new lexer */
            /******************************/
            l = new Lexer(file_reader);

            /*******************************/
			/* [4] Initialize a new parser */
            /*******************************/
            p = new Parser(l);

            /***********************************/
			/* [5] 3 ... 2 ... 1 ... Parse !!! */
            /***********************************/
            prog = (AST_PROG) p.parse().value;

            /*************************/
			/* [6] Print the AST ... */
            /*************************/
			System.out.printf("PRINTING THE TREE...\n");
            prog.PrintMe();

            /**************************/
			/* [7] Semant the AST ... */
            /**************************/
			System.out.printf("SEMANTING...\n");

            prog.SemantMe();

            sir_MIPS_a_lot.getInstance().start_writing(outputFilename);


			System.out.printf("IRME....n");
			/**********************/
			/* [8] IR the AST ... */
			/**********************/
			prog.IRme();

			/***********************/
			/* [9] MIPS the IR ... */
			/***********************/
			IR.getInstance().MIPSme();

			
			CONTROL_FLOW.CFG.colorMe(sir_MIPS_a_lot.getInstance().getCommandList());


			/**************************************/
			/* [10] Finalize AST GRAPHIZ DOT file */
			/**************************************/
			AST_GRAPHVIZ.getInstance().finalize();

			/***************************/
			/* [11] Finalize MIPS file */
			/***************************/
			sir_MIPS_a_lot.getInstance().finalize();
        }
        
		catch (AST_ParsingException e) { // PARSER exception

			System.out.println("A Parser Exception occurred");
			printStringToFile(outputFilename, e.getMessage());
			e.printStackTrace();
		}	     

		catch (java.lang.Error e) { // LEXER exception
			System.out.println("A Lexer Exceprion occurred");
			String msg = (l!=null) ? (String.format("ERROR(%d)\n", l.getLine())) : ("ERROR\n");
			printStringToFile(outputFilename, msg);
			e.printStackTrace();
		}
        
        catch (MAIN.SemanticException e) { //SEMANTER exception
			System.out.println("A Semanter Exceprion occurred");
        	System.out.println(e.getMessage());
			printStringToFile(outputFilename, String.format("ERROR(%d)\n", e.getLineNumber()));
			e.printStackTrace();
		}
		
		catch (Exception e) { // general exception
			System.out.println("A Semanter exception occurred");
			printStringToFile(outputFilename, "ERROR\n");
			e.printStackTrace();
		}
	}
	
	static void printStringToFile(String outputFilename, String str) {

		PrintWriter writer = null;
		try {
			// to prevent collision while writing to files
			if (sir_MIPS_a_lot.getInstance().fileCreated) {
				sir_MIPS_a_lot.getInstance().finalize();	
			}
			writer = new PrintWriter(outputFilename);
			writer.print(str);
			writer.close();
		} catch (Exception e) {
			System.out.printf("Error while writing to output file: %s\n", outputFilename);
		}
	}
}
