package kz.solo3;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class EditorHistoryTest {
    @Test void undoRedoAndPreservedBranch(){
        TextEditor e=new TextEditor(new GapBuffer());e.execute(new InsertCommand(0,"A"));e.execute(new InsertCommand(1,"B"));
        e.undo();assertEquals("A",e.text());e.execute(new InsertCommand(1,"C"));assertEquals("AC",e.text());
        e.undo();assertEquals(2,e.redoBranches());e.redo(0);assertEquals("AB",e.text());e.undo();e.redo(1);assertEquals("AC",e.text());
    }
    @Test void compositeIsOneUndo(){
        TextEditor e=new TextEditor(new GapBuffer());
        CompositeCommand macro=new CompositeCommand().add(new InsertCommand(0,"Hello")).add(new InsertCommand(5," world"));
        e.execute(macro);assertEquals("Hello world",e.text());e.undo();assertEquals("",e.text());e.redo();assertEquals("Hello world",e.text());
    }
    @Test void mementoRestoresWithoutLosingBranches(){
        TextEditor e=new TextEditor(new PieceTable());e.execute(new InsertCommand(0,"one"));Memento m=e.save();
        e.execute(new InsertCommand(3," two"));e.restore(m);assertEquals("one",e.text());
    }
    @Test void observerReceivesOnlyChangedRange(){
        TextEditor e=new TextEditor(new GapBuffer("hello world"));var events=new ArrayList<ChangeEvent>();e.addObserver(events::add);
        e.execute(new InsertCommand(5,"!"));assertEquals(1,events.size());assertEquals(5,events.get(0).start());assertEquals(0,events.get(0).oldLength());assertEquals("!",events.get(0).newText());
    }
}
