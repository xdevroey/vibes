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

import be.vibes.ts.TestSet;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class TestSetXmlPrinter {
    
    protected OutputStream output;
    
    private TestCaseElementPrinter printer;

    public TestSetXmlPrinter(OutputStream output, TestCaseElementPrinter printer) {
        this.output = output;
        this.printer = printer;
    }
    
    public TestSetXmlPrinter(File output, TestCaseElementPrinter printer) throws FileNotFoundException {
        this(new FileOutputStream(output), printer);
    }

    public void setOutput(OutputStream output) {
        this.output = output;
    }

    public void print(TestSet set) throws XMLStreamException {
        XMLOutputFactory xof = XMLOutputFactory.newInstance();
        XMLStreamWriter xtw = xof.createXMLStreamWriter(this.output);
        xtw.writeStartDocument("1.0");
        this.printer.printElement(xtw, set);
        xtw.writeEndDocument();
        xtw.flush();
        xtw.close();
    }
    
}
