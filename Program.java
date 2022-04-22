class Program extends Token {
  String id;
  Memberdecls membdecls;
  public Program(String id, Memberdecls membdecls) {
    this.id = id;
    this.membdecls = membdecls;
    table = new SymTable();
  }
  public String toString(int spaces) {
    return "class " + id + " {\n" + membdecls.toString(spaces+1) + "}";
  }
  
  String printSpaces(int number) {
		String spaces = "";
		for (int i = 0; i < number; i++) {
			spaces += "\t";
		}
		return spaces;
	}
  
  public void typeCheck() throws Exception {
	    membdecls.typeCheck();
	  }
}