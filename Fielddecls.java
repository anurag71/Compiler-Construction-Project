class Fielddecls implements Token
{
  Fielddecl fdecl;
  Fielddecls fdecls;
  public Fielddecls(Fielddecl fdecl, Fielddecls fdecls)
  {
    this.fdecl = fdecl;
    this.fdecls = fdecls;
  }

  public Fielddecls(Fielddecl fdecl)
  {
    this.fdecl=fdecl;
    this.fdecls = null;
  }


  public String toString(int t)
  {
    if(fdecls!=null){
      return ("Fielddecls Declaration:\n" + fdecl.toString(t) +"\t\t" + fdecls.toString(t)+"\n");
    }
    else{
      return ("Fielddecls Declaration:\n" + fdecl.toString(t)+"\n");
    }
  }
}
