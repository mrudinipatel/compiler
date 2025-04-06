package absyn;

public class FunProtoNode extends Exp {
    public NodeType type;
    public String name;
    public ExpList params;

    public FunProtoNode(int row, int col, NodeType type, String name, ExpList params) {
        this.row = row;
        this.col = col;
        this.type = type;
        this.name = name;
        this.params = params;
    }

    @Override
    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }
}
