import java.util.ArrayList;

class Memberdecls extends Token {
  ArrayList<Fielddecl> fdecls;
  ArrayList<Methoddecl> mdecls;
  public Memberdecls(Fielddecl fdecls, Memberdecls mdecls) {
    mdecls.fdecls.add(0, fdecls);
    this.fdecls = mdecls.fdecls;
    this.mdecls = mdecls.mdecls;
  }
  public Memberdecls(ArrayList<Fielddecl> fdeclarray, ArrayList<Methoddecl> mdeclarray) {
    this.fdecls = fdeclarray;
    this.mdecls = mdeclarray;
  }
  public Memberdecls(ArrayList<Methoddecl> mdeclarray) {
    this.fdecls = new ArrayList<Fielddecl>();
    this.mdecls = mdeclarray;
  }

  public String toString(int spaces) {
    String result = "";
    for (Fielddecl fdecl: fdecls)
      result += fdecl.toString(spaces) + "\n";
    for (Methoddecl mdecl: mdecls)
      result += mdecl.toString(spaces) + "\n";
    return result;
  }
  
  String printSpaces(int number) {
		String spaces = "";
		for (int i = 0; i < number; i++) {
			spaces += "\t";
		}
		return spaces;
	}
  
  public void typeCheck() throws Exception {
	    table.createScope();
	    for (Fielddecl f: fdecls)
	      f.typeCheck();
	    for (Methoddecl m: mdecls)
	      m.typeCheck();
	    table.destroyScope();
	  }
}