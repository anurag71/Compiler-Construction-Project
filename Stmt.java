import java.util.ArrayList;

class Stmt extends Token{
	int toStringreturnnumber;
	String id;
	ArrayList<Fielddecl> fdecls;
	Name nm;
	Stmt elsestmt;
	ArrayList<Stmt> stms;
	ArrayList funclist;
	Stmt stm;
	String unryop;
	Expr ex;
	boolean isLoop, hasSemi;
	ArrayList<Expr> args;


	public Stmt(Expr ex, Stmt stm, Stmt elsest) {
		this.ex = ex;
		this.stm = stm;
		this.elsestmt = elsest;
		this.toStringreturnnumber = 0;
	}
	public Stmt(String func, ArrayList funclist) {
		this.id = func;
		this.funclist = funclist;
		this.toStringreturnnumber = ( func == "read" ? 1 : ( func == "print" ? 2 : 3 ) );
	}
	public Stmt(Expr ex, Stmt stm) {
		this.ex = ex;
		this.stm = stm;
		this.toStringreturnnumber = 4;
	}
	public Stmt(Name nm, Expr e) {
		this.nm = nm;
		this.ex = e;
		this.toStringreturnnumber = 5;
	}
	public Stmt(Name nm, String unryop) {
		this.nm = nm;
		this.unryop = unryop;
		this.toStringreturnnumber = 6;
	}
	public Stmt(String id, ArrayList<Expr> args, boolean func) {
		this.id = id;
		this.args = args;
		this.toStringreturnnumber = 7;
	}
	public Stmt(String id) {
		this.id = id;
		this.toStringreturnnumber = 8;
	}
	public Stmt() {
		this.toStringreturnnumber = 9;
	}
	public Stmt(ArrayList<Fielddecl> fdecls, ArrayList<Stmt> stms, boolean hasSemi) {
		this.fdecls = fdecls;
		this.stms = stms;
		this.hasSemi = hasSemi;
		this.toStringreturnnumber = 10;
	}
	public Stmt(Expr ex) {
		this.ex = ex;
		this.toStringreturnnumber = 11;
	}

	public String toString(int spaces) {
		String resultliststr="";
		String resultstr="";
		if(this.toStringreturnnumber==0) {
			return printSpaces(spaces) +
					"if (" + ex.toString() + ")\n" +
					( stm.toStringreturnnumber == 2 ? stm.toString(spaces) : printSpaces(spaces) +"{\n" + stm.toString(spaces+1) + "\n"+ printSpaces(spaces) + "}" ) +
					( elsestmt != null ? "\n" + printSpaces(spaces) + "else\n" + ( elsestmt.toStringreturnnumber == 2 ? elsestmt.toString(spaces) : printSpaces(spaces) +"{\n" + elsestmt.toString(spaces+1) + "\n"+ printSpaces(spaces) + "}") : "" );    
		}
		else if(this.toStringreturnnumber==1){
			resultliststr = "";
			for (Name nm: (ArrayList<Name>)funclist) {
				resultliststr += nm.toString() + ", ";
			} 
			resultliststr = resultliststr.substring(0, resultliststr.length() > 0 ? resultliststr.length() - 2 : 0);
			return printSpaces(spaces) + id + "(" + resultliststr + ");";
		}
		else if(this.toStringreturnnumber==2){
			resultliststr = "";
			for (Expr e: (ArrayList<Expr>)funclist) {
				resultliststr += e.toString() + ", ";
			} 
			resultliststr = resultliststr.substring(0, resultliststr.length() > 0 ? resultliststr.length() - 2 : 0);
			return printSpaces(spaces) + id + "(" + resultliststr + ");";
		}
		else if(this.toStringreturnnumber==3){
			resultliststr = "";
			for (Expr e: (ArrayList<Expr>)funclist) {
				resultliststr += e.toString() + ", ";
			} 
			resultliststr = resultliststr.substring(0, resultliststr.length() > 0 ? resultliststr.length() - 2 : 0);
			return printSpaces(spaces) + id + "(" + resultliststr + ");";
		}
		else if(this.toStringreturnnumber==4){
			return printSpaces(spaces) +
					"while (" + ex.toString() + ")\n" + stm.toString(stm.toStringreturnnumber == 2 ? spaces : spaces+1) + "\n";
		}
		else if(this.toStringreturnnumber==5){
			return printSpaces(spaces) +
					nm.toString() + " = " + ex.toString() + ";";
		}
		else if(this.toStringreturnnumber==6){
			return printSpaces(spaces) + nm.toString() + unryop + ";";
		}
		else if(this.toStringreturnnumber==7){
			resultliststr = "";
			for (Expr ex: args) {
				resultliststr += ex.toString() + ", ";
			} 
			resultliststr = resultliststr.substring(0, resultliststr.length() > 0 ? resultliststr.length() - 2 : 0);
			return printSpaces(spaces) + id + "(" + resultliststr + ");";
		}
		else if(this.toStringreturnnumber==8){
			return printSpaces(spaces) + id + "();";
		}
		else if(this.toStringreturnnumber==9){
			return printSpaces(spaces) + "return;";
		}
		else if(this.toStringreturnnumber==10){
			resultstr = "";
			for (Fielddecl f: fdecls) {
				resultstr += f.toString(spaces+1) + "\n";
			}
			for (Stmt st: stms) {
				resultstr += st.toString(spaces+1) + "\n";
			}
			return printSpaces(spaces) + "{\n" + resultstr + printSpaces(spaces) + "}";
		}

		else if(this.toStringreturnnumber==11){
			return printSpaces(spaces) + "return " + ex.toString() + ";";
		}
		else{
			return "";
		}
		}

