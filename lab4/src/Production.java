import java.util.List;

public class Production {
    private String nonTerminal;
    private List<String> derivations;

    public String getNonTerminal() {
        return nonTerminal;
    }

    public void setNonTerminal(String nonTerminal) {
        this.nonTerminal = nonTerminal;
    }

    public List<String> getDerivations() {
        return derivations;
    }

    public void setDerivations(List<String> derivations) {
        this.derivations = derivations;
    }

    public void removeDerivation(String derivationToRemove) {
        this.derivations.remove(derivationToRemove);
    }

    public void addDerivation(String derivationToAdd) {
        this.derivations.add(derivationToAdd);
    }

    public void display() {
        System.out.println(this.getNonTerminal() + "->" + this.getDerivations());
    }

}
