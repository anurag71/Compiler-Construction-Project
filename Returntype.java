class Returntype implements Token
{
  Type tp;
  public Returntype(Type tp)
  {
    this.tp = tp;
  }

  public Returntype(){
    this.tp=null;
  }

  public String toString(int t)
  {
      if(tp!=null){
        return ("Return Declaration:\n" + tp.toString(t) + "\n");
      }
      else{
        return ("Return Declaration:\n" + "void \n");
      }
  }
}
