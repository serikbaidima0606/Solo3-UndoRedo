package kz.solo3;
import java.util.ArrayList;
import java.util.List;
final class UndoNode {
    final UndoNode parent; final Command command; final List<UndoNode> children=new ArrayList<>();
    UndoNode(UndoNode parent,Command command){this.parent=parent;this.command=command;}
}
