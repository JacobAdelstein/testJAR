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
    String[] profileList;
    String serverURL;
    boolean debug = false;
    boolean offlineSave = false;


    public String listProfiles() {
        StringBuilder returnString = new StringBuilder();
        for(int i=0; i < inspectionProfiles.size(); i++) {

                gui.sysConsole.println(inspectionProfiles.get(i).toString());


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
        gui.sysConsole.println("Starting loading settings module");
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
                    for(int i = 0; i < settingsList.getLength(); i++) {
                        if(settingsList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                            if(settingsList.item(i).getNodeName().equalsIgnoreCase("globalsettings")) {
                                //Now we're going to load global program settings
                                NodeList globalSettingsNL = settingsList.item(i).getChildNodes();
                                for (int k = 0; k < globalSettingsNL.getLength(); k++){
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
                                    if(inspectionProfileNS.item(k).getNodeType() == Node.ELEMENT_NODE) {
                                        //Ensure it's an element node
                                        //Make a new inspectionProfile Object and start filling it with data
                                        inspectionProfile currentProfile = new inspectionProfile(inspectionProfileNS.item(k).getNodeName());
                                        NodeList IPSettingsNS = inspectionProfileNS.item(k).getChildNodes();
                                        //IPSettingsNS = Inspection Profile Settings NodeList
                                        for(int h=0; h < IPSettingsNS.getLength(); h++) {
                                            if(IPSettingsNS.item(h).getNodeType() == Node.ELEMENT_NODE) {
                                                //Here's where we'll read each individual setting element
                                                if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("lowerthreshold")) {
                                                    currentProfile.setLowerThreshold(Double.parseDouble(IPSettingsNS.item(h).getTextContent()));
                                                }
                                                if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("upperthreshold")) {
                                                    currentProfile.setUpperThreshold(Double.parseDouble(IPSettingsNS.item(h).getTextContent()));
                                                }
                                                if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("sizemin")) {
                                                    currentProfile.setSizeMin(Double.parseDouble(IPSettingsNS.item(h).getTextContent()));
                                                }
                                                if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("sizemax")) {
                                                    currentProfile.setSizeMax(Double.parseDouble(IPSettingsNS.item(h).getTextContent()));
                                                }
                                                if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("circularitymin")) {
                                                    currentProfile.setCircularityMin(Double.parseDouble(IPSettingsNS.item(h).getTextContent()));
                                                }
                                                if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("circularitymax")) {
                                                    currentProfile.setCircularityMax(Double.parseDouble(IPSettingsNS.item(h).getTextContent()));
                                                }
                                                if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("distance")) {
                                                    currentProfile.setDistance(Double.parseDouble(IPSettingsNS.item(h).getTextContent()));
                                                }
                                                if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("known")) {
                                                    currentProfile.setKnown(Double.parseDouble(IPSettingsNS.item(h).getTextContent()));
                                                }
                                                if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("pixel")) {
                                                    currentProfile.setPixel(Double.parseDouble(IPSettingsNS.item(h).getTextContent()));
                                                }
                                                if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("feretmin")) {
                                                    currentProfile.setFeretMin(Double.parseDouble(IPSettingsNS.item(h).getTextContent()));
                                                }
                                                if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("feretmax")) {
                                                    currentProfile.setFeretMax(Double.parseDouble(IPSettingsNS.item(h).getTextContent()));
                                                }
                                                if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("blackbackground")) {
                                                    if (IPSettingsNS.item(h).getTextContent().equalsIgnoreCase("true")) {
                                                        currentProfile.setBlackBackground(true);
                                                    } else {
                                                        currentProfile.setBlackBackground(false);
                                                    }
                                                }
                                                if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("holecount")) {
                                                    currentProfile.setHoleCount(Integer.parseInt(IPSettingsNS.item(h).getTextContent()));
                                                }
                                            }
                                        }

                                        //Our new inspectionProfile object is now full of data, add it to the ArrayList
                                        if(inspectionProfiles.add(currentProfile)) {
                                            gui.sysConsole.println("Profile added successfully");
                                        } else {
                                            gui.sysConsole.println("Issue adding profile");
                                        }
                                    }
                                }
                            }
                        }
                    }
        } else {
            //Otherwise throw an exception
            gui.sysConsole.println("Error processing settings file");
        }
        profileList = new String[inspectionProfiles.size()];
        for(int i=0; i < inspectionProfiles.size(); i++) {
            profileList[i] = inspectionProfiles.get(i).profileName;
        }










    }
}
