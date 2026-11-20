package kz.solo3;
public final class DeleteCommand implements Command {
    private final int start,end; private String removed;
    public DeleteCommand(int start,int end){this.start=start;this.end=end;}
    @Override public void execute(TextBuffer b){removed=b.delete(start,end);}
    @Override public void undo(TextBuffer b){b.insert(start,removed);}
    public int start(){return start;} public int end(){return end;} public String removed(){return removed;}
}
