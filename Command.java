package kz.solo3;
public interface Command { void execute(TextBuffer buffer); void undo(TextBuffer buffer); }
