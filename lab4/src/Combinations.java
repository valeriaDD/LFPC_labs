import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Combinations {
    private StringBuffer output = new StringBuffer();
    private  String inputString;
    private Set<String> newWordsSet = new HashSet<>();


    public Combinations( final String str ){
        inputString = str;
    }

    public void combine(int start){
        for( int i = start; i < inputString.length(); i++ ){
            output.append( inputString.charAt(i) );
            if (output.toString().contains("B") && output.toString().contains("C"))
                 newWordsSet.add(output.toString());
            if ( i < inputString.length() )
                combine( i + 1);
            output.setLength( output.length() - 1 );
        }
    }

    public void displayCombination(){
        combine(0);

        System.out.println(newWordsSet);
    }

    public static String reverseString(String str){
        StringBuilder sb=new StringBuilder(str);
        sb.reverse();
        return sb.toString();
    }
}