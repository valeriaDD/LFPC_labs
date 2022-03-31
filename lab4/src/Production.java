import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.concat;

public class Production {
    private String nonTerminal;
    private Set<String> derivations;

    private StringBuffer combinations = new StringBuffer();

    public static String removeDuplicateCharacters(String word) {
        final StringBuilder output = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            String character = word.substring(i, i + 1);
            if (output.indexOf(character) < 0)
                output.append(character);
        }
        return output.toString();
    }

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

    public void display() {
        System.out.println(this.getNonTerminal() + "->" + this.getDerivations());
    }

    //  ***** Elimination of empty productions functions *****
    public boolean hasEmptyTransition() {
        for (String word : this.derivations)
            if (word.contains("-"))
                return true;
        return false;
    }

    public boolean hasOnlyEmptyTransition() {
        return this.derivations.size() == 1 && this.derivations.contains("-");
    }

    public boolean hasUnitProduction(String nonTerminal) {
        for (String word: this.derivations)
            if (word.equals(nonTerminal))
                return true;

        return false;
    }

    public boolean hasProduction(String nonTerminal) {
        for (String word : this.derivations)
            if (word.contains(nonTerminal))
                return true;
        return false;
    }

    public void eliminateEmptyState(String emptyProduction) {
        Set<String> newWordsSet = new HashSet<>();
        Set<String> toRemove = new HashSet<>();

        for (String word : this.derivations) {
            if (word.length() == 1 && word.contains("-")) {
                toRemove.add(word);
            } else {
                newWordsSet.addAll(makeCombinations(0, word, newWordsSet));
                filterStates(newWordsSet, emptyProduction, word);
            }
        }
        this.derivations.removeAll(toRemove);
    }

    // Make all possible combinations of characters from a given String
    public Set<String> makeCombinations(int start, String inputString, Set<String> newWordsSet) {
        for (int i = start; i < inputString.length(); i++) {
            combinations.append(inputString.charAt(i));
            newWordsSet.add(combinations.toString());
            makeCombinations(i + 1, inputString, newWordsSet);
            combinations.setLength(combinations.length() - 1);
        }
        return newWordsSet;
    }

    public void filterStates(Set<String> newProductions, String emptyProduction, String word) {
        Set<String> toRemove = new HashSet<>();
        List<Character> nonEmptyCharacters = new ArrayList<>();
        StringBuilder bufferedWord = new StringBuilder(word);

        // Find all nonEmpty states contained in the word
        for (int i = 0; i < bufferedWord.length(); i++)
            if (bufferedWord.charAt(i) != emptyProduction.charAt(0))
                nonEmptyCharacters.add(bufferedWord.charAt(i));

        // Add to "toRemove" Set Words that do not contain all nonEmpty states from a word
        for (String newProductionWord : newProductions) {
            StringBuilder newProductionWordCopy = new StringBuilder(newProductionWord);
            int counter = 0;

            for (Character nonEmpty : nonEmptyCharacters)
                if (newProductionWordCopy.indexOf(nonEmpty.toString()) != -1) {
                    newProductionWordCopy.deleteCharAt(newProductionWordCopy.indexOf(nonEmpty.toString()));
                    counter++;
                }

            if (counter != nonEmptyCharacters.size())
                toRemove.add(newProductionWord);
        }

        // Remove unneeded productions and concat derivations
        newProductions.removeAll(toRemove);
        this.derivations = concat(newProductions.stream(), this.derivations.stream())
                .collect(toSet());
        this.derivations.remove("-");
    }

    public void deleteAll(String nonTerminal) {
        Set<String> toRemove = new HashSet<>();
        Set<String> toAdd = new HashSet<>();

        for (String word : this.derivations) {
            StringBuffer bufferedWord = new StringBuffer(word);

            while (bufferedWord.indexOf(nonTerminal) != -1) {
                toRemove.add(word);
                bufferedWord.deleteCharAt(bufferedWord.indexOf(nonTerminal));
            }
            toAdd.add(bufferedWord.toString());
        }
        this.derivations.removeAll(toRemove);
        this.derivations.addAll(toAdd);
    }

    public boolean hasOnlyOneProduction(String nonTerminal) {
        return this.derivations.size() == 1 && this.derivations.contains(nonTerminal);
    }

    public void replaceThings(String characterToReplace, String replacementCharacter) {
        Set<String> toRemove = new HashSet<>();
        Set<String> toAdd = new HashSet<>();

        for (String word : this.derivations)
            if (word.contains(characterToReplace)) {
                toRemove.add(word);
                String replacementWord = Pattern.compile(characterToReplace).matcher(word).replaceAll(replacementCharacter);
                toAdd.add(replacementWord);
            }

        this.derivations.removeAll(toRemove);
        this.derivations.addAll(toAdd);
    }

    public boolean containsTerminal() {
        String regex = "[a-z]";
        for (String word : this.derivations) {
            if (word.matches(regex))
                return true;
        }
        return false;
    }

    public boolean hasProductiveStates(Set<String> productiveSet) {

        for (String word : this.derivations) {
            StringBuilder bufferedWord = new StringBuilder(removeDuplicateCharacters(word));

            for (String productiveProduction : productiveSet) {
                for (int i = 0; i < bufferedWord.length(); i++) {
                    char ch = bufferedWord.charAt(i);

                    if (Character.isLowerCase(ch)) {
                        bufferedWord.deleteCharAt(i);
                    } else if (Character.toString(ch).equals(productiveProduction)) {
                        bufferedWord.deleteCharAt(i);
                    }
                }
            }
            if (bufferedWord.isEmpty())
                return true;
        }
        return false;
    }

    public Set<String> addAccessibleStates(Set<String> accessibleSet) {
        Set<String> accessibleStates = new HashSet<>();

        for (String accessibleProduction : accessibleSet)
            if(this.nonTerminal.equals(accessibleProduction))
                for (String word : this.derivations){
                    StringBuilder bufferedWord = new StringBuilder(word);
                    for (int i = 0; i < bufferedWord.length(); i++) {
                        char charFound = bufferedWord.charAt(i);
                        if (Character.isUpperCase(charFound)) {
                            accessibleStates.add(Character.toString(charFound));
                        }
                    }
            }

        return accessibleStates;
    }


//  ***** END  of Elimination of empty productions functions *****
}
