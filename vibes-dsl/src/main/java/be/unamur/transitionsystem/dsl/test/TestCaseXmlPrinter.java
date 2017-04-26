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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.xml.stream.XMLStreamException;

import be.unamur.transitionsystem.dsl.exception.TestCaseDefinitionException;
import be.unamur.transitionsystem.test.FtsTestCase;
import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.TestSet;
import be.unamur.transitionsystem.test.UsageModelTestCase;
import be.unamur.transitionsystem.transformation.xml.FtsTestCaseXmlGenerator;
import be.unamur.transitionsystem.transformation.xml.LtsTestCaseXmlGenerator;
import be.unamur.transitionsystem.transformation.xml.TestCaseXmlGenerator;
import be.unamur.transitionsystem.transformation.xml.UsageModelTestCaseXmlGenerator;

public class TestCaseXmlPrinter {

    public static void print(TestCase tc, OutputStream out) {
        try {
            LtsTestCaseXmlGenerator gen = new LtsTestCaseXmlGenerator(new PrintStream(out));
            gen.startDocumen();
            gen.printTestCase(tc);
            gen.endDocument();
        } catch (XMLStreamException e) {
            throw new TestCaseDefinitionException("Exception while printing test case!", e);
        }
    }

    public static void print(TestCase tc, File out) {
        try {
            print(tc, new FileOutputStream(out));
        } catch (FileNotFoundException e) {
            throw new TestCaseDefinitionException("Exception while printing test case!", e);
        }
    }

    public static void print(TestCase tc, String file) {
        print(tc, new File(file));
    }

    public static void print(FtsTestCase tc, OutputStream out) {
        try {
            FtsTestCaseXmlGenerator gen = new FtsTestCaseXmlGenerator(new PrintStream(out));
            gen.startDocumen();
            gen.printTestCase(tc);
            gen.endDocument();
        } catch (XMLStreamException e) {
            throw new TestCaseDefinitionException("Error while printing test case!", e);
        }
    }

    public static void print(FtsTestCase tc, File out) {
        try {
            print(tc, new FileOutputStream(out));
        } catch (FileNotFoundException e) {
            throw new TestCaseDefinitionException("Exception while printing test case!", e);
        }
    }

    public static void print(FtsTestCase tc, String file) {
        print(tc, new File(file));
    }

    public static void print(UsageModelTestCase tc, OutputStream out) {
        try {
            UsageModelTestCaseXmlGenerator gen = new UsageModelTestCaseXmlGenerator(new PrintStream(out));
            gen.startDocumen();
            gen.printTestCase(tc);
            gen.endDocument();
        } catch (XMLStreamException e) {
            throw new TestCaseDefinitionException("Exception while printing test case!", e);
        }
    }

    public static void print(UsageModelTestCase tc, File out) {
        try {
            print(tc, new FileOutputStream(out));
        } catch (FileNotFoundException e) {
            throw new TestCaseDefinitionException("Exception while printing test case!", e);
        }
    }

    public static void print(UsageModelTestCase tc, String file) {
        print(tc, new File(file));
    }

    public static void print(TestSet set, OutputStream out) {
        try {
            TestCaseXmlGenerator gen;
            if (set.size() == 0) {
                gen = new LtsTestCaseXmlGenerator(new PrintStream(out));
            } else if (set.get(0) instanceof FtsTestCase) {
                gen = new FtsTestCaseXmlGenerator(new PrintStream(out));
            } else if (set.get(0) instanceof UsageModelTestCase) {
                gen = new UsageModelTestCaseXmlGenerator(new PrintStream(out));
            } else {
                gen = new LtsTestCaseXmlGenerator(new PrintStream(out));
            }
            gen.startDocumen();
            gen.printTestSet(set);
            gen.endDocument();
        } catch (XMLStreamException e) {
            throw new TestCaseDefinitionException("Exception while printing test case!", e);
        }
    }

    public static void print(TestSet set, File out) {
        try {
            print(set, new FileOutputStream(out));
        } catch (FileNotFoundException e) {
            throw new TestCaseDefinitionException("Exception while printing test case!", e);
        }
    }

    public static void print(TestSet set, String file) {
        print(set, new File(file));
    }

}
