package be.vibes.solver;

/*-
 * #%L
 * VIBeS: featured expressions
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

import be.vibes.fexpression.FExpression;
import be.vibes.fexpression.FExpressionVisitorWithReturn;
import java.util.Map;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import be.vibes.fexpression.Feature;
import be.vibes.fexpression.exception.FExpressionException;
import static com.google.common.base.Preconditions.*;
import java.util.List;

public class FExpressionBDDBuilder implements FExpressionVisitorWithReturn<BDD> {

    private BDDFactory factory;
    private Map<String, BDD> featureMapping;

    public FExpressionBDDBuilder(BDDFactory factory, Map<String, BDD> featureMapping) {
        this.factory = factory;
        this.featureMapping = featureMapping;
    }

    @Override
    public BDD constant(boolean val) {
        if (val) {
            return this.factory.one();
        } else {
            return this.factory.zero();
        }
    }

    @Override
    public BDD feature(Feature feature) {
        BDD featBdd = this.featureMapping.get(feature.getName());
        checkNotNull(featBdd, "Feature %s not found in feature to BDD mapping!", feature);
        return featBdd;
    }

    @Override
    public BDD not(FExpression expr) {
        try {
            BDD operand = expr.accept(this);
            return operand.not();
        } catch (FExpressionException ex) {
            throw new IllegalStateException("No exception should happen while using this visitor!", ex);
        }
    }

    @Override
    public BDD and(List<FExpression> operands) {
        try {
            BDD conj = factory.one();
            for (FExpression e : operands) {
                conj = conj.and(e.accept(this));
            }
            return conj;
        } catch (FExpressionException ex) {
            throw new IllegalStateException("No exception should happen while using this visitor!", ex);
        }
    }

    @Override
    public BDD or(List<FExpression> operands) {
        try {
            BDD disj = factory.zero();
            for (FExpression e : operands) {
                disj = disj.or(e.accept(this));
            }
            return disj;
        } catch (FExpressionException ex) {
            throw new IllegalStateException("No exception should happen while using this visitor!", ex);
        }
    }

}
