import java.util.ArrayList;
import java.util.HashSet;
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
    public boolean hasRecursion() {
        for (String word : derivations) {
            if (nonTerminal.equals(String.valueOf(word.charAt(0))))
                return true;
        }
        return false;
    }
    // End For Recursion

    // For Left Factoring
    static String commonPrefixUtil(String str1, String str2) {
        StringBuilder result = new StringBuilder();
        int n1 = str1.length();
        int n2 = str2.length();

        // Compare str1 and str2
        for (int i = 0, j = 0; i <= n1 - 1 && j <= n2 - 1; i++, j++) {
            if (str1.charAt(i) != str2.charAt(j)) {
                break;
            }
            result.append(str1.charAt(i));
        }
        return (result.toString());
    }

    public String commonPrefix() {
        ArrayList<String> arr = new ArrayList<>(this.derivations);
        if (arr.size() > 1) {
            String prefix = arr.get(0);
            for (int i = 1; i <= arr.size() - 1; i++) {
                prefix = commonPrefixUtil(prefix, arr.get(i));
            }
            return (prefix);
        } else
            return "";
    }

    public Set<String> changeToPrefix(String prefix, String newNonTerminal) {
        Set<String> toAdd = new HashSet<>();
        Set<String> toRemove = new HashSet<>();
        Set<String> newProductions = new HashSet<>();

        for (String word : this.derivations) {
            if (word.startsWith(prefix)) {
                if (word.equals(prefix))
                    newProductions.add("-");
                else
                    newProductions.add(word.substring(prefix.length()));
                toRemove.add(word);
                toAdd.add(prefix.concat(newNonTerminal));
            }
        }
        this.derivations.removeAll(toRemove);
        this.derivations.addAll(toAdd);
        return newProductions;
    }

    // END Left Factoring

    // For FIRST

    public boolean containsEmptyDerivation() {
        return this.derivations.contains("-");
    }

    public HashSet<String> getWordsWith(String nonTerminal) {
        HashSet<String> newSet = new HashSet<>();

        for (String word: this.derivations)
            if(word.contains(nonTerminal))
                newSet.add(word);
        return newSet;
    }
}
