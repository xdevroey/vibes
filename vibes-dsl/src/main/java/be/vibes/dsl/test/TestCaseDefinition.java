package be.vibes.dsl.test;

import java.util.List;

import com.google.common.collect.Lists;


/**
 * This class represents a partial definition of a test case used in
 TestSetDefinition class.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 * @see TestSetDefinition
 */
public class TestCaseDefinition {

    private final String id;
    private final TestSetDefinition context;
    private final List<String> actions;

    TestCaseDefinition(String id, TestSetDefinition context) {
        this.id = id;
        this.context = context;
        this.actions = Lists.newArrayList();
    }

    /**
     * Creates a new actions with the given name and add it to the end of this
     * partial test case.
     *
     * @param actionNames The name of the action(s) to add.
     * @return This.
     */
    public TestCaseDefinition action(String... actionNames) {
        for (String name : actionNames) {
            actions.add(name);
        }
        return this;
    }

    /**
     * Add the given action to the end of this partial test case.
     *
     * @param act The action to add.
     * @return This.
     */
    public TestCaseDefinition action(String act) {
        actions.add(act);
        return this;
    }

    /**
     * Marks this partial test case as finished.
     */
    public void end() {
        this.context.notifyTestCaseDefinitionComplete(this);
    }

    String getId() {
        return id;
    }

    List<String> actions() {
        return this.actions;
    }

}
