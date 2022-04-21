class Optionalexpr implements Token
{
  Expr ex;
  public Optionalexpr(Expr ex)
  {
    this.ex=ex;
  }

  public Optionalexpr(){
    this.ex=null;
  }

  public String toString(int t)
  {
    if(ex==null){
      return "";
    }
    return ("Optionalexpr declaration:\n" + ex.toString(t));
  }
}