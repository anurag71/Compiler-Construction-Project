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
//		
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
		      case 7: // function call with args
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
		      case 10: // statement block
		        table.createScope();
		        for (Fielddecl f: fdecls)
		          f.typeCheck();
		        for (Stmt st: stms)
		          st.typeCheck();
		        table.destroyScope();
		        break;          
		      case 4: // while
		        if (!canBeBool(ex))
		          throw new ConditionExpectedException(ex.typeCheck(), ex.isArray());
		        table.createScope();
		        stm.typeCheck();
		        table.destroyScope();
		        break;
		      case 5: // assignment
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
		      case 1: // read
		        for (Name n: (ArrayList<Name>)funclist) {
		          if (n.isArray() || n.isFinal())
		            throw new Exception("cannot read an array or final.");
		        }
		        break;
		      case 2: case 3:// print and printline
		        for (Expr e: (ArrayList<Expr>)funclist) {
		          e.typeCheck();
		          if (e.isArray() || e.isFunction())
		            throw new Exception("cannot print an array or function.");
		        }
		        break;
		      case 6: // INC and DEC
		        if (!canBeFloat(nm))
		          throw new UnaryArithmeticException();
		        if (table.getVar(nm.id).isFinal())
		          throw new CannotModifyFinalException(nm.id);
		        break;
		      case 11: // return with value
		        return ex.typeCheck();
		      case 8: // function call no args
		        if (!table.getVar(id).isFunction())
		          throw new UndefinedFunctionException(id);
		        break;
		      case 9: // return;
		        return SymTable.DeclarationType.VOID;
		    }
		    return null;
		  }
	
