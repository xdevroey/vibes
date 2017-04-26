package be.unamur.transitionsystem.transformation.fts;

/*
 * #%L
 * vibes-transformation
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
import be.unamur.fts.fexpression.configuration.Configuration;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.transformation.exception.UnresolvedFExpression;

/**
 * Class implementing this interface allow to project a
 * {@link FeaturedTransitionSystem} to a {@link TransitionSystem} according to a
 * given {@link Configuration}
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public interface Projection {

    /**
     * This method performs a total projection of the FTS using the given
     * product.
     *
     * @param fts The FTS to project.
     * @param product The configuration to use for projection.
     * @return A new TS that is the projection of the FTS using the given
     * product.
     * @throws UnresolvedFExpression If an fexpression can not be resolved using
     * the given configuration.
     */
    public TransitionSystem project(FeaturedTransitionSystem fts, Configuration product)
            throws UnresolvedFExpression;

}
