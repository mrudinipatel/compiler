package absyn;

public class ParamNode extends Exp {
    public NodeType type; 
    public String name;

    public ParamNode(int row, int col, NodeType type, String name) {
        this.row = row;
        this.col = col;
        this.type = type;
        this.name = name;
    }

    @Override
    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }
}
