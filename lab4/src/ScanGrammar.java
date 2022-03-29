import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ScanGrammar {
    public String terminals;
    public String nonTerminals;
    public ArrayList<String> Productions = new ArrayList<>();

    public ScanGrammar(Grammar grammar, String path) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(path));

        terminals = scanner.nextLine();
        nonTerminals = scanner.nextLine();

        while (scanner.hasNextLine()) {
            Productions.add(scanner.nextLine());
        }

        splitTerminals(grammar);
        splitNonTerminals(grammar);
        splitProductions(grammar);
    }

    private void splitTerminals(Grammar grammar) {
        Set<String> setTerminals = new HashSet<>(Arrays.asList(terminals.split(",")));
        grammar.setTerminals(setTerminals);
    }

    private void splitNonTerminals(Grammar grammar) {
        Set<String> setNonTerminals = new HashSet<>(Arrays.asList(nonTerminals.split(",")));
        grammar.setNonTerminals(setNonTerminals);
    }

    private void splitProductions(Grammar grammar) {
        for (String derivation : Productions) {
            Production production = new Production();

            String[] splitByArrow = derivation.split("->");
            String[] splitByBar = splitByArrow[1].split("\\|");
            Set<String> splitByBarList = new HashSet<>(Arrays.asList(splitByBar));

            production.setNonTerminal(splitByArrow[0]);
            production.setDerivations(splitByBarList);

            grammar.addProductions(production);
        }
    }
}
