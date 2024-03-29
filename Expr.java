import java.util.ArrayList;

class Expr extends Token implements DeclarationChecking {
	int resultreturn;
	String id;
	String str;
	int intlit;
	float floatlit;
	Name nm;
	boolean bool;
	Expr ex[];
	Binaryop binaryop;
	String unryop;
	ArrayList<Expr> exarray;
	String vartype;

	public Expr(String id, boolean isMethod) {
		this.id = id;
		this.resultreturn = 0; 
	}

	public Expr(Name nm) {
		this.nm = nm;
		this.resultreturn = 1;
	}

	public Expr(int intlit) {
		this.intlit = intlit;
		this.resultreturn = 2;
	}
	public Expr(Expr ex, String unryop) {
		this.unryop = unryop;
		this.ex = new Expr[]{ex};
		this.resultreturn = 3;
	}
	public Expr(boolean bool) {
		this.bool = bool;
		this.resultreturn = 4;
	}
	public Expr(FieldType vartype, Expr ex) {
		this.vartype = vartype.toString();
		this.ex = new Expr[]{ex};
		this.resultreturn = 5;
	}

	public Expr(float floatlit) {
		this.floatlit = floatlit;
		this.resultreturn = 6;
	}
	public Expr(String id, ArrayList<Expr> exarray) {
		this.id = id;
		this.exarray = exarray;
		this.resultreturn = 7; 
	}
	public Expr(String str, char isstr) {
		this.str = str;
		this.resultreturn = isstr == 's' ? 8 : 9;
	}
	public Expr(Expr ex) {
		this.ex = new Expr[]{ex};
		this.resultreturn = 10;
	}
	public Expr(Expr ex1, Expr ex2, Expr ex3) {
		this.ex = new Expr[]{ex1, ex2, ex3};
		this.resultreturn = 11;
	}

	public Expr(Expr ex1, Binaryop binaryop, Expr ex2) {
		this.ex = new Expr[]{ex1, ex2};
		this.binaryop = binaryop;
		this.resultreturn = 12;
	}


