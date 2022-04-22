import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        Grammar grammar = new Grammar();
        new GrammarScanner(grammar, "C:\\Users\\valeria\\Desktop\\Uni\\LFPC\\LFPC_labs\\lab_5\\src\\toScann");
        grammar.display();

        LL1 parser =  new LL1(grammar);

    }
}