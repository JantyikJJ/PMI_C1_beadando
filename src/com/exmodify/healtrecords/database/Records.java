package com.exmodify.healtrecords.database;

import com.exmodify.healtrecords.database.models.*;

import com.exmodify.healtrecords.database.models.Record;
import com.exmodify.healtrecords.database.models.events.ProgressChangeListener;
import com.exmodify.healtrecords.main.Main;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handler for the XML file for storing the values of each Record instance.
 */
public class Records {
    /**
     * The event listeners for the progress change
     */
    private static final List<ProgressChangeListener> progressChanges = new ArrayList<>();
    /**
     * The list that stores all the Record isntances
     */
    private static final List<Record> records = new ArrayList<>();

    /**
     * Get records object
     * @return Records list (ArrayList)
     */
    public static List<Record> getRecords() {
        return records;
    }

    /**
     * Attach listener for loading progress
     * @param listener the listener for loading progress
     */
    public static void addEventListener(ProgressChangeListener listener) {
        progressChanges.add(listener);
    }

    /**
     * Loads Records from XML file
     *
     * @throws IOException if there was an error while reading the file
     * @throws org.xml.sax.SAXException if there was a corruption within the XML content
     * @throws ParserConfigurationException if secure XML parser couldn't be initialized
     */
    public static void load() throws IOException, org.xml.sax.SAXException, ParserConfigurationException {
        if (new File("records.xml").exists()) {
            // open file
            FileInputStream fis = new FileInputStream("records.xml");

            // initialize XML document builders to parse the contents of the file
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();

            // parsing the XML file and closing the file stream
            Document document = db.parse(fis);
            fis.close();
            // normalizing document (removing corrupted line breaks which may break the input)
            document.getDocumentElement().normalize();

            // getting all the records out of the root element
            NodeList recordList = document.getElementsByTagName("record");
            int c = recordList.getLength();
            for (int i = 0; i < c; i++) {
                Element element = (Element)recordList.item(i);

                // parsing the data out of the XML nodes
                String firstName = element.getElementsByTagName("firstName").item(0).getTextContent();
                String lastName = element.getElementsByTagName("lastName").item(0).getTextContent();
                Birth birth = Birth.fromNode(element);
                String gender = element.getElementsByTagName("gender").item(0).getTextContent();
                String bloodPressure = element.getElementsByTagName("bloodPressure").item(0).getTextContent();
                String cholesterol = element.getElementsByTagName("cholesterol").item(0).getTextContent();

                boolean smoker = element.getElementsByTagName("smoker").item(0)
                        .getTextContent().equals("true");
                double weight = Double.parseDouble(element.getElementsByTagName("weight").item(0)
                        .getTextContent());

                // adding the extracted record to the records list
                records.add(new Record(firstName, lastName, birth, gender, bloodPressure, cholesterol, smoker, weight));

                // fire all the event listeners
                for (ProgressChangeListener listener : progressChanges) {
                    listener.update(i + 1, c);
                }
            }
            // fire the event to show there aren't any elements (since it doesn't enter the for loop above)
            if (c == 0) {
                for (ProgressChangeListener listener : progressChanges) {
                    listener.update(0, c);
                }
            }
        }
        else { // file doesn't exit
            // save empty Records -> generate base of XML
            try {
                save();
            }
            catch (Exception e) { e.printStackTrace(); }

            // fake loading so the splash screen doesn't immediately disappear for the first time
            // for presentation purposes
            for (int i = 0; i < 100; i++) {
                for (ProgressChangeListener listener : progressChanges) {
                    listener.update(i + 1, 100);
                }
                try {
                    Thread.sleep(3);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Writes all the records into an XML file
     *
     * @throws IOException if the file couldn't be opened, flushed or written
     * @throws ParserConfigurationException if secure XML parser couldn't be initialized
     * @throws TransformerException if there was an unsupported data written into XML or the transformer couldn't be
     * initialized
     */
    public static void save() throws IOException, ParserConfigurationException, TransformerException {
        // open file and flush all the content
        FileWriter fw = new FileWriter("records.xml");
        fw.flush();

        // create new XML builder
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        DocumentBuilder db = dbf.newDocumentBuilder();

        // create new empty document
        Document document = db.newDocument();

        // create root element
        Element recordsElement = document.createElement("records");
        // generate Elements out of records and add them to the document
        for (Record r : records) {
            recordsElement.appendChild(r.toElement(document));
        }
        // add root element to the document
        document.appendChild(recordsElement);

        // create transformer that transform Document into raw XML string
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        // specify 'document' as source
        DOMSource domSource = new DOMSource(document);
        // create result stream where the transformer saves the XML string
        StreamResult result = new StreamResult(fw);
        // transform document
        transformer.transform(domSource, result);
        // close file
        fw.close();

        // since the changes have been saved into the file, toggle the boolean
        Main.setPendingChanges(false);
    }

    /**
     * Generate and append an Element to a parent Element
     *
     * @param tag the name of the XML tag
     * @param value the value of the tag
     * @param doc the document where the parent belongs
     * @param parent the parent where the new Element should be added
     */
    public static void appendElement(String tag, Object value, Document doc, Element parent) {
        // create new element
        Element element = doc.createElement(tag);
        // create new text node and add it to the new element
        element.appendChild(doc.createTextNode(value.toString()));
        // add new element to the parent
        parent.appendChild(element);
    }

    /**
     * Records constructor.
     *
     * @deprecated this class only contains static fields and methods, thus instancing is unnecessary.
     */
    public Records() {

    }
}
