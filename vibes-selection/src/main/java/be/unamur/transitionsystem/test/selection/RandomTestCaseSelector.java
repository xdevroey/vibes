package be.unamur.transitionsystem.test.selection;

import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.exception.VisitException;
import be.unamur.transitionsystem.test.LtsMutableTestCase;
import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.TestCaseFactory;
import be.unamur.transitionsystem.test.exception.TestCaseException;
import be.unamur.transitionsystem.test.selection.exception.TestCaseSelectionException;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;

/**
 * 
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class RandomTestCaseSelector<T extends TransitionSystem> extends AbstractTestCaseSelector<T> {

    public static final int DEFAULT_MAX_NUMBER_TRY = 1000;
    public static final int DEFAULT_MAX_LENGTH = 2000;

    private int maxNbrTry;
    private int maxLength;
    private TestCaseValidator validator;
    private TestCaseFactory factory;
    private Long randomSeed;

    public RandomTestCaseSelector(T transitionSystem, int maxNbrTry, int maxLength, TestCaseValidator validator, TestCaseFactory factory) {
        super(transitionSystem);
        this.maxNbrTry = maxNbrTry;
        this.maxLength = maxLength;
        this.validator = validator;
        this.factory = factory;
    }

    public RandomTestCaseSelector(T transitionSystem, int maxNbrTry, int maxLength, TestCaseValidator validator) {
        this(transitionSystem, maxNbrTry, maxLength, validator, LtsMutableTestCase.FACTORY);
    }

    public RandomTestCaseSelector(T transitionSystem, int maxNbrTry, int maxLength) {
        this(transitionSystem, DEFAULT_MAX_NUMBER_TRY, DEFAULT_MAX_LENGTH, AlwaysTrueValidator.TRUE_VALIDATOR);
    }

    public RandomTestCaseSelector(T transitionSystem) {
        this(transitionSystem, DEFAULT_MAX_NUMBER_TRY, DEFAULT_MAX_LENGTH);
    }

    public int getMaxNbrTry() {
        return maxNbrTry;
    }

    public int getMaxLength() {
        return maxLength;
    }

    protected TestCaseValidator getTestCaseValidator() {
        return this.validator;
    }

    protected TestCaseFactory getFactory() {
        return factory;
    }

    public Long getRandomSeed() {
        return randomSeed;
    }

    public void setMaxNbrTry(int maxNbrTry) {
        this.maxNbrTry = maxNbrTry;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public void setValidator(TestCaseValidator validator) {
        this.validator = validator;
    }

    public void setRandomSeed(Long randomSeed) {
        this.randomSeed = randomSeed;
    }

    @Override
    public TestCase select() throws TestCaseSelectionException {
        Random random;
        if (getRandomSeed() == null) {
            random = new Random();
        } else {
            random = new Random(getRandomSeed());
        }
        RandomVisitor visitor = new RandomVisitor(random, getFactory(), getTestCaseValidator());
        visitor.setMaxLength(getMaxLength());
        int maxTry = getMaxNbrTry();
        return select(maxTry, visitor);
    }

    @Override
    public List<TestCase> select(int nbr) throws TestCaseSelectionException {
        Random random;
        if (getRandomSeed() == null) {
            random = new Random();
        } else {
            random = new Random(getRandomSeed());
        }
        RandomVisitor visitor = new RandomVisitor(random, getFactory(), getTestCaseValidator());
        visitor.setMaxLength(getMaxLength());
        int maxTry = getMaxNbrTry();
        List<TestCase> list = Lists.newArrayList();
        for(int i = 0 ; i < nbr ; i++){
            list.add(select(maxTry, visitor));
        }
        return list;
    }

    @Override
    public void select(int nbr, TestCaseWrapUp wrapUp) throws TestCaseSelectionException {
        Random random;
        if (getRandomSeed() == null) {
            random = new Random();
        } else {
            random = new Random(getRandomSeed());
        }
        RandomVisitor visitor = new RandomVisitor(random, getFactory(), getTestCaseValidator());
        visitor.setMaxLength(getMaxLength());
        int maxTry = getMaxNbrTry();
        for(int i = 0 ; i < nbr ; i++){
            try {
                wrapUp.wrapUp(select(maxTry, visitor));
            } catch (TestCaseException ex) {
                throw new TestCaseSelectionException("Error while wrapping up test case!", ex);
            }
        }
    }

    private TestCase select(int maxTry, RandomVisitor visitor) throws TestCaseSelectionException {
        int invalid = 0;
        while (invalid < maxTry) {
            try {
                visitor.reset();
                getTransitionSystem().getInitialState().accept(visitor);
                if (visitor.getTestCase() != null) {
                    return visitor.getTestCase();
                } else {
                    invalid++;
                }
            } catch (VisitException e) {
                throw new TestCaseSelectionException("Error while generating test case!", e);
            }
        }
        throw new TestCaseSelectionException("Was unnable to select a random test case within "+maxTry+ " tries!");
    }

}
