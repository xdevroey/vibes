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

import be.vibes.ts.exception.TransitionSystenExecutionException;
import be.vibes.ts.execution.Execution;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class TestCase extends Execution {
    
    private final String id;

    public TestCase(String id) {
        super();
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public Execution copy() {
        TestCase copy = new TestCase(getId());
        try {
            copy.enqueueAll(this);
        } catch (TransitionSystenExecutionException ex) {
            throw new IllegalStateException("Copy of an inconsistent TestCase!", ex);
        }
        return copy;
    }
    
}
