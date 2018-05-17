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
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class FeaturedTransitionSystemFactory extends TransitionSystemFactory{
    
    public FeaturedTransitionSystemFactory(String initialState) {
        super(new DefaultFeaturedTransitionSystem(initialState));
    }

    @Override
    public void addTransition(String source, String action, String target) {
        addTransition(source, action, FExpression.trueValue(), target);
    }
    
    public void addTransition(String source, String action, FExpression fexpr, String target){
        DefaultFeaturedTransitionSystem fts = (DefaultFeaturedTransitionSystem) ts;
        State src = fts.addState(source);
        State trg = fts.addState(target);
        Action act = fts.addAction(action);
        Transition tr = fts.addTransition(src, act, trg);
        fts.setFExpression(tr, fexpr);
    }

    @Override
    public FeaturedTransitionSystem build() {
        return (DefaultFeaturedTransitionSystem) super.build();
    }
    
}
