package be.vibes.selection.exception;

/*-
 * #%L
 * VIBeS: test case selection
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

import be.vibes.ts.State;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class SinkStateReachedException extends Exception {
    
    private final State sinkState;

    public SinkStateReachedException(String message, State sinkState) {
        super(message);
        this.sinkState = sinkState;
    }

    public State getSinkState() {
        return sinkState;
    }
    
}
