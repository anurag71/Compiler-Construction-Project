class Binaryop extends Token {
	String op;
	public Binaryop(String op) {
		this.op = op;
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