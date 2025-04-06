package absyn;

public class ReturnNode extends Exp {
    public Exp returnValue;

    public ReturnNode(int row, int col, Exp returnValue) {
        this.row = row;
        this.col = col;
        this.returnValue = returnValue;
    }

    @Override
    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }
}