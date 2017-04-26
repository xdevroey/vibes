package be.unamur.transitionsystem.test.mutation;

/*
 * #%L
 * vibes-mutation
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
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.xpath.XPathExpressionEngine;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.test.mutation.exception.MutationConfigurationException;
import com.google.common.collect.Lists;

public class MutationConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(MutationConfiguration.class);

    public static final int DEFAULT_MUTANT_SIZE = 100;
    public static final boolean DEFAULT_UNIQUE = false;
    public static final Class<? extends ActionSelectionStrategy> DEFAULT_ACTION_SELECTION_STRATEGY = RandomSelectionStrategy.class;
    public static final Class<? extends StateSelectionStrategy> DEFAULT_STATE_SELECTION_STRATEGY = RandomSelectionStrategy.class;
    public static final Class<? extends TransitionSelectionStrategy> DEFAULT_TRANSITION_SELECTION_STRATEGY = RandomSelectionStrategy.class;

    private XMLConfiguration config;

    private Class<? extends ActionSelectionStrategy> defaultActionSelectionStrategyClass;
    private Class<? extends StateSelectionStrategy> defaultStateSelectionStrategyClass;
    private Class<? extends TransitionSelectionStrategy> defaultTransitionSelectionStrategyClass;

    public MutationConfiguration(File xmlFile) throws ConfigurationException, MutationConfigurationException {
        this.config = new XMLConfiguration(xmlFile);
        this.config.setExpressionEngine(new XPathExpressionEngine());
        this.defaultActionSelectionStrategyClass = loadDefaultActionSelectionStrategy();
        this.defaultStateSelectionStrategyClass = loadDefaultStateSelectionStrategy();
        this.defaultTransitionSelectionStrategyClass = loadDefaultTransitionSelectionStrategy();
    }

    public MutationConfiguration(URI xmlFile) throws ConfigurationException, MutationConfigurationException {
        this(new File(xmlFile));
    }
    
    public MutationConfiguration(String xmlFile) throws ConfigurationException, MutationConfigurationException {
        this(new File(xmlFile));
    }

    @SuppressWarnings("unchecked")
    private Class<? extends TransitionSelectionStrategy> loadDefaultTransitionSelectionStrategy() throws MutationConfigurationException {
        String xpath = "transitionSelection";
        Class<? extends TransitionSelectionStrategy> strategyClass = DEFAULT_TRANSITION_SELECTION_STRATEGY;
        if (this.config.containsKey(xpath)) {
            try {
                strategyClass = (Class<? extends TransitionSelectionStrategy>) MutationConfiguration.class.getClassLoader().loadClass(this.config.getString(xpath));
            } catch (ClassNotFoundException e) {
                throw new MutationConfigurationException("Unable to find ActionSelectionStrategy " + this.config.getString(xpath), e);
            } catch (ClassCastException e) {
                throw new MutationConfigurationException("Unable to cast " + this.config.getString(xpath) + " to  ActionSelectionStrategy", e);
            }
        }
        return strategyClass;
    }

    @SuppressWarnings("unchecked")
    private Class<? extends StateSelectionStrategy> loadDefaultStateSelectionStrategy() throws MutationConfigurationException {
        String xpath = "stateSelection";
        Class<? extends StateSelectionStrategy> strategyClass = DEFAULT_STATE_SELECTION_STRATEGY;
        if (this.config.containsKey(xpath)) {
            try {
                strategyClass = (Class<? extends StateSelectionStrategy>) MutationConfiguration.class.getClassLoader().loadClass(this.config.getString(xpath));
            } catch (ClassNotFoundException e) {
                throw new MutationConfigurationException("Unable to find ActionSelectionStrategy " + this.config.getString(xpath), e);
            } catch (ClassCastException e) {
                throw new MutationConfigurationException("Unable to cast " + this.config.getString(xpath) + " to  ActionSelectionStrategy", e);
            }
        }
        return strategyClass;
    }

    @SuppressWarnings("unchecked")
    private Class<? extends ActionSelectionStrategy> loadDefaultActionSelectionStrategy() throws MutationConfigurationException {
        String xpath = "actionSelection";
        Class<? extends ActionSelectionStrategy> strategyClass = DEFAULT_ACTION_SELECTION_STRATEGY;
        if (this.config.containsKey(xpath)) {
            try {
                strategyClass = (Class<? extends ActionSelectionStrategy>) MutationConfiguration.class.getClassLoader().loadClass(this.config.getString(xpath));
            } catch (ClassNotFoundException e) {
                throw new MutationConfigurationException("Unable to find ActionSelectionStrategy " + this.config.getString(xpath), e);
            } catch (ClassCastException e) {
                throw new MutationConfigurationException("Unable to cast " + this.config.getString(xpath) + " to  ActionSelectionStrategy", e);
            }
        }
        return strategyClass;
    }

    public int getDefaultMutationSize() {
        if (this.config.containsKey("mutantsSize")) {
            logger.debug("Configuration file contains default mutant size");
            return this.config.getInt("mutantsSize");
        } else {
            logger.debug("Returns default mutant size");
            return DEFAULT_MUTANT_SIZE;
        }
    }

    public boolean isMutantUniqueDefault() {
        if (this.config.containsKey("unique")) {
            return this.config.getBoolean("unique");
        } else {
            return DEFAULT_UNIQUE;
        }
    }

    public int getMutationSize(Class<? extends MutationOperator> operator) {
        String xpath = "operators/operator[class=\"" + operator.getCanonicalName() + "\"]/mutantsSize";
        logger.debug("getMutationSize XPath expression is {}", xpath);
        if (this.config.containsKey(xpath)) {
            return this.config.getInt(xpath);
        } else {
            return getDefaultMutationSize();
        }
    }

    public boolean isMutantUnique(Class<? extends MutationOperator> operator) {
        String xpath = "operators/operator[class=\"" + operator.getCanonicalName() + "\"]/unique";
        if (this.config.containsKey(xpath)) {
            return this.config.getBoolean(xpath);
        } else {
            return isMutantUniqueDefault();
        }
    }

    public ActionSelectionStrategy getDefaultActionSelectionStrategy() throws MutationConfigurationException {
        try {
            return this.defaultActionSelectionStrategyClass.newInstance();
        } catch (InstantiationException e) {
            throw new MutationConfigurationException("Unable to access default constructor for class " + this.defaultActionSelectionStrategyClass.getCanonicalName(), e);
        } catch (IllegalAccessException e) {
            throw new MutationConfigurationException("Unable to access class " + this.defaultActionSelectionStrategyClass.getCanonicalName(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public ActionSelectionStrategy getActionSelectionStrategy(Class<? extends MutationOperator> operator) throws MutationConfigurationException {
        Class<? extends ActionSelectionStrategy> strategyClass = this.defaultActionSelectionStrategyClass;
        String xpath = "operators/operator[class=\"" + operator.getCanonicalName() + "\"]/actionSelection";
        if (this.config.containsKey(xpath)) {
            try {
                strategyClass = (Class<? extends ActionSelectionStrategy>) getClass().getClassLoader().loadClass(this.config.getString(xpath));
            } catch (ClassNotFoundException e) {
                throw new MutationConfigurationException("Unable to find ActionSelectionStrategy " + this.config.getString(xpath), e);
            } catch (ClassCastException e) {
                throw new MutationConfigurationException("Unable to cast " + this.config.getString(xpath) + " to  ActionSelectionStrategy", e);
            }
        }
        try {
            return strategyClass.newInstance();
        } catch (InstantiationException e) {
            throw new MutationConfigurationException("Unable to access default constructor for class " + strategyClass.getCanonicalName(), e);
        } catch (IllegalAccessException e) {
            throw new MutationConfigurationException("Unable to access class " + strategyClass.getCanonicalName(), e);
        }
    }

    public StateSelectionStrategy getDefaultStateSelectionStrategy() throws MutationConfigurationException {
        try {
            return this.defaultStateSelectionStrategyClass.newInstance();
        } catch (InstantiationException e) {
            throw new MutationConfigurationException("Unable to access default constructor for class " + this.defaultStateSelectionStrategyClass.getCanonicalName(), e);
        } catch (IllegalAccessException e) {
            throw new MutationConfigurationException("Unable to access class " + this.defaultStateSelectionStrategyClass.getCanonicalName(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public StateSelectionStrategy getStateSelectionStrategy(Class<? extends MutationOperator> operator) throws MutationConfigurationException {
        Class<? extends StateSelectionStrategy> strategyClass = this.defaultStateSelectionStrategyClass;
        String xpath = "operators/operator[class=\"" + operator.getCanonicalName() + "\"]/stateSelection";
        if (this.config.containsKey(xpath)) {
            try {
                strategyClass = (Class<? extends StateSelectionStrategy>) getClass().getClassLoader().loadClass(this.config.getString(xpath));
            } catch (ClassNotFoundException e) {
                throw new MutationConfigurationException("Unable to find StateSelectionStrategy " + this.config.getString(xpath), e);
            } catch (ClassCastException e) {
                throw new MutationConfigurationException("Unable to cast " + this.config.getString(xpath) + " to  StateSelectionStrategy", e);
            }
        }
        try {
            return strategyClass.newInstance();
        } catch (InstantiationException e) {
            throw new MutationConfigurationException("Unable to access default constructor for class " + strategyClass.getCanonicalName(), e);
        } catch (IllegalAccessException e) {
            throw new MutationConfigurationException("Unable to access class " + strategyClass.getCanonicalName(), e);
        }
    }

    public TransitionSelectionStrategy getDefaultTransitionSelectionStrategy() throws MutationConfigurationException {
        try {
            return this.defaultTransitionSelectionStrategyClass.newInstance();
        } catch (InstantiationException e) {
            throw new MutationConfigurationException("Unable to access default constructor for class " + this.defaultTransitionSelectionStrategyClass.getCanonicalName(), e);
        } catch (IllegalAccessException e) {
            throw new MutationConfigurationException("Unable to access class " + this.defaultTransitionSelectionStrategyClass.getCanonicalName(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public TransitionSelectionStrategy getTransitionSelectionStrategy(Class<? extends MutationOperator> operator) throws MutationConfigurationException {
        Class<? extends TransitionSelectionStrategy> strategyClass = this.defaultTransitionSelectionStrategyClass;
        String xpath = "operators/operator[class=\"" + operator.getCanonicalName() + "\"]/TransitionSelection";
        if (this.config.containsKey(xpath)) {
            try {
                strategyClass = (Class<? extends TransitionSelectionStrategy>) getClass().getClassLoader().loadClass(this.config.getString(xpath));
            } catch (ClassNotFoundException e) {
                throw new MutationConfigurationException("Unable to find TransitionSelectionStrategy " + this.config.getString(xpath), e);
            } catch (ClassCastException e) {
                throw new MutationConfigurationException("Unable to cast " + this.config.getString(xpath) + " to  TransitionSelectionStrategy", e);
            }
        }
        try {
            return strategyClass.newInstance();
        } catch (InstantiationException e) {
            throw new MutationConfigurationException("Unable to access default constructor for class " + strategyClass.getCanonicalName(), e);
        } catch (IllegalAccessException e) {
            throw new MutationConfigurationException("Unable to access class " + strategyClass.getCanonicalName(), e);
        }
    }

    /**
     * Returns the lists of operators contained in this configuration. To know
     * if the mutants using this operator have to be unique (i.e., applied only
     * once for one set of given parameters), call the method
     * isMutantUnique(op.getClass()). To know the number of time the operator
     * should be applied, use getMutationSize(op.getClass()).
     *
     * @param ts The TS on which apply the mutation operators.
     * @return The list of mutation operators of this configuration.
     */
    public List<MutationOperator> getOperators(TransitionSystem ts) {
        List<MutationOperator> operators = Lists.newLinkedList();
        String[] operatorsClass = this.config.getStringArray("operators/operator/class");
        MutationOperator operator;
        for (String opClassName : operatorsClass) {
            try {
                @SuppressWarnings("unchecked")
                Class<? extends MutationOperator> opClass = (Class<? extends MutationOperator>) getClass().getClassLoader().loadClass(opClassName);
                operator = ConstructorUtils.invokeConstructor(opClass, ts);
                operator.setActionSelectionStrategy(getActionSelectionStrategy(opClass));
                operator.setStateSelectionStrategy(getStateSelectionStrategy(opClass));
                operator.setTransitionSelectionStrategy(getTransitionSelectionStrategy(opClass));
                operators.add(operator);
            } catch (ClassNotFoundException e) {
                logger.error("Unalbe to load MutationOperator class {}", opClassName, e);
            } catch (ClassCastException e) {
                logger.error("Unalbe to cast class {} to MutationOperator class", opClassName, e);
            } catch (NoSuchMethodException e) {
                logger.error("Unalbe to access constructor with TransitionSystemInterface as arguent for class", opClassName, e);
            } catch (IllegalAccessException e) {
                logger.error("Unalbe to access constructor with TransitionSystemInterface as arguent for class", opClassName, e);
            } catch (InvocationTargetException e) {
                logger.error("Unalbe to access constructor with TransitionSystemInterface as arguent for class", opClassName, e);
            } catch (InstantiationException e) {
                logger.error("Unalbe to access constructor with TransitionSystemInterface as arguent for class", opClassName, e);
            } catch (MutationConfigurationException e) {
                logger.error("Unalbe to load default selection strategies for mutation operator {}", opClassName, e);
            }
        }
        return operators;
    }

}
