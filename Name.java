import javax.lang.model.type.DeclaredType;

class Name extends Token implements DeclarationChecking {
	String id;
	Expr ex;
	public Name(String id) {
		this.id = id;
		this.ex = null;
	}
	public Name(String id, Expr ex) {
		this.id = id;
		this.ex = ex;
	}
	public String toString() {
		return id + ( ex!=null ? "[" + ex.toString() + "]" : "" );
	}
	
	String printSpaces(int number) {
		String spaces = "";
		for (int i = 0; i < number; i++) {
			spaces += "\t";
		}
		return spaces;
	}
	
	private boolean hasIndex() {
	    return ex != null;
	  }

	  public boolean isArray() throws Exception {
	    return table.getVar(id).isArray() && !hasIndex();
	  }
	  public boolean isFinal() throws Exception {
	    return table.getVar(id).isFinal();
	  }
	  public boolean isFunction() throws Exception {
	    return table.getVar(id).isFunction();
	  }
	  
	  
	  public SymTable.DeclarationType typeCheck() throws Exception {
	    if (!hasIndex() || ex.typeCheck().equals(SymTable.DeclarationType.INT))
	      return table.getVar(id).typeCheck();
	    else 
	      throw new Exception("array index must be an INT!");
	  }
}