package kz.solo3;

import java.util.Arrays;

/** Gap buffer: insertion near the current gap is amortized O(1). */
public final class GapBuffer implements TextBuffer {
    private char[] data;
    private int gapStart, gapEnd;

    public GapBuffer() { this(""); }
    public GapBuffer(String initial) {
        int capacity = Math.max(32, initial.length() + 32);
        data = new char[capacity];
        initial.getChars(0, initial.length(), data, 0);
        gapStart = initial.length(); gapEnd = capacity;
    }
    @Override public int length() { return data.length - (gapEnd-gapStart); }
    @Override public char charAt(int index) {
        if (index < 0 || index >= length()) throw new IndexOutOfBoundsException(index);
        return data[index < gapStart ? index : index + gapEnd-gapStart];
    }
    private void moveGap(int index) {
        if (index < 0 || index > length()) throw new IndexOutOfBoundsException(index);
        if (index < gapStart) {
            int count = gapStart-index;
            System.arraycopy(data, index, data, gapEnd-count, count);
            gapStart -= count; gapEnd -= count;
        } else if (index > gapStart) {
            int count = index-gapStart;
            System.arraycopy(data, gapEnd, data, gapStart, count);
            gapStart += count; gapEnd += count;
        }
    }
    private void ensureGap(int needed) {
        int gap = gapEnd-gapStart;
        if (gap >= needed) return;
        int extra = Math.max(needed-gap, data.length);
        char[] next = new char[data.length+extra];
        System.arraycopy(data, 0, next, 0, gapStart);
        int tail = data.length-gapEnd;
        int newGapEnd = next.length-tail;
        System.arraycopy(data, gapEnd, next, newGapEnd, tail);
        data=next; gapEnd=newGapEnd;
    }
    @Override public void insert(int index, String text) {
        if (text == null) throw new IllegalArgumentException("text cannot be null");
        moveGap(index); ensureGap(text.length());
        text.getChars(0, text.length(), data, gapStart); gapStart += text.length();
    }
    @Override public String delete(int start, int end) {
        if (start < 0 || end < start || end > length()) throw new IndexOutOfBoundsException();
        moveGap(start);
        String removed = new String(data, gapEnd, end-start);
        gapEnd += end-start;
        return removed;
    }
    @Override public String getText() {
        return new String(data, 0, gapStart) + new String(data, gapEnd, data.length-gapEnd);
    }
}
