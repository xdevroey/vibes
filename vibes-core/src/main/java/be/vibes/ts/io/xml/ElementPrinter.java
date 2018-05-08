package be.vibes.ts.io.xml;

import be.vibes.ts.State;
import be.vibes.ts.Transition;
import be.vibes.ts.TransitionSystem;
import java.util.Iterator;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;


public interface ElementPrinter {

    public void printElement(XMLStreamWriter xtw, TransitionSystem ts) throws XMLStreamException;

    public void printElement(XMLStreamWriter xtw, Iterator<State> states) throws XMLStreamException;

    public void printElement(XMLStreamWriter xtw, State state) throws XMLStreamException;

    public void printElement(XMLStreamWriter xtw, Transition transition) throws XMLStreamException;

}
