class Printlinelist implements Token
{
  Printlist pl;
  public Printlinelist(Printlist pl)
  {
    this.pl = pl;
  }


  public String toString(int t)
  {
      return ("Printlinelist Declaration:\n" + pl.toString(t) +"\n");
  }
}
