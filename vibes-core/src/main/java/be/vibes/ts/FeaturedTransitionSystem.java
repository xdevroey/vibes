package be.vibes.ts;

/*-
 * #%L
 * VIBeS: core
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

/**
 * An FTS is a tuple (S, Act, trans, i, AP, L, d, gamma). Where S, Act, trans,
 * i, AP, L are a TransitionSystem; d is a feature model; gamma : trans -&gt;
 * B(N) is a total function, labeling each transition with a feature expression,
 * i.e., a Boolean expression over the features.
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public interface FeaturedTransitionSystem extends TransitionSystem {
    
    public FExpression getFExpression(Transition transition);

}
