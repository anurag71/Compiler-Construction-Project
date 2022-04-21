class Args implements Token
{
  Args args;
  Expr ex;
  public Args(Expr ex, Args args)
  {
    this.ex = ex;
    this.args = args;
  }

  public Args(Expr ex)
  {
    this.ex=ex;
    this.args = null;
  }


  public String toString(int t)
  {
    if(args!=null){
      return ("Args Declaration:\n" + ex.toString(t) +"\t\t" + args.toString(t)+"\n");
    }
    else{
      return ("Args Declaration:\n" + ex.toString(t)+"\n");
    }
  }
}
