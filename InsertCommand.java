package kz.solo3;
public final class InsertCommand implements Command {
    private final int index; private final String text;
    public InsertCommand(int index,String text){this.index=index;this.text=text;}
    @Override public void execute(TextBuffer b){b.insert(index,text);}
    @Override public void undo(TextBuffer b){b.delete(index,index+text.length());}
    public int index(){return index;} public String text(){return text;}
}
