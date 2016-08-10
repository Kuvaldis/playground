package kuvaldis.play.java;

import org.junit.Test;
import org.w3c.dom.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class XML {

    @Test
    public void testDomParser() throws Exception {
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setValidating(false); // will not print warnings
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilderFactory.setIgnoringComments(true);
        documentBuilderFactory.setIgnoringElementContentWhitespace(true);
        final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        final Document document = documentBuilder.parse(getClass().getClassLoader().getResourceAsStream("test.xml"));
        final Element root = document.getDocumentElement();
        assertEquals("People", root.getTagName());
        final NodeList personList = root.getElementsByTagName("Person");
        assertEquals(2, personList.getLength());
        final Node billNode = personList.item(0);
        assertEquals("M", billNode.getAttributes().getNamedItem("sex").getNodeValue());
        final Node billNameNode = billNode.getFirstChild().getNextSibling();
        assertEquals("Name", billNameNode.getNodeName());
        assertEquals("Bill Clinton", billNameNode.getFirstChild().getNodeValue());
    }

    @Test
    public void testDomBuilder() throws Exception {
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        final Document document = documentBuilder.newDocument();
        final Element people = document.createElement("People");
        final Element person = document.createElement("Person");
        final Element name = document.createElement("Name");
        final Text nameValue = document.createTextNode("Tom");
        name.appendChild(nameValue);
        person.appendChild(name);
        people.appendChild(person);
        document.appendChild(people);
        final Transformer transformer = TransformerFactory.newInstance().newTransformer();
        final Result output = new StreamResult(new File("testDomBuilder.xml"));
        final Source input = new DOMSource(document);
        transformer.transform(input, output);
        final String savedToFile = new Scanner(new File("testDomBuilder.xml")).useDelimiter("\\Z").next();
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><People><Person><Name>Tom</Name></Person></People>", savedToFile);
    }

    @Test
    public void testSaxParser() throws Exception {
        final SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        final SAXParser parser = parserFactory.newSAXParser();
        final NamesCapturer namesCapturer = new NamesCapturer();
        parser.parse(getClass().getClassLoader().getResourceAsStream("test.xml"), namesCapturer);
        assertEquals(2, namesCapturer.names.size());
        assertEquals("Bill Clinton", namesCapturer.names.get(0));
        assertEquals("Hilary Clinton", namesCapturer.names.get(1));
    }

    private static class NamesCapturer extends DefaultHandler {
        boolean nameContext = false;
        final List<String> names = new ArrayList<>();

        @Override
        public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
            if ("Name".equals(qName)) {
                nameContext = true;
            }
        }

        @Override
        public void characters(final char[] ch, final int start, final int length) throws SAXException {
            if (nameContext) {
                names.add(new String(ch, start, length));
            }
        }

        @Override
        public void endElement(final String uri, final String localName, final String qName) throws SAXException {
            this.nameContext = false;
        }
    }
}
