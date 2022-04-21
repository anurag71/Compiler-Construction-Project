class Methoddecls implements Token
{
  Methoddecl mdecl;
  Methoddecls mdecls;
  public Methoddecls(Methoddecl mdecl, Methoddecls mdecls)
  {
    this.mdecl = mdecl;
    this.mdecls = mdecls;
  }

  public Methoddecls(Methoddecl mdecl)
  {
    this.mdecl=mdecl;
    this.mdecls = null;
  }


  public String toString(int t)
  {
    if(mdecls!=null){
      return ("Methoddecls Declaration:\n" + mdecl.toString(t) +"\t\t" + mdecls.toString(t)+"\n");
    }
    else{
      return ("Methoddecls Declaration:\n" + mdecl.toString(t)+"\n");
    }
  }
}
