import org.jruby.RubyProcess;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;

public class settings {


    String sizeMin;
    String sizeMax;
    String circularityMin;
    String circularityMax;
    String distance;
    String known;
    String pixel;
    double feretMin;
    double feretMax;
    double lowerThreshold;
    double upperThreshold;
    Boolean blackBackground;

    public settings() throws ParserConfigurationException, IOException, SAXException {
        //Initialize when settings object is created
        System.out.println("Starting loading settings module");

        //Create document builder stuff
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.parse(new File( "settings.xml" ));

        //Normalize the XML Structure
        document.getDocumentElement().normalize();

        //Here comes the root node
        Element root = document.getDocumentElement();
        System.out.println("Root node= " + root.getNodeName());


        //Do some other shit
        NodeList analysisList = root.getElementsByTagName("analysis");
        System.out.println("Analysis node " + analysisList.item(0));
        Node analysisNode = (Element) analysisList;
        System.out.println("AN-NODE VALUE: " + analysisNode.getNodeValue());
        analysisNode


//        for (int i = 0; i < nList.getLength(); i++) {
//            Node node = (Element) analysisList;
//            node.get
//            System.out.println(nList.item(i).getNodeName() + " " );
//            Node childNode = nList.item(i);
//            System.out.println("ChildList length: " + childNode.getLength());
//
//
//            for (int k = 0; k < childList.getLength(); k++) {
//                System.out.println("    " + childList.item(i).getNodeName() + " : " + childList.item(i).getTextContent());
//
//            }
//        }




    }
}
