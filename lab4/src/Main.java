import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Grammar grammar = new Grammar();
        new ScanGrammar(grammar, "C:\\Users\\valeria\\Desktop\\Uni\\LFPC\\lab4\\src\\grammar.txt");
        grammar.convertToChomskyNormalForm();


//            Combinations combobj= new Combinations("ABACA");
//            combobj.displayCombination();

    }
}
