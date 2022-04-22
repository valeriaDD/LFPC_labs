import java.util.HashSet;

public class First {
    String nonTerminal;
    HashSet<String> elements = new HashSet<>();

    public String getNonTerminal() {
        return nonTerminal;
    }

    public void setNonTerminal(String nonTerminal) {
        this.nonTerminal = nonTerminal;
    }

    public HashSet<String> getElements() {
        return elements;
    }

    public void setElements(HashSet<String> elements) {
        this.elements.addAll(elements);
    }

    public void setElement(String element) {
        this.elements.add(element);
    }
}
