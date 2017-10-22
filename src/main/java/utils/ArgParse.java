/*
 * Mateusz Ledzianowski inf117226
 * Patryk Gliszczynski inf117288
 */
package utils;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;


public class ArgParse {
        
        public Long time;
        public String dataPath;
        public String solver;
        
        public ArgParse(String[] args) {
            ArgumentParser parser = ArgumentParsers.newFor("STSP").build()
                .defaultHelp(true)
                .description("Program to calculate STSP.");
            parser.addArgument("-t", "--time")
                    .type(long.class)
                    .help("Time limit")
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
            this.dataPath = ns.getString("data");
            this.solver = ns.getString("solver");
        }
    }
