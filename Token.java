abstract class Token {
  protected static SymTable table;

  protected SymTable.DeclarationType getType(String t) {
    return
      t.equals("int") ? SymTable.DeclarationType.INT :
      t.equals("bool") ? SymTable.DeclarationType.BOOL :
      t.equals("char") ? SymTable.DeclarationType.CHAR :
      t.equals("float") ? SymTable.DeclarationType.FLOAT :
      t.equals("string") ? SymTable.DeclarationType.STRING :
      t.equals("void") ? SymTable.DeclarationType.VOID :
      null;
  }


  protected boolean canAssignFrom(DeclarationChecking startType, DeclarationChecking targetType) throws Exception {
    return
      startType.isArray() && targetType.isArray() && startType.typeCheck().equals(targetType.typeCheck()) ||
      !startType.isArray() && !targetType.isArray() && canConvertFrom(startType.typeCheck(), targetType.typeCheck());
  }
    
  protected boolean canConvertFrom(SymTable.DeclarationType RHStype, SymTable.DeclarationType LHStype) {
    return RHStype.equals(SymTable.DeclarationType.INT)
      ? LHStype.equals(SymTable.DeclarationType.BOOL) || LHStype.equals(SymTable.DeclarationType.FLOAT) || LHStype.equals(SymTable.DeclarationType.INT)
      : RHStype.equals(LHStype);
  }
    
  protected boolean canBeBool(DeclarationChecking expr) throws Exception {
    return !expr.isArray() && ( expr.typeCheck().equals(SymTable.DeclarationType.BOOL) || expr.typeCheck().equals(SymTable.DeclarationType.INT) );
  }
  protected boolean canBeFloat(DeclarationChecking expr) throws Exception {
    return !expr.isArray() && ( expr.typeCheck().equals(SymTable.DeclarationType.FLOAT) || expr.typeCheck().equals(SymTable.DeclarationType.INT) );
  }


  class AssignmentTypeMismatchException extends Exception {
    public AssignmentTypeMismatchException(SymTable.DeclarationType lhs, boolean lhsArr, SymTable.DeclarationType rhs, boolean rhsArr) {
      super("lhs type:<"+lhs+(lhsArr?"[]":"")+"> does not match rhs type:<"+rhs+(rhsArr?"[]":"")+">.");
    }
    public AssignmentTypeMismatchException(SymTable.DeclarationType lhs, SymTable.DeclarationType rhs) {
      this(lhs, false, rhs, false);
    }
  }

  class ReturnTypeMismatchException extends Exception {
    public ReturnTypeMismatchException(String function, SymTable.DeclarationType returnType, SymTable.DeclarationType expectedType) {
      super("return type:<"+returnType+"> does not match expected type:<"+expectedType+"> for function:<"+function+">.");
    }
  }

  class MissingReturnException extends Exception {
    public MissingReturnException(String function) {
      super("return statement required for function:<"+function+">.");
    }
  }

  class RedefinedVariableException extends Exception {
    public RedefinedVariableException(String id) {
      super("id:<"+id+"> is already defined within the current scope.");
    }
  }

  class CannotModifyFinalException extends Exception {
    public CannotModifyFinalException(String id) {
      super("id:<"+id+"> is final and cannot be modified.");
    }
  }

  class ConditionExpectedException extends Exception {
    public ConditionExpectedException(SymTable.DeclarationType type, boolean isArray) {
      super("condition type:<"+type+(isArray?"[]":"")+"> is not coercible to type:<"+SymTable.DeclarationType.BOOL+">.");
    }
  }

  class UnaryArithmeticException extends Exception {
    public UnaryArithmeticException() {
      super("type:<"+SymTable.DeclarationType.INT+"|"+SymTable.DeclarationType.FLOAT+"> expected for unary arithmetic operators.");
    }
  }
  class UnaryLogicalException extends Exception {
    public UnaryLogicalException() {
      super("type:<"+SymTable.DeclarationType.BOOL+"> expected for unary logical operators.");
    }
  }

  class SignedValueException extends Exception {
    public SignedValueException() {
      super("type:<"+SymTable.DeclarationType.INT+"|"+SymTable.DeclarationType.INT+" expected in order to utilize a sign prefix.");
    }
  }

  class TernaryResultException extends Exception {
    public TernaryResultException() {
      super("ternary expression results must be homogeneous.");
    }
  }
  class UndefinedFunctionException extends Exception {
    public UndefinedFunctionException(String s) {
      super("function <"+s+"> has not been defined.");
    }
  }

  class ArgListException extends Exception {
    public ArgListException(String func) {
      super("function <"+func+"> does not have the correct number of arguments.");
    }
    public ArgListException(String func, int index, SymTable.DeclarationType idType, boolean idArr, SymTable.DeclarationType argType, boolean argArr) {
      super("argument["+index+"] of function:<"+func+"> expects type:<"+idType+(idArr?"[]":"")+">, but got type:<"+argType+(argArr?"[]":"")+">.");
    }
    public ArgListException(String func, int index, SymTable.DeclarationType idType, SymTable.DeclarationType argType) {
      this(func, index, idType, false, argType, false);
    }
  }

  class StringOperationException extends Exception {
    public StringOperationException() {
      super("invalid string operation.");
    }
  }

  class BinaryExpressionException extends Exception {
    public BinaryExpressionException(String op, SymTable.DeclarationType type, boolean isArray) {
      super("binary operator:<"+op+"> does not accept type:<"+type+(isArray?"[]":"")+">.");
    }
  }

}