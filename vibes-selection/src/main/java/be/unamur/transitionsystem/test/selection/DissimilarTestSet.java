package be.unamur.transitionsystem.test.selection;

import java.util.Collection;
import java.util.Iterator;

import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.TestSet;
import be.unamur.transitionsystem.test.selection.exception.DissimilarityComputationException;

public class DissimilarTestSet extends TestSet implements Comparable<DissimilarTestSet> {

    private double fitness;
    private PrioritizationTechnique priorotizationTech;

    public DissimilarTestSet(Iterator<? extends TestCase> testCases, PrioritizationTechnique technique) {
        super(testCases);
        fitness = -1;
        priorotizationTech = technique;
    }

    public DissimilarTestSet(Collection<? extends TestCase> testCases, PrioritizationTechnique technique) {
        super(testCases);
        fitness = -1;
        priorotizationTech = technique;
    }

    public double getFitness() throws DissimilarityComputationException {
        if (fitness == -1) {
            computeFitnessAndOrder();
        }
        return fitness;
    }

    private void computeFitnessAndOrder() throws DissimilarityComputationException {
        this.testCases = priorotizationTech.prioritize(testCases);
        fitness = priorotizationTech.getFitness();
    }

    @Override
    public void add(TestCase tc) {
        super.add(tc);
        fitness = -1;
    }

    @Override
    public boolean remove(TestCase tc) {
        fitness = -1;
        return super.remove(tc);
    }

    @Override
    public TestCase get(int index) {
        if (fitness == -1) {
            try {
                computeFitnessAndOrder();
            } catch (DissimilarityComputationException e) {
                throw new RuntimeException("Error while computing fitness!", e);
            }
        }
        return super.get(index);
    }

    @Override
    public int compareTo(DissimilarTestSet ts) {
        try {
            double fitness1 = getFitness();
            double fitness2 = ts.getFitness();
            return Double.compare(fitness1, fitness2);
        } catch (DissimilarityComputationException e) {
            throw new RuntimeException("Error while computing fitness!", e);
        }

    }

}
