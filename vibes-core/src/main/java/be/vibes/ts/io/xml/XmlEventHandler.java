package be.vibes.ts.io.xml;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

public interface XmlEventHandler {

    public void handleStartDocument();

    public void handleEndDocument();

    public void handleStartElement(StartElement asStartElement) throws XMLStreamException;

    public void handleEndElement(EndElement asEndElement) throws XMLStreamException;

    public void handleCharacters(Characters asCharacters) throws XMLStreamException;

}