		String printSpaces(int number) {
			String spaces = "";
			for (int i = 0; i < number; i++) {
				spaces += "\t";
			}
			return spaces;
		}
		
		public boolean isReturn() {
		    return this.toStringreturnnumber == 9 | this.toStringreturnnumber == 11;
		  }

		  public SymTable.DeclarationType typeCheck() throws Exception {
		    switch (this.toStringreturnnumber) {
		      case 0: // if 
		        if (!canBeBool(ex))
		          throw new ConditionExpectedException(ex.typeCheck(), ex.isArray());
		        table.createScope();
		        stm.typeCheck();
		        table.destroyScope();
		        if ( elsestmt != null ) {
		          table.createScope();
		          stm.typeCheck();
		          table.destroyScope();
		        }
		        break;
		      case 1: // function call with args
		        SymTable.Var fun = table.getVar(id);
		        if (!fun.isFunction())
		          throw new UndefinedFunctionException(id);
		        if (fun.arguments.size() != args.size())
		          throw new ArgListException(id);
		        for (int i = 0; i < args.size(); i++) {
		          if (!canAssignFrom(args.get(i), fun.arguments.get(i)))
		            throw new ArgListException(
		              id, i,
		              fun.arguments.get(i).typeCheck(), 
		              fun.arguments.get(i).isArray(),
		              args.get(i).typeCheck(), 
		              args.get(i).isArray()
		            );
		        }
		        break;
		      case 2: // statement block
		        table.createScope();
		        for (Fielddecl f: fdecls)
		          f.typeCheck();
		        for (Stmt st: stms)
		          st.typeCheck();
		        table.destroyScope();
		        break;          
		      case 3: // while
		        if (!canBeBool(ex))
		          throw new ConditionExpectedException(ex.typeCheck(), ex.isArray());
		        table.createScope();
		        stm.typeCheck();
		        table.destroyScope();
		        break;
		      case 4: // assignment
		        if (!canAssignFrom(this.ex, this.nm))
		          throw new AssignmentTypeMismatchException(
		            nm.typeCheck(),
		            nm.isArray(),
		            ex.typeCheck(),
		            ex.isArray()
		          );
		        if (table.getVar(nm.id).isFinal())
		          throw new CannotModifyFinalException(nm.id);
		        break;
		      case 5: // read
		        for (Name n: (ArrayList<Name>)funclist) {
		          if (n.isArray() || n.isFinal())
		            throw new Exception("cannot read an array or final.");
		        }
		        break;
		      case 6: case 7:// print and printline
		        for (Expr e: (ArrayList<Expr>)funclist) {
		          e.typeCheck();
		          if (e.isArray() || e.isFunction())
		            throw new Exception("cannot print an array or function.");
		        }
		        break;
		      case 8: // INC and DEC
		        if (!canBeFloat(nm))
		          throw new UnaryArithmeticException();
		        if (table.getVar(nm.id).isFinal())
		          throw new CannotModifyFinalException(nm.id);
		        break;
		      case 9: // return with value
		        return ex.typeCheck();
		      case 10: // function call no args
		        if (!table.getVar(id).isFunction())
		          throw new UndefinedFunctionException(id);
		        break;
		      case 11: // return;
		        return SymTable.DeclarationType.VOID;
		    }
		    return null;
		  }
	}