//	int typeNumber;
//	  boolean isLoop, hasSemi;
//	  ArrayList<Fielddecl> fielddecls;
//	  ArrayList<Stmt> stmts;
//	  ArrayList funclist;
//	  ArrayList<Expr> args;
//	  Expr expr;
//	  Stmt stmt, elsestmt;
//	  Name name;
//	  String id;
//	  String unaryOp;
//	  
//	  public Stmt(Expr e, Stmt st, Stmt elsest) { // PROD 1 - if
//	    expr = e;
//	    stmt = st;
//	    elsestmt = elsest;
//	    typeNumber = 0;
//	  }
//	  public Stmt(String id, ArrayList<Expr> ag, boolean func) { // PROD 8 - id(args)
//	    this.id = id;
//	    args = ag;
//	    typeNumber = 1;
//	  }
//	  public Stmt(ArrayList<Fielddecl> fs, ArrayList<Stmt> sts, boolean semi) { // PROD 12 - block
//	    fielddecls = fs;
//	    stmts = sts;
//	    hasSemi = semi;
//	    typeNumber = 2;
//	  }
//	  public Stmt(Expr e, Stmt st) { // PROD 2 - while
//	    expr = e;
//	    stmt = st;
//	    typeNumber = 3;
//	  }
//	  public Stmt(Name n, Expr e) { // PROD 3 - assign
//	    name = n;
//	    expr = e;
//	    typeNumber = 4;
//	  }
//	  public Stmt(String func, ArrayList lst) { // PROD 4,5,6 - read, print, printline
//	    id = func;
//	    funclist = lst;
//	    typeNumber = ( func == "read" ? 5 : ( func == "print" ? 6 : 7 ) );
//	  }
//	  public Stmt(Name n, String unary) { // PROD 10,11 - INC and DEC
//	    name = n;
//	    unaryOp = unary;
//	    typeNumber = 8;
//	  }
//	  public Stmt(Expr e) { // PROD 9 - return expr
//	    expr = e;
//	    typeNumber = 9;
//	  }
//	  public Stmt(String id) { // PROD 7 - id()
//	    this.id = id;
//	    typeNumber = 10;
//	  }
//	  public Stmt() { // PROD 8 - return 
//	    typeNumber = 11;
//	  }
//
//	  public String toString(int depth) {
//	    switch (this.typeNumber) {
//	      case 0:
//	        return printSpaces(depth) +
//	          "if (" + expr.toString() + ")\n" +
//	          ( stmt.typeNumber == 2 ? stmt.toString(depth) : printSpaces(depth) +"{\n" + stmt.toString(depth+1) + "\n"+ printSpaces(depth) + "}" ) +
//	          ( elsestmt != null ? "\n" + printSpaces(depth) + "else\n" + ( elsestmt.typeNumber == 2 ? elsestmt.toString(depth) : printSpaces(depth) +"{\n" + elsestmt.toString(depth+1) + "\n"+ printSpaces(depth) + "}") : "" );    
//	      case 1:
//	        String list = "";
//	        for (Expr e: args) {
//	          list += e.toString() + ", ";
//	        } list = list.substring(0, list.length() > 0 ? list.length() - 2 : 0);
//	        return printSpaces(depth) + id + "(" + list + ");";
//	      case 2:
//	        String result = "";
//	        for (Fielddecl f: fielddecls) {
//	          result += f.toString(depth+1) + "\n";
//	        }
//	        for (Stmt st: stmts) {
//	          result += st.toString(depth+1) + "\n";
//	        }
//	        return printSpaces(depth) + "{\n" + result + printSpaces(depth) + "}";
//	      case 3:
//	        return printSpaces(depth) +
//	          "while (" + expr.toString() + ")\n" + stmt.toString(stmt.typeNumber == 2 ? depth : depth+1) + "\n";
//	      case 4:
//	        return printSpaces(depth) +
//	          name.toString() + " = " + expr.toString() + ";"; 
//	      case 5:
//	        list = "";
//	        for (Name n: (ArrayList<Name>)funclist) {
//	          list += n.toString() + ", ";
//	        } list = list.substring(0, list.length() > 0 ? list.length() - 2 : 0);
//	        return printSpaces(depth) + id + "(" + list + ");";
//	      case 6:
//	        list = "";
//	        for (Expr e: (ArrayList<Expr>)funclist) {
//	          list += e.toString() + ", ";
//	        } list = list.substring(0, list.length() > 0 ? list.length() - 2 : 0);
//	        return printSpaces(depth) + id + "(" + list + ");";
//	      case 7:
//	        list = "";
//	        for (Expr e: (ArrayList<Expr>)funclist) {
//	          list += e.toString() + ", ";
//	        } list = list.substring(0, list.length() > 0 ? list.length() - 2 : 0);
//	        return printSpaces(depth) + id + "(" + list + ");";
//	      case 8:
//	        return printSpaces(depth) + name.toString() + unaryOp + ";";
//	      case 9:
//	        return printSpaces(depth) + "return " + expr.toString() + ";"; 
//	      case 10:
//	        return printSpaces(depth) + id + "();";
//	      case 11:
//	        return printSpaces(depth) + "return;";
//	      default:
//	        return "";
//	    }
//	  }
//
//	  public boolean isReturn() {
//	    return this.typeNumber == 9 | this.typeNumber == 11;
//	  }
//
//	  public SymTable.DeclarationType typeCheck() throws Exception {
//	    switch (this.typeNumber) {
//	      case 0: // if 
//	        if (!canBeBool(expr))
//	          throw new ConditionExpectedException(expr.typeCheck(), expr.isArray());
//	        table.createScope();
//	        stmt.typeCheck();
//	        table.destroyScope();
//	        if ( elsestmt != null ) {
//	          table.createScope();
//	          stmt.typeCheck();
//	          table.destroyScope();
//	        }
//	        break;
//	      case 1: // function call with args
//	        SymTable.Var fun = table.getVar(id);
//	        if (!fun.isFunction())
//	          throw new UndefinedFunctionException(id);
//	        if (fun.arguments.size() != args.size())
//	          throw new ArgListException(id);
//	        for (int i = 0; i < args.size(); i++) {
//	          if (!canAssignFrom(args.get(i), fun.arguments.get(i)))
//	            throw new ArgListException(
//	              id, i,
//	              fun.arguments.get(i).typeCheck(), 
//	              fun.arguments.get(i).isArray(),
//	              args.get(i).typeCheck(), 
//	              args.get(i).isArray()
//	            );
//	        }
//	        break;
//	      case 2: // statement block
//	        table.createScope();
//	        for (Fielddecl f: fielddecls)
//	          f.typeCheck();
//	        for (Stmt st: stmts)
//	          st.typeCheck();
//	        table.destroyScope();
//	        break;          
//	      case 3: // while
//	        if (!canBeBool(expr))
//	          throw new ConditionExpectedException(expr.typeCheck(), expr.isArray());
//	        table.createScope();
//	        stmt.typeCheck();
//	        table.destroyScope();
//	        break;
//	      case 4: // assignment
//	        if (!canAssignFrom(this.expr, this.name))
//	          throw new AssignmentTypeMismatchException(
//	            name.typeCheck(),
//	            name.isArray(),
//	            expr.typeCheck(),
//	            expr.isArray()
//	          );
//	        if (table.getVar(name.id).isFinal())
//	          throw new CannotModifyFinalException(name.id);
//	        break;
//	      case 5: // read
//	        for (Name n: (ArrayList<Name>)funclist) {
//	          if (n.isArray() || n.isFinal())
//	            throw new Exception("cannot read an array or final.");
//	        }
//	        break;
//	      case 6: case 7:// print and printline
//	        for (Expr e: (ArrayList<Expr>)funclist) {
//	          e.typeCheck();
//	          if (e.isArray() || e.isFunction())
//	            throw new Exception("cannot print an array or function.");
//	        }
//	        break;
//	      case 8: // INC and DEC
//	        if (!canBeFloat(name))
//	          throw new UnaryArithmeticException();
//	        if (table.getVar(name.id).isFinal())
//	          throw new CannotModifyFinalException(name.id);
//	        break;
//	      case 9: // return with value
//	        return expr.typeCheck();
//	      case 10: // function call no args
//	        if (!table.getVar(id).isFunction())
//	          throw new UndefinedFunctionException(id);
//	        break;
//	      case 11: // return;
//	        return SymTable.DeclarationType.VOID;
//	    }
//	    return null;
//	  }
	}


