package be.unamur.transitionsystem.dsl.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;

import javax.xml.stream.XMLStreamException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.transitionsystem.dsl.exception.TestCaseDefinitionException;
import be.unamur.transitionsystem.test.FtsTestCase;
import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.UsageModelTestCase;
import be.unamur.transitionsystem.transformation.xml.FtsTestCaseXmlReader;
import be.unamur.transitionsystem.transformation.xml.LtsTestCaseXmlReader;
import be.unamur.transitionsystem.transformation.xml.UsageModelTestCaseXmlReader;

public class TestCaseXmlLoader {

    private static final Logger logger = LoggerFactory.getLogger(TestCaseXmlLoader.class);

    public static Iterator<FtsTestCase> loadFtsTestCases(InputStream in) {
        try {
            return new FtsTestCaseXmlReader(in);
        } catch (XMLStreamException e) {
            logger.error("Error while loading test cases!", e);
            throw new TestCaseDefinitionException("Error while creating FTS test case reader!", e);
        }
    }

    public static Iterator<FtsTestCase> loadFtsTestCases(File in) {
        try {
            return loadFtsTestCases(new FileInputStream(in));
        } catch (FileNotFoundException e) {
            logger.error("Error while loading test cases file!", e);
            throw new TestCaseDefinitionException("Error while loading test cases file!", e);
        }
    }

    public static Iterator<FtsTestCase> loadFtsTestCases(String in) {
        try {
            return loadFtsTestCases(new FileInputStream(in));
        } catch (FileNotFoundException e) {
            logger.error("Error while loading test cases file!", e);
            throw new TestCaseDefinitionException("Error while loading test cases file!", e);
        }
    }
    
    public static Iterator<UsageModelTestCase> loadUsageModelTestCases(InputStream in) {
        try {
            return new UsageModelTestCaseXmlReader(in);
        } catch (XMLStreamException e) {
            logger.error("Error while loading test cases!", e);
            throw new TestCaseDefinitionException("Error while creating FTS test case reader!", e);
        }
    }

    public static Iterator<UsageModelTestCase> loadUsageModelTestCases(File in) {
        try {
            return loadUsageModelTestCases(new FileInputStream(in));
        } catch (FileNotFoundException e) {
            logger.error("Error while loading test cases file!", e);
            throw new TestCaseDefinitionException("Error while loading test cases file!", e);
        }
    }

    public static Iterator<UsageModelTestCase> loadUsageModelTestCases(String in) {
        try {
            return loadUsageModelTestCases(new FileInputStream(in));
        } catch (FileNotFoundException e) {
            logger.error("Error while loading test cases file!", e);
            throw new TestCaseDefinitionException("Error while loading test cases file!", e);
        }
    }

    public static Iterator<TestCase> loadLtsTestCases(InputStream in) {
        try {
            return new LtsTestCaseXmlReader(in);
        } catch (XMLStreamException e) {
            logger.error("Error while loading test cases!", e);
            throw new TestCaseDefinitionException("Error while creating FTS test case reader!", e);
        }
    }

    public static Iterator<TestCase> loadLtsTestCases(File in) {
        try {
            return loadLtsTestCases(new FileInputStream(in));
        } catch (FileNotFoundException e) {
            logger.error("Error while loading test cases file!", e);
            throw new TestCaseDefinitionException("Error while loading test cases file!", e);
        }
    }

    public static Iterator<TestCase> loadLtsTestCases(String in) {
        try {
            return loadLtsTestCases(new FileInputStream(in));
        } catch (FileNotFoundException e) {
            logger.error("Error while loading test cases file!", e);
            throw new TestCaseDefinitionException("Error while loading test cases file!", e);
        }
    }

}
