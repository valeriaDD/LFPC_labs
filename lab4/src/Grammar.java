import java.util.*;

public class Grammar {
    private Set<String> terminals = new HashSet<>();
    private Set<String> nonTerminals = new HashSet<>();
    private String startSymbol;
    private List<Production> productions = new ArrayList<>();

    public void convertToChomskyNormalForm() {
        System.out.println("\n\t\t\tInput:");
        display();

        removeEmptyTransitions();
        removeUnitTransitions();
//        removeUnproductiveSymbols();
        removeInaccessibleSymbols();
    }

    private void removeInaccessibleSymbols() {
    }

    private void removeUnproductiveSymbols() {
        Set<String> productive = new HashSet<>();

        for (Production production : productions) {
            if (production.containsTerminal()) // All terminals should be lowercase for it to work
                productive.add(production.getNonTerminal());
        }

        System.out.println("Productive " + productive);

    }

    private void removeUnitTransitions() {
        HashMap<String, String> toReplace = new HashMap<>();
        Set<Production> toRemove = new HashSet<>();

        for (Production production : productions)
            for (String nonTerminal : nonTerminals)
                if (production.hasOnlyOneProduction(nonTerminal)) {
                    toReplace.put(production.getNonTerminal(), nonTerminal);
                    toRemove.add(production);

                } else if (production.hasOnlyProduction(nonTerminal)) {
                    production.getDerivations().remove(nonTerminal);
                    production.getDerivations().addAll(getProductionsOf(nonTerminal));
                }

        for (Map.Entry<String, String> set : toReplace.entrySet()) {
            for (Production production : this.productions)
                if (production.hasProduction(set.getKey())) {
                    production.replaceThings(set.getKey(), set.getValue());
                }
        }

        this.productions.removeAll(toRemove);
        System.out.println("\n\t\t\tGrammar after elimination of unit productions:");
        display();

    }

    private Set<String> getProductionsOf(String nonTerminal) {
        Set<String> toAdd = new HashSet<>();

        for (Production production : productions)
            if (production.getNonTerminal().equals(nonTerminal))
                toAdd.addAll(production.getDerivations());

        return toAdd;
    }

    //  ***** Elimination of empty productions functions *****
    private void removeEmptyTransitions() {
        Set<String> emptyProductions = findEmptyStates();

        while (!emptyProductions.isEmpty()) {
            String emptyProduction = emptyProductions.iterator().next();

            for (Production production : productions) {
                if (production.hasProduction(emptyProduction)) {
                    production.eliminateEmptyState(emptyProduction);
                }
//                if (production.hasProduction("-")) {
//                        production.getDerivations().remove("-");
//                }
            }
            emptyProductions.remove(emptyProduction);
//            emptyProductions.addAll(findEmptyStates());
        }

        System.out.println("\n\t\t\tGrammar after elimination of empty transitions:");
        display();
    }

    public Set<String> findEmptyStates() {
        Set<String> nullTransitions = new HashSet<>();
        Set<Production> productionsToRemove = new HashSet<>();

        for (Production production : this.productions) {
//             if (production.hasEmptyTransition())
//                nullTransitions.add(production.getNonTerminal());
            if (production.hasOnlyProduction("-")) {
                productionsToRemove.add(production);
                removeEverywhere(production);
            }
        }
        this.productions.removeAll(productionsToRemove);

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


    public List<Production> getProductions() {
        return productions;
    }

    public void setProductions(List<Production> productions) {
        this.productions = productions;
    }

    public List<Production> removeProduction(Production productionToRemove) {
        productions.remove(productionToRemove);
        return productions;
    }

    public Set<String> getTerminals() {
        return terminals;
    }

    public void setTerminals(Set<String> terminals) {
        this.terminals = terminals;
    }

    public Set<String> getNonTerminals() {
        return nonTerminals;
    }

    public void setNonTerminals(Set<String> nonTerminals) {
        this.nonTerminals = nonTerminals;
    }

    public String getStartSymbol() {
        return startSymbol;
    }

    public void setStartSymbol(String startSymbol) {
        this.startSymbol = startSymbol;
    }

    public void addProductions(Production production) {
        this.productions.add(production);
    }

    public void display() {
        System.out.println("nonterminals: " + nonTerminals);
        System.out.println("terminals " + terminals);
        for (Production production : this.productions) {
            production.display();
        }
    }
}
