package absyn;

public class ArrNode extends Exp {
  public String name;
  public Exp index;

  public ArrNode( int row, int col, String name, Exp index ) {
    this.col = col;
    this.row = row;
    this.name = name;
    this.index = index;
  }

  @Override
    public void accept( AbsynVisitor visitor, int level ) {
        visitor.visit( this, level );
    }
}
