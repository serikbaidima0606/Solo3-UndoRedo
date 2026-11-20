# Solo 3 — Undo/Redo мәтін редакторының өзегі

Java 17 + Maven жобасы. Жоба суреттегі S3 талаптарын орындауға арналған оқу жобасы.

## VS Code-та ашу
1. ZIP архивті шығарып алыңыз.
2. VS Code → **File → Open Folder** → `Solo3-UndoRedo` таңдаңыз.
3. Extension Pack for Java орнатылғанын тексеріңіз.
4. Терминалда:
   ```bash
   mvn clean test
   ```
5. Демоны іске қосу:
   ```bash
   mvn -q package
   java -cp target/classes kz.solo3.Main
   ```

## Іске асырылған мүмкіндіктер
- **R1 Strategy:** `TextBuffer` интерфейсі, `GapBuffer` және `PieceTable`.
- **R2 Command + Composite:** Insert/Delete командалары және макро-команда; бір Undo бүкіл макросты қайтарады.
- **R3 Undo tree:** жаңа өңдеу Undo-дан кейін жасалса, ескі redo тармағы сақталады; тармақты индексімен таңдауға болады.
- **R4 Memento:** Memento құжат мәтінін көшірмейді, Undo ағашындағы күй түйінін сақтайды.
- **R5 Observer:** тек өзгерген аралық (`start`, `oldLength`, `newText`) туралы хабарлайды.
- **R6 Differential tests:** 3 seed бойынша кездейсоқ операциялар `StringBuilder` эталонымен салыстырылады.
- **Benchmark:** 1 000 000 таңбалық құжаттың ортасына енгізу үшін `GapBuffer` пен `PieceTable` өлшенеді.

## Қолдану мысалы
`Main.java` ішінде Insert, Undo, Redo, Undo-дан кейін жаңа тармақ жасау және Memento restore мысалы бар.
Негізгі логика графикалық интерфейстен тәуелсіз — кейін Swing/JavaFX интерфейсін қосуға болады.

## Benchmark
```bash
java -cp target/classes kz.solo3.Benchmark
```
Нәтиже `benchmark-report.md` файлына жазылады. Өлшем компьютерге, JVM-ге және іске қосу жағдайына тәуелді. 50x жылдамдыққа кепілдік берілмейді: оны нақты құрылғыда өлшеп дәлелдеу керек.

## Coverage
`mvn clean test` кейін `target/site/jacoco/index.html` ашыңыз. Суреттегі Line coverage ≥85% талабы осы есеппен тексеріледі. Mutation score ≥65% үшін PIT Mutation Testing-ті бөлек іске қосып, нәтижесін есепке тіркеу қажет; бұл жобаға әдейі жалған пайыз енгізілген жоқ.
