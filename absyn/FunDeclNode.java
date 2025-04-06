package absyn;

public class FunDeclNode extends Exp {
    public NodeType type;
    public String name;
    public ExpList params;
    public Exp body;
    public int funaddr;

    public FunDeclNode(int row, int col, NodeType type, String name, ExpList params, Exp body) {
        this.row = row;
        this.col = col;
        this.type = type;
        this.name = name;
        this.params = params;
        this.body = body;
    }

    @Override
    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }
}