package kz.solo3;

/** Strategy interface shared by all text-buffer implementations. */
public interface TextBuffer {
    int length();
    char charAt(int index);
    void insert(int index, String text);
    String delete(int start, int end);
    String getText();
}
