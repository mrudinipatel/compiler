package absyn;

public class IfNode extends Exp {
    public Exp test;
    public Exp thenpart;
    public Exp elsepart;

    public IfNode(int row, int col, Exp test, Exp thenpart, Exp elsepart) {
        this.row = row;
        this.col = col;
        this.test = test;
        this.thenpart = thenpart;
        this.elsepart = elsepart;
    }

    @Override
    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }
}