package kuvaldis.play.java;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.w3c.dom.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class XML {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

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

    @Test
    public void testStaxParser() throws Exception {
        final XMLInputFactory factory = XMLInputFactory.newFactory();
        final XMLStreamReader reader = factory.createXMLStreamReader(getClass().getClassLoader().getResourceAsStream("test.xml"));
        final List<String> names = new ArrayList<>();
        boolean nameStarted = false;
        String name = null;
        while (reader.hasNext()) {
            final int event = reader.next();
            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    if ("Name".equals(reader.getLocalName())) {
                        nameStarted = true;
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if (nameStarted) {
                        name = reader.getText().trim();
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (nameStarted) {
                        names.add(name);
                        name = null;
                        nameStarted = false;
                    }
            }
        }
        assertEquals("Bill Clinton", names.get(0));
        assertEquals("Hilary Clinton", names.get(1));
    }

    @Test
    public void testValidateXmlByXsd() throws Exception {
        final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        final Schema schema = factory.newSchema(new StreamSource(getClass().getClassLoader().getResourceAsStream("data.xsd")));
        final Validator validator = schema.newValidator();
        validator.validate(new StreamSource(getClass().getClassLoader().getResourceAsStream("data.xml")));
    }

    @Test
    public void testXslt() throws Exception {
        final TransformerFactory factory = TransformerFactory.newInstance();
        final Source xslt = new StreamSource(getClass().getClassLoader().getResourceAsStream("hello.xslt"));
        final Transformer transformer = factory.newTransformer(xslt);

        final Source text = new StreamSource(getClass().getClassLoader().getResourceAsStream("hello.xml"));
        transformer.transform(text, new StreamResult(new File("hello-out.html")));

        final String savedToFile = new Scanner(new File("hello-out.html")).useDelimiter("\\Z").next();
        final String removeLineSeparators = savedToFile.replace("\n", "").replace("\r", "");
        assertEquals("<html>" +
                "<body>" +
                "<h1>Hello, World!</h1>" +
                "<div>from <i>An XSLT Programmer</i>" +
                "</div>" +
                "<table border=\"1\">" +
                "<tr>" +
                "<th>Name</th>" +
                "</tr>" +
                "<tr>" +
                "<td>Mozart</td>" +
                "</tr>" +
                "<tr>" +
                "<td>Mahler</td>" +
                "</tr>" +
                "</table>" +
                "</body>" +
                "</html>", removeLineSeparators);
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
