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

import be.unamur.fts.fexpression.FExpression;
import be.unamur.fts.fexpression.exception.ParserException;
import be.unamur.fts.fexpression.ParserUtil;
import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.dsl.exception.TestCaseDefinitionException;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystemElementFactory;

public class FtsTestCaseDefinition extends TestCaseDefinition {

    private List<FExpression> fExpressions;

    FtsTestCaseDefinition(String id, FtsTestSetDefinition context) {
        super(id, context);
        this.factory = new FeaturedTransitionSystemElementFactory();
        fExpressions = Lists.newArrayList();
    }

    @Override
    public FtsTestCaseDefinition action(String... actionNames) {
        for(String name: actionNames){
            this.action(name, FExpression.trueValue());
        }
        return this;
    }

    @Override
    public FtsTestCaseDefinition action(Action act) {
        return this.action(act, FExpression.trueValue());
    }

    public FtsTestCaseDefinition action(String actionName, String fexpression) {
        try {
            return this.action(actionName, ParserUtil.getInstance().parse(fexpression));
        } catch (ParserException e) {
            throw new TestCaseDefinitionException("Exception while parsing!" + fexpression,
                    e);
        }
    }

    public FtsTestCaseDefinition action(String actionName,
            FExpression fexpression) {
        return this.action(factory.buildAction(actionName), fexpression);
    }
    
    public FtsTestCaseDefinition action(Action act, FExpression fexpression) {
        super.action(act);
        fExpressions.add(fexpression);
        return this;
    }
    
    List<FExpression> fExpressions() {
        return fExpressions;
    }


}
