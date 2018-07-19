package be.vibes.dsl.transforme;

/*-
 * #%L
 * VIBeS: dsl
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

import be.vibes.dsl.exception.TransformationException;
import be.vibes.fexpression.configuration.Configuration;
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.Projection;
import be.vibes.ts.SimpleProjection;
import be.vibes.ts.TransitionSystem;
import be.vibes.ts.exception.UnresolvedFExpression;
import static com.google.common.base.Preconditions.*;

public class Project {

    public static TransitionSystem projectProduct(FeaturedTransitionSystem fts, Configuration config) {
        checkNotNull(fts);
        checkNotNull(config);
        Projection proj = SimpleProjection.getInstance();
        try {
            return proj.project(fts, config);
        } catch (UnresolvedFExpression e) {
            throw new TransformationException("Exception while performing projection", e);
        }
    }

}