	public String toString() {
		if(this.resultreturn==0) {
			return id +"()";
		}
		else if(this.resultreturn==1){
			return nm.toString();
		}
		else if(this.resultreturn==2){
			return ""+intlit;
		}
		else if(this.resultreturn==3){
			return "(" + unryop + " " + ex[0].toString() + ")";
		}
		else if(this.resultreturn==4){
			return bool ? "true" : "false";
		}
		else if(this.resultreturn==5){
			return "(" + vartype + ")" + ex[0].toString();
		}
		else if(this.resultreturn==6){
			return ""+floatlit;
		}
		else if(this.resultreturn==7){
			String ret = "";
			for (Expr e: exarray) {
				ret += e.toString() + ", ";
			} 
			ret = ret.substring(0, ret.length() > 0 ? ret.length() - 2 : 0 );
			return id + "(" + ret + ")";
		}
		else if(this.resultreturn==8){
			return str;
		}
		else if(this.resultreturn==9){
			return str;
		}
		else if(this.resultreturn==10){
			return ex[0].toString();
		}
		else if(this.resultreturn==11){
			return "( " + ex[0].toString() + " ? " + ex[1].toString() + " : " + ex[2].toString() + " )";
		}
		
		
		else if(this.resultreturn==12){
			return "(" + ex[0].toString() + " " + binaryop.toString() + " " + ex[1].toString() + ")";
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
	
	public boolean isArray() throws Exception {
	    return nm != null && nm.isArray() || resultreturn == 5 && ex[0].isArray();
	  }
	  public boolean isFinal() throws Exception {
	    return nm != null && nm.isFinal() || resultreturn == 5 && ex[0].isFinal();
	  }
	  public boolean isFunction() throws Exception {
	    return nm != null && nm.isFunction() || resultreturn == 5 && ex[0].isFunction();
	  }

	  public SymTable.DeclarationType typeCheck() throws Exception {
	    switch (this.resultreturn) {
	      case 8: // strlit
	        return SymTable.DeclarationType.STRING;
	      case 9: // charlit
	        return SymTable.DeclarationType.CHAR;
	      case 2: // intlit
	        return SymTable.DeclarationType.INT;
	      case 6: // floatlit
	        return SymTable.DeclarationType.FLOAT;
	      case 1: // var or array index
	        return nm.typeCheck();
	      case 4: // boollit
	        return SymTable.DeclarationType.BOOL;
	      case 10: // value in parenthesis
	        return ex[0].typeCheck();
	      case 3: // unary ops
	        if (this.unryop.equals("~")) {
	          if (!canBeBool(ex[0])) {
	      
	        	  throw new UnaryLogicalException();
	          }
	          return SymTable.DeclarationType.BOOL;
	        } else {
	          if (!canBeFloat(ex[0]))
	          {
	        	  System.out.println(ex[0].toString());
	          
	        	  throw new SignedValueException();
	          }
	        	  return ex[0].typeCheck();
	        }        
	      case 5: // casting
	        return getType(vartype); // casting special case?
	      case 12: // binary ops
	        if (ex[0].typeCheck().equals(SymTable.DeclarationType.STRING) || ex[1].typeCheck().equals(SymTable.DeclarationType.STRING)) {
	          if (!binaryop.op.equals("+"))
	            throw new StringOperationException();
	          return SymTable.DeclarationType.STRING;
	        }
	        if (binaryop.op.equals("+") || binaryop.op.equals("-") || binaryop.op.equals("*") || binaryop.op.equals("/")) {
	          if ( !canBeFloat(ex[0]) && !canBeFloat(ex[1]) )
	            throw new BinaryExpressionException(
	              binaryop.op,
	              ( canBeFloat(ex[0]) ? ex[0].typeCheck() : ex[1].typeCheck() ),
	              ( canBeFloat(ex[0]) ? ex[0].isArray() : ex[1].isArray() )
	            );
	          return ex[0].typeCheck().equals(SymTable.DeclarationType.FLOAT) || ex[1].typeCheck().equals(SymTable.DeclarationType.FLOAT)
	            ? SymTable.DeclarationType.FLOAT
	            : SymTable.DeclarationType.INT;
	        }
	        if (binaryop.op.equals("<") || binaryop.op.equals("<=") || binaryop.op.equals(">") || binaryop.op.equals(">=") || binaryop.op.equals("==") || binaryop.op.equals("<>")) {
	          if ( !canBeFloat(ex[0]) && !canBeFloat(ex[1]) )
	            throw new BinaryExpressionException(
	              binaryop.op, 
	              ( canBeFloat(ex[0]) ? ex[0].typeCheck() : ex[1].typeCheck() ),
	              ( canBeFloat(ex[0]) ? ex[0].isArray() : ex[1].isArray() )
	            );
	          return SymTable.DeclarationType.BOOL;
	        }
	        if (binaryop.op.equals("||") || binaryop.op.equals("&&")) {
	          if ( !canBeBool(ex[0]) && !canBeBool(ex[1]) )
	            throw new BinaryExpressionException(
	              binaryop.op, 
	              ( canBeBool(ex[0]) ? ex[0].typeCheck() : ex[1].typeCheck() ),
	              ( canBeBool(ex[0]) ? ex[0].isArray() : ex[1].isArray() )
	            );
	          return SymTable.DeclarationType.BOOL;
	        }
	      case 11: // ternary expression
	        if ( !canBeBool(ex[0]) )
	          throw new ConditionExpectedException(ex[0].typeCheck(), ex[0].isArray());
	        if ( !ex[1].typeCheck().equals(ex[2].typeCheck()))
	          throw new TernaryResultException();
	        return ex[1].typeCheck(); 
	      case 7: // function call with exarray return value
	        SymTable.Var fun = table.getVar(id);
	        if (!fun.isFunction())
	          throw new UndefinedFunctionException(id);
	        if (fun.arguments.size() != exarray.size())
	          throw new ArgListException(id);
	        for (int i = 0; i < exarray.size(); i++) {
	          if (!canAssignFrom(exarray.get(i), fun.arguments.get(i)))
	            throw new ArgListException(
	              id, i,
	              fun.arguments.get(i).typeCheck(),
	              fun.arguments.get(i).isArray(),
	              exarray.get(i).typeCheck(), 
	              exarray.get(i).isArray()
	            );
	        }
	        return table.getVar(id).typeCheck();
	      case 0: // function call no exarray return value
	        if (!table.getVar(id).isFunction())
	          throw new UndefinedFunctionException(id);
	        return table.getVar(id).typeCheck();
	    }
	    return null;
	  }
	
//	int typeNumber;
//	  String charStr, id;
//	  int intlit;
//	  float floatlit;
//	  Name name;
//	  boolean bool;
//	  Expr expr[];
//	  Binaryop binOp;
//	  String unaryOp, castType;
//	  ArrayList<Expr> args;
//
//	  public Expr(String i, char isStr) { // PROD 5,6 - strlit
//	    charStr = i;
//	    typeNumber = isStr == 's' ? 0 : 12;
//	  }
//	  public Expr(int i) { // PROD 4 - intlit
//	    intlit = i;
//	    typeNumber = 1;
//	  }
//	  public Expr(float i) { // PROD 7 - floatlit
//	    floatlit = i;
//	    typeNumber = 2;
//	  }
//	  public Expr(Name n) { // PROD 1 - name
//	    name = n;
//	    typeNumber = 3;
//	  }
//	  public Expr(boolean b) { // PROD 8,9 - booleans
//	    bool = b;
//	    typeNumber = 4;
//	  }
//	  public Expr(Expr e) { // PROD 10 - paren
//	    expr = new Expr[]{e};
//	    typeNumber = 5;
//	  }
//	  public Expr(Expr e, String op) { // PROD 11,12,13 - prefix expr
//	    unaryOp = op;
//	    expr = new Expr[]{e};
//	    typeNumber = 6;
//	  }
//	  public Expr(FieldType ct, Expr e) { // PROD 14 - cast
//	    castType = ct.toString();
//	    expr = new Expr[]{e};
//	    typeNumber = 7;
//	  }
//	  public Expr(Expr e1, Binaryop bOp, Expr e2) { // PROD 15 - binop
//	    expr = new Expr[]{e1, e2};
//	    binOp = bOp;
//	    typeNumber = 8;
//	  }
//	  public Expr(Expr e1, Expr e2, Expr e3) { // PROD 16 - ternary
//	    expr = new Expr[]{e1, e2, e3};
//	    typeNumber = 9;
//	  }
//	  public Expr(String id, ArrayList<Expr> args) { // PROD 3 - func(args)
//	    this.id = id;
//	    this.args = args;
//	    typeNumber = 10; 
//	  }
//	  public Expr(String id, boolean isMethod) { // PROD 2 - func()
//	    this.id = id;
//	    typeNumber = 11; 
//	  }
//
//	  public String toString() {
//	    switch (this.typeNumber) {
//	      case 0: case 12:
//	        return charStr;
//	      case 1:
//	        return ""+intlit;
//	      case 2:
//	        return ""+floatlit;
//	      case 3:
//	        return name.toString();
//	      case 4:
//	        return bool ? "true" : "false";
//	      case 5:
//	        return expr[0].toString();
//	      case 6:
//	        return "(" + unaryOp + " " + expr[0].toString() + ")";
//	      case 7:
//	        return "(" + castType + ")" + expr[0].toString();
//	      case 8:
//	        return "(" + expr[0].toString() + " " + binOp.toString() + " " + expr[1].toString() + ")";
//	      case 9:
//	        return "( " + expr[0].toString() + " ? " + expr[1].toString() + " : " + expr[2].toString() + " )";
//	      case 10:
//	        String ret = "";
//	        for (Expr e: args) {
//	          ret += e.toString() + ", ";
//	        } ret = ret.substring(0, ret.length() > 0 ? ret.length() - 2 : 0 );
//	        return id + "(" + ret + ")";
//	      case 11:
//	        return id +"()";
//	      default:
//	        return "";
//	    }
//	  }
//	  
//
//	  public boolean isArray() throws Exception {
//	    return name != null && name.isArray() || typeNumber == 5 && expr[0].isArray();
//	  }
//	  public boolean isFinal() throws Exception {
//	    return name != null && name.isFinal() || typeNumber == 5 && expr[0].isFinal();
//	  }
//	  public boolean isFunction() throws Exception {
//	    return name != null && name.isFunction() || typeNumber == 5 && expr[0].isFunction();
//	  }
//
//	  public SymTable.DeclarationType typeCheck() throws Exception {
//	    switch (this.typeNumber) {
//	      case 0: // strlit
//	        return SymTable.DeclarationType.STRING;
//	      case 12: // charlit
//	        return SymTable.DeclarationType.CHAR;
//	      case 1: // intlit
//	        return SymTable.DeclarationType.INT;
//	      case 2: // floatlit
//	        return SymTable.DeclarationType.FLOAT;
//	      case 3: // var or array index
//	        return name.typeCheck();
//	      case 4: // boollit
//	        return SymTable.DeclarationType.BOOL;
//	      case 5: // value in parenthesis
//	        return expr[0].typeCheck();
//	      case 6: // unary ops
//	        if (unaryOp.equals("~")) {
//	          if (!canBeBool(expr[0]))
//	            throw new UnaryLogicalException();
//	          return SymTable.DeclarationType.BOOL;
//	        } else {
//	          if (!canBeFloat(expr[0]))
//	            throw new SignedValueException();
//	          return expr[0].typeCheck();
//	        }        
//	      case 7: // casting
//	        return getType(castType); // casting special case?
//	      case 8: // binary ops
//	        if (expr[0].typeCheck().equals(SymTable.DeclarationType.STRING) || expr[1].typeCheck().equals(SymTable.DeclarationType.STRING)) {
//	          if (!binOp.op.equals("+"))
//	            throw new StringOperationException();
//	          return SymTable.DeclarationType.STRING;
//	        }
//	        if (binOp.isArithmetic()) {
//	          if ( !canBeFloat(expr[0]) && !canBeFloat(expr[1]) )
//	            throw new BinaryExpressionException(
//	              binOp.op,
//	              ( canBeFloat(expr[0]) ? expr[0].typeCheck() : expr[1].typeCheck() ),
//	              ( canBeFloat(expr[0]) ? expr[0].isArray() : expr[1].isArray() )
//	            );
//	          return expr[0].typeCheck().equals(SymTable.DeclarationType.FLOAT) || expr[1].typeCheck().equals(SymTable.DeclarationType.FLOAT)
//	            ? SymTable.DeclarationType.FLOAT
//	            : SymTable.DeclarationType.INT;
//	        }
//	        if (binOp.isRelational()) {
//	          if ( !canBeFloat(expr[0]) && !canBeFloat(expr[1]) )
//	            throw new BinaryExpressionException(
//	              binOp.op, 
//	              ( canBeFloat(expr[0]) ? expr[0].typeCheck() : expr[1].typeCheck() ),
//	              ( canBeFloat(expr[0]) ? expr[0].isArray() : expr[1].isArray() )
//	            );
//	          return SymTable.DeclarationType.BOOL;
//	        }
//	        if (binOp.isLogical()) {
//	          if ( !canBeBool(expr[0]) && !canBeBool(expr[1]) )
//	            throw new BinaryExpressionException(
//	              binOp.op, 
//	              ( canBeBool(expr[0]) ? expr[0].typeCheck() : expr[1].typeCheck() ),
//	              ( canBeBool(expr[0]) ? expr[0].isArray() : expr[1].isArray() )
//	            );
//	          return SymTable.DeclarationType.BOOL;
//	        }
//	      case 9: // ternary expression
//	        if ( !canBeBool(expr[0]) )
//	          throw new ConditionExpectedException(expr[0].typeCheck(), expr[0].isArray());
//	        if ( !expr[1].typeCheck().equals(expr[2].typeCheck()))
//	          throw new TernaryResultException();
//	        return expr[1].typeCheck(); 
//	      case 10: // function call with args return value
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
//	        return table.getVar(id).typeCheck();
//	      case 11: // function call no args return value
//	        if (!table.getVar(id).isFunction())
//	          throw new UndefinedFunctionException(id);
//	        return table.getVar(id).typeCheck();
//	    }
//	    return null;
//	  }
}