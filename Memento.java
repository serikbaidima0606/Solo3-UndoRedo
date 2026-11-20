package kz.solo3;
/** Stores a reference to a persistent history node, not a full document copy. */
public final class Memento {
    final UndoNode state;
    Memento(UndoNode state){this.state=state;}
}
