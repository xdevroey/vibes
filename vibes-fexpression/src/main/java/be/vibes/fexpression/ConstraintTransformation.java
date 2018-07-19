package be.vibes.fexpression;

/*
 * #%L
 * VIBeS: featured expressions
 * %%
 * Copyright (C) 2014 PReCISE, University of Namur
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
public class ConstraintTransformation {

    public static FExpression getConjunction(Iterable<FExpression> expressions) {
        FExpression expr = FExpression.trueValue();
        for (FExpression f : expressions) {
            expr.andWith(f);
        }
        return expr.applySimplification();
    }

    public static FExpression getDisjunction(Iterable<FExpression> expressions) {
        FExpression expr = FExpression.falseValue();
        boolean iterated = false;
        for (FExpression f : expressions) {
            iterated = true;
            expr.orWith(f);
        }
        return iterated ? expr.applySimplification() : FExpression.trueValue();
    }

}
