class Argdecls implements Token
{
  ArgdeclList adecllist;
  public Argdecls(ArgdeclList adecllist)
  {
    this.adecllist = adecllist;
  }

  public String toString(int t)
  {
      if(adecllist!=null){
        return ("Return Declaration:\n" + adecllist.toString(t) + "\n");
      }
      else{
        return "";
      }
  }
}
