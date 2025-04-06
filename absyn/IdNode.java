package absyn;

public class IdNode extends Exp {
    public String name;

    public IdNode(int row, int col, String name) {
        this.row = row;
        this.col = col;
        this.name = name;
    }

    @Override
    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }
}
