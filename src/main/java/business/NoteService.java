package business;
import models.Note;
import services.NoteRepository;
import java.util.List;


public class NoteService {
    private final NoteRepository db;
    private static final String DB_RESP = "ok";

    // helpers
    public NoteService() {
        this.db = new NoteRepository();
    }

    private boolean stringIsInvalid(String note){
        if (note == null){
            System.out.println("Note can't be null!");
            return true;
        }

        String trimmed = note.trim();
        if (trimmed.length() < 3){
            System.out.println("Note must contain 3 characters!");
            return true;
        }

        if( !Character.isLetter(trimmed.charAt(0)) ){
            System.out.println("Note must start with letters!");
            return true;
        }
        return false;
    }


    // main functions
    public Boolean createNote(String new_note){
        if(stringIsInvalid(new_note)){ return false; }

        Note noteModel = new Note();
        noteModel.setNote(new_note.trim());
        String request = db.createData(noteModel);

        if (!request.equalsIgnoreCase(DB_RESP) ){
            System.out.println(request);
            return false;
        }

        System.out.println("Note created!");
        return true;
    }

    public List<Note> readAllNotes(){
        List<Note> requestResults = db.readAllData();
        if (requestResults.isEmpty()){
            System.out.println("Nothing here yet!");
        }
        return requestResults;
    }

    public Note readNoteByID(int ID){
        if (ID < 1){
            System.out.println("ID can't be less than 1!");
            return null;
        }

        Note selectedNote = db.readDataByID(ID);
        if (selectedNote == null){
            System.out.println("ID invalid!");
            return null;
        }

        return selectedNote;
    }

    public List<Note> readByStatus(boolean status){
        List<Note> requestResults = db.readDataByStatus(status);
        if (requestResults.isEmpty()){
            System.out.println("Nothing registered with this status!");
        }
        return requestResults;
    }

    public boolean updateNote(int ID, String newNote, boolean is_done){
        if(stringIsInvalid(newNote)){ return false; }

        Note oldNote = readNoteByID(ID);
        if (oldNote == null){ return  false; }

        if(newNote.trim().equalsIgnoreCase(oldNote.getNote()) && is_done == oldNote.isDone()){
            System.out.println("Canceling operation.\nThe update is the same as the original note!");
            return false;
        }

        Note nn = new Note(ID, newNote, is_done); //nn == new note
        String updateNote = db.updateData(nn);

        if(!updateNote.equalsIgnoreCase(DB_RESP)){
            System.out.println(updateNote);
            return false;
        }

        System.out.println("Note updated!");
        return true;
    }

    public boolean deleteNote(int ID){
        if (readNoteByID(ID) == null){ return  false; }

        String tryDelete = db.deleteData(ID);
        if(!tryDelete.equalsIgnoreCase(DB_RESP)){
            System.out.println(tryDelete);
            return false;
        }

        System.out.println("Note deleted!");
        return true;
    }
}
