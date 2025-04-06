package absyn;

public class NodeType extends Absyn {
    public final static int VOID = 0;
    public final static int INT = 1;
    public final static int BOOL = 2;

    public int type;

    public NodeType(int row, int col, int type) {
        this.row = row;
        this.col = col;
        this.type = type;
    }

    @Override
    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }

    @Override
    public String toString() {
        switch (type) {
            case VOID:
                return "void";
            case INT:
                return "int";
            case BOOL:
                return "bool";
            default:
                return "unknown";
        }
    }
}
