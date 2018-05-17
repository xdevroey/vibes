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

import be.vibes.ts.TransitionSystem;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class TransitionSystemXmlPrinter {

    protected OutputStream output;

    private TransitionSystemElementPrinter tsPrinter;

    public TransitionSystemXmlPrinter(OutputStream output, TransitionSystemElementPrinter tsPrinter) {
        this.output = output;
        this.tsPrinter = tsPrinter;
    }

    public TransitionSystemXmlPrinter(File outputFile, TransitionSystemElementPrinter tsPrinter) throws FileNotFoundException {
        this(new FileOutputStream(outputFile), tsPrinter);
    }

    public void setOutput(OutputStream output) {
        this.output = output;
    }

    public void print(TransitionSystem ts) throws XMLStreamException {
        XMLOutputFactory xof = XMLOutputFactory.newInstance();
        XMLStreamWriter xtw = xof.createXMLStreamWriter(this.output);
        xtw.writeStartDocument("1.0");
        this.tsPrinter.printElement(xtw, ts);
        xtw.writeEndDocument();
        xtw.flush();
        xtw.close();
    }
}
