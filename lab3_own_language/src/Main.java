import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) throws FileNotFoundException {
        List<Token> tokens = new ArrayList<>();

        Scanner scanner = new Scanner(new File("C:\\Users\\valeria\\Desktop\\Uni\\LFPC\\LFPC_labs\\lab3_own_language\\src\\Program.txt"));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Lexer lexer = new Lexer(line);
            tokens.addAll(lexer.tokenizer());
        }

        for (Token token : tokens)
            System.out.println(token.display());
    }


}
