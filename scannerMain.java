import java.io.*;
import java_cup.runtime.*;

public class scannerMain{

    public static void main(String[] args) {
        Symbol sym;
        try {
            Scanner scanner = new Scanner(new FileReader(args[0]));
            for (sym = scanner.next_token(); sym.sym != 0;
                    sym = scanner.next_token()) {

                System.out.println("Token " + sym +
                    ", with value = " + sym.value + 
                    "; at line " + sym.left + ", column " + sym.right);

            }
        }
        catch (Exception e) {
        }
    }
}
