package helpers;
import java.util.Scanner;

public class Helper {
    private final Scanner sc = new Scanner(System.in);

    public void printMenu(String[] optionChoice, String[] optionDescription){
        if(optionChoice.length != optionDescription.length){
            System.out.println("Both lists must have the same length!");
        }else{

            for(int x = 0; x < optionChoice.length; x++){
                System.out.printf("[ %s ] - %s%n", optionChoice[x], optionDescription[x]);
            }
        }
    }

    public String userInputString(){
        String ui;
        do {
            System.out.print("> ");
            ui = sc.nextLine().trim();

            if(ui.isEmpty()){
                System.out.println("You must digit something!\n");
            }

        } while (ui.isEmpty());
        return ui;
    }

    public void closeScanner() {
        sc.close();
    }
}
