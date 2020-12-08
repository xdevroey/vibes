package be.vibes.selection.ec.operator;

import be.vibes.selection.ec.TestSuiteSolution;
import org.uma.jmetal.operator.crossover.impl.NPointCrossover;

public class SinglePointCrossover extends NPointCrossover<TestSuiteSolution> {

    public SinglePointCrossover(double probability) {
        super(probability, 1);
    }

}
