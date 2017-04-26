package be.unamur.transitionsystem.dsl.test.mutation;

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
import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.reflect.ConstructorUtils;

import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.dsl.exception.MutantDefinitionException;
import be.unamur.transitionsystem.test.mutation.ActionExchange;
import be.unamur.transitionsystem.test.mutation.ActionMissing;
import be.unamur.transitionsystem.test.mutation.ActionSelectionStrategy;
import be.unamur.transitionsystem.test.mutation.DefinedActionsSelectionStrategy;
import be.unamur.transitionsystem.test.mutation.DefinedStatesSelectionStrategy;
import be.unamur.transitionsystem.test.mutation.DefinedTransitionsSelectionStrategy;
import be.unamur.transitionsystem.test.mutation.MutationOperator;
import be.unamur.transitionsystem.test.mutation.RandomSelectionStrategy;
import be.unamur.transitionsystem.test.mutation.StateMissing;
import be.unamur.transitionsystem.test.mutation.StateSelectionStrategy;
import be.unamur.transitionsystem.test.mutation.TransitionAdd;
import be.unamur.transitionsystem.test.mutation.TransitionDestinationExchange;
import be.unamur.transitionsystem.test.mutation.TransitionMissing;
import be.unamur.transitionsystem.test.mutation.TransitionSelectionStrategy;
import be.unamur.transitionsystem.test.mutation.WrongInitialState;

/**
 * Defines static methods to build mutation operators.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public class Mutagen {

    private MutationOperator operator;

    protected Mutagen() {
    }

    public static Mutagen newMutant(Class<? extends MutationOperator> operatorClass,
            TransitionSystem ts) {
        Mutagen m = new Mutagen();
        try {
            m.operator = ConstructorUtils.invokeConstructor(operatorClass, ts);
            RandomSelectionStrategy strategy = new RandomSelectionStrategy();
            m.operator.setActionSelectionStrategy(strategy);
            m.operator.setTransitionSelectionStrategy(strategy);
            m.operator.setStateSelectionStrategy(strategy);
        } catch (NoSuchMethodException e) {
            throw new MutantDefinitionException("Unable to create mutation operator!", e);
        } catch (IllegalAccessException e) {
            throw new MutantDefinitionException("Unable to create mutation operator!", e);
        } catch (InvocationTargetException e) {
            throw new MutantDefinitionException("Unable to create mutation operator!", e);
        } catch (InstantiationException e) {
            throw new MutantDefinitionException("Unable to create mutation operator!", e);
        }
        return m;
    }

    public static Mutagen actionExchange(TransitionSystem ts) {
        return newMutant(ActionExchange.class, ts);
    }

    public static Mutagen actionMissing(TransitionSystem ts) {
        return newMutant(ActionMissing.class, ts);
    }

    public static Mutagen stateMissing(TransitionSystem ts) {
        return newMutant(StateMissing.class, ts);
    }

    public static Mutagen transitionAdd(TransitionSystem ts) {
        return newMutant(TransitionAdd.class, ts);
    }

    public static Mutagen transitionDestinationExchange(TransitionSystem ts) {
        return newMutant(TransitionDestinationExchange.class, ts);
    }

    public static Mutagen transitionMissing(TransitionSystem ts) {
        return newMutant(TransitionMissing.class, ts);
    }

    public static Mutagen wrongInitialState(TransitionSystem ts) {
        return newMutant(WrongInitialState.class, ts);
    }

    public Mutagen actionSelectionStrategy(ActionSelectionStrategy strategy) {
        operator.setActionSelectionStrategy(strategy);
        return this;
    }
    
    public Mutagen actionSelectionStrategy(Action... actionsToSelect){
        return actionSelectionStrategy(new DefinedActionsSelectionStrategy(actionsToSelect));
    }

    public Mutagen stateSelectionStrategy(StateSelectionStrategy strategy) {
        operator.setStateSelectionStrategy(strategy);
        return this;
    }
    
    public Mutagen stateSelectionStrategy(State... statesToSelect) {
        return stateSelectionStrategy(new DefinedStatesSelectionStrategy(statesToSelect));
    }

    public Mutagen transitionSelectionStrategy(TransitionSelectionStrategy strategy) {
        operator.setTransitionSelectionStrategy(strategy);
        return this;
    }
    
    public Mutagen transitionSelectionStrategy(Transition... transitionsToSelect) {
        return transitionSelectionStrategy(new DefinedTransitionsSelectionStrategy(transitionsToSelect));
    }

    public MutationOperator done() {
        return operator;
    }

}
