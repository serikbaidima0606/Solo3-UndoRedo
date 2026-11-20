package kz.solo3;

import java.nio.file.Files;
import java.nio.file.Path;

public final class Benchmark {
    public static void main(String[] args) throws Exception {
        int size=1_000_000, repeats=5;
        String initial="a".repeat(size), payload="XYZ";
        long gap=measure(new GapBuffer(initial),payload,repeats);
        long piece=measure(new PieceTable(initial),payload,repeats);
        String report="# Benchmark report\n\nDocument: 1,000,000 characters; insert: 3 characters at middle; repeats: "+repeats+".\n\n"
          +"| Buffer | Average ns/op |\n|---|---:|\n| GapBuffer | "+gap+" |\n| PieceTable | "+piece+" |\n\n"
          +"Machine/JVM dependent. Run several times after JVM warm-up; do not treat one run as a universal performance guarantee.\n";
        Files.writeString(Path.of("benchmark-report.md"),report);
        System.out.println(report);
    }
    private static long measure(TextBuffer prototype,String value,int repeats){
        long total=0;
        for(int i=0;i<repeats;i++){
            TextBuffer b=prototype instanceof GapBuffer?new GapBuffer(prototype.getText()):new PieceTable(prototype.getText());
            long t=System.nanoTime();b.insert(b.length()/2,value);total+=System.nanoTime()-t;
        }
        return total/repeats;
    }
}
