import java.util.*;
import java.util.stream.IntStream;

public class Grammar {
    private final List<Production> productions = new ArrayList<>();
    private Set<String> terminals = new HashSet<>();
    private Set<String> nonTerminals = new HashSet<>();
    private String startSymbol;
    private final ArrayList<String> alphabet = new ArrayList<>();


    public void setStartSymbol(String startSymbol){
        this.startSymbol = startSymbol;
    }

    public void setTerminals(Set<String> terminals) {
        this.terminals = terminals;
    }

    public void setNonTerminals(Set<String> nonTerminals) {
        this.nonTerminals = nonTerminals;
    }

    public void addProductions(Production production) {
        this.productions.add(production);
    }

    private void prepareAlphabet(){
        //generates all Upper Case Letters and append them to alphabet set
        IntStream.rangeClosed('A', 'Z').mapToObj(var -> (char) var).forEach(
                element -> alphabet.add(Character.toString(element))
        );
        //eliminate from alphabet all used nonTerminals
        alphabet.removeAll(nonTerminals);
    }

    public void display() {
        eliminateLeftRecursion();

        System.out.println("Start Symbol \t" + this.startSymbol);
        System.out.println("Terminals \t\t" + this.terminals);
        System.out.println("NonTerminals \t" + this.nonTerminals);
        for (Production production : this.productions) {
            production.display();
        }
    }
    private Production getProductionWith(String nonterminal) {

        for (Production production : productions) {
            if (production.getNonTerminal().equals(nonterminal)) {
                return production;
            }
        }
        return null;
    }
// --> STEP 1: Elimination of Left Recursion
    private void eliminateLeftRecursion() {
        HashSet<String> containRecursion = new HashSet<>();
        for (Production production: productions)
            if (production.hasRecursion())
                containRecursion.add(production.getNonTerminal());

        for (String nonTerminal: containRecursion) {
            Production recursiveProduction = getProductionWith(nonTerminal);
            ArrayList<String> alfa = new ArrayList<>();
            ArrayList<String> beta = new ArrayList<>();

            for (String word: Objects.requireNonNull(recursiveProduction).getDerivations()) {
                StringBuilder wordCopy = new StringBuilder(word);
                if(recursiveProduction.getNonTerminal().equals(String.valueOf(wordCopy.charAt(0))))
                    alfa.add(wordCopy.substring(1));
                 else
                    beta.add(word);
            }

            prepareAlphabet();
            Set<String> emptySet = new HashSet<>();
            recursiveProduction.setDerivations(emptySet);

            for (String element: beta) {
                String newNonTerminal = alphabet.get(0);
                recursiveProduction.addDerivation(element.concat(newNonTerminal));
            }

            for (String element: alfa){
                String newNonTerminal = alphabet.get(0);
                recursiveProduction.addDerivation(element.concat(newNonTerminal));
                recursiveProduction.addDerivation("-");
            }
            alphabet.remove(0);
        }
    }

    // STEP 2: Left Factoring

}
