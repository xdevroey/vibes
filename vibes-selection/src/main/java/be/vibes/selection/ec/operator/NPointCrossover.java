package be.vibes.selection.ec.operator;

import be.vibes.selection.ec.TestSuiteSolution;

public class NPointCrossover extends org.uma.jmetal.operator.crossover.impl.NPointCrossover<TestSuiteSolution> {

    public NPointCrossover(double probability, int points) {
        super(probability, points);
    }

}
