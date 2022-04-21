import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        Grammar grammar = new Grammar();
        new GrammarScanner(grammar, "/home/valeria/IdeaProjects/LFPC_lab5/toScan.txt");
        grammar.display();

    }
}