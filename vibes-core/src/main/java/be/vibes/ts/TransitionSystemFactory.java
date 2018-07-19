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

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class TransitionSystemFactory {
    
    protected final DefaultTransitionSystem ts;
    
    protected TransitionSystemFactory(DefaultTransitionSystem ts){
        this.ts = ts;
    }
    
    public TransitionSystemFactory(String initialState){
        this(new DefaultTransitionSystem(initialState));
    }
    
    public void addState(String name){
        ts.addState(name);
    }
    
    public void addStates(String... names){
        for(String name: names){
            addState(name);
        }
    }
    
    public void addProposition(String prop){
        ts.addProposition(prop);
    }
    
    public void addPropositions(String... props){
        for(String p: props){
            addProposition(p);
        }
    }
    
    public void labelState(String state, String prop){
        State s = ts.getState(state);
        AtomicProposition p = ts.getAtomicProposition(prop);
        ts.setLabel(s, p);
    }
    
    public void addAction(String action){
        ts.addAction(action);
    }
    
    public void addActions(String... actions){
        for(String action: actions){
            addAction(action);
        }
    }
    
    public void addTransition(String source, String action, String target){
        State src = ts.addState(source);
        State trg = ts.addState(target);
        Action act = ts.addAction(action);
        ts.addTransition(src, act, trg);
    }
    
    public TransitionSystem build(){
        return ts;
    }
    
    public void validate(){
        // TODO Implement checks for deterministic LTS, connected LTS, and other well formed rules
        throw new UnsupportedOperationException("Not implemented yet!");
    }
    
    
}
