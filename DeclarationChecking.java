
interface DeclarationChecking {
  public boolean isArray() throws Exception;
  public boolean isFinal() throws Exception;
  public boolean isFunction() throws Exception;
  public SymTable.DeclarationType typeCheck() throws Exception;
}