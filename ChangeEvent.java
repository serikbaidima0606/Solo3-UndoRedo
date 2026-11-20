package kz.solo3;
public record ChangeEvent(int start, int oldLength, String newText) {
    public ChangeEvent { if(start<0||oldLength<0||newText==null) throw new IllegalArgumentException(); }
    public int newLength(){ return newText.length(); }
}
