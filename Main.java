package kz.solo3;

public final class Main {
    public static void main(String[] args) {
        TextEditor editor=new TextEditor(new GapBuffer());
        editor.addObserver(c->System.out.printf("View update: start=%d, oldLength=%d, newText=\"%s\"%n",c.start(),c.oldLength(),c.newText()));
        editor.execute(new InsertCommand(0,"Сәлем"));
        Memento saved=editor.save();
        editor.execute(new InsertCommand(editor.buffer().length()," әлем"));
        System.out.println("Құжат: "+editor.text());
        editor.undo();
        System.out.println("Undo: "+editor.text());
        editor.execute(new InsertCommand(editor.buffer().length()," Java"));
        System.out.println("Жаңа тармақ: "+editor.text()+" | redo тармақтары: "+editor.redoBranches());
        editor.restore(saved);
        System.out.println("Memento restore: "+editor.text());
        System.out.println("PieceTable demo: "+new PieceTable("Undo/Redo").getText());
    }
}
