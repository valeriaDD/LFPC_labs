import java.util.*;
import java.util.stream.IntStream;

public class Grammar {
    private final List<Production> productions = new ArrayList<>();
    private Set<String> terminals = new HashSet<>();
    private Set<String> nonTerminals = new HashSet<>();
    private ArrayList<String> alphabet = new ArrayList<>();
    private int maxWordLength = 1;


    public void convertToChomskyNormalForm() {
        HashMap<String, String> toReplace = new HashMap<>();

        //generates all Upper Case Letters and append them to alphabet set
        IntStream.rangeClosed('A', 'Z').mapToObj(var -> (char) var).forEach(
                element -> alphabet.add(Character.toString(element))
        );

        System.out.println("\n\t\t\t\tInput:");
        display();

        removeEmptyTransitions();
        removeUnitTransitions();
        removeUnproductiveSymbols();
        removeInaccessibleSymbols();

        //eliminate from alphabet all used nonTerminals
        alphabet.removeAll(nonTerminals);

        toChomskyLength2(toReplace);
        toChomskyLongWords(toReplace);

        for (Map.Entry<String, String> set : toReplace.entrySet()) {
            Production newProduction = new Production();
            Set<String> derivations = new HashSet<>();
            derivations.add(set.getKey());

            newProduction.setNonTerminal(set.getValue());
            newProduction.setDerivations(derivations);
            this.productions.add(newProduction);
        }

        System.out.println("\n\t\t\tGrammar in Chomsky Normal Form:");
        display();

        System.out.println(toReplace);
    }

    public void replace(HashMap<String, String> toReplace) {
        for (Map.Entry<String, String> set : toReplace.entrySet()) {
            this.nonTerminals.remove(set.getKey());
            for (Production production : this.productions)
                if (production.hasProduction(set.getKey())) {
                    production.chomskyFormatter(set.getKey(), set.getValue());
                    nonTerminals.add(set.getValue());
                }
        }
    }

    public void setMaxWordLength() {
        for (Production production : this.productions)
            for (String word : production.getDerivations())
                if (maxWordLength < word.length())
                    maxWordLength = word.length();
    }

    public void toChomskyLongWords(HashMap<String, String> toReplace) {
        setMaxWordLength();

        for (int j = 0; j <= maxWordLength / 2; j++) {
            for (Production production : this.productions)
                for (String word : production.getDerivations())
                    if (word.length() != 1)
                        for (int i = 2; i < word.length(); i += 2)
                            if (!toReplace.containsKey(word.substring(i - 2, i))) {
                                toReplace.put(word.substring(i - 2, i), alphabet.get(0));
                                alphabet.remove(0);
                            }
            replace(toReplace);
        }
    }

    public void toChomskyLength2(HashMap<String, String> toReplace) {
        for (Production production : this.productions)
            for (String word : production.getDerivations())
                if (word.length() == 2) {
                    StringBuilder bufferedWord = new StringBuilder(word);

                    for (int i = 0; i < 2; i++) {
                        char ch = bufferedWord.charAt(i);

                        if (Character.isLowerCase(ch))
                            if (!toReplace.containsKey(ch)) {
                                toReplace.put(Character.toString(ch), alphabet.get(0));
                                alphabet.remove(0);
                            }

                    }
                }
        replace(toReplace);
    }


    private void removeInaccessibleSymbols() {
        Set<String> accessibleStates = new HashSet<>();
        Set<String> inaccessibleStates = new HashSet<>(nonTerminals);

        accessibleStates.add("S");
        Production startProduction = getProductionWith("S");

        for (String word : startProduction.getDerivations()) {
            StringBuilder bufferedWord = new StringBuilder(word);
            for (int i = 0; i < bufferedWord.length(); i++) {
                char charFound = bufferedWord.charAt(i);
                if (Character.isUpperCase(charFound)) {
                    accessibleStates.add(Character.toString(charFound));
                }
            }
        }

        for (Production production : this.productions) {
            accessibleStates.addAll(production.addAccessibleStates(accessibleStates));
        }

        inaccessibleStates.removeAll(accessibleStates);

        for (String nonTerminal : inaccessibleStates) {
            Production found = getProductionWith(nonTerminal);
            if (found != null) {
                this.nonTerminals.remove(found.getNonTerminal());
                this.productions.remove(found);
            }
        }

        System.out.println("\n\t\t\tGrammar after elimination of Inaccessible productive Productions:");
        display();
    }

