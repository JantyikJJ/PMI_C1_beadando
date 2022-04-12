package com.exmodify.healtrecords.database;

import com.exmodify.healtrecords.database.models.*;

import com.exmodify.healtrecords.database.models.Record;
import com.exmodify.healtrecords.database.models.events.ProgressChangeListener;
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

public class Records {
    private static final List<ProgressChangeListener> progressChanges = new ArrayList<>();
    private static final List<Record> records = new ArrayList<>();

    public static List<Record> getRecords() {
        return records;
    }
    public static boolean updateRecord(Record oldRecord, Record newRecord) {
        for (int i = 0; i < records.size(); i++){
            if (records.get(i) == oldRecord) {
                records.set(i, newRecord);
                try {
                    save();
                }
                catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    public static void addEventListener(ProgressChangeListener listener) {
        progressChanges.add(listener);
    }

    public static void load() throws IOException, org.xml.sax.SAXException, ParserConfigurationException {
        if (new File("records.xml").exists()) {
            FileInputStream fis = new FileInputStream("records.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(fis);
            fis.close();
            document.getDocumentElement().normalize();

            NodeList recordList = document.getElementsByTagName("record");
            int c = recordList.getLength();
            for (int i = 0; i < c; i++) {
                Element element = (Element)recordList.item(i);

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

                records.add(new Record(firstName, lastName, birth, gender, bloodPressure, cholesterol, smoker, weight));

                for (ProgressChangeListener listener : progressChanges) {
                    listener.update(i + 1, c);
                }
            }
            if (c == 0) {
                for (ProgressChangeListener listener : progressChanges) {
                    listener.update(0, c);
                }
            }
        }
        else {
            try {
                save();
            }
            catch (Exception e) { e.printStackTrace(); }
            for (int i = 0; i < 100; i++) {
                for (ProgressChangeListener listener : progressChanges) {
                    listener.update(i + 1, 100);
                }
                try {
                    Thread.sleep(10);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void save() throws IOException, ParserConfigurationException, TransformerException {
        FileWriter fw = new FileWriter("records.xml");
        fw.flush();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        DocumentBuilder db = dbf.newDocumentBuilder();

        Document document = db.newDocument();

        Element recordsElement = document.createElement("records");
        for (Record r : records) {
            recordsElement.appendChild(r.toElement(document));
        }
        document.appendChild(recordsElement);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult result = new StreamResult(fw);
        transformer.transform(domSource, result);

        fw.close();
    }

    public static void appendElement(String tag, Object value, Document doc, Element parent) {
        Element element = doc.createElement(tag);
        element.appendChild(doc.createTextNode(value.toString()));
        parent.appendChild(element);
    }
}
