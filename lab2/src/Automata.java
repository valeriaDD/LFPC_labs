import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Automata {
    private final ArrayList<String> Q = new ArrayList<>();
    private final ArrayList<String> F = new ArrayList<>();
    private final ArrayList<String> Alphabet = new ArrayList<>();
    private final HashMap<String, ArrayList<HashMap<String, Set<String>>>> Transactions = new HashMap<>();

    Scanner scanner = new Scanner(System.in);

    private String @NotNull [] scanAndSplit(String regex) {
        String scannedStr = scanner.nextLine();
        return scannedStr.split(regex);
    }

    private void addQ() {
        System.out.println("Add values of Q this format: Q = q1 q2 q3 ");
        System.out.print("Q = ");

        String[] splitScannedStr = scanAndSplit(" ");
        Collections.addAll(Q, splitScannedStr);
    }


    private void addF() {
        System.out.println("Add values of the Final States in format: F = q1 q2");
        System.out.print("F = ");

        String[] splitScannedStr = scanAndSplit(" ");

        for (String element : splitScannedStr)
            if (Q.contains(element)) {
                F.add(element);
            } else {
                System.out.println("Error: Final state should be a part of Q");
            }
    }

    private void addAlphabet() {
        System.out.println("Add values of the alphabet this format: Alph = a b ");
        System.out.print("Alph = ");

        String[] splitScannedStr = scanAndSplit(" ");
        Collections.addAll(Alphabet, splitScannedStr);
    }

    private boolean verifyInput(String @NotNull [] splitByEqual, String @NotNull [] splitByComma) {
        return Q.contains(splitByComma[0])
                && Q.contains(splitByEqual[splitByEqual.length - 1])
                && Alphabet.contains(splitByComma[splitByComma.length - 1]);
    }

    private void addTransaction() {
        System.out.println("How many transactions do you have?");
        int transactionsNr = scanner.nextInt();
        System.out.println("Add transactions in this format: \n\tq1,a=q2 \n\tq2,b=q2 ");
        System.out.print("Transactions = ");

        while (transactionsNr >= 0) {
            String[] splitByEqual = scanAndSplit("=");
            String[] splitByComma = splitByEqual[0].split(",");

            String Node1 = splitByComma[0];
            String transitionVariable = splitByComma[splitByComma.length - 1];
            String Node2 = splitByEqual[splitByEqual.length - 1];

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
            transactionsNr--;
        }
    }

    public void inputAutomata() {
//        addQ();
//        addF();
//        addAlphabet();
        addTransaction();
        System.out.println(Transactions);
    }

    public static void main(String[] args) {
        Automata NFA = new Automata();
        NFA.inputAutomata();
    }
}
