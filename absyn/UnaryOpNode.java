package absyn;

public class UnaryOpNode extends Exp {
    // Operator Constants
    public static final int UBW = 0;

    public int op;
    public Exp operand;

    public UnaryOpNode(int row, int col, int op, Exp operand) {
        this.row = row;
        this.col = col;
        this.op = op;
        this.operand = operand;
    }

    @Override
    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit(this, level);
    }
}