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

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class DefaultUsageModel extends DefaultTransitionSystem implements UsageModel{
    
    private final Map<Transition, Double> proba;

    public DefaultUsageModel(String initialState) {
        super(initialState);
        this.proba = new HashMap<>();
    }

    @Override
    public double getProbability(Transition transition) {
        Preconditions.checkNotNull(transition, "Transition may not be null!");
        Double pr = proba.get(transition);
        if(pr == null){
            pr = 0.0;
        }
        return pr;
    }

    @Override
    public void setProbability(Transition transition, double proba){
        Preconditions.checkNotNull(transition, "Transition may not be null!");
        Preconditions.checkArgument(proba >= 0 && proba <= 1.0, "Proba must have a value between 0 and 1!");
        this.proba.put(transition, proba);
    }
    
}
