package absyn;

public interface AbsynVisitor {

    // Expressions
    void visit(UnaryOpNode unaryOpNode, int level);
    void visit(BinaryOpNode binaryOpNode, int level);
    void visit(AssignNode assignNode, int level);
    void visit(NumNode numNode, int level);
    void visit(TruthNode numNode, int level);
    void visit(IdNode idNode, int level);

    // Control Structures
    void visit(IfNode ifNode, int level);
    void visit(WhileNode whileNode, int level);

    // Functions
    void visit(FunDeclNode funDeclNode, int level);
    void visit(FunProtoNode funProtoNode, int level);
    void visit (FunCallNode funCallNode, int level);

    // Statements
    void visit(ReturnNode returnNode, int level);
    void visit(CompoundStmtNode compoundStmtNode, int level);

    // Declarations
    void visit(VarDeclNode varDeclNode, int level);
    void visit(VarNode varNode, int level);
    void visit(ParamNode paramNode, int level);

    // Arrays
    void visit(ArrDeclNode arrDeclNode, int level);
    void visit(ArrNode arrNode, int level);

    void visit(TypeNode typeNode, int level);
    void visit(ExpList expList, int level);
    
    // Types
    void visit(NodeType nodeType, int level);

}
