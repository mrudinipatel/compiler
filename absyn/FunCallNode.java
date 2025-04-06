package absyn;

public class FunCallNode extends Exp {
  public String name;
  public ExpList argList;
  
  public FunCallNode( int row, int col, String name, ExpList argList ) {
    this.row = row;
    this.col = col;
    this.name = name;
    this.argList = argList;
  }

  @Override
  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
}