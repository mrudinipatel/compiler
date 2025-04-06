package absyn;

public class TypeNode extends Absyn {
    public static final int INT  = 0;
    public static final int BOOL = 1;
    public static final int VOID = 2;

    public int type;

    public TypeNode(int row, int col, int type) {
        this.row = row;
        this.col = col;
        this.type = type;
    }

    @Override
    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }
}
