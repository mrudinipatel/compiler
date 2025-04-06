import java.util.HashMap;
import java.util.ArrayList;
import java.util.*;
import absyn.*;

import java.io.*;

public class CodeGenerator implements AbsynVisitor {

    // Special Registers
    private static final int PC = 7;
    private static final int GP = 6;
    private static final int FP = 5;
    private static final int AC = 0;
    private static final int AC1 = 1;

    // Offsets
    public int emitLoc = 0;
    public int highEmitLoc = 0;
    public int globalOffset = 0;
    public String filename;

    public void GenCode(ExpList tree, SemanticAnalyzer semanticAnalyzer) {
        try {
            PrintWriter pw = new PrintWriter(this.filename);
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        /* Generate standard prelude */
        emitComment("Standard prelude:");
        emitRM("LD", GP, 0, AC, "load gp with maxaddress");
        emitRM("LDA", FP, 0, GP, "copy gp to fp");
        emitRM("ST", AC, 0, AC, "clear at location " + AC);
        int savedLoc = emitSkip(1);

        /* Generate code for input */
        emitComment("Jump around i/o routines here");
        emitComment("code for input routine");
        emitRM("ST", AC, -1, FP, "store return");
        emitRO("IN", 0, 0, 0, "input");
        emitRM("LD", PC, -1, FP, "return to caller");

        /* Generate code for output */
        emitComment("code for output routine");
        emitRM("ST", AC, -1, FP, "store return");
        emitRM("LD", AC, -2, FP, "load output value");
        emitRO("OUT", 0, 0, 0, "output");
        emitRM("LD", PC, -1, FP, "return to caller");
        int savedLoc2 = emitSkip(0);

        /* Jump around I/O functions */
        emitBackup(savedLoc);
        emitRM("LDA", PC, savedLoc2, PC, "jump around i/o routines");
        emitRestore();
        emitComment("End of standard prelude");


        /* Generate code for the program */
        while (tree != null) {
            if (tree.head != null) {
                visit(tree.head);
            }
            tree = tree.tail;
        }

        /* Generate code for the main function */
        emitComment("Processing main function:");
        NodeType mainFunction = semanticAnalyzer.lookup("main");
        if (mainFunction == null) {
            System.err.println("Error: 'main' function not declared.");
        } else {
            /*
            emitComment("Generating code for an empty main function");
            emitRM("ST", FP, globalOffset, FP, "push old frame pointer");
            emitRM("LDA", FP, globalOffset, FP, "push frame");
            emitRM("LDA", 0, 1, PC, "load ac with ret ptr");
            emitRM_Abs("LDA", PC, 0, "jump to main");
            emitRM("LD", FP, 0, FP, "pop frame");
            emitComment("End of main function");
            */
        }

        /* Generate finale */
        emitComment("End of execution");
        emitRO("HALT", 0, 0, 0, "");
    }

    /* Visitor functions */

    /* List Structures */

    // Variable Declaration List
    public int visit(ExpList tree, int offset, boolean isParameter) {
        while (tree != null) {
            if (tree.head != null) {
                visit(tree.head, offset, false);
            }
            tree = tree.tail;
        }
        return offset;
    }

    // Expression List
    public void visit(ExpList tree, int offset) {
        while (tree != null) {
            if(tree.head != null) {
                visit(tree.head, offset, false);
            }
            tree = tree.tail;
        }
    }

    /* Abstract Classes */

    // Declaration
    public void visit(Exp tree) {
        if(tree instanceof FunDeclNode) {
            visit((FunDeclNode) tree);
        }
        else if (tree instanceof VarDeclNode) {
            VarDeclNode var = (VarDeclNode) tree;
            emitComment("Allocating global var: " + var.name);
            emitComment("<- vardecl");
        }
        else if (tree instanceof ArrDeclNode) {
            ArrDeclNode arr = (ArrDeclNode) tree;
            emitComment("Allocating global array: " + arr.name);
            emitComment("<- arrdecl");
        }
    }

    // Variable/Parameter Declaration

    // General Expression
    public int visit (Exp tree, int offset, boolean isAddress) {
        if (tree instanceof VarNode) {
            visit((VarNode) tree, offset);
        }
        else if (tree instanceof FunCallNode) {
            visit((FunCallNode) tree, offset);
        }
        else if (tree instanceof BinaryOpNode) {
            visit((BinaryOpNode) tree, offset);
        }
        else if (tree instanceof AssignNode) {
            visit((AssignNode) tree, offset);
        }
        else if (tree instanceof IfNode) {
            visit((IfNode) tree, offset);
        }
        else if (tree instanceof WhileNode) {
            visit((WhileNode) tree, offset);
        }
        else if (tree instanceof ReturnNode) {
            visit((ReturnNode) tree, offset);
        }
        else if (tree instanceof CompoundStmtNode) {
            visit((CompoundStmtNode) tree, offset);
        }
        else if (tree instanceof NumNode) {
            visit((NumNode) tree, offset);
        }
        return offset;
    }

    // Function Declaration
    public void visit(FunDeclNode tree) {
        int offset = -2;
        emitComment("-> funnction declaration");
        emitComment(" processing function: " + tree.name);
        emitComment(" jump around function body here");

        int savedLoc = emitSkip(1);
        emitRM("ST", 0, -1, FP, "store return");
        offset = visit(tree.params, offset, true);
        offset = visit(tree.body, offset, false);
        emitRM("LD", PC, -1, FP, "return caller");
        int savedLoc2 = emitSkip(0);
        emitBackup(savedLoc);
        emitRM_Abs("LDA", PC, savedLoc2, "jump around function body");
        emitRestore();
        emitComment("<- function declaration");
    }

    // Var Node
    public void visit(VarNode tree, int offset, boolean isAddress) {
        emitComment("-> var node");
        emitComment("processing variable: " + tree.name);
        if (isAddress) {
            emitRM("LDA", AC, offset, GP, "load address of variable");
        } else {
            emitRM("LD", AC, offset, GP, "load value of variable");
        }
        emitComment("<- var node");
    }

    // Int Node
    public void visit(NumNode tree, int offset) {
        emitComment("-> constant");
        emitRM("LDC", AC, Integer.parseInt(tree.value), 0, "load constant");
        emitComment("<- constant");
    }

    // Function Call Node
    public void visit(FunCallNode tree, int offset) {
        // Args
        int i = -2;
        emitComment("-> call");
        emitComment("call of function: " + tree.name);

        while(tree.argList != null) {
            if(tree.argList.head != null) {
                visit(tree.argList.head, offset, false);
                emitRM("ST", AC, offset+i, FP, "op: push left");
                i--;
            }
            tree.argList = tree.argList.tail;
        }

        emitRM("ST", FP, offset, FP, "push old frame pointer");
        emitRM("LDA", FP, offset, FP, "push frame");
        emitRM("LDA", 0, 1, PC, "load ac with ret pointer");
        emitRM_Abs("LDA", PC, 0, "jump to function");
        emitRM("LD", FP, 0, FP, "pop frame");
        emitComment("<- call");
        
    }

    // Binary Operation Node
    public void visit(BinaryOpNode tree, int offset) {
        emitComment("-> binary operation");
        if(tree.left instanceof NumNode) {
            visit(tree.left, offset, false);
            emitRM("ST", AC, offset--, FP, "op: push left");
        }
        else if(tree.left instanceof VarNode) {
            visit(tree.left, offset, false);
            emitRM("ST", AC, offset--, FP, "op: push left");
        }
        else if(tree.left instanceof FunCallNode) {
            visit(tree.left, offset, false);
        }
        else if(tree.left instanceof BinaryOpNode) {
            visit(tree.left, offset, false);
            emitRM("ST", AC, offset--, FP, "");
        }
        else if(tree.right instanceof NumNode) {
            visit(tree.right, offset, false);
        }
        else if(tree.right instanceof VarNode) {
            visit(tree.right, offset, false);
        }
        else if(tree.right instanceof FunCallNode) {
            visit(tree.right, offset, false);
        }
        else if(tree.right instanceof BinaryOpNode) {
            visit(tree.right, offset, false);
        }

        emitRM("LD", 1, ++offset, FP, "op: load left");

        // Operator
        switch(tree.op) {
            case BinaryOpNode.PLUS:
                emitRM("ADD", AC, 1, AC, "op: add");
                break;
            case BinaryOpNode.MINUS:
                emitRM("SUB", AC, 1, AC, "op: sub");
                break;
            case BinaryOpNode.TIMES:
                emitRM("MUL", AC, 1, AC, "op: mul");
                break;
            case BinaryOpNode.OVER:
                emitRM("DIV", AC, 1, AC, "op: div");
                break;
            case BinaryOpNode.EQ:
                emitRM("JEQ", AC, 1, AC, "op: eq");
                break;
            case BinaryOpNode.LT:
                emitRM("JLT", AC, 1, AC, "op: lt");
                break;
            case BinaryOpNode.GT:
                emitRM("JGT", AC, 1, AC, "op: gt");
                break;
            case BinaryOpNode.LEQT:
                emitRM("JLE", AC, 1, AC, "op: leqt");
                break;
            case BinaryOpNode.GEQT:
                emitRM("JGE", AC, 1, AC, "op: geqt");
                break;
            case BinaryOpNode.NEQ:
                emitRM("JNE", AC, 1, AC, "op: neq");
                break;
            case BinaryOpNode.AND:
                emitRM("AND", AC, 1, AC, "op: and");
                break;
            case BinaryOpNode.OR:
                emitRM("OR", AC, 1, AC, "op: or");
                break;
        }
        emitComment("<- binary operation");
    }

    // Assignment Node
    public void visit(AssignNode tree, int offset) {
        emitComment("-> operation");
        // LHS
        if(tree.lhs instanceof VarNode) {
            visit(tree.lhs, offset, true);
        }
        else if(tree.lhs instanceof FunCallNode) {
            visit(tree.lhs, offset, false);
        }
        else if(tree.lhs instanceof BinaryOpNode) {
            visit(tree.lhs, offset, false);
        }
        else if(tree.lhs instanceof NumNode) {
            visit(tree.lhs, offset, false);
        }

        emitRM("LD", 1, offset, FP, "op: load left");

        emitRM("ST", AC, 0, 1, "assign: store value");
        emitComment("<- operation");
    }

    /* Control Structures */

    // If Statement
    public void visit(IfNode tree, int offset) {
        emitComment("-> if");
        visit(tree.test, offset, false);
        int savedLoc = emitSkip(1);
        visit(tree.thenpart, offset, false);
        int savedLoc2 = emitSkip(0);
        emitBackup(savedLoc);
        emitRM_Abs("JEQ", AC, savedLoc2, "if: jump to else part");
        emitRestore();
        emitComment("<- if");
    }

    // While Statement
    public void visit(WhileNode tree, int offset) {
        emitComment("-> while");
        emitComment("while: jump after body comes back here");
        int savedLoc3 = emitSkip(0);
        visit(tree.test, offset, false);
        int savedLoc = emitSkip(1);
        visit(tree.statements, offset, false);
        emitRM_Abs("LDA", PC, savedLoc3, "while: absolute jump to test");
        int savedLoc2 = emitSkip(0);
        emitBackup(savedLoc);
        emitRM_Abs("JEQ", 0, savedLoc2, "while: jump to end");
        emitRestore();
        emitComment("<- while");
    }

    // Return Statement
    public void visit(ReturnNode tree, int offset) {
        emitComment("-> return");
        visit(tree.returnValue, offset, false);
        emitRM("LD", PC, -1, FP, "return to caller");
        emitComment("<- return");
    }

    
    // Compound Statement
    public void visit(CompoundStmtNode tree, int offset) {
        emitComment("-> compound statement");
        offset = visit(tree.localDeclarations, offset, false);
        visit(tree.statementList, offset);
        emitComment("<- compound statement");
        //return offset;
    }

    /* Code Emitting Routines */
    public void emitRM(String op, int r, int d, int s, String c) {
        String code = emitLoc + ": " + op + " " + r + "," + d + "(" + s + ")";
        writeCode(code);
        ++emitLoc;
        writeCode("\t" + c);
        writeCode("\n");
        if(highEmitLoc < emitLoc) {
            highEmitLoc = emitLoc;
        }
    }

    public void emitRM_Abs(String op, int r, int a, String c) {
        String code = emitLoc + ": " + op + " " + r + "," + (a - (emitLoc + 1)) + "(" + PC + ")";
        writeCode(code);
        ++emitLoc;
        writeCode("\t" + c);
        writeCode("\n");
        if(highEmitLoc < emitLoc) {
            highEmitLoc = emitLoc;
        }
    }

    public void emitRO(String op, int r, int s, int t, String c) {
        String code = emitLoc + ": " + op + " " + r + "," + s + "," + t;
        writeCode(code);
        ++emitLoc;
        writeCode("\t" + c);
        writeCode("\n");
        if(highEmitLoc < emitLoc) {
            highEmitLoc = emitLoc;
        }
    }

    /* Routines to maintain the code space */
    public int emitSkip(int distance) {
        int i = emitLoc;
        emitLoc += distance;
        if(highEmitLoc < emitLoc) {
            highEmitLoc = emitLoc;
        }
        return i;
    }

    public void emitBackup(int loc) {
        if(loc > highEmitLoc) {
            emitComment("BUG in emitBackup");
        }
        emitLoc = loc;
    }

    public void emitRestore() {
        emitLoc = highEmitLoc;
    }

    /* Routine to generate one line of comment */
    public void emitComment(String c) {
        c = "* " + c + "\n";
        writeCode(c);
    }

    public void writeCode(String content) {
        PrintWriter outputStream = null;
        try {
            outputStream = new PrintWriter(new FileOutputStream(this.filename, true));
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        outputStream.printf(content);
        outputStream.close();
    }

    /* Visitor functions not yet implemented */

    @Override
    public void visit(UnaryOpNode unaryOpNode, int level) {
        emitComment("-> unary operation");
        visit(unaryOpNode.operand, level, false);
        /*switch (unaryOpNode.op) {
            case UnaryOpNode.NOT:
                emitRO("NOT", AC, AC, 0, "unary op: not");
                break;
            case UnaryOpNode.NEG:
                emitRO("NEG", AC, AC, 0, "unary op: neg");
                break;
        }*/
        emitComment("<- unary operation");
    }

    @Override
    public void visit(TruthNode truthNode, int level) {
        emitComment("-> truth node");
        //emitRM("LDC", AC, truthNode.value ? 1 : 0, 0, "load truth value");
        emitComment("<- truth node");
    }

    @Override
    public void visit(IdNode idNode, int level) {
        emitComment("-> id node");
        emitComment("processing identifier: " + idNode.name);
        //emitRM("LD", AC, idNode.offset, GP, "load value of identifier");
        emitComment("<- id node");
    }

    @Override
    public void visit(FunDeclNode funDeclNode, int level) {
        emitComment("-> function declaration");
        emitComment("processing function: " + funDeclNode.name);
        int savedLoc = emitSkip(1); // Reserve space for jump
        emitRM("ST", AC, -1, FP, "store return address");
        visit(funDeclNode.params, level, true);
        visit(funDeclNode.body, level, false);
        emitRM("LD", PC, -1, FP, "return to caller");
        int savedLoc2 = emitSkip(0);
        emitBackup(savedLoc);
        emitRM_Abs("LDA", PC, savedLoc2, "jump around function body");
        emitRestore();
        emitComment("<- function declaration");
    }

    @Override
    public void visit(FunProtoNode funProtoNode, int level) {
        emitComment("-> function prototype");
        emitComment("processing prototype: " + funProtoNode.name);
        emitComment("<- function prototype");
    }

    @Override
    public void visit(VarDeclNode varDeclNode, int level) {
        emitComment("-> variable declaration");
        emitComment("allocating variable: " + varDeclNode.name);
        emitComment("<- variable declaration");
    }

    @Override
    public void visit(VarNode varNode, int level) {
        emitComment("-> variable node");
        emitComment("processing variable: " + varNode.name);
        //emitRM("LD", AC, varNode.offset, GP, "load value of variable");
        emitComment("<- variable node");
    }

    @Override
    public void visit(ParamNode paramNode, int level) {
        emitComment("-> parameter node");
        emitComment("processing parameter: " + paramNode.name);
        emitComment("<- parameter node");
    }

    @Override
    public void visit(ArrDeclNode arrDeclNode, int level) {
        emitComment("-> array declaration");
        emitComment("allocating array: " + arrDeclNode.name + " of size " + arrDeclNode.size);
        emitComment("<- array declaration");
    }

    @Override
    public void visit(ArrNode arrNode, int level) {
        emitComment("-> array node");
        visit(arrNode.index, level, false);
        //emitRM("LDA", AC1, arrNode.offset, GP, "load base address of array");
        emitRO("ADD", AC, AC1, AC, "compute address of array element");
        emitRM("LD", AC, 0, AC, "load value of array element");
        emitComment("<- array node");
    }

    @Override
    public void visit(TypeNode typeNode, int level) {
        emitComment("-> type node");
        emitComment("processing type: " + typeNode.type);
        emitComment("<- type node");
    }

    @Override
    public void visit(NodeType nodeType, int level) {
        emitComment("-> node type");
        emitComment("processing node type");
        emitComment("<- node type");
    }
}