import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ScanGrammar {
    public String terminals;
    public String nonTerminals;
    public ArrayList<String> Productions = new ArrayList<>();
    Grammar grammar = new Grammar();

    public ScanGrammar() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("C:\\Users\\valeria\\Desktop\\Uni\\LFPC\\lab4\\src\\grammar.txt"));

        terminals = scanner.nextLine();
        nonTerminals = scanner.nextLine();

        while (scanner.hasNextLine()) {
            Productions.add(scanner.nextLine());
        }

        splitTerminals();
        splitNonTerminals();
        splitProductions();
    }

    private void splitTerminals() {
        Set<String> setTerminals = new HashSet<>(Arrays.asList(terminals.split(",")));
        grammar.setTerminals(setTerminals);
    }

    private void splitNonTerminals() {
        Set<String> setNonTerminals = new HashSet<>(Arrays.asList(nonTerminals.split(",")));
        grammar.setNonTerminals(setNonTerminals);
    }

    private void splitProductions() {
        for (String derivation : Productions) {
            Production production = new Production();

            String[] splitByArrow = derivation.split("->");
            String[] splitByBar = splitByArrow[1].split("\\|");
            List<String> splitByBarList = new ArrayList<>(Arrays.asList(splitByBar));

            production.setNonTerminal(splitByArrow[0]);
            production.setDerivations(splitByBarList);

            grammar.addProductions(production);
        }
    }
}
