package kz.solo3;

import java.util.ArrayList;
import java.util.List;

/** Piece table with immutable add chunks; snapshots can share chunk storage. */
public final class PieceTable implements TextBuffer {
    private record Piece(int chunk, int start, int length) {}
    private final List<String> chunks = new ArrayList<>();
    private final List<Piece> pieces = new ArrayList<>();
    private int length;
    public PieceTable() { this(""); }
    public PieceTable(String initial) {
        chunks.add(initial);
        if (!initial.isEmpty()) pieces.add(new Piece(0,0,initial.length()));
        length=initial.length();
    }
    @Override public int length() { return length; }
    private int[] locate(int position) {
        if (position<0 || position>length) throw new IndexOutOfBoundsException(position);
        int offset=0;
        for(int i=0;i<pieces.size();i++){
            Piece p=pieces.get(i);
            if(position<offset+p.length) return new int[]{i,position-offset};
            offset+=p.length;
        }
        return new int[]{pieces.size(),0};
    }
    private void splitAt(int position) {
        int[] loc=locate(position); int i=loc[0], within=loc[1];
        if(i>=pieces.size() || within==0) return;
        Piece p=pieces.remove(i);
        pieces.add(i,new Piece(p.chunk,p.start+within,p.length-within));
        pieces.add(i,new Piece(p.chunk,p.start,within));
    }
    @Override public void insert(int index,String text) {
        if(text==null) throw new IllegalArgumentException("text cannot be null");
        splitAt(index);
        int at=locate(index)[0];
        if(!text.isEmpty()){
            int chunk=chunks.size(); chunks.add(text);
            pieces.add(at,new Piece(chunk,0,text.length())); length+=text.length();
        }
    }
    @Override public String delete(int start,int end) {
        if(start<0||end<start||end>length) throw new IndexOutOfBoundsException();
        if(start==end) return "";
        String all=getText(); String removed=all.substring(start,end);
        splitAt(end); splitAt(start);
        int from=locate(start)[0], to=locate(end)[0];
        pieces.subList(from,to).clear(); length-=end-start;
        return removed;
    }
    @Override public char charAt(int index) {
        if(index<0||index>=length) throw new IndexOutOfBoundsException(index);
        int offset=0;
        for(Piece p:pieces){ if(index<offset+p.length) return chunks.get(p.chunk).charAt(p.start+index-offset); offset+=p.length; }
        throw new IndexOutOfBoundsException(index);
    }
    @Override public String getText() {
        StringBuilder b=new StringBuilder(length);
        for(Piece p:pieces) b.append(chunks.get(p.chunk),p.start,p.start+p.length);
        return b.toString();
    }
}
