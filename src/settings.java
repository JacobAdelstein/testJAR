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
import java.util.ArrayList;

public class settings {



    ArrayList<inspectionProfile> inspectionProfiles;
    String serverURL;


    public String listProfiles() {
        StringBuilder returnString = new StringBuilder();
        for(int i=0; i < inspectionProfiles.size(); i++) {

            System.out.println(inspectionProfiles.get(i));


            if(i == inspectionProfiles.size() - 1) {
                returnString.append(inspectionProfiles.get(i).profileName);
            } else {
                returnString.append(inspectionProfiles.get(i).profileName + ", ");
            }
        }
        return returnString.toString();
    }

    public settings() throws ParserConfigurationException, IOException, SAXException {
        //Initialize when settings object is created
        System.out.println("Starting loading settings module");
        inspectionProfiles = new ArrayList<inspectionProfile>();

        //Create document builder stuff
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File( "settings.xml" ));

        //Normalize the XML Structure
        document.getDocumentElement().normalize();

        //Here comes the root node
        Element root = document.getDocumentElement();
//        System.out.println("Root node= " + root.getNodeName());


        //Do some other shit
        if (root.getNodeName().equalsIgnoreCase("settings")){
            //Continue processing
             NodeList settingsList = root.getChildNodes();
//                    System.out.println("Settings Length: " + settingsList.getLength());
                    for(int i = 0; i < settingsList.getLength(); i++) {
                        if(settingsList.item(i).getNodeType() == Node.ELEMENT_NODE) {
//                            System.out.println("Node Name: " + settingsList.item(i).getNodeName() + " Node Value: " + settingsList.item(i).getNodeValue());


                            if(settingsList.item(i).getNodeName().equalsIgnoreCase("globalsettings")) {
                                //Now we're going to load global program settings
//                                System.out.println("Found global setttings node");
                                NodeList globalSettingsNL = settingsList.item(i).getChildNodes();
                                for (int k = 0; k < globalSettingsNL.getLength(); k++){
//                                    System.out.println("Node type= " + globalSettingsNL.item(k).getNodeType() + " Node Name: " + globalSettingsNL.item(k).getNodeName());
                                    if(globalSettingsNL.item(k).getNodeType() == Node.ELEMENT_NODE) {
                                        //Ensure it's an element node
                                        if(globalSettingsNL.item(k).getNodeName().equals("serverURL")) {
                                            //Read in server URL global setting
                                            serverURL = globalSettingsNL.item(k).getTextContent();
                                        }
                                    }


                                }
                            }

                            if(settingsList.item(i).getNodeName().equalsIgnoreCase("inspectionprofile")) {
                                //Now we'll load in our inspection profiles
                                NodeList inspectionProfileNS = settingsList.item(i).getChildNodes();
                                for (int k=0; k < inspectionProfileNS.getLength(); k++){
//                                    System.out.println("ISNode type= " + inspectionProfileNS.item(k).getNodeType() + " ISNode Name: " + inspectionProfileNS.item(k).getNodeName());
                                    if(inspectionProfileNS.item(k).getNodeType() == Node.ELEMENT_NODE) {
                                        //Ensure it's an element node
                                        //Make a new inspectionProfile Object and start filling it with data
                                        inspectionProfile currentProfile = new inspectionProfile(inspectionProfileNS.item(k).getNodeName());
                                        NodeList IPSettingsNS = inspectionProfileNS.item(k).getChildNodes();
                                        for(int h=0; h < IPSettingsNS.getLength(); h++) {
                                            if(IPSettingsNS.item(k).getNodeType() == Node.ELEMENT_NODE) {
                                                //Here's where we'll read each individual setting element
                                                if (IPSettingsNS.item(k).getNodeName().equalsIgnoreCase("lowerthreshold")) {
                                                    currentProfile.setLowerThreshold(Double.parseDouble(IPSettingsNS.item(k).getTextContent()));
                                                }
                                                if (IPSettingsNS.item(k).getNodeName().equalsIgnoreCase("upperthreshold")) {
                                                    currentProfile.setUpperThreshold(Double.parseDouble(IPSettingsNS.item(k).getTextContent()));
                                                }
                                                if (IPSettingsNS.item(k).getNodeName().equalsIgnoreCase("sizemin")) {
                                                    currentProfile.setSizeMin(Double.parseDouble(IPSettingsNS.item(k).getTextContent()));
                                                    System.out.println("Sizemin: " + IPSettingsNS.item(k).getTextContent());
                                                }
                                                if (IPSettingsNS.item(k).getNodeName().equalsIgnoreCase("sizemax")) {
                                                    currentProfile.setSizeMax(Double.parseDouble(IPSettingsNS.item(k).getTextContent()));
                                                }
                                                if (IPSettingsNS.item(k).getNodeName().equalsIgnoreCase("circularitymin")) {
                                                    currentProfile.setCircularityMin(Double.parseDouble(IPSettingsNS.item(k).getTextContent()));
                                                }
                                                if (IPSettingsNS.item(k).getNodeName().equalsIgnoreCase("circularityman")) {
                                                    currentProfile.setCircularityMax(Double.parseDouble(IPSettingsNS.item(k).getTextContent()));
                                                }
                                                if (IPSettingsNS.item(k).getNodeName().equalsIgnoreCase("distance")) {
                                                    currentProfile.setDistance(Double.parseDouble(IPSettingsNS.item(k).getTextContent()));
                                                }
                                                if (IPSettingsNS.item(k).getNodeName().equalsIgnoreCase("known")) {
                                                    currentProfile.setKnown(Double.parseDouble(IPSettingsNS.item(k).getTextContent()));
                                                }
                                                if (IPSettingsNS.item(k).getNodeName().equalsIgnoreCase("pixel")) {
                                                    currentProfile.setPixel(Double.parseDouble(IPSettingsNS.item(k).getTextContent()));
                                                }
                                                if (IPSettingsNS.item(k).getNodeName().equalsIgnoreCase("feretmin")) {
                                                    currentProfile.setFeretMin(Double.parseDouble(IPSettingsNS.item(k).getTextContent()));
                                                }
                                                if (IPSettingsNS.item(k).getNodeName().equalsIgnoreCase("feretmax")) {
                                                    currentProfile.setFeretMax(Double.parseDouble(IPSettingsNS.item(k).getTextContent()));
                                                }
                                                if (IPSettingsNS.item(k).getNodeName().equalsIgnoreCase("blackbackground")) {
                                                    if (IPSettingsNS.item(k).getTextContent().equalsIgnoreCase("true")) {
                                                        currentProfile.setBlackBackground(true);
                                                    } else {
                                                        currentProfile.setBlackBackground(false);
                                                    }
                                                }
                                            }
                                        }


//                                        inspectionProfileNS.item(k).

//                                        if(inspectionProfileNS.item(k).getNodeName().equalsIgnoreCase("lowerthreshold")) {
//
//                                        }
                                        //Our new inspectionProfile object is now full of data, add it to the ArrayList
                                        if(inspectionProfiles.add(currentProfile)) {
                                            System.out.println("Profile added successfully");
                                        } else {
                                            System.out.println("Issue adding profile");
                                        }

                                    }


                                }
                            }



                        }

//                        System.out.println("Node Type: " + settingsList.item(i).getNodeType() + " Node Name: " + settingsList.item(i).getNodeName() + " Node Value: " + settingsList.item(i).getNodeValue());

                    }

        } else {
            //Otherwise throw an exception
            System.out.println("Error processing settings file");
        }








    }
}
