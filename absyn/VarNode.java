package absyn;

public class VarNode extends Exp {
  public String name;

  public VarNode( int col, int row, String name ) {
    this.col = col;
    this.row = row;
    this.name = name;
  }

  @Override
  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
}
