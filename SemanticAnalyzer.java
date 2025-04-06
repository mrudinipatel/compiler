import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import absyn.*;

public class SemanticAnalyzer implements AbsynVisitor {
    
    @Override
    public void visit(NodeType node, int level) {
        // Implementation for visiting NodeType
    }
    final static int SPACES = 4;
    private int currentLevel = 0;
    private Stack<HashMap<String, NodeType>> scopeStack = new Stack<>();
    private HashMap<String, ExpList> functionParamList = new HashMap<>();
    private HashMap<String, NodeType> functionReturnList = new HashMap<>();

    public SemanticAnalyzer() {
        scopeStack.push(new HashMap<>()); // Global scope
    }

    private void indent(int level) {
        for (int i = 0; i < level * SPACES; i++) System.out.print(" ");
    }

    private void enterScope() {
        currentLevel++;
        scopeStack.push(new HashMap<>());
        indent(currentLevel);
        System.out.println("Entering a new block:");
    }

    private void leaveScope() {
        printCurrentScope();
        scopeStack.pop();
        indent(currentLevel);
        System.out.println("Leaving the block");
        currentLevel--;
    }

    private void printCurrentScope() {
        HashMap<String, NodeType> currentScope = scopeStack.peek();
        for (String key : currentScope.keySet()) {
            indent(currentLevel + 1);
            System.out.println(key + ": " + currentScope.get(key));
        }
    }

    private void insert(String key, NodeType value) {
        HashMap<String, NodeType> currentScope = scopeStack.peek();
        if (currentScope.containsKey(key)) {
            System.err.println("Error: Variable '" + key + "' is already defined in the current scope at line " + value.row + ", column " + value.col);
        } else {
            currentScope.put(key, value);
        }
    }

    public NodeType lookup(String key) {
        for (int i = scopeStack.size() - 1; i >= 0; i--) {
            HashMap<String, NodeType> scope = scopeStack.get(i);
            if (scope.containsKey(key)) {
                return scope.get(key);
            }
        }
        return null;
    }

    public void delete(String key) {
        scopeStack.peek().remove(key);
    }

    public boolean isInt(Exp exp) {
        if (exp instanceof NumNode) {
            return true;
        } else if (exp instanceof VarNode) {
            String name = ((VarNode) exp).name;
            NodeType type = lookup(name);
            return type != null && type.type == NodeType.INT;
        } else if (exp instanceof FunCallNode) {
            String name = ((FunCallNode) exp).name;
            NodeType type = lookup(name);
            return type != null && type.type == NodeType.INT;
        } else if (exp instanceof BinaryOpNode) {
            BinaryOpNode binaryOpNode = (BinaryOpNode) exp;
            return binaryOpNode.op >= BinaryOpNode.PLUS && binaryOpNode.op <= BinaryOpNode.OVER;
        } else if (exp instanceof ArrNode) {
            String name = ((ArrNode) exp).name;
            NodeType type = lookup(name);
            return type != null && type.type == NodeType.INT;
        } else if (exp instanceof ParamNode) {
            String name = ((ParamNode) exp).name;
            NodeType type = ((ParamNode) exp).type;
            return type != null && type.type == NodeType.INT;
        } else if (exp instanceof VarDeclNode) {
            String name = ((VarDeclNode) exp).name;
            NodeType type = ((VarDeclNode) exp).type;
            return type != null && type.type == NodeType.INT;
        } else if (exp instanceof ArrDeclNode) {
            String name = ((ArrDeclNode) exp).name;
            NodeType type = ((ArrDeclNode) exp).type;
            return type != null && type.type == NodeType.BOOL;
        }
        return false;
    }

    public boolean isBool(Exp exp){
       if (exp instanceof TruthNode) {
            return true;
        } else if (exp instanceof VarNode) {
            String name = ((VarNode) exp).name;
            NodeType type = lookup(name);
            return type != null && type.type == NodeType.BOOL;
        } else if (exp instanceof FunCallNode) {
            String name = ((FunCallNode) exp).name;
            NodeType type = lookup(name);
            return type != null && type.type == NodeType.BOOL;
        } else if (exp instanceof BinaryOpNode) {
            BinaryOpNode binaryOpNode = (BinaryOpNode) exp;
            return binaryOpNode.op > BinaryOpNode.OVER && binaryOpNode.op <= BinaryOpNode.OR;
        } else if (exp instanceof UnaryOpNode) {
            return true;
        } else if (exp instanceof ArrNode) {
            String name = ((ArrNode) exp).name;
            NodeType type = lookup(name);
            return type != null && type.type == NodeType.BOOL;
        } else if (exp instanceof ParamNode) {
            String name = ((ParamNode) exp).name;
            NodeType type = ((ParamNode) exp).type;
            return type != null && type.type == NodeType.BOOL;
        } else if (exp instanceof VarDeclNode) {
            String name = ((VarDeclNode) exp).name;
            NodeType type = ((VarDeclNode) exp).type;
            return type != null && type.type == NodeType.BOOL;
        } else if (exp instanceof ArrDeclNode) {
            String name = ((ArrDeclNode) exp).name;
            NodeType type = ((ArrDeclNode) exp).type;
            return type != null && type.type == NodeType.BOOL;
        }
        return false;
    }

    public boolean isVoid(Exp exp){
        if (exp instanceof VarNode) {
            String name = ((VarNode) exp).name;
            NodeType type = lookup(name);
            return type != null && type.type == NodeType.VOID;
        } else if (exp instanceof FunCallNode) {
            String name = ((FunCallNode) exp).name;
            NodeType type = lookup(name);
            return type != null && type.type == NodeType.VOID;
        } 
        return false;
    }

    public int getNodeType(Exp exp){
        if(isInt(exp)){
            return NodeType.INT;
        } else if(isBool(exp)){
            return NodeType.BOOL;
        } else if(isVoid(exp)){
            return NodeType.VOID;
        }
        return -1;
    }

    public void visit(VarDeclNode varDeclNode, int level) {
        NodeType type = varDeclNode.type;
        if(type.type == NodeType.VOID)
        {
            System.err.println("Error: Variable cannot be of type VOID at Line " + (varDeclNode.col + 1) + ", Column " + varDeclNode.row);
            varDeclNode.type.type = NodeType.INT;
        }
        insert(varDeclNode.name, varDeclNode.type);
    }

    public void visit(FunDeclNode funDeclNode, int level) {
        insert(funDeclNode.name, funDeclNode.type);
        indent(currentLevel);
        System.out.println("Now entering the scope for function '" + funDeclNode.name + "':");
        enterScope();
        if (funDeclNode.params != null) {
            funDeclNode.params.accept(this, level + 1);
        }
        
        funDeclNode.body.accept(this, level + 1);
        functionParamList.put(funDeclNode.name, funDeclNode.params);
        functionReturnList.put(funDeclNode.name, funDeclNode.type);
        leaveScope();
        indent(currentLevel);
        System.out.println("Leaving the function scope");
    }

    public void visit(CompoundStmtNode compoundStmtNode, int level) {
        enterScope();
        if (compoundStmtNode.localDeclarations != null) {
            compoundStmtNode.localDeclarations.accept(this, level + 1);
        }
        if (compoundStmtNode.statementList != null) {
            compoundStmtNode.statementList.accept(this, level + 1);
        }
        leaveScope();
    }

    public void visit(ParamNode paramNode, int level) {
        insert(paramNode.name, paramNode.type);
    }

    // Other visit methods for different node types
    public void visit(BinaryOpNode binaryOpNode, int level) {
        int leftType = getNodeType(binaryOpNode.left);
        int rightType = getNodeType(binaryOpNode.right);
        if(binaryOpNode.op >= BinaryOpNode.PLUS && binaryOpNode.op <= BinaryOpNode.NEQ)
        {
            if(isInt(binaryOpNode.left)){
                if (isInt(binaryOpNode.right)){
                    binaryOpNode.left.accept(this, level + 1);
                    binaryOpNode.right.accept(this, level + 1);
                } else {
                    System.err.println("Error: Mismatch RIGHT operand type in binary operation at Line " + (binaryOpNode.right.col + 1) + ", Column " + binaryOpNode.left.row);
                    binaryOpNode.right = new NumNode(binaryOpNode.right.row, binaryOpNode.right.col, "0");
                    binaryOpNode.left.accept(this, level + 1);
                    binaryOpNode.right.accept(this, level + 1);
                }
            }
            else 
            {
                System.err.println("Error: Mismatch LEFT operand type in binary operation at Line " + (binaryOpNode.right.row + 1) + ", Column " + binaryOpNode.left.col);
                binaryOpNode.right = new NumNode(binaryOpNode.right.row, binaryOpNode.right.col, "0");
                binaryOpNode.left.accept(this, level + 1);
                binaryOpNode.right.accept(this, level + 1); 
            } 
        } 
        else 
        {
            if(leftType != NodeType.VOID && rightType != NodeType.VOID)
            {
                binaryOpNode.left.accept(this, level + 1);
                binaryOpNode.right.accept(this, level + 1);
            }
            else 
            {
                if(leftType == NodeType.VOID)
                {
                    System.err.println("Error: Mismatch operand types in binary operation. Left operand is VOID at Line " + (binaryOpNode.left.row + 1) + ", Column " + binaryOpNode.left.col);
                    binaryOpNode.left = new TruthNode(binaryOpNode.left.row, binaryOpNode.left.col, "False");
                    binaryOpNode.left.accept(this, level + 1);
                    binaryOpNode.right.accept(this, level + 1);
                }
                else if(rightType == NodeType.VOID)
                {
                    System.err.println("Error: Mismatch operand types in binary operation. Left operand is VOID at Line " + (binaryOpNode.right.row + 1) + ", Column " + binaryOpNode.right.col);
                    binaryOpNode.right = new TruthNode(binaryOpNode.right.row, binaryOpNode.right.col, "False");
                    binaryOpNode.left.accept(this, level + 1);
                    binaryOpNode.right.accept(this, level + 1);
                }
            }
        }
    }

    public void visit(AssignNode assignNode, int level) {

        boolean isInput = false; 

         if (assignNode.lhs != null && assignNode.rhs != null) {
            // Check if lhs is a declared variable
            if (assignNode.lhs instanceof VarDeclNode) {
                VarDeclNode varDeclNode = (VarDeclNode) assignNode.lhs;
                if (lookup(varDeclNode.name) == null) {
                    System.err.println("Error: Variable '" + varDeclNode.name + "' is not declared before assignment at line " + (assignNode.row + 1) + ", column " + assignNode.col);
                }
            } else if (assignNode.lhs instanceof VarNode) {
                VarNode varNode = (VarNode) assignNode.lhs;
                if (lookup(varNode.name) == null) {
                    System.err.println("Error: Variable '" + varNode.name + "' is not declared before assignment at line " + (assignNode.row + 1) + ", column " + assignNode.col);
                }
            } 

            if(assignNode.rhs instanceof FunCallNode)
            {
                String name = ((FunCallNode) assignNode.rhs).name;

                if(name.equals("input"))
                {
                    isInput = true;
                }
            }

            // Type checking and assignment validation
            if (isInt(assignNode.lhs)) {
                if (isInt(assignNode.rhs) || isInput) {
                    assignNode.lhs.accept(this, level + 1);
                    assignNode.rhs.accept(this, level + 1);
                } else {
                    System.err.println("Error: Mismatch RIGHT operand type in assignment operation. Expected integer at Line " + (assignNode.rhs.row + 1) + ", Column " + assignNode.rhs.col);
                    assignNode.rhs = new NumNode(assignNode.rhs.row, assignNode.rhs.col, "0");
                    assignNode.lhs.accept(this, level + 1);
                    assignNode.rhs.accept(this, level + 1);
                }
            } else if (isBool(assignNode.lhs)) {
                if (isBool(assignNode.rhs) || isInput) {
                    assignNode.lhs.accept(this, level + 1);
                    assignNode.rhs.accept(this, level + 1);
                } else {
                    System.err.println("Error: Mismatch RIGHT operand types in assignment operation at Line " + (assignNode.rhs.row + 1) + ", Column " + assignNode.rhs.col);
                    assignNode.rhs = new TruthNode(assignNode.rhs.row, assignNode.rhs.col, "True");
                    assignNode.lhs.accept(this, level + 1);
                    assignNode.rhs.accept(this, level + 1);
                }
            }
        }
    }


    public void visit(NumNode numNode, int level) {
        // No action needed for NumNode
    }

    public void visit(TruthNode truthNode, int level) {
        // No action needed for TruthNode
    }

    public void visit(IdNode idNode, int level) {
        if (lookup(idNode.name) == null) {
            System.err.println("Error: Variable '" + idNode.name + "' is not declared at line " + idNode.row + ", column " + idNode.col);
        }
    }

    public void visit(IfNode ifNode, int level) {
        if (ifNode.test != null) {
            if(isInt(ifNode.test) || isBool(ifNode.test)){
                ifNode.test.accept(this, level + 1);
            } else {
                System.err.println("Error: Non integer or boolean test condition in if statement at Line " + (ifNode.test.row + 1) + ", Column " + ifNode.test.col);
                ifNode.test = new TruthNode(ifNode.test.row, ifNode.test.col, "true");
                ifNode.test.accept(this, level + 1);
            }
        }
        
        ifNode.thenpart.accept(this, level + 1);

        if (ifNode.elsepart != null) {
            ifNode.elsepart.accept(this, level + 1);
        }
    }

    public void visit(WhileNode whileNode, int level) {
        if (whileNode.test != null) {
            if(isInt(whileNode.test) || isBool(whileNode.test)){
                whileNode.test.accept(this, level + 1);
            } else {
                System.err.println("Error: Non integer or boolean test condition in while statement at Line " + (whileNode.test.row + 1) + ", Column " + whileNode.test.col);
                whileNode.test = new TruthNode(whileNode.test.row, whileNode.test.col, "true");
                whileNode.test.accept(this, level + 1);
            }
        }
        whileNode.statements.accept(this, level + 1);
    }

    public void visit(FunProtoNode funProtoNode, int level) {
        // No action needed for FunProtoNode
    }

    public void visit(ReturnNode returnNode, int level) {
        if (returnNode.returnValue != null) {
            returnNode.returnValue.accept(this, level + 1);
        }
    }

    public void visit(ExpList expList, int level) {
        while (expList != null) {
            expList.head.accept(this, level + 1);
            expList = expList.tail;
        }
    }

    public void visit(TypeNode typeNode, int level) {
        // No action needed for TypeNode
    }

    public void visit(UnaryOpNode unaryOpNode, int level) {
        if (unaryOpNode.operand != null && isBool(unaryOpNode.operand)) {
            unaryOpNode.operand.accept(this, level + 1);
        } else {
            System.err.println("Error: Non boolean operand type in Unary operation at Line " + (unaryOpNode.operand.row + 1) + ", Column " + unaryOpNode.operand.col);
            unaryOpNode.operand = new TruthNode(unaryOpNode.operand.row, unaryOpNode.operand.col, "true");
        }
    }

    public void visit(FunCallNode funCallNode, int level) {
        ExpList argList = funCallNode.argList;
        ExpList paramList = functionParamList.get(funCallNode.name);

        if(!funCallNode.name.equals("output") && !funCallNode.name.equals("input"))
        {
            if (argList != null && paramList != null) {          
                while (argList != null && paramList != null) {
                    int argType = -1; 
                    int paramType = -1;

                    argType = getNodeType((Exp) argList.head);
                    paramType = getNodeType((Exp) paramList.head);

                    if (argType != paramType) {
                        System.err.println("Error: Mismatch argument type in function call at Line " + (funCallNode.row + 1) + ", Column " + funCallNode.col);
                        if (paramType == NodeType.INT) {
                            argList.head = new NumNode(argList.head.row, argList.head.col, "0");
                        } else if (paramType == NodeType.BOOL) {
                            argList.head = new TruthNode(argList.head.row, argList.head.col, "true");
                        }
                    }
                    argList = argList.tail;
                    paramList = paramList.tail;
                }
            } 
            
            if (argList != null) {
                System.err.println("Error: More arguments than expected in function call at Line " + (funCallNode.row + 1) + ", Column " + funCallNode.col);
            } else if (paramList != null) {
                System.err.println("Error: Expected more arguments in function call at Line " + (funCallNode.row + 1) + ", Column " + funCallNode.col);
            }

            if (funCallNode.argList != null) {
                funCallNode.argList.accept(this, level + 1);
            }
        }
    }

    public void visit(VarNode varNode, int level) {
        // No action needed for VarNode
    }

    public void visit(ArrNode arrNode, int level) {
        boolean isInt = false;
        if (arrNode.index != null) {
            isInt = isInt(arrNode.index);

            if(isInt){
                arrNode.index.accept(this, level + 1);
            } else {
                System.err.println("Error: Array index must be an integer at Line " + (arrNode.index.col + 1) + ", Column " + arrNode.index.row);
                arrNode.index = new NumNode(arrNode.row, arrNode.col, "0");
                arrNode.index.accept(this, level + 1);
            }
        }
    }

    public void visit(ArrDeclNode arrDeclNode, int level) {
        if(arrDeclNode.type.type == NodeType.VOID){
            System.err.println("Error: Array cannot be of type VOID at Line " + (arrDeclNode.row + 1) + ", Column " + arrDeclNode.col);
            arrDeclNode.type.type = NodeType.INT;
        }

        insert(arrDeclNode.name, arrDeclNode.type);
    }

    public void analyze(Absyn program) {
        System.out.println("Entering the global scope:");
        program.accept(this, 0);
        printCurrentScope();
        System.out.println("Leaving the global scope");
    }
}
