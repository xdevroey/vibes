package be.unamur.transitionsystem.test.selection.main;

/*
 * #%L
 * vibes-selection
 * %%
 * Copyright (C) 2014 PReCISE, University of Namur
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.PrintStream;
//
//import org.apache.commons.cli.BasicParser;
//import org.apache.commons.cli.CommandLine;
//import org.apache.commons.cli.CommandLineParser;
//import org.apache.commons.cli.HelpFormatter;
//import org.apache.commons.cli.OptionBuilder;
//import org.apache.commons.cli.Options;
//import org.apache.commons.cli.ParseException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import be.unamur.fts.solver.Sat4JSolverFacade;
//import be.unamur.transitionsystem.TransitionSystem;
//import be.unamur.transitionsystem.test.TestCaseFactory;
//import be.unamur.transitionsystem.test.FtsMutableTestCase;
//import be.unamur.transitionsystem.test.LtsMutableTestCase;
//import be.unamur.transitionsystem.test.selection.AllStatesTestCaseGenerator;
//import be.unamur.transitionsystem.test.selection.AlwaysTrueValidator;
//import be.unamur.transitionsystem.test.selection.RandomTestCaseGenerator;
//import be.unamur.transitionsystem.test.selection.TestCaseValidator;
//import be.unamur.transitionsystem.test.selection.WarshallScoreComputor;
//import be.unamur.transitionsystem.test.selection.XmlPrintWrapUp;
//import be.unamur.transitionsystem.test.selection.fts.FtsTestCaseValidator;
//import be.unamur.transitionsystem.transformation.xml.TestCaseXmlGenerator;
//import be.unamur.transitionsystem.transformation.xml.FtsTestCaseXmlGenerator;
//import be.unamur.transitionsystem.transformation.xml.FtsHandler;
//import be.unamur.transitionsystem.transformation.xml.LtsTestCaseXmlGenerator;
//import be.unamur.transitionsystem.transformation.xml.LtsHandler;
//import be.unamur.transitionsystem.transformation.xml.XmlReader;
public class Main {
    /*
	
     private static final Logger logger = LoggerFactory.getLogger(Main.class);

     private static final String HELP = "help";
     private static final String RANDOM = "random";
     private static final String PATHS = "paths";
     private static final String STATES = "states";
     private static final String SAT = "sat";
     private static final String TS_INPUT = "ts";
     private static final String FTS_INPUT = "fts";
     private static final String OUTPUT = "out";
	
     private static final int WARSHALL_DEPTH = 5;
	
     private static void printHelpMessage(Options options) {
     HelpFormatter formatter = new HelpFormatter();
     formatter.setWidth(128);
     formatter.printHelp("dtmc-usage-model <options>", options);
     }
	
     @SuppressWarnings("static-access")
     public static void main(String[] args) throws Exception {
     CommandLineParser parser = new BasicParser();
     Options options = new Options();

     options.addOption(OptionBuilder.hasArg().withArgName("n")
     .withValueSeparator(' ')
     .withDescription("Generates 'n' random test cases.")
     .create(RANDOM));

     options.addOption(OptionBuilder
     .hasArg()
     .withArgName("n")
     .withValueSeparator(' ')
     .withDescription(
     "Generates all states coverage test cases from input with 'n' states prevision for the heuristic.")
     .create(STATES));

     options.addOption(OptionBuilder.withDescription("Generates all path test cases.")
     .create(PATHS));

     options.addOption(OptionBuilder
     .hasArgs(2)
     .withArgName("fd.dimacs> <fd.map")
     .withValueSeparator(' ')
     .withDescription(
     "Uses MiniSat with the given dimacs and feature/clause mapping files")
     .create(SAT));
		
     options.addOption(OptionBuilder
     .hasArg()
     .withArgName("fts")
     .withDescription("Input FTS model.")
     .create(FTS_INPUT));
		
     options.addOption(OptionBuilder
     .hasArg()
     .withArgName("ts")
     .withDescription("Input TS model.")
     .create(TS_INPUT));
		
     options.addOption(OptionBuilder
     .hasArg()
     .withArgName("test-cases.xml")
     .withDescription("Output file (optional")
     .create(OUTPUT));

     options.addOption(HELP, false, "Prints this message");
		
     CommandLine line = null;
     try {
     line = parser.parse(options, args);
     } catch (ParseException e) {
     System.err.println("Problem in the command line arguments: " + e.getMessage()
     + " !");
     System.exit(-1);
     }
		
     if(line.hasOption(HELP)){
     printHelpMessage(options);
     System.exit(0);
     }
		
     TestCaseFactory tcFactory = null;
     TestCaseValidator validator = null;
     TransitionSystem ts = null;
     String inputFile = null;
     TestCaseXmlGenerator printer = null;
     LtsHandler handler = null;
     PrintStream out = System.out;
		
     // Set output if it is defined
     if(line.hasOption(OUTPUT)){
     File outputFile = new File(line.getOptionValue(OUTPUT));
     if(outputFile.exists()){
     outputFile.delete();
     }
     outputFile.createNewFile();
     out = new PrintStream(new FileOutputStream(outputFile));
     }
		
     // Loading input model
     if(line.hasOption(TS_INPUT)){
     inputFile = line.getOptionValue(TS_INPUT);
     tcFactory = LtsMutableTestCase.FACTORY;
     validator = AlwaysTrueValidator.INSTANCE;
     handler = new LtsHandler();
     printer = new LtsTestCaseXmlGenerator(out);
     } else  if(line.hasOption(FTS_INPUT)){
     inputFile = line.getOptionValue(FTS_INPUT);
     tcFactory = FtsMutableTestCase.FACTORY;
     handler = new FtsHandler();
     if(!line.hasOption(SAT)){
     System.err.println("No "+SAT+" option provided!");
     System.exit(-1);
     }
     String[] satValues = line.getOptionValues(SAT);
     validator = new FtsTestCaseValidator(new Sat4JSolverFacade(new File(satValues[0]), new File(satValues[1])));
     printer = new FtsTestCaseXmlGenerator(out);
     } else {
     System.err.println("No input model specifyed!");
     System.exit(-1);
     }
		
     new XmlReader(handler, new File(inputFile)).readDocument();
     ts = handler.geTransitionSystem();
		
     // Process generation
		
     if(line.hasOption(PATHS)){
     // TODO implement !
     logger.error("Not yet implemented");
     } else if(line.hasOption(RANDOM)){
     printer.startDocumen();
     RandomTestCaseGenerator generator = new RandomTestCaseGenerator(tcFactory, validator, new XmlPrintWrapUp(printer));
     logger.info("Starting generation");
     generator.generateAbstractTestSet(ts, Integer.parseInt(line.getOptionValue(RANDOM)));
     logger.info("End of generation");
     } else if(line.hasOption(STATES)){
     printer.startDocumen();
     logger.info("Starting Warshall computation");
     WarshallScoreComputor scoreComputor = new WarshallScoreComputor();
     scoreComputor.initilise(ts);
     scoreComputor.warshall(WARSHALL_DEPTH);
     AllStatesTestCaseGenerator generator = new AllStatesTestCaseGenerator(tcFactory, validator, new XmlPrintWrapUp(printer), scoreComputor);
     logger.info("Starting generation");
     generator.generateAbstractTestSet(ts);
     logger.info("End of generation");
     } else {
     System.err.println("No generation model specifyed!");
     System.exit(-1);
     }

     printer.endDocument();
     }

     */
}
