import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Grammar {
    private Set<String> terminals = new HashSet<>();
    private Set<String> nonTerminals = new HashSet<>();
    private String startSymbol;
    private List<Production> productions = new ArrayList<>();

    public List<Production> getProductions() {
        return productions;
    }

    public void setProductions(List<Production> productions) {
        this.productions = productions;
    }

    public List<Production> removeProduction(Production productionToRemove){
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

    public void display(){
        for (Production production: this.productions) {
            production.display();
        }
    }
}
