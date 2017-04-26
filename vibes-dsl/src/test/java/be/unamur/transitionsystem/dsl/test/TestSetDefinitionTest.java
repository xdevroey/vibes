/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package be.unamur.transitionsystem.dsl.test;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.TestSet;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class TestSetDefinitionTest extends TestSuiteDefinition{

    private static final Logger logger = LoggerFactory.getLogger(TestSetDefinitionTest.class);
    
    private String[] ids = new String[]{"tc1", "tc2", "tc3"};
    private String[][] actions = new String[][]{
        new String[]{"act1", "act2", "act3"},
        new String[]{"act4", "act5", "act6"},
        new String[]{}
    };

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            logger.info(String.format("Starting test: %s()...",
                    description.getMethodName()));
        }
    ;

    };

    @Test
    public void testSomeMethod() {
        TestSet ts = getTestSet();
        assertThat(ts.size(), equalTo(actions.length));
        int i = 0;
        for(TestCase tc : ts){
            assertThat(tc.size(), equalTo(actions[i].length));
            int j = 0;
            for(Action act : tc){
                assertThat(act.getName(), equalTo(actions[i][j]));
                j++;
            }
            i++;
        }
    }

    @Override
    protected void define() {
        for(int i = 0 ; i < actions.length ; i++){
            TestCaseDefinition def = id(ids[i]);
            for(int j = 0 ; j < actions[i].length ; j ++){
                def = def.action(actions[i][j]);
            }
            def.end();
        }
    }
    
    

}