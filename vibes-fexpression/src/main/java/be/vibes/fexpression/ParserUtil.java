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
import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.*;

import be.vibes.fexpression.exception.ParserException;
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
            return parse(CharStreams.fromString(expression));
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
        return parse(CharStreams.fromStream(input));
    }

    /**
     *
     * @param input The input to parse
     * @return The root ASTNode of the parsed expression
     * @throws ParserException If an error occurs while parsing expression
     * @throws IOException
     */
    public FExpression parse(CharStream input) throws ParserException, IOException {
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
