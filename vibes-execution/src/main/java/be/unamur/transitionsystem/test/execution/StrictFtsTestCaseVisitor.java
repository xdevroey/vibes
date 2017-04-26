package be.unamur.transitionsystem.test.execution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.transitionsystem.ExecutionNode;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.fts.FeaturedTransition;
import be.unamur.transitionsystem.test.FtsMutableTestCase;
import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.exception.TestCaseException;
import be.unamur.transitionsystem.test.selection.fts.FtsTestCaseValidator;

class StrictFtsTestCaseVisitor extends StrictTestCaseVisitor {

    private static final Logger logger = LoggerFactory.getLogger(StrictFtsTestCaseVisitor.class);

    private FtsMutableTestCase testCase = new FtsMutableTestCase();

    private FtsTestCaseValidator validator;

    public StrictFtsTestCaseVisitor(TestCase testCase, ExecutionNode<FeaturedTransition> executionNode, FtsTestCaseValidator validator) {
        super(testCase, executionNode);
        this.validator = validator;
    }

    @Override
    protected void addTransition(Transition trans) {
        super.addTransition(trans);
        try {
            testCase.enqueue(trans);
        } catch (TestCaseException e) {
            logger.error("Error while adding transition!", e);
            throw new IllegalStateException("Error while adding transition, this should not happen but ...", e);
        }
    }

    @Override
    protected void removeTransition() {
        super.removeTransition();
        try {
            testCase.dequeue();
        } catch (TestCaseException e) {
            logger.error("Error while removing transition!", e);
            throw new IllegalStateException("Error while removing transition, this should not happen but ...", e);
        }
    }

    @Override
    protected boolean isCurrentBranchValid() {
        return super.isCurrentBranchValid() && this.validator.isValid(testCase);
    }

}
