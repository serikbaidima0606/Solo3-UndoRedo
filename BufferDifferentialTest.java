package kz.solo3;

import org.junit.jupiter.api.Test;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

class BufferDifferentialTest {
    @Test void threeSeedsMatchStringBuilderForBothStrategies() {
        long[] seeds={7L,2024L,99173L};
        for(long seed:seeds) for(int strategy=0;strategy<2;strategy++){
            TextBuffer actual=strategy==0?new GapBuffer():new PieceTable();
            StringBuilder expected=new StringBuilder();Random r=new Random(seed);
            for(int i=0;i<100_000;i++){
                if(expected.length()==0||r.nextBoolean()){
                    int at=r.nextInt(expected.length()+1);String s=String.valueOf((char)('a'+r.nextInt(26)));
                    actual.insert(at,s);expected.insert(at,s);
                }else{
                    int a=r.nextInt(expected.length());int b=a+1+r.nextInt(expected.length()-a);
                    assertEquals(expected.substring(a,b),actual.delete(a,b));expected.delete(a,b);
                }
                assertEquals(expected.toString(),actual.getText(),"seed="+seed+", op="+i+", strategy="+strategy);
            }
        }
    }
    @Test void gapBufferHandlesUnicodeAndBounds(){
        TextBuffer b=new GapBuffer("🙂abc");b.insert(2,"漢");assertEquals("🙂漢abc",b.getText());
        assertThrows(IndexOutOfBoundsException.class,()->b.insert(99,"x"));
    }
    @Test void pieceTableDeleteInsert() {
        TextBuffer b=new PieceTable("abcdef");assertEquals("cd",b.delete(2,4));b.insert(2,"XY");assertEquals("abXYef",b.getText());
    }
}
