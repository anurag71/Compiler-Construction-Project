class Stmts implements Token
{
  Stmt st;
  Stmts sts;
  public Stmts(Stmt st, Stmts sts)
  {
    this.st = st;
    this.sts =sts;
  }


  public String toString(int t)
  {
    if(sts!=null){
      return "Stmts decl:\n" + st.toString(t) + " " + sts.toString(t) + "\n"; 
    }
    else{
      return "Stmts decl:\n" + st.toString(t) + "\n";
    }
  }
}