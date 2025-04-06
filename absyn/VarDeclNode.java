package absyn;

public class VarDeclNode extends Exp {
  public NodeType type;
  public String name;
  public VarDeclNode( int col, int row, NodeType type, String name ) {
    this.col = col;
    this.row = row;
    this.type = type;
    this.name = name;
  }

  @Override
    public void accept( AbsynVisitor visitor, int level ) {
        visitor.visit( this, level );
    }
}