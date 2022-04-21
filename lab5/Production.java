package lab5;

import java.util.Set;

public class Production {
    private String nonTerminal;
    private Set<String> derivations;

    public String getNonTerminal() {
        return nonTerminal;
    }

    public void setNonTerminal(String nonTerminal) {
        this.nonTerminal = nonTerminal;
    }

    public Set<String> getDerivations() {
        return derivations;
    }

    public void setDerivations(Set<String> derivations) {
        this.derivations = derivations;
    }
    public void addDerivation(String derivations) {
        this.derivations.add(derivations);
    }

    public void display() {
        System.out.println(this.getNonTerminal() + "->" + this.getDerivations());
    }

    // For Recursion
    public boolean hasRecursion(){
        for (String word:derivations) {
            if(nonTerminal.equals(String.valueOf(word.charAt(0))))
                return true;
        }
        return false;
    }
    // End For Recursion

}
