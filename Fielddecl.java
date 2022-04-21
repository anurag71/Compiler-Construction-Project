import javax.sound.sampled.LineEvent.Type;

class Fielddecl extends Token {
	boolean optFinal;
	Type type;
	String id;
	int arrLength;
	Expr ex;
	int declType;
	public Fielddecl(Type type, String id, Expr ex, boolean optFinal) {
		this.type = type;
		this.id = id;
		this.ex = ex;
		this.optFinal = optFinal;
		declType = 0;
	}
	public Fielddecl(Type type, String id, int len) {
		this.type = type;
		this.id = id;
		this.arrLength = len;
		declType = 1;
	}
	public String toString(int spaces) {
		switch (declType) {
		case 0:
			return printSpaces(spaces) + ( optFinal ? "final " : "" ) + type.toString() + " " + id  + ( ex != null ? " = "+ ex.toString() : "" ) + ";";  
		case 1:
			return printSpaces(spaces) + type.toString() + " " + id + "[" + arrLength + "]" + ";";
		default:
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
	
	public void typeCheck() throws Exception {
	    switch (declType) {
	      case 0: // var def with optional assign
	        if (ex != null)
	          if (!canConvertFrom(ex.typeCheck(), getType(type.toString())))
	            throw new AssignmentTypeMismatchException(getType(type.toString()), ex.typeCheck());
	        if (!table.add(id, (optFinal ? "final" : ""), getType(type.toString()), null))
	          throw new RedefinedVariableException(id);
	        break;
	      case 1: // array decl def
	        if (!table.add(id, "array", getType(type.toString()), null))
	          throw new RedefinedVariableException(id);
	        break;
	    }
	  }
}