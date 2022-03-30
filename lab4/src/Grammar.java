import java.util.*;

public class Grammar {
    private Set<String> terminals = new HashSet<>();
    private Set<String> nonTerminals = new HashSet<>();
    private List<Production> productions = new ArrayList<>();

    public void convertToChomskyNormalForm() {
        System.out.println("\n\t\t\tInput:");
        display();

        removeEmptyTransitions();
        removeUnitTransitions();
        removeUnproductiveSymbols();
        removeInaccessibleSymbols();
    }

    private void removeInaccessibleSymbols() {
    }

    private void removeUnproductiveSymbols() {
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

    private Set<String> getProductionsOf(String nonTerminal){
        Set<String > toAdd = new HashSet<>();

        for (Production production: productions)
            if (production.getNonTerminal().equals(nonTerminal))
                toAdd.addAll(production.getDerivations());

        return toAdd;
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
            if (production.hasOnlyEmptyTransition()){
                productionsToRemove.add(production);
                removeEverywhere(production);
            }
            else if (production.hasEmptyTransition()) {
                nullTransitions.add(production.getNonTerminal());
                production.getDerivations().remove("-");
            }
        }
        this.productions.removeAll(productionsToRemove);

        return nullTransitions;
    }

    private void removeEverywhere(Production onlyEmpty){
        this.nonTerminals.remove(onlyEmpty.getNonTerminal());

        for (Production production: this.productions){
            if(production.hasProduction(onlyEmpty.getNonTerminal())){
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
        for (Production production : this.productions) {
            production.display();
        }
    }
}
