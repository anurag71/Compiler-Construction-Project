import javax.sound.sampled.LineEvent.Type;

class Argdecl extends Token{
	FieldType type; 
	String id;
	boolean isArray;
	public Argdecl(FieldType type, String id, boolean isArray) {
		this.type = type;
		this.id = id;
		this.isArray = isArray;
	}
	public String toString() {
		return type.toString() + " " + id + ( isArray ? "[]" : "" );
	}
	
	String printSpaces(int number) {
		String spaces = "";
		for (int i = 0; i < number; i++) {
			spaces += "\t";
		}
		return spaces;
	}
	
	public void typeCheck() throws Exception {
	    // define function parameters in current scope
	    if (!table.add(id, (isArray ? "array" : ""), getType(type.toString()), null))
	      throw new RedefinedVariableException(id);
	  }
}