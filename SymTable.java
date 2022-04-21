import java.util.*;
public class SymTable {
  enum DeclarationType {
    VOID, INT, FLOAT, CHAR, BOOL, STRING
  }
  LinkedList<HashMap<String, Var>> scopes;

  public SymTable() {
    scopes = new LinkedList<HashMap<String, Var>>();
  }

  public boolean add(String varName, String ref, DeclarationType type, ArrayList args) {
    if (scopes.getFirst().containsKey(varName))
      return false;
    scopes.getFirst().put(varName, new Var(varName, ref, type, args));
    return true;
  }

  public Var getVar(String varName) throws Exception {
    for (HashMap<String, Var> scope: scopes)
      if (scope.containsKey(varName))
        return scope.get(varName);
    throw new Exception(varName+" not defined in the SymbolTable.");
  }

  public void createScope()   { scopes.addFirst(new HashMap<String, Var>()); }
  public void destroyScope()  { scopes.removeFirst(); }


  public Arg newArg(DeclarationType t, boolean arr) {
    return new Arg(t, arr);
  }

  class Arg implements DeclarationChecking {
    DeclarationType type;
    boolean isArray;
    public Arg(DeclarationType t, boolean a) {
      type = t;
      isArray = a;
    }
    public boolean isArray() { return isArray; }
    public boolean isFinal() { return false; }
    public boolean isFunction() { return false; }
    public DeclarationType typeCheck() { return type; }
  }

  class Var implements DeclarationChecking {
    ArrayList<Arg> arguments;
    String refType, name;
    DeclarationType type;
    public Var(String n, String r, DeclarationType t, ArrayList args) {
      name = n;
      refType = r;
      type = t;
      arguments = args;
    }
    public boolean isFinal() {
      return refType.equals("final");
    }
    public boolean isArray() {
      return refType.equals("array");
    }
    public boolean isFunction() {
      return refType.equals("function");
    }
    public DeclarationType typeCheck() {
      return type;
    }
  }
}