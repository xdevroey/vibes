package be.unamur.transitionsystem.dsl.test;

/*
 * #%L
 * vibes-dsl
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
import java.util.List;

import com.google.common.collect.Lists;

import be.unamur.transitionsystem.TransitionSystemElementFactory;
import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.LabelledTransitionSystemElementFactory;

/**
 * This class represents a partial defintion of a test case used in
 * TestSuiteDefinition class.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 * @see TestSuiteDefinition
 */
public class TestCaseDefinition {

    private String id;
    private TestSuiteDefinition context;
    private List<Action> actions;
    @SuppressWarnings("rawtypes")
    protected TransitionSystemElementFactory factory;

    TestCaseDefinition(String id, TestSuiteDefinition context) {
        this.id = id;
        this.context = context;
        this.actions = Lists.newArrayList();
        this.factory = new LabelledTransitionSystemElementFactory();
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
            actions.add(factory.buildAction(name));
        }
        return this;
    }

    /**
     * Add the given action to the end of this partial test case.
     *
     * @param act The action to add.
     * @return This.
     */
    public TestCaseDefinition action(Action act) {
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

    List<Action> actions() {
        return this.actions;
    }

}
