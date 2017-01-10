package smtplearning;


import java.io.IOException;

import java.io.Writer;
import java.util.Random;
import de.learnlib.algorithms.lstargeneric.mealy.ExtensibleLStarMealyBuilder;
import de.learnlib.api.EquivalenceOracle.MealyEquivalenceOracle;
import de.learnlib.api.LearningAlgorithm.MealyLearner;
import de.learnlib.api.SUL;
import de.learnlib.cache.sul.SULCaches;
import de.learnlib.eqtests.basic.mealy.RandomWalkEQOracle;
import de.learnlib.experiments.Experiment.MealyExperiment;
import de.learnlib.oracles.ResetCounterSUL;
import de.learnlib.oracles.SULOracle;
import de.learnlib.statistics.SimpleProfiler;
import de.learnlib.statistics.StatisticSUL;
import net.automatalib.automata.transout.MealyMachine;
import net.automatalib.commons.dotutil.DOT;
import net.automatalib.util.graphs.dot.GraphDOT;

public class LearnLib{
	@SuppressWarnings("unused")
	private static boolean verbose = true;
	@SuppressWarnings("unused")
	private static int portNumber = 0;

	public static void handleArgs(String[] paramArrayOfString) {
		for (int i = 0; i < paramArrayOfString.length; i++)
			if ("--verbose".equals(paramArrayOfString[i])) {
				verbose = true;
			} else if ("-v".equals(paramArrayOfString[i])) {
				verbose = true;
			} else if ("--port".equals(paramArrayOfString[i])) {
				if (i == paramArrayOfString.length - 1) {
					System.err.println("Missing argument for --port.");
					printUsage();
					System.exit(-1);
				}
				try {
					portNumber = new Integer(paramArrayOfString[(++i)]).intValue();
				} catch (NumberFormatException localNumberFormatException) {
					System.err.println("Error parsing argument for --port. Must be integer. " + paramArrayOfString[i]);
					System.exit(-1);
				}
			}
	}

	public static void printUsage() {
		System.out.println(" options:");
		System.out.println("    --port n         use tcp port n to listen on for incoming connections");
		System.out.println("    -v|--verbose     verbose mode");
	}

	/*
	 * @reference = https://github.com/LearnLib/learnlib/blob/develop/examples/src/main/java/de/learnlib/examples/example2/Example.java
	*/
	
	public static void main(String[] paramArrayOfString) throws IOException  {
		handleArgs(paramArrayOfString);
		System.out.println("Starting client...");
		
		//MealyMembershipOracle<String,String> smtpOracle = new SMTPOracle();

		//			smtpConnection = new SMTP("192.168.222.1", 25);
		SmtpSUL smtpDriver = new SmtpSUL("192.168.222.1", 25);
		
        // oracle for counting queries wraps sul
        StatisticSUL<SmtpInputs, String> statisticSul = 
                new ResetCounterSUL<SmtpInputs, String>("membership queries", smtpDriver);
        
        SUL<SmtpInputs, String> effectiveSul = statisticSul;
        // use caching in order to avoid duplicate queries
        effectiveSul = SULCaches.createCache(smtpDriver.getInputs(), effectiveSul);
        
        SULOracle<SmtpInputs, String> mqOracle = new SULOracle<SmtpInputs, String>(effectiveSul);
		
        // construct L* instance (almost classic Mealy version)
        // almost: we use words (Word<String>) in cells of the table 
        // instead of single outputs.
        MealyLearner<SmtpInputs, String> lstar
        	= new ExtensibleLStarMealyBuilder<SmtpInputs,String>()
        		.withAlphabet(smtpDriver.getInputs()) // input alphabet
        		.withOracle(mqOracle)			  // membership oracle
        		.create();
                

        // create random walks equivalence test
        MealyEquivalenceOracle<SmtpInputs, String> randomWalks =
                new RandomWalkEQOracle<SmtpInputs, String>(
                0.05, // reset SUL w/ this probability before a step 
                10000, // max steps (overall)
                false, // reset step count after counterexample 
                new Random(46346293), // make results reproducible 
                smtpDriver // system under learning
                );

        // construct a learning experiment from
        // the learning algorithm and the random walks test.
        // The experiment will execute the main loop of
        // active learning
        MealyExperiment<SmtpInputs, String> experiment =
                new MealyExperiment<SmtpInputs, String>(lstar, randomWalks, smtpDriver.getInputs());

        // turn on time profiling
        experiment.setProfile(true);

        // enable logging of models
        experiment.setLogModels(true);

        // run experiment
        experiment.run();

        // get learned model
        MealyMachine<?, SmtpInputs, ?, String> result = 
                experiment.getFinalHypothesis();

        // report results
        System.out.println("-------------------------------------------------------");

        // profiling
        System.out.println(SimpleProfiler.getResults());

        // learning statistics
        System.out.println(experiment.getRounds().getSummary());
        System.out.println(statisticSul.getStatisticalData().getSummary());

        // model statistics
        System.out.println("States: " + result.size());
        System.out.println("Sigma: " + smtpDriver.getInputs().size());

        // show model
        System.out.println();
        System.out.println("Model: ");
        
        GraphDOT.write(result, smtpDriver.getInputs(), System.out); // may throw IOException!
        Writer w = DOT.createDotWriter(true);
        GraphDOT.write(result, smtpDriver.getInputs(), w);
        w.close();

        System.out.println("-------------------------------------------------------");
		}



	}
