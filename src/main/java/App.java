import ast.statements.Stmt;
import java_cup.runtime.ComplexSymbolFactory;
import cg.CodeGen;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.impl.Arguments;

import java.io.StringReader;
import java.lang.ProcessBuilder;
import java.lang.Process;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App {
    public static void main(String[] args) throws Exception {
        ArgumentParser argParser = ArgumentParsers.newFor("Compiler").build()
                .defaultHelp(true)
                .description("Compiler for lang");
        argParser.addArgument("-o", "--output").type(String.class).setDefault("a.out").help("Output file");
        argParser.addArgument("-c", "--compiler").type(String.class).setDefault("cc").help("C compiler to link object code");
        argParser.addArgument("-d", "--debug").action(Arguments.storeTrue()).help("Enable debug printing");
        argParser.addArgument("source").type(String.class).required(true).help("Source file");
        Namespace arguments = argParser.parseArgsOrFail(args);
        String source_file = arguments.getString("source");
        String output_file = arguments.getString("output");
        String compiler = arguments.getString("compiler");
        Boolean debug_enabled = arguments.getBoolean("debug");
        Reader in = new StringReader(new String(Files.readAllBytes(Paths.get(source_file))) + System.lineSeparator());
        ComplexSymbolFactory symbolFactory = new ComplexSymbolFactory();
        Lexer lexer = new Lexer(in, symbolFactory);
        Parser parser = new Parser(lexer, symbolFactory);
        Stmt astRoot = (Stmt) parser.parse().value;
        CodeGen codegen = CodeGen.getInstance();
        codegen.initVariables(astRoot.getIdentifiers());
        astRoot.codeGen(codegen);
        if(debug_enabled) {
            System.err.println("AST dump:");
            System.err.println(astRoot.toString());
        }
        codegen.emitObj(output_file + ".o", debug_enabled);
        Process cc = (new ProcessBuilder(compiler, output_file + ".o", "-o", output_file)).start();
        cc.waitFor();
        if(cc.exitValue() != 0) {
            System.err.println("CC returned non-zero value!");
            System.exit(1);
        }
        Files.delete(Paths.get(output_file + ".o"));
    }
}
