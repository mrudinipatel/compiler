package absyn;

public class AssignNode extends Exp {
    public Exp lhs;
    public Exp rhs;

    // Constructor
    public AssignNode(int row, int col, Exp lhs, Exp rhs) {
        this.row = row;
        this.col = col;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }
}
