import absyn.*;

public class ShowTreeVisitor implements AbsynVisitor {

  final static int SPACES = 4;

  private void indent(int level) {
    for (int i = 0; i < level * SPACES; i++) System.out.print(" ");
  }

  public void visit(BinaryOpNode binaryOpNode, int level) {
    indent(level);
    System.out.print("BinaryOp: ");
    switch (binaryOpNode.op) {
      case BinaryOpNode.PLUS:
        System.out.println("+");
        break;
      case BinaryOpNode.MINUS:
        System.out.println("-");
        break;
      case BinaryOpNode.TIMES:
        System.out.println("*");
        break;
      case BinaryOpNode.OVER:
        System.out.println("/");
        break;
      case BinaryOpNode.EQ:
        System.out.println("=");
        break;
      case BinaryOpNode.LT:
        System.out.println("<");
        break;
      case BinaryOpNode.GT:
        System.out.println(">");
        break;
      case BinaryOpNode.LEQT:
        System.out.println("<=");
        break;
      case BinaryOpNode.GEQT:
        System.out.println(">=");
        break;
      case BinaryOpNode.NEQ:
        System.out.println("!=");
        break;
      case BinaryOpNode.AND:
        System.out.println("AND");
        break;
      case BinaryOpNode.OR:
        System.out.println("OR");
        break;
      default:
        System.out.println("Unknown operator");
    }
    if (binaryOpNode.left != null) {
        binaryOpNode.left.accept(this, level + 1);
    }
    if (binaryOpNode.right != null) {
        binaryOpNode.right.accept(this, level + 1);
    }
  }

  public void visit(AssignNode assignNode, int level) {
    indent(level);
    System.out.println("AssignStmt:");
    if(assignNode.lhs != null){
      assignNode.lhs.accept(this, level + 1);
    }
    else if (assignNode.lhs != null);
    {
      assignNode.rhs.accept(this, level + 1);
    }
  }

  public void visit(NumNode numNode, int level) {
    indent(level);
    System.out.println("Num: " + numNode.value);
  }

  public void visit(TruthNode truthNode, int level) {
    indent(level);
    System.out.println("Truth: " + truthNode.value);
  }

  public void visit(IdNode idNode, int level) {
    indent(level);
    System.out.println("Id: " + idNode.name);
  }

  public void visit(IfNode ifNode, int level) {
    indent(level);
    System.out.println("IfStmt:");
    if(ifNode.test != null){
      ifNode.test.accept(this, level + 1);
    }
    ifNode.thenpart.accept(this, level + 1);
    if (ifNode.elsepart != null) {
      ifNode.elsepart.accept(this, level + 1);
    }
  }

  public void visit(WhileNode whileNode, int level) {
    indent(level);
    System.out.println("WhileStmt:");
    if(whileNode.test != null){
      whileNode.test.accept(this, level + 1);
    }
    whileNode.statements.accept(this, level + 1);
  }

  public void visit(FunDeclNode funDeclNode, int level) {
    indent(level);
    System.out.println("FunDecl: " + funDeclNode.type + " " + funDeclNode.name);
    if (funDeclNode.params != null) {
        funDeclNode.params.accept(this, level + 1);
    }
    funDeclNode.body.accept(this, level + 1);
  }

  public void visit(FunProtoNode funProtoNode, int level) {
    indent(level);
    System.out.println("FunProto: " + funProtoNode.type + " " + funProtoNode.name);
    if (funProtoNode.params != null) {
        funProtoNode.params.accept(this, level + 1);
    }
  }

  public void visit(ReturnNode returnNode, int level) {
    indent(level);
    System.out.println("ReturnStmt:");
    if (returnNode.returnValue != null) {
      returnNode.returnValue.accept(this, level + 1);
    }
  }

  public void visit(CompoundStmtNode compoundStmtNode, int level) {
    indent(level);
    System.out.println("CompoundStmt:");
    if (compoundStmtNode.localDeclarations != null) {
        compoundStmtNode.localDeclarations.accept(this, level + 1);
    }
    if (compoundStmtNode.statementList != null) {
        compoundStmtNode.statementList.accept(this, level + 1);
    }
  }

  public void visit(VarDeclNode varDeclNode, int level) {
    indent(level);
    System.out.println("VarDecl: " + varDeclNode.type + " " + varDeclNode.name);
  }

  public void visit(ParamNode paramNode, int level) {
    indent(level);
    System.out.println("Param: " + paramNode.type + " " + paramNode.name);
  }

  // New visit method for ExpList
  public void visit(ExpList expList, int level) {
    indent(level);
    System.out.println("ExpList:");
    while (expList != null) {
      expList.head.accept(this, level + 1);
      expList = expList.tail;
    }
  }

  // New visit method for TypeNode
  public void visit(TypeNode typeNode, int level) {
    indent(level);
    System.out.print("Type: ");
    switch (typeNode.type) {
      case TypeNode.INT:
        System.out.println("INT");
        break;
      case TypeNode.BOOL:
        System.out.println("BOOL");
        break;
      case TypeNode.VOID:
        System.out.println("VOID");
        break;
      default:
        System.out.println("Unknown Type");
    }
  }

  // New visit method for UnaryOpNode
  public void visit(UnaryOpNode unaryOpNode, int level) {
    indent(level);
    System.out.print("UnaryOp: ");
    switch (unaryOpNode.op) {
      case UnaryOpNode.UBW:
        System.out.println("UBW");
        break;
      default:
        System.out.println("Unknown operator");
    }
    unaryOpNode.operand.accept(this, level + 1);
  }

  // New visit method for FunCallNode
  public void visit(FunCallNode funCallNode, int level) {
    indent(level);
    System.out.println("FunCall: " + funCallNode.name);
    if (funCallNode.argList != null) {
        funCallNode.argList.accept(this, level + 1);
    }
  }

  // New visit method for VarNode
  public void visit(VarNode varNode, int level) {
    indent(level);
    System.out.println("Var: " + varNode.name);
  }

  // New visit method for ArrNode
  public void visit(ArrNode arrNode, int level) {
    indent(level);
    System.out.println("Arr: " + arrNode.name);
    arrNode.index.accept(this, level + 1);
  }

  // New visit method for ArrDeclNode
  public void visit(ArrDeclNode arrDeclNode, int level) {
    indent(level);
    System.out.println("ArrDecl: " + arrDeclNode.type + " " + arrDeclNode.name);
  }

  // New visit method for NodeType
  public void visit(NodeType nodeType, int level) {
    indent(level);
    System.out.print("NodeType: ");
    switch (nodeType.type) {
      case NodeType.INT:
        System.out.println("INT");
        break;
      case NodeType.BOOL:
        System.out.println("BOOL");
        break;
      case NodeType.VOID:
        System.out.println("VOID");
        break;
      default:
        System.out.println("Unknown Type");
    }
  }
}
