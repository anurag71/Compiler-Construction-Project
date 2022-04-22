class FieldType
{
  String tp;
  public FieldType(String tp)
  {
    this.tp = tp;
  }

  public String toString()
  {
    return  tp;
  }
  
  String printSpaces(int number) {
		String spaces = "";
		for (int i = 0; i < number; i++) {
			spaces += "\t";
		}
		return spaces;
	}
}
