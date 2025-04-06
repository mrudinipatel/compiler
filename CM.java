import java.io.*;
import absyn.*;
   
class CM {
  public static boolean SHOW_TREE = false;
  public static boolean SHOW_SYMBOL_TABLE = false;
  public static boolean PARSER_ERROR = false;
  public static boolean GENERATE_CODE = false;

  static public void main(String argv[]) {    
    /* Check for '-a', '-s', and '-c' arguments */
    for (String arg : argv) {
      if (arg.equals("-a")) {
        SHOW_TREE = true;
      }
      if (arg.equals("-s")) {
        SHOW_SYMBOL_TABLE = true;
      }
      if (arg.equals("-c")) {
        GENERATE_CODE = true;
      }
    }

    /* Start the parser */
    try {
      parser p = new parser(new Lexer(new FileReader(argv[0])));
      Absyn result = (Absyn)(p.parse().value);      

      String inputFileName = argv[0];
      String baseFileName = inputFileName.substring(0, inputFileName.lastIndexOf('.'));

      // Save abstract syntax tree to .abs file
      if (SHOW_TREE && result != null) {
        PrintStream originalOut = System.out;
        try (PrintStream fileOut = new PrintStream(new FileOutputStream(baseFileName + ".abs"))) {
          System.setOut(fileOut);
          System.out.println("The abstract syntax tree is:\n");
          AbsynVisitor visitor = new ShowTreeVisitor();
          result.accept(visitor, 0);
          System.out.println("\n");
        } finally {
          System.setOut(originalOut);
        }

        if (!parser.valid) {
          PARSER_ERROR = true;
          System.err.println("Parser error detected. Exiting.");
          return;
        }
      }

      // Save symbol table to .sym file
      if (SHOW_SYMBOL_TABLE && result != null && !PARSER_ERROR) {
        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;
        try (PrintStream fileOut = new PrintStream(new FileOutputStream(baseFileName + ".sym"))) {
          System.setOut(fileOut);
          System.out.println("Performing semantic analysis:\n");
          SemanticAnalyzer analyzer = new SemanticAnalyzer();
          analyzer.analyze(result);
          System.out.println("\n");
        } finally {
          System.setOut(originalOut);
          System.setErr(originalErr);
        }
      }

      // Generate code to .tm file
      if (GENERATE_CODE && result != null && !PARSER_ERROR) {
        String outputFileName = baseFileName + ".tm";
        CodeGenerator codeGen = new CodeGenerator();
        codeGen.filename = outputFileName;

        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
        semanticAnalyzer.analyze(result); // Populate the symbol table

        codeGen.GenCode((ExpList) result, semanticAnalyzer); // Pass the semantic analyzer
        System.out.println("Code generation complete. Output written to " + outputFileName);
      }
    } catch (Exception e) {
      /* do cleanup here -- possibly rethrow e */
      e.printStackTrace();
    }
  }
}
