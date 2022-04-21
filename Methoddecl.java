import java.util.ArrayList;

class Methoddecl extends Token {
	ArrayList<Argdecl> adecls;
	ArrayList<Fielddecl> fdecls;
	ArrayList<Stmt> stms;
	String type, id;
	boolean hasSemi;
	public Methoddecl(Type type, String id, ArrayList<Argdecl> adecls, ArrayList<Fielddecl> fdecls, ArrayList<Stmt> stms, boolean hasSemi) {
		this.type = type.toString();
		this.id = id;
		this.adecls = adecls;
		this.fdecls = fdecls;
		this.stms = stms;
		this.hasSemi = hasSemi;
	}
	public Methoddecl(String type, String id, ArrayList<Argdecl> adecls, ArrayList<Fielddecl> fdecls, ArrayList<Stmt> stms, boolean hasSemi) {
		this.type = type;
		this.id = id;
		this.adecls = adecls;
		this.fdecls = fdecls;
		this.stms = stms;
		this.hasSemi = hasSemi;
	}

	public String toString(int spaces) {
		String args = "";
		for (Argdecl argdecl: adecls) {
			args += argdecl.toString() + ", ";
		} args = args.substring(0, args.length() > 0 ? args.length()-2 : 0);

		String result = printSpaces(spaces) + type + " " + id + "(" + args + ")" + " {\n";
		for (Fielddecl f: fdecls) {
			result += f.toString(spaces+1) + "\n";
		}
		for (Stmt st: stms) {
			result += st.toString(spaces+1) + "\n";
		} result += printSpaces(spaces) + "}" + ( hasSemi ? ";" : "" );
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
	    // create list of params for function definition
	    ArrayList<SymTable.Arg> params = new ArrayList<SymTable.Arg>();
	    for (Argdecl arg: adecls)
	      params.add(table.newArg(getType(arg.type.toString()), arg.isArray));
	    // define function id in scope with argdecls
	    if (!table.add(id, "function", getType(type.toString()), params))
	      throw new RedefinedVariableException(id);
	    // step into new method scope
	    // add vars to scope
	    table.createScope();
	    for (Argdecl arg: adecls)
	      arg.typeCheck();
	    for (Fielddecl f: fdecls)
	      f.typeCheck();
	    // verify whether return is needed for function
	    boolean needsReturn = !getType(type.toString()).equals(SymTable.DeclarationType.VOID);
	    // typecheck inner statements
	    for (Stmt stmt: stms) {
	      if (stmt.isReturn()) {
	        // satisfy return requirement
	        needsReturn = false;
	        // check that returntype == function type
	        if (!canConvertFrom(stmt.typeCheck(), getType(type.toString())))
	          throw new ReturnTypeMismatchException(id, stmt.typeCheck(), getType(type.toString()));
	      } else stmt.typeCheck();
	    }
	    // throw exception if a return statement was not found
	    if (needsReturn)
	      throw new MissingReturnException(id);
	    // step out of method scope
	    table.destroyScope();
	  }
}