SRC = src/main/java
TEST_SRC = src/test/java
BIN = bin
MAIN = ace.Ace

SOURCES = $(shell find $(SRC) -name "*.java")
TEST_SOURCES = $(shell find $(TEST_SRC) -name "*.java")

.PHONY: all compile run test clean

all: run

compile:
	mkdir -p $(BIN)
	javac -d $(BIN) $(SOURCES)

run: compile
	java -cp $(BIN) $(MAIN)

test: compile
	javac -cp $(BIN) -d $(BIN) $(TEST_SOURCES)
	java -cp $(BIN) ace.AllTests

clean:
	rm -rf $(BIN)
