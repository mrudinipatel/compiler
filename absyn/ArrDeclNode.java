package absyn;

public class ArrDeclNode extends Exp {
  public String name;
  public NodeType type;
  public String size;

  public ArrDeclNode( int col, int row, NodeType type, String name, String size) {
    this.col = col;
    this.row = row;
    this.type = type;
    this.name = name;
    this.size = size;
  }

  @Override
  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
}
