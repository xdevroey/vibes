package be.vibes.dsl.exception;

/*
 * #%L
 * vibes-dsl
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
public class TransitionSystemIOException extends RuntimeException {

    private static final long serialVersionUID = -4547394048073310803L;

    public TransitionSystemIOException(String message) {
        super(message);
    }

    public TransitionSystemIOException(String message, Exception cause) {
        super(message, cause);
    }

}
