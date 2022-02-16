import java.util.*;

public class Automata {
    private final ArrayList<String> Q = new ArrayList<>();
    private String startState = "q0";
    private final ArrayList<String> F = new ArrayList<>();
    private final ArrayList<String> Alphabet = new ArrayList<>();
    private final HashMap<String, ArrayList<HashMap<String, Set<String>>>> Transactions = new HashMap<>();

    Scanner scanner = new Scanner(System.in);

    private String[] scanAndSplit(String regex) {
        String scannedStr = scanner.nextLine();
        return scannedStr.split(regex);
    }

    private void addQ() {
//        System.out.println("Add values of Q this format: Q = q1 q2 q3 ");
//        System.out.print("Q = ");
//
//        String[] splitScannedStr = scanAndSplit(" ");
//        Collections.addAll(Q, splitScannedStr);

        Q.add("q0");
        Q.add("q1");
        Q.add("q2");
        Q.add("q3");
        Q.add("q4");
    }

    private void addF() {
//        System.out.println("Add values of the Final States in format: F = q1 q2");
//        System.out.print("F = ");
//
//        String[] splitScannedStr = scanAndSplit(" ");
//
//        for (String element : splitScannedStr)
//            if (Q.contains(element)) {
//                F.add(element);
//            } else {
//                System.out.println("Error: Final state should be a part of Q");
//            }
        F.add("q3");
    }

    private void addAlphabet() {
//        System.out.println("Add values of the alphabet this format: Alph = a b ");
//        System.out.print("Alph = ");
//
//        String[] splitScannedStr = scanAndSplit(" ");
//        Collections.addAll(Alphabet, splitScannedStr);
        Alphabet.add("a");
        Alphabet.add("b");
    }

    private boolean verifyInput(String Node1, String Node2, String transitionVariable) {
        return Q.contains(Node1)
                && Q.contains(Node2)
                && Alphabet.contains(transitionVariable);
    }

    private void addTransactions() {
        String scannedStr = null;
        System.out.println("Add transactions in this format: \n\tq1,a=q2 \n\tq2,b=q2 ");
        System.out.print("Transactions = ");

        while (!(scannedStr = scanner.nextLine()).isEmpty()) {
            String[] splitByEqual = scannedStr.split("=");
            String[] splitByComma = splitByEqual[0].split(",");

            String Node1 = splitByComma[0];
            String transitionVariable = splitByComma[splitByComma.length - 1];
            String Node2 = splitByEqual[splitByEqual.length - 1];

            if (verifyInput(Node1, Node2, transitionVariable)) {
                Set<String> newSet = new HashSet<String>();
                HashMap<String, Set<String>> newHashMap = new HashMap<>();
                ArrayList<HashMap<String, Set<String>>> value = new ArrayList<>();

                if (!Transactions.containsKey(Node1)) {
                    newSet.add(Node2);
                    newHashMap.put(transitionVariable, newSet);
                    value.add(newHashMap);
                    Transactions.put(Node1, value);
                } else {
                    value = Transactions.get(Node1);
                    for (HashMap<String, Set<String>> element : value)
                        if (element.containsKey(transitionVariable)) {
                            newSet = element.get(transitionVariable);
                            newSet.add(Node2);
                            element.replace(transitionVariable, newSet);
                        } else {
                            newSet.add(Node2);
                            element.put(transitionVariable, newSet);
                        }
                }
            } else
                System.out.println("Error, alphabet or q do not match");
        }
    }

    public void inputAutomata() {
        addQ();
//        System.out.println("State the start q: ");
//        startState = scanner.nextLine();
        addF();
        addAlphabet();
        addTransactions();
    }

    public void printAutomata() {
        if (!Q.isEmpty()) {
            for (Map.Entry<String, ArrayList<HashMap<String, Set<String>>>> entry : Transactions.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                System.out.print(key + " -> ");
                System.out.println(value);
            }
        }
    }

    private HashMap<Set<String>, ArrayList<HashMap<String, Set<String>>>> initiateDFA() {
        HashMap<Set<String>, ArrayList<HashMap<String, Set<String>>>> DFA = new HashMap<>();

        ArrayList<HashMap<String, Set<String>>> initialValue = Transactions.get(startState);
        Set<String> initialKey = new HashSet<String>();

        initialKey.add(startState);
        DFA.put(initialKey, initialValue);

        return DFA;
    }

    private ArrayList<Set<String>> findNextStates(ArrayList<HashMap<String, Set<String>>> currentStateValue) {
        ArrayList<Set<String>> newStatesArray = new ArrayList<>();

        for (HashMap<String, Set<String>> StringSetHashMap : currentStateValue)
            for (String letter : Alphabet)
                if (StringSetHashMap.containsKey(letter)) {
                    Set<String> newState = StringSetHashMap.get(letter);
                    newStatesArray.add(newState);
                }
        return newStatesArray;
    }

    private ArrayList<HashMap<String, Set<String>>> findNewStatesValues(Set<String> newState) {
        ArrayList<HashMap<String, Set<String>>> NewStateValues = new ArrayList<>();

        for (String letter : Alphabet) {
            Set<String> newSetString = new HashSet<>();
            for (String state : newState) {
                ArrayList<HashMap<String, Set<String>>> values = Transactions.get(state);
                for (HashMap<String, Set<String>> stringSetHashMap : values)
                    if (stringSetHashMap.containsKey(letter))
                        for (String string : stringSetHashMap.get(letter))
                            newSetString.add(string);
            }
            HashMap<String, Set<String>> map = new HashMap<>();
            map.put(letter, newSetString);
            NewStateValues.add(map);
        }
        return NewStateValues;
    }

    public void printDFA(HashMap<Set<String>, ArrayList<HashMap<String, Set<String>>>> DFA) {
        System.out.println(" ");
        for (Map.Entry<Set<String>, ArrayList<HashMap<String, Set<String>>>> entry : DFA.entrySet()) {
            Set<String> key = entry.getKey();
            Object value = entry.getValue();
            System.out.print(key + " -> ");
            System.out.println(value);
        }
    }

    public void toDFA() {
        HashMap<Set<String>, ArrayList<HashMap<String, Set<String>>>> DFA = new HashMap<>();
        DFA = initiateDFA();

        Set<String> initialState = new HashSet<>();
        initialState.add(startState);

        ArrayList<Set<String>> nextStates = new ArrayList<>();
        nextStates = findNextStates(Transactions.get(startState));


            Set<String> state = nextStates.get(0);
            DFA.put(state, findNewStatesValues(state));
            nextStates.addAll(findNextStates(DFA.get(state)));

        System.out.println(nextStates);





        printDFA(DFA);

    }


}
