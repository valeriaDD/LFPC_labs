import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class LL1 {
    Grammar grammar;
    HashSet<First> firstList = new HashSet<>();
    HashSet<Follow> followList = new HashSet<>();

    public LL1(Grammar grammar) {
        this.grammar = grammar;

        for (Production production : grammar.getProductions()) {
            First first = new First();
            first.setNonTerminal(production.getNonTerminal());
            First(production.getNonTerminal(), first);
            firstList.add(first);
        }
        for (Production production : grammar.getProductions()) {
            Follow follow = new Follow();
            follow.setNonTerminal(production.getNonTerminal());
            Follow(production.getNonTerminal(), follow);

            followList.add(follow);
        }
        System.out.println("\n\tPrint FIRST:");
        for (First first : firstList) {
            System.out.println(first.getNonTerminal() + " : " + first.getElements());
        }

        System.out.println("\n\tPrint Follow:");
        for (Follow follow : followList) {
            System.out.println(follow.getNonTerminal() + " : " + follow.getElements());
        }

        PredictiveTable predictiveTable = new PredictiveTable(this.grammar, this.firstList, this.followList);
        predictiveTable.print();
    }

    private void First(String nonTerminal, First first) {
        Production production = grammar.getProductionWith(nonTerminal);
        if (production != null) {

            for (String derivation : production.getDerivations()) {
                String firstSymbol = Character.toString(derivation.charAt(0));
                if (Character.isLowerCase(firstSymbol.charAt(0))) {
                    first.setElement(firstSymbol);
                } else if (Character.isUpperCase(firstSymbol.charAt(0))) {
                    if (getFIRSTof(firstSymbol).isEmpty()) {
                        First(firstSymbol, first);

                    } else {
                        first.setElements(getFIRSTof(firstSymbol));
                    }
                }

                if (production.containsEmptyDerivation())
                    first.setElement("-");
            }

        }
    }

    private HashSet<String> getFIRSTof(String nonTerminal) {
        HashSet<String> emptySet = new HashSet<>();
        for (First first : this.firstList) {
            if (first.getNonTerminal().equals(nonTerminal)) {
                return first.getElements();
            }
        }
        return emptySet;
    }

    private HashSet<String> getFOLLOWof(String nonTerminal) {
        HashSet<String> emptySet = new HashSet<>();
        for (Follow follow : this.followList) {
            if (follow.getNonTerminal().equals(nonTerminal)) {
                return follow.getElements();
            }
        }
        return emptySet;
    }

    private void Follow(String nonTerminal, Follow follow) {
        if (nonTerminal.equals(grammar.getStartSymbol())) {
            follow.addElement("$");
        }
        HashMap<String, HashSet<String>> dictionary = grammar.getWordsThatContains(nonTerminal);

        for (Map.Entry<String, HashSet<String>> entry : dictionary.entrySet()) {
            String where = entry.getKey();
            HashSet<String> productions = entry.getValue();
            HashSet<String> FirstOfList;
            HashSet<String> followOfList;

            for (String word : productions) {
                int indexOfSymbol = word.indexOf(nonTerminal);
                int followIndex = indexOfSymbol + 1;

                if (word.length() > followIndex) {
                    String firstOf = Character.toString(word.charAt(followIndex));
                    if (Character.isLowerCase(firstOf.charAt(0))) {
                        follow.addElement(firstOf);
                    } else {
                        FirstOfList = getFIRSTof(firstOf);
                        follow.addElements(FirstOfList);

                        if (FirstOfList.contains("-")) {
                            follow.remove("-");
                            Follow(firstOf, follow);
                        }
                    }
                } else if (word.length() == followIndex) {
                    followOfList = getFOLLOWof(where);

                    if (followOfList.isEmpty() && !where.equals(nonTerminal)) {
                        Follow(where, follow);
                    }
                    follow.addElements(followOfList);
                }

            }

        }

    }

}