package models;

public class Note {
    private int ID;
    private String Note;
    private boolean isDone;

    public Note(){}
    public Note(int ID, String Note, boolean isDone){
        this.ID = ID;
        this.Note = Note;
        this.isDone = isDone;
    }

    // Getters
    public int ID(){
        return ID;
    }

    public String getNote(){
        return Note;
    }

    public boolean isDone() {
        return isDone;
    }

    // Setters
    public void setID(int newID){
        this.ID = newID;
    }

    public void setNote(String newNote){
        this.Note = newNote;
    }

    public void setDone(boolean isDone){
        this.isDone = isDone;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + ID +
                ", note='" + Note + '\'' +
                ", isDone=" + isDone +
                '}';
    }
}
