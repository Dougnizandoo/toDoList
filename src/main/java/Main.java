import business.NoteService;
import models.Note;
import java.util.List;
import helpers.Helper;


public class Main {

    public static void main(String[] args) {
        NoteService ns = new NoteService();
        boolean userWantQuit = false;
        Helper hp = new Helper();

        String[] principalMenuChoices = {"1", "2", "3", "4", "5", "q"};
        String[] principalMenuDescription = {"See all notes", "Add new note", "Update a note", "Delete note", "Filtered by status", "Exit program"};

        System.out.println("Welcome to the task manager!");
        System.out.println();


        // Looping start
        while(true){
            String menuChoice;
            boolean validChoice = false;
            int menuChoiceConverted = -1;

            do{
                System.out.println("Choose a option:");
                hp.printMenu(principalMenuChoices, principalMenuDescription);

                menuChoice = hp.userInputString();
                if(!Character.isDigit(menuChoice.charAt(0))){
                    if(menuChoice.equalsIgnoreCase("q")){
                        userWantQuit = true;
                        validChoice = true;
                    }else {
                        System.out.println("Only numbers and the letter 'q' is accepted!");
                        System.out.println("Try again!");
                    }

                }else{
                    menuChoiceConverted = Integer.parseInt(menuChoice);
                    if (menuChoiceConverted >= 1 && menuChoiceConverted <= principalMenuChoices.length){
                        validChoice = true;
                    }else{ System.out.println("Invalid choice!\nTry again!"); }
                }
                System.out.println();
            }while (!validChoice);

            if(userWantQuit){
                System.out.println("Finishing Program...");
                hp.closeScanner();
                break;
            }

            switch (menuChoiceConverted){
                case 1 -> { // See all notes
                    List<Note> allNotes = ns.readAllNotes();
                    if (!allNotes.isEmpty()) {
                        System.out.printf("| %-3s | %-50s | %-5s |%n", "ID", "Note", "Done");
                        for (Note noteSelected : allNotes) {
                            System.out.printf("| %-3d | %-50s | %-5b |%n",
                                    noteSelected.ID(),
                                    noteSelected.getNote().length() > 47 ?
                                            noteSelected.getNote().substring(0, 47) + "..." :
                                            noteSelected.getNote(),
                                    noteSelected.isDone());
                        }
                    }
                }
                case 2 ->{ // Create new note
                    boolean createSuccess = false;
                    String userInput = "000";
                    do{
                        System.out.println("Digit your new note or digit 'back' to go back:");
                        userInput = hp.userInputString();

                        if(userInput.equalsIgnoreCase("back")){
                            System.out.println("Returning to menu...");
                            break;
                        } else{ createSuccess = ns.createNote(userInput);}

                    }while (!createSuccess);
                }

                case 3 -> { // Update note
                    while (true) {
                        System.out.println("Digit the Note's ID or 'back' to go back:");
                        String idInput = hp.userInputString();

                        if (idInput.equalsIgnoreCase("back")) {
                            System.out.println("Returning to menu...");
                            break;
                        }

                        try {
                            int idToUpdate = Integer.parseInt(idInput);
                            Note oldNote = ns.readNoteByID(idToUpdate);

                            if (oldNote == null) {
                                System.out.println("Invalid ID! Try again.");
                                continue;
                            }

                            System.out.println("The note is:");
                            System.out.printf("| %-50s | %-5s |%n", oldNote.getNote(), oldNote.isDone());

                            String fieldChoice;
                            do {
                                System.out.println("\nWhat do you want to change?");
                                hp.printMenu(new String[]{"n", "s", "back"}, new String[]{"Note", "Status (Done)", "Cancel"});
                                fieldChoice = hp.userInputString();
                            } while (!fieldChoice.matches("(?i)n|s|back"));

                            if (fieldChoice.equalsIgnoreCase("back")) {
                                System.out.println("Going back to menu...");
                                break;
                            }

                            boolean updated = false;
                            if (fieldChoice.equalsIgnoreCase("n")) {
                                System.out.println("Type the new note:");
                                String newText = hp.userInputString();
                                updated = ns.updateNote(idToUpdate, newText, oldNote.isDone());
                            } else {
                                hp.printMenu(new String[]{"1", "0"}, new String[]{"done", "not done"});
                                String statusInput = hp.userInputString();
                                if (statusInput.equals("1") || statusInput.equals("0")) {
                                    boolean newStatus = statusInput.equals("1");
                                    updated = ns.updateNote(idToUpdate, oldNote.getNote(), newStatus);
                                } else {
                                    System.out.println("Invalid input for status!");
                                }
                            }

                            if (updated) {
                                System.out.println("Note updated successfully!");
                            }
                            break;

                        } catch (NumberFormatException e) {
                            System.out.println("Invalid ID format! Try again.");
                        }
                    }
                }


                case 4 -> { // Delete Note
                    boolean deleteSuccess = false;

                    do {
                        System.out.println("Digit the Note's ID or type 'back' to go back:");
                        String userInput = hp.userInputString();

                        if (userInput.equalsIgnoreCase("back")) {
                            System.out.println("Returning to menu...");
                            break;
                        }

                        if (userInput.matches("\\d+")) {
                            int idToDelete = Integer.parseInt(userInput);
                            deleteSuccess = ns.deleteNote(idToDelete);
                        } else {
                            System.out.println("Invalid input! Please type a valid number or 'back'.");
                        }

                    } while (!deleteSuccess);
                }
                case 5 -> {
                    boolean validInput = false;
                    int userOption = 0;

                    do {
                        System.out.println("What status do you want to see:");
                        hp.printMenu(new String[]{"1", "2", "back"}, new String[]{"Done", "Not Done", "Back to menu"});
                        String userChoice = hp.userInputString();

                        if (userChoice.equalsIgnoreCase("back")) {
                            validInput = true;
                        }
                        if (userChoice.matches("\\d+")) {
                            validInput = true;
                            userOption = Integer.parseInt(userChoice);
                        }
                    } while (!validInput);

                    if (userOption != 0){
                        List<Note> filteredNotes = ns.readByStatus(userOption == 1);
                        if (!filteredNotes.isEmpty()) {
                            System.out.printf("| %-3s | %-50s |%n", "ID", "Note");

                            for (Note noteSelected : filteredNotes) {
                                System.out.printf("| %-3d | %-100s |%n",
                                        noteSelected.ID(),
                                        noteSelected.getNote().length() > 97 ?
                                                noteSelected.getNote().substring(0, 97) + "..." :
                                                noteSelected.getNote()); }

                        }
                    }


                }
                default -> {
                    System.out.println("Invalid option!\nHow did you do that ?");
                }
            }
            System.out.println();
        } // looping end
    }
}
