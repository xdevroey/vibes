package be.vibes.solver;

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
import be.vibes.solver.exception.ConstraintSolvingException;

public class SolverFatalErrorException extends ConstraintSolvingException {

    private static final long serialVersionUID = -2325925323317474526L;

    public SolverFatalErrorException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public SolverFatalErrorException(String arg0) {
        super(arg0);
    }

}
