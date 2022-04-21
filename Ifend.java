class Ifend
{
  Stmt stm;
  public IfEnd(Stmt stm)
  {
    this.stm = stm;
  }


  public String toString(int t)
  {
    return stm.toString(t));
  }
  
  String printSpaces(int number) {
		String spaces = "";
		for (int i = 0; i < number; i++) {
			spaces += "\t";
		}
		return spaces;
	}
}
