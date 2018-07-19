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

import be.vibes.fexpression.configuration.Configuration;
import be.vibes.ts.exception.UnresolvedFExpression;

/**
 * Class implementing this interface allow to project a
 * {@link FeaturedTransitionSystem} to a {@link TransitionSystem} according to a
 * given {@link Configuration}
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
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
