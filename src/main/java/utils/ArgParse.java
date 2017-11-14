package utils;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * 
 * @author Patryk Gliszczynski
 * @author Mateusz Ledzianowski
 */

public final class ArgParse {
        
        public Long time;
        public Long minIterations;
        public Long maxIterations;
        public String dataPath;
        public String solver;
        public String output;
        
        public ArgParse(String[] args) {
            ArgumentParser parser = ArgumentParsers.newFor("STSP").build()
                .defaultHelp(true)
                .description("Program to calculate STSP.");
            parser.addArgument("-o", "--output")
                    .type(String.class)
                    .help("Output file name")
                    .required(true);
            parser.addArgument("-t", "--time")
                    .type(long.class)
                    .help("Time limit")
                    .required(true);
            parser.addArgument("-i", "--miniterations")
                    .type(long.class)
                    .help("Min iterations number")
                    .required(true);
            parser.addArgument("-I", "--maxiterations")
                    .type(long.class)
                    .help("Max iterations number")
                    .required(true);
            parser.addArgument("-d", "--data")
                    .type(String.class)
                    .help("Path to data")
                    .required(true);
            parser.addArgument("-s", "--solver")
                    .type(String.class)
                    .help("Name of the solver")
                    .required(true);
            
            Namespace ns = null;
            try {
                ns = parser.parseArgs(args);
            } catch (ArgumentParserException a) {
                parser.handleError(a);
                System.exit(1);
            }
            
            this.time = ns.getLong("time");
            this.minIterations = ns.getLong("miniterations");
            this.maxIterations = ns.getLong("maxiterations");
            this.dataPath = ns.getString("data");
            this.solver = ns.getString("solver");
            this.output = ns.getString("output");
        }
    }
