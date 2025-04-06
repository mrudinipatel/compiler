package absyn;

public class NumNode extends Exp {
    public String value;

    public NumNode(int row, int col, String value) {
        this.row = row;
        this.col = col;
        this.value = value;
    }

    @Override
    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }
}
