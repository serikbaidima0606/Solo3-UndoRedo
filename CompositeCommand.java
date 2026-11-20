package kz.solo3;
import java.util.ArrayList;
import java.util.List;
/** Macro command; one undo reverses all child commands. */
public final class CompositeCommand implements Command {
    private final List<Command> commands=new ArrayList<>();
    public CompositeCommand add(Command c){commands.add(c);return this;}
    @Override public void execute(TextBuffer b){int done=0;try{for(Command c:commands){c.execute(b);done++;}}catch(RuntimeException e){for(int i=done-1;i>=0;i--)commands.get(i).undo(b);throw e;}}
    @Override public void undo(TextBuffer b){for(int i=commands.size()-1;i>=0;i--)commands.get(i).undo(b);}
}
