# Compiler ⚙️


## Project Overview 📋

The project is a simulator of an actual compiler. It contains the following key components:  

Lexer Parser:  
- Processes an inputted character stream, including letters, numbers, and various symbols, and transform them into recognizable tokens.  
- These tokens are finite character strings and have an associated token type to make computer processing more structured and manageable.  
- Lexer accomplishes this task by leveraging JFlex – a Java library that simplifies this transformation process by only requiring specific regular expressions (i.e., regex) to identify patterns for each token type. 
- JFlex is very compatible with the Construction of Usable Parsers (CUP) which further simplifies the parser’s development process.  

Abstract Syntax Tree (AST):
- The tokens are then organized into a more structured format known as ASTs, which are used to represent syntax elements without any annotations, type checking, or symbol tables. 
- The developmental complexity of this process is reduced by tools such as CUP, which not only helps in defining the Context-Free Grammar (CFG) of the target language’s syntax, but also in parsing the lexical tokens in an organized manner. 
- CUP operates as a Look-Ahead Left-to-Right(1) (LALR(1)) parser, and is capable of predicting token sequences, terminals/non-terminals, enhancing error detection, and clarifying syntax ambiguities.  
- By defining the CFG of the C- language in a CUP file, we can outline syntax rules and set precedence for operators, using the compatibility and collaboration between CUP and JFlex.

Semantic Analysis:  
- Basic semantic analysis is also integrated to better maintain the symbol tables and perform type checking.  
- The semantic analyzer class traverses the AST to ensure proper type agreements and semantic accuracy, including the detection of invalid expressions, assignments, and ensuring overall scoping consistency.  
- The scoping rules are defined from a stack of hash-maps with in-built function definitions.  
- The AST then uses post-order traversing to check for valid variable and function usages within the existing scopes. 
- The type checking emphasizes the validity of various C- expressions. It is further reinforced by our custom tests, which intentionally introduce errors in input programs to demonstrate how our program correctly identifies invalid syntax and usages.

TM Simulator:
- The CodeGenerator class transforms valid C- language programs into assembly code for the TM simulator to convert high-level programs into computer-executable instructions.  
- The program re-implements the visitor class to generate TM assembly code by using code emitter functions and special registers.
- From here, the program recursively generates assembly code and includes predefined prelude and finale code to handle input and output functions and ensure there is a proper
start and end to the program.

Please note, that this program's type checking contains non-exhaustive coverage.


## Deployment 🚀 

The steps below assume that Jflex and CUP are preinstalled on your machine. Open terminal in your working directory and run:   

`make all`  
This compiles the program and creates all necessary files.  

`java -cp [path to CUP .jar] Scanner < 'filename.cm'`   
This runs the scanner with the file provided and prints any relevant tokens to stdout and any errors to stderr. 

`java -cp [path to CUP .jar] CM 'filename.cm' -a`   
This runs the parser with the file provided and sends the output to a `filename.abs` file. 

`java -cp [path to CUP .jar] CM 'filename.cm' -s`   
This runs the parser and creates the symbol table. It will also send the output to `filename.abs` and `filename.sym` files. 

`java -cp [path to CUP .jar] CM 'filename.cm' -c`   
This runs the code generator and sends all output and steps of the compiler (e.g., assembly code) to a `filename.tm` file.  

`make clean`  
This cleans all compilation in your working directory.  


## Future Improvements 🔮

- Implement remaining code generation, as all visitor functions are not yet fully implemented.  
- Implement a code optimization component which could be an interesting addition to this project for the future.  


## Team Members 💻
- William Borruat  
- Mrudini Patel  
- Chetram Sanichar

This project was created for CIS*4650 taught by Professor Fei Song in W25.
