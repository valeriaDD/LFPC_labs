import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class PredictiveTable {
    private final Grammar grammar;
    private final HashSet<First> firsts;
    private final HashSet<Follow> follows;
    private final HashMap<String, HashMap<String, String>> table = new HashMap<>();

    public PredictiveTable(Grammar grammar, HashSet<First> firsts, HashSet<Follow> follows) {
        this.firsts = firsts;
        this.grammar = grammar;
        this.follows = follows;
    }

    public void print() {
        constructTable();
        System.out.println("\n\t  Predictive Parsing Table");

        for (Map.Entry<String, HashMap<String, String>> tableRow : table.entrySet()) {
            String nonTerminal = tableRow.getKey();
            HashMap<String, String> terminalAndDerivation = tableRow.getValue();

            for (Map.Entry<String, String> tableColumn : terminalAndDerivation.entrySet()) {
                String terminal = tableColumn.getKey();
                String derivation = tableColumn.getValue();

                System.out.println("[" + nonTerminal + "]" + "[" + terminal + "] = " + derivation);
            }
        }
    }

    private void constructTable() {
        for (Production production : grammar.getProductions()) {
            HashMap<String, String> toAddInTable = new HashMap<>();

            for (String word : production.getDerivations())
                for (First firstsElement : firsts) {

                    if (production.getNonTerminal().equals(firstsElement.getNonTerminal())) {
                        for (String firstOf : firstsElement.getElements()) {

                            if (Character.isLowerCase(word.charAt(0)) && Character.toString(word.charAt(0)).equals(firstOf))
                                toAddInTable.put(firstOf, word);
                            else if (Character.isUpperCase(word.charAt(0)))
                                toAddInTable.put(firstOf, word);

                            if (production.containsEmptyDerivation())
                                for (Follow followElement : follows)
                                    if (followElement.getNonTerminal().equals(production.getNonTerminal()))
                                        for (String followOf : followElement.getElements())
                                            toAddInTable.put(followOf, "-");

                            this.table.put(production.getNonTerminal(), toAddInTable);
                        }
                    }
                }
        }
    }
}
