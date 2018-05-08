package be.vibes.ts.io.xml;

import be.vibes.ts.TransitionSystem;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class XmlPrinter {

    protected OutputStream output;

    private ElementPrinter tsPrinter;

    public XmlPrinter(OutputStream output, ElementPrinter tsPrinter) {
        this.output = output;
        this.tsPrinter = tsPrinter;
    }

    public XmlPrinter(File outputFile, ElementPrinter tsPrinter) throws FileNotFoundException {
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