    private void removeUnproductiveSymbols() {
        Set<String> productiveSet = new HashSet<>(initiateProductiveStates());
        Set<String> unproductiveSet = new HashSet<>(nonTerminals);

        for (Production production : productions) {
            if (production.hasProductiveStates(productiveSet))
                productiveSet.add(production.getNonTerminal());
        }

        unproductiveSet.removeAll(productiveSet);

        for (String nonTerminal : unproductiveSet) {
            Production found = getProductionWith(nonTerminal);
            if (found != null) {
                this.nonTerminals.remove(found.getNonTerminal());
                this.productions.remove(found);
            }
        }

        System.out.println("\n\t\t\tGrammar after elimination of Unproductive Productions:");
        display();

    }

    public Set<String> initiateProductiveStates() {
        Set<String> productive = new HashSet<>();

        for (Production production : productions)
            if (production.containsTerminal()) // All terminals should be lowercase for it to work
                productive.add(production.getNonTerminal());

        return productive;
    }

    private void removeUnitTransitions() {
        HashMap<String, String> toReplace = new HashMap<>();
        Set<Production> toRemove = new HashSet<>();

        for (Production production : productions)
            for (String nonTerminal : nonTerminals)
                if (production.hasOnlyOneProduction(nonTerminal)) {
                    toReplace.put(production.getNonTerminal(), nonTerminal);
                    toRemove.add(production);

                } else if (production.hasUnitProduction(nonTerminal)) {
                    production.getDerivations().remove(nonTerminal);
                    production.getDerivations().addAll(getDerivationsOf(nonTerminal));
                }

        for (Map.Entry<String, String> set : toReplace.entrySet()) {
            this.nonTerminals.remove(set.getKey());
            for (Production production : this.productions)
                if (production.hasProduction(set.getKey())) {
                    production.replaceThings(set.getKey(), set.getValue());

                }
        }

        this.productions.removeAll(toRemove);
        for (Production productionToRemove : toRemove) {
            this.nonTerminals.remove(productionToRemove);
        }
        System.out.println("\n\t\t\tGrammar after elimination of unit productions:");
        display();

    }

    private Set<String> getDerivationsOf(String nonTerminal) {
        Set<String> toAdd = new HashSet<>();

        for (Production production : productions)
            if (production.getNonTerminal().equals(nonTerminal))
                toAdd.addAll(production.getDerivations());

        return toAdd;
    }

    private Production getProductionWith(String nonterminal) {
        for (Production production : productions) {
            if (production.getNonTerminal().equals(nonterminal)) {
                return production;
            }
        }
        return null;
    }

    //  ***** Elimination of empty productions functions *****
    private void removeEmptyTransitions() {
        Set<String> emptyProductions = findEmptyStates();
        while (!emptyProductions.isEmpty()) {
            String emptyProduction = emptyProductions.iterator().next();

            for (Production production : productions)
                if (production.hasProduction(emptyProduction)) {
                    production.eliminateEmptyState(emptyProduction);
                }
            emptyProductions.remove(emptyProduction);
        }

        System.out.println("\n\t\t\tGrammar after elimination of empty transitions:");
        display();
    }

    public Set<String> findEmptyStates() {
        Set<String> nullTransitions = new HashSet<>();
        Set<Production> productionsToRemove = new HashSet<>();

        for (Production production : this.productions) {
            if (production.hasOnlyEmptyTransition()) {
                productionsToRemove.add(production);
                removeEverywhere(production);

            } else if (production.hasEmptyTransition()) {
                nullTransitions.add(production.getNonTerminal());
                production.getDerivations().remove("-");
            }
        }
        this.productions.removeAll(productionsToRemove);
        for (Production productionToRemove : productionsToRemove) {
            this.nonTerminals.remove(productionToRemove);
        }

        return nullTransitions;
    }

    private void removeEverywhere(Production onlyEmpty) {
        this.nonTerminals.remove(onlyEmpty.getNonTerminal());

        for (Production production : this.productions) {
            if (production.hasProduction(onlyEmpty.getNonTerminal())) {
                production.deleteAll(onlyEmpty.getNonTerminal());
            }
        }
    }
//  ***** END of Elimination of empty productions functions *****

    public void setTerminals(Set<String> terminals) {
        this.terminals = terminals;
    }


    public void setNonTerminals(Set<String> nonTerminals) {
        this.nonTerminals = nonTerminals;
    }

    public void addProductions(Production production) {
        this.productions.add(production);
    }

    public void display() {
        System.out.println("Terminals \t\t" + this.terminals);
        System.out.println("NonTerminals \t" + this.nonTerminals);
        for (Production production : this.productions) {
            production.display();
        }
    }
}
