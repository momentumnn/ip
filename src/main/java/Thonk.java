import java.util.ArrayList;
import java.util.Scanner;

public class Thonk {
    public static void main(String[] args) {
        banner();
        echo();

        System.out.println("ok bye bye\n" );

    }

    public static void banner(){
        String logo ="""
               _____ _                 _
              |_   _| |__   ___  _ __ | | __
                | | | '_ \\ / _ \\| '_ \\| |/ /
                | | | | | | (_) | | | |   <
                |_| |_| |_|\\___/|_| |_|_|\\_\\
              """;
        String divider = "_______________________________\n\n";
        System.out.println("Hello from\n" + logo + "what u want \n" + divider);
    }
    public static void echo(){
        String divider = "_______________________________\n\n";
        Scanner inp = new Scanner(System.in);
        ArrayList<String> past_words = new ArrayList<String>();
        while(true){
            String word = inp.nextLine();
            switch(word){
                case "bye":
                    return;
                case "list":
                    list(past_words);
                    break;
                default:
                    past_words.add(word);
                    String outp= past_words.toString()
                            .replace("[","")
                            .replace("]","");
                    System.out.println("i also can " + outp + "\n" + divider);

            }}
    }

    public static void list(ArrayList<String> past_words){
        int i =1;
        for(String word: past_words)
       {
            System.out.println(i + ". " + word);
            i++;
        };
        System.out.println("\n");
    }
}
