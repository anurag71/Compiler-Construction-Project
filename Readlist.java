class Readlist implements Token
{
  Name n;
  Readlist rl;
  public Readlist(Name n, Readlist rl)
  {
    this.n = n;
    this.rl = rl;
  }

  public Readlist(Name n)
  {
    this.n=n;
    this.rl = null;
  }


  public String toString(int t)
  {
    if(rl!=null){
      return ("Readlist Declaration:\n" + n.toString(t) +"\t\t" + rl.toString(t)+"\n");
    }
    else{
      return ("Readlist Declaration:\n" + n.toString(t)+"\n");
    }
  }
}
