package be.unamur.fts.fexpression;

/*
 * #%L
 * VIBeS: featured expressions
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
import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.*;

import be.unamur.fts.fexpression.exception.ParserException;
import org.antlr.v4.runtime.tree.ParseTree;

public class ParserUtil {

    private static ParserUtil instance;

    public static ParserUtil getInstance() {
        return instance == null ? instance = new ParserUtil() : instance;
    }

    private ParserUtil() {
    }

    /**
     *
     * @param expression The string to parse
     * @return The root ASTNode of the parsed expression
     * @throws ParserException If an error occurs while parsing expression
     */
    public FExpression parse(String expression) throws ParserException {
        try {
            return parse(new ANTLRInputStream(expression));
        } catch (IOException e) {
            throw new ParserException("IOException while parsing!", e);
        }
    }

    /**
     *
     * @param input The input to parse
     * @return The root ASTNode of the parsed expression
     * @throws ParserException If an error occurs while parsing expression
     * @throws IOException
     */
    public FExpression parse(InputStream input) throws ParserException, IOException {
        return parse(new ANTLRInputStream(input));
    }

    /**
     *
     * @param input The input to parse
     * @return The root ASTNode of the parsed expression
     * @throws ParserException If an error occurs while parsing expression
     * @throws IOException
     */
    public FExpression parse(ANTLRInputStream input) throws ParserException, IOException {
        CommonTokenStream tokens = new CommonTokenStream(new FexpressionLexer(input));
        FexpressionParser parser = new FexpressionParser(tokens);
        try {
            ParseTree tree = parser.expression();
            FexpressionBuilderVisitor visitor = new FexpressionBuilderVisitor();
            FExpression exp = visitor.visit(tree);
            return exp;
        } catch (RecognitionException e) {
            throw new ParserException("Error while parsing stream!", e);
        }
    }

}
