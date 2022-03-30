import javax.sound.midi.Soundbank;
import java.util.*;

public class Grammar {
    private Set<String> terminals = new HashSet<>();
    private Set<String> nonTerminals = new HashSet<>();
    private final List<Production> productions = new ArrayList<>();

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
        Set<String> productiveSet = new HashSet<>(addProductiveStates());
        Set<String> unproductiveSet = new HashSet<>(nonTerminals);

        for (Production production : productions) {
            if (production.hasProductiveStates(productiveSet))
                productiveSet.add(production.getNonTerminal());
        }

        unproductiveSet.removeAll(productiveSet);

        for (String nonTerminal : unproductiveSet) {
            Production found = getProductionWith(nonTerminal);
            if (found != null){
                this.nonTerminals.remove(found.getNonTerminal());
                this.productions.remove(found);
            }
        }

        System.out.println("\n\t\t\tGrammar after elimination of Unproductive Productions:");
        display();

    }

    public Set<String> addProductiveStates() {
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

                } else if (production.hasOnlyProduction(nonTerminal)) {
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
        for (Production productionToRemove: toRemove) {
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
        for (Production productionToRemove: productionsToRemove) {
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
