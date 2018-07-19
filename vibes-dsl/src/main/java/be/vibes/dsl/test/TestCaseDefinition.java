package be.vibes.dsl.test;

/*-
 * #%L
 * VIBeS: dsl
 * %%
 * Copyright (C) 2014 - 2018 University of Namur
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
