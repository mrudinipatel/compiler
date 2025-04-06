package absyn;

public class WhileNode extends Exp {
    public Exp test; 
    public Exp statements;

    public WhileNode(int row, int col, Exp test, Exp statements) {
        this.row = row;
        this.col = col;
        this.test = test;
        this.statements = statements;
    }

    @Override
    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }
}
