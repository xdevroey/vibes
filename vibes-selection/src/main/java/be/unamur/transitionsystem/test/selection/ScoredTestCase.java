package be.unamur.transitionsystem.test.selection;

import be.unamur.transitionsystem.test.TestCase;

class ScoredTestCase implements Comparable<ScoredTestCase> {

    private double score;
    private TestCase testCase;

    public ScoredTestCase(double score, TestCase testCase) {
        this.score = score;
        this.testCase = testCase;
    }

    public double getScore() {
        return score;
    }

    public TestCase getTestCase() {
        return testCase;
    }

    @Override
    public int compareTo(ScoredTestCase o) {
        double diff = this.score - o.score;
        if (diff < 0) {
            return -1;
        } else if (diff > 0) {
            return 1;
        } else {
            return 0;
        }
    }

}
