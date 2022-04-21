abstract class BaseToken {

  protected String getTabs(int num) {
    String tabs = "";
    for (int i = 0; i < num; i++)
      tabs += "\t";
    return tabs;
  }
}