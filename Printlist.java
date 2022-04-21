class Printlist implements Token
{
  Expr ex;
  Printlist pl;
  public Printlist(Expr ex, Printlist pl)
  {
    this.ex = ex;
    this.pl = pl;
  }

  public Printlist(Expr ex)
  {
    this.ex=ex;
    this.pl = null;
  }


  public String toString(int t)
  {
    if(pl!=null){
      return ("Printlist Declaration:\n" + ex.toString(t) +"\t\t" + pl.toString(t)+"\n");
    }
    else{
      return ("Printlist Declaration:\n" + ex.toString(t)+"\n");
    }
  }
}
