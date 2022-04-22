JAVA=java
JAVAC=javac
RM = del /Q /F
CT = cat
JFLEX=$(JAVA) -jar jflex-1.8.2/lib/jflex-full-1.8.2.jar
CUPJAR=./java-cup-11b.jar
CUP=$(JAVA) -jar $(CUPJAR)
CP=.;$(CUPJAR)

default: run

.SUFFIXES: $(SUFFIXES) .class .java

.java.class:
				$(JAVAC) -cp $(CP) $*.java

FILE=		Scanner.java	parser.java    sym.java \
			scannerMain.java	parserMain.java \
			Program.java ExampleTypeCheckerTest.java ExampleInterpretTest.java

dump: parserD.java $(FILE:java=class)

run: parser-input.txt scanner-input.txt typeCheckingTests

parser-input.txt: all
		$(JAVA) -cp $(CP) parserMain parser-input.txt > parser-output.txt

scanner-input.txt: all
		$(JAVA) -cp $(CP) scannerMain parser-input.txt > scanner-output.txt

all: Scanner.java parser.java $(FILE:java=class)

typeCheckingTests:
		$(JAVA) -cp $(CP) ExampleTypeCheckerTest typecheck-input.txt > typecheck-output.txt

clean:
		$(RM) *.class *~ *.bak Scanner.java parser.java sym.java

Scanner.java:
		$(JFLEX) grammar.jflex

parser.java: scannerTokens.cup
		$(CUP) -expect 4 -interface -progress < scannerTokens.cup

parserD.java: scannerTokens.cup
		$(CUP) -expect 4 -interface -dump < scannerTokens.cup
