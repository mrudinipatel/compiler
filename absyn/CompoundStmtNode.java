package absyn;

public class CompoundStmtNode extends Exp {
    public ExpList localDeclarations;
    public ExpList statementList;

    public CompoundStmtNode(int row, int col, ExpList localDeclarations, ExpList statementList) {
        this.row = row;
        this.col = col;
        this.localDeclarations = localDeclarations;
        this.statementList = statementList;
    }
    
    @Override
    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }
}
