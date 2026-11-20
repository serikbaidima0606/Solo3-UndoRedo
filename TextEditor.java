package kz.solo3;

import java.util.ArrayList;
import java.util.List;

public final class TextEditor {
    private final TextBuffer buffer;
    private final UndoNode root=new UndoNode(null,null);
    private UndoNode current=root;
    private final List<EditorObserver> observers=new ArrayList<>();
    public TextEditor(TextBuffer buffer){this.buffer=buffer;}
    public TextBuffer buffer(){return buffer;}
    public String text(){return buffer.getText();}
    public void addObserver(EditorObserver o){observers.add(o);}
    public void removeObserver(EditorObserver o){observers.remove(o);}
    private void notifyChange(int start,int oldLen,String newText){ChangeEvent e=new ChangeEvent(start,oldLen,newText);for(EditorObserver o:List.copyOf(observers))o.onChange(e);}
    public void execute(Command command){
        String before=buffer.getText();
        command.execute(buffer);
        UndoNode node=new UndoNode(current,command);current.children.add(node);current=node;
        String after=buffer.getText();
        int prefix=0;while(prefix<before.length()&&prefix<after.length()&&before.charAt(prefix)==after.charAt(prefix))prefix++;
        int suffix=0;while(suffix<before.length()-prefix&&suffix<after.length()-prefix&&before.charAt(before.length()-1-suffix)==after.charAt(after.length()-1-suffix))suffix++;
        notifyChange(prefix,before.length()-prefix-suffix,after.substring(prefix,after.length()-suffix));
    }
    public boolean canUndo(){return current!=root;}
    public boolean canRedo(){return !current.children.isEmpty();}
    public int redoBranches(){return current.children.size();}
    public void undo(){
        if(!canUndo())return;
        UndoNode node=current;String before=buffer.getText();node.command.undo(buffer);current=node.parent;
        emitDiff(before,buffer.getText());
    }
    public void redo(){redo(0);}
    public void redo(int branch){
        if(branch<0||branch>=current.children.size())throw new IndexOutOfBoundsException("No redo branch: "+branch);
        UndoNode node=current.children.get(branch);String before=buffer.getText();node.command.execute(buffer);current=node;emitDiff(before,buffer.getText());
    }
    private void emitDiff(String before,String after){
        int p=0;while(p<before.length()&&p<after.length()&&before.charAt(p)==after.charAt(p))p++;
        int s=0;while(s<before.length()-p&&s<after.length()-p&&before.charAt(before.length()-1-s)==after.charAt(after.length()-1-s))s++;
        notifyChange(p,before.length()-p-s,after.substring(p,after.length()-s));
    }
    public Memento save(){return new Memento(current);}
    public void restore(Memento m){
        if(m==null)throw new IllegalArgumentException("memento cannot be null");
        UndoNode target=m.state;
        java.util.IdentityHashMap<UndoNode,Boolean> ancestors=new java.util.IdentityHashMap<>();
        for(UndoNode n=current;n!=null;n=n.parent)ancestors.put(n,true);
        UndoNode lca=target;while(lca!=null&&!ancestors.containsKey(lca))lca=lca.parent;
        while(current!=lca)undo();
        List<UndoNode> path=new ArrayList<>();for(UndoNode n=target;n!=lca;n=n.parent)path.add(n);
        for(int i=path.size()-1;i>=0;i--){UndoNode n=path.get(i);int idx=n.parent.children.indexOf(n);redo(idx);}
    }
}
