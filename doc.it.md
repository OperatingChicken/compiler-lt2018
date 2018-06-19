# Progetto d'esame di Linguaggi e Traduttori
## Appello di Giugno 2018
**Ostuni Dario `<dario.ostuni@studenti.unimi.it>` (mat. 870321)**

**Nitti Gianluca `<gianluca.nitti@studenti.unimi.it>` (mat. 871108)**

### Struttura del progetto
Il progetto è gestito con Gradle. Per compilarlo, generando un *fat jar* (cioè un archivio *jar* eseguibile contenente tutte le dipendenze), si esegue il comando:

(su Linux / macOS)
```
./gradlew jar
```
(su Windows)
```
gradlew.bat jar
```

Il *jar* viene generato in `build/libs/compiler-lt2018.jar`. Quindi, ad esempio, per compilare un programma contenuto in un file chiamato `programma.lang`, il comando da eseguire è
```
java -jar build/libs/compiler-lt2018.jar programma.lang
```
che, se non vi sono errori, genera un eseguibile `a.out` nella cartella corrente.

Eseguendo invece
```
java -jar build/libs/compiler-lt2018.jar -h
```
si otterrà un'elenco delle opzioni da riga di comando disponibili, che consentono ad esempio di modificare il linker da utilizzare (l'opzione di default è `cc`) o di salvare in forma testuale l'assembly LLVM-IR.

### Elenco di classi ed altri files sorgenti
* `src/main/jflex/Lexer.jflex`

	File di specifica JFlex dell'analizzatore lessicale; scansiona il file sorgente in input, producendo token compatibili con il parser generato da CUP (i token sono costituiti da oggeti della classe Symbol, dalle librerie di runtime di CUP). Sono presenti 3 stati:
	* Nello stato iniziale (`YYINITIAL`), l'analizzatore lessicale scarta eventuali commenti o ritorni a capo
	* Nello stato `BODY`, vengono prodoti i token che rappresentano i simboli terminali della grammatica del linguaggio, ad eccezione dei letterali stringa
	* Lo stato `STRING_LITERAL` è utilizzato per riconoscere i letterali stringa; il carattere `"` è interpretato come fine della stringa, mentre ogni altro carattere viene aggiunto alla stringa che si sta costruendo. Utilizzare uno stato dedicato consente di riconoscere correttamente più stringhe nella stessa istruzione/riga; l'implementazione iniziale consisteva in una regola `\".*\"` all'interno dello stato BODY; tuttavia in questo modo, ad esempio, in un'istruzione `input "A:" + input "B:"`, `"A:" + input "B:"` veniva erroneamente riconosciuto come un'unico letterale stringa, poichè JFlex effettua il match della stringa più lunga.
Per semplificare il lavoro del parser, l'analizzatore lessicale restituisce un'unico token di tipo NEWLINE (separatore delle istruzioni) anche in presenza di più ritorni a capo e/o linee di commento consecutivi. Inoltre, i simboli terminali corrispondenti a letterali di tipo numerico (decimali o esadecimali) non vengono forniti al parser sotto forma di stringa ma direttamente come numeri (oggetti della classe `Long`).
* `src/main/cup/Parser.cup`

	File di specifica CUP contenente la grammatica del linguaggio, utilizzato per generare il parser.
Un breve frammento di codice Java inserito con la direttiva `parser code` è utilizzato per migliorare la segnalazione degli errori, effettuando l'override di alcuni metodi della classe padre e sollevando delle eccezioni (definite nel package `err`) all'interno delle implementazioni.
Seguono la dichiarazione dei simboli terminali e nonterminali, delle precedenze ed infine le regole della grammatica. Le azioni semantiche associate ad ogni regola costruiscono degli oggetti del package `ast`, che costituiscono i nodi dell'albero di parsing.
* `src/main/java/App.java`

	Classe principale dell'applicazione (contenente il metodo `void main(String[] args)`). Viene utilizzata la libreria esterna [Argparse4j](https://argparse4j.github.io/) per semplificare la gestione degli argomenti da riga di comando. Una volta letti gli argomenti, il programma legge il file sorgente e lo fornisce in input all'analizzatore lessicale, che a sua volta viene utilizzato come input del parser. Prima di fare ciò, tuttavia, alla fine dell'input viene aggiunto un ritorno a capo; ciò è necessario poichè nella grammatica il ritorno a capo è utilizzato come separatore delle istruzioni, quindi se non ve ne fosse almeno uno alla fine del sorgente, il parser non sarebbe in grado di riconoscere l'input. Se l'operazione di parsing ha successo, avremo ottenuto come radice dell'Abstract Syntax Tree un oggetto di tipo `ast.statements.Stmt` che rappresenta la lista di istruzioni del programma. A questo punto viene estratto l'elenco di variabili utilizzate e passato alla classe `CodeGen`, che lo utilizzerà per costruire la symbol table. A questo punto, sempre tramite i metodi della classe `CodeGen`, viene generato il codice eseguibile che viene salvato in un file oggetto; infine, viene invocato il linker di sistema per produrre il file eseguibile.
* `src/main/java/ast/statements/Stmt.java`

	Classe astratta che rappresenta un'istruzione all'interno dell'albero sintattico. Dichiara i metodi astratti che devono essere implementati da ogni tipo di istruzione, per costruire l'elenco di variabili e per effettuare la generazione del codice. Le quattro classi seguenti (`OutputStmt`, `StmtList`, `AssignStmt` e `LoopStmt`) estendono `Stmt` fornendo ciascuna le implementazioni adeguate di tali metodi.
* `src/main/java/ast/statements/OutputStmt.java`

	Rappresenta un'istruzione `output` o `newLine` del linguaggio. L'implementazione del metodo di genrazione del codice genera una chiamata alla funzione `printf` di `libc`.
* `src/main/java/ast/statements/StmtList.java`

	Sequenza di istruzioni; viene utilizzata per rappresentare un intero programma o il corpo di un ciclo.
* `src/main/java/ast/statements/AssignStmt.java`

	Rappresenta un'istruzione di assegnamento; un oggetto della classe `AssignStmt` viene costruito dal parser solamente quando un assegnamento è utilizzato come istruzione ed il suo valore non viene utilizzato. Ad esempio, nell'istruzione `a = b = c` il primo `=` dà origine ad un'istanza di `AssignStmt`, mentre il secondo ad un'istanza di `AssignExpr`.
* `src/main/java/ast/statements/LoopStmt.java`

	Rappresenta un ciclo. Ha come nodi figli un'`Expr` (condizione) ed uno `Stmt` (corpo).
* `src/main/java/ast/expressions/Expr.java`

	Classe astratta simile a `Stmt`, ma che rappresenta un'espressione invece che un'istruzione. La principale differenza è che qui il metodo `codeGen` restituisce un riferimento alla posizione (ad esempio un registro del processore o un'indirizzo di memoria) in cui, in fase di esecuzione, sarà presente il valore dell'espressione.
* `src/main/java/ast/expressions/AssignExpr.java`

	Espressione di assegnamento; vedere l'esempio per `AssignStmt`.
* `src/main/java/ast/expressions/BinaryOpExpr.java`

	Classe astratta che estende `Expr` e rappresenta un'operazione binaria tra due espressioni (i suoi nodi figli). Definisce un metodo astratto `doBinaryCG`, che, dati i valori dei due operandi (valutati ricorsivamente), deve resitituire il riferimento al risultato dell'operLa symbol table è una mappa associativa dove le chiavi sono i nomi delle variabili e i valori sono puntatori a dove si trovano le variabili in memoriaazione.
* `src/main/java/ast/expressions/AddExpr.java`
* `src/main/java/ast/expressions/SubExpr.java`
* `src/main/java/ast/expressions/MulExpr.java`
* `src/main/java/ast/expressions/DivExpr.java`
* `src/main/java/ast/expressions/ModExpr.java`

	Sottoclassi di `BinaryOpExpr` ch implementano le rispettive operazioni.
* `src/main/java/ast/expressions/NegExpr.java`

	Rappresenta la negazione di un'espressione; il metodo di generazione codice calcola il valore di tale espressione e lo nega.
* `src/main/java/ast/expressions/InputExpr.java`

	Espressione di input; il metodo `codeGen` genera una chiamata alla funzione di sistema `printf` che stampa il messagio di prompt, seguita da una chiamata a `scanf` che legge un numero e ne restituisce il riferimento come valore dell'espressione.
* `src/main/java/ast/expressions/NumLiteralExpr.java`

	Letterale numerico presente nel programma; `codeGen` inserisce il valore come immediato e ne restituisce il riferimento.
* `src/main/java/ast/expressions/IdentifierExpr.java`

	Rappresenta l'utilizzo di un identificatore; viene cercato nella symbol table l'indirizzo di memoria associato alla variabile, e viene generato il codice che carica il valore leggendo da tale indirizzo.
* `src/main/java/ast/expressions/IfExpr.java`

	Rappresenta un utilizzo dell'operatore condizionale (ternario); ha come nodi figli tre espressioni: la condizione e i due possibili risultati. Viene generato il codice per valutare ognuna di esse, e la logica per scegliere il ramo corretto.
* `src/main/java/cg/CodeGen.java`

	Classe singleton per generare codice macchina tramite [LLVM](https://llvm.org/). La classe (la prima volta che viene instanziato l'oggetto singleton) inizializza LLVM e crea le necessarie strutture. Ne viene poi chiamata una funzione per inizializzarne la symbol table. Offre funzioni per la traduzione dell'AST nella rappresentazione intermedia di LLVM. Infine offre un metodo di finalizzazione per emettere il codice oggetto generato.
* `src/main/java/err/AbstractError.java`

	Sottoclasse di `java.lang.Exception` che rappresenta un generico errore di compilazione, memorizzandone la posizione (indici di riga e colonna).
* `src/main/java/err/ParseError.java`

	Sottoclasse di `AbstractError` utilizzata per rappresentare errori riportati dall'analizzatore sintattico durante il parsing.
* `src/main/java/err/LexError.java`

	Sottoclasse di `AbstractError` utilizzata per rappresentare errori riportati dall'analizzatore lessicale durante l'individuazione dei token.

### Struttura della symbol table
La symbol table è una mappa associativa dove le chiavi sono i nomi delle variabili e i valori sono puntatori a dove si trovano le variabili in memoria.
