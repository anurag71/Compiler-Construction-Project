class ArgdeclList implements Token
{
  Argdecl adecl;
  ArgdeclList adecllist;
  public ArgdeclList(Argdecl adecl, ArgdeclList adecllist)
  {
    this.adecl = adecl;
    this.adecllist = adecllist;
  }

  public ArgdeclList(Argdecl adecl)
  {
    this.adecl=adecl;
    this.adecllist = null;
  }


  public String toString(int t)
  {
    if(adecllist!=null){
      return ("ArgdeclList Declaration:\n" + adecl.toString(t) +"\t\t" + adecllist.toString(t)+"\n");
    }
    else{
      return ("ArgdeclList Declaration:\n" + adecl.toString(t)+"\n");
    }
  }
}