class Binaryop extends Token {
	String op;
	public Binaryop(String op) {
		this.op = op;
	}
	public boolean isArithmetic() {
	    return op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/");
	  }
	  public boolean isRelational() {
	    return op.equals("<") || op.equals("<=") || op.equals(">") || op.equals(">=") || op.equals("==") || op.equals("<>");
	  }
	  public boolean isLogical() {
	    return op.equals("||") || op.equals("&&");
	  }
	  public String toString() {
	    return op;
	  }

	String printSpaces(int number) {
		String spaces = "";
		for (int i = 0; i < number; i++) {
			spaces += "\t";
		}
		return spaces;
	}
}