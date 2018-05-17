package be.vibes.ts.io.xml;

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

import be.vibes.ts.State;
import be.vibes.ts.Transition;
import be.vibes.ts.TransitionSystem;
import java.util.Iterator;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;


public interface TransitionSystemElementPrinter {

    public void printElement(XMLStreamWriter xtw, TransitionSystem ts) throws XMLStreamException;

    public void printElement(XMLStreamWriter xtw, Iterator<State> states) throws XMLStreamException;

    public void printElement(XMLStreamWriter xtw, State state) throws XMLStreamException;

    public void printElement(XMLStreamWriter xtw, Transition transition) throws XMLStreamException;

}
