import java.util.HashSet;

public class Follow {
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

    public void addElements(HashSet<String> elements) {
        this.elements.addAll(elements);
    }

    public void addElement(String element) {
        this.elements.add(element);
    }

    public void remove(String s) {
        this.elements.remove("-");
    }
}
