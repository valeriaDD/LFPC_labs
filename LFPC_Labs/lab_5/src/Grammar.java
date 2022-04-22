import java.util.*;
import java.util.stream.IntStream;

public class Grammar {

    private final List<Production> productions = new ArrayList<>();
    private Set<String> terminals = new HashSet<>();
    private Set<String> nonTerminals = new HashSet<>();
    private String startSymbol;

    public Set<String> getTerminals() { return terminals; }

    public Set<String> getNonTerminals() { return nonTerminals; }

    public String getStartSymbol() { return startSymbol;}

    private final ArrayList<String> alphabet = new ArrayList<>();

    public List<Production> getProductions() {
        return productions;
    }

    public void setStartSymbol(String startSymbol) {
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

    public void addNewProduction(String nonTerminal, Set<String> derivation) {
        this.nonTerminals.add(nonTerminal);
        Production newProduction = new Production();

        newProduction.setNonTerminal(nonTerminal);
        newProduction.setDerivations(derivation);

        this.productions.add(newProduction);
    }

    private void prepareAlphabet() {
        //generates all Upper Case Letters and append them to alphabet set
        IntStream.rangeClosed('A', 'Z').mapToObj(var -> (char) var).forEach(
                element -> alphabet.add(Character.toString(element))
        );
        //eliminate from alphabet all used nonTerminals
        alphabet.removeAll(nonTerminals);
    }

    public void display() {
        prepareAlphabet();
        eliminateLeftRecursion();
        leftFactoring();

        System.out.println("Start Symbol \t" + this.startSymbol);
        System.out.println("Terminals \t\t" + this.terminals);
        System.out.println("NonTerminals \t" + this.nonTerminals);

        for (Production production : this.productions) {
            production.display();
        }
    }

    public Production getProductionWith(String nonterminal) {

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
        for (Production production : productions)
            if (production.hasRecursion())
                containRecursion.add(production.getNonTerminal());

        for (String nonTerminal : containRecursion) {
            Production recursiveProduction = getProductionWith(nonTerminal);
            ArrayList<String> alfa = new ArrayList<>();
            ArrayList<String> beta = new ArrayList<>();

            for (String word : Objects.requireNonNull(recursiveProduction).getDerivations()) {
                StringBuilder wordCopy = new StringBuilder(word);
                if (recursiveProduction.getNonTerminal().equals(String.valueOf(wordCopy.charAt(0))))
                    alfa.add(wordCopy.substring(1));
                else
                    beta.add(word);
            }

            Set<String> emptySet = new HashSet<>();
            recursiveProduction.setDerivations(emptySet);

            String newNonTerminal = alphabet.get(0);
            Set<String> newDerivations = new HashSet<>();

            for (String element : beta) {
                recursiveProduction.addDerivation(element.concat(newNonTerminal));
            }

            for (String element : alfa) {
                newDerivations.add(element.concat(newNonTerminal));
                newDerivations.add("-");
            }
            addNewProduction(newNonTerminal, newDerivations);
            alphabet.remove(0);
        }
    }

    // STEP 2: Left Factoring
    private void leftFactoring() {
        HashMap<Production, String> leftFactoringDictionary = new HashMap<>();

        for (Production production : productions) {
            String prefix = production.commonPrefix();
            if (prefix.length() != 0)
                leftFactoringDictionary.put(production, prefix);
        }

        for (Map.Entry<Production, String> leftFactoring : leftFactoringDictionary.entrySet()) {
            Production production = leftFactoring.getKey();
            String prefix = leftFactoring.getValue();
            String newNonTerminal = alphabet.get(0);

            Set<String> newProductions = new HashSet<>(production.changeToPrefix(prefix, newNonTerminal));
            addNewProduction(newNonTerminal, newProductions);

            alphabet.remove(0);
        }
    }

    // STEP 3

    public HashMap<String, HashSet<String>> getWordsThatContains(String nonTerminal) {
        HashMap<String, HashSet<String>> dictionary = new HashMap<>();
        for (Production production : productions) {
            if (!production.getWordsWith(nonTerminal).isEmpty()) {
                dictionary.put(production.getNonTerminal(), production.getWordsWith(nonTerminal));
            }
        }
        return dictionary;
    }

}