import org.jruby.RubyProcess;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        for (int i = 0; i < inspectionProfiles.size(); i++) {

            gui.sysConsole.println(inspectionProfiles.get(i).toString());


            if (i == inspectionProfiles.size() - 1) {
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
        Document document = builder.parse(new File("settings.xml"));

        //Normalize the XML Structure
        document.getDocumentElement().normalize();

        //Here comes the root node
        Element root = document.getDocumentElement();
//        System.out.println("Root node= " + root.getNodeName());


        //Do some other shit
        if (root.getNodeName().equalsIgnoreCase("settings")) {
            //Continue processing
            NodeList settingsList = root.getChildNodes();
            for (int i = 0; i < settingsList.getLength(); i++) {
                if (settingsList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    if (settingsList.item(i).getNodeName().equalsIgnoreCase("globalsettings")) {
                        //Now we're going to load global program settings
                        NodeList globalSettingsNL = settingsList.item(i).getChildNodes();
                        for (int k = 0; k < globalSettingsNL.getLength(); k++) {
                            if (globalSettingsNL.item(k).getNodeType() == Node.ELEMENT_NODE) {
                                //Ensure it's an element node
                                if (globalSettingsNL.item(k).getNodeName().equals("serverURL")) {
                                    //Read in server URL global setting
                                    serverURL = globalSettingsNL.item(k).getTextContent();
                                }
                            }
                        }
                    }

                    if (settingsList.item(i).getNodeName().equalsIgnoreCase("inspectionprofile")) {
                        //Now we'll load in our inspection profiles
                        NodeList inspectionProfileNS = settingsList.item(i).getChildNodes();
                        for (int k = 0; k < inspectionProfileNS.getLength(); k++) {
                            if (inspectionProfileNS.item(k).getNodeType() == Node.ELEMENT_NODE) {
                                //Ensure it's an element node
                                //Make a new inspectionProfile Object and start filling it with data
                                inspectionProfile currentProfile = new inspectionProfile(inspectionProfileNS.item(k).getNodeName());
                                Element currElement = (Element) inspectionProfileNS.item(k);
                                currentProfile.setProfileType(currElement.getAttribute("profileType"));
                                NodeList IPSettingsNS = inspectionProfileNS.item(k).getChildNodes();
                                //IPSettingsNS = Inspection Profile Settings NodeList
                                for (int h = 0; h < IPSettingsNS.getLength(); h++) {
                                    if (IPSettingsNS.item(h).getNodeType() == Node.ELEMENT_NODE) {
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

                                        if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("submitaddress")) {
                                            currentProfile.setsubmitAddress(IPSettingsNS.item(h).getTextContent());
                                        }

                                    }
                                }

                                //Our new inspectionProfile object is now full of data, add it to the ArrayList
                                if (inspectionProfiles.add(currentProfile)) {
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
        for (int i = 0; i < inspectionProfiles.size(); i++) {
            profileList[i] = inspectionProfiles.get(i).profileName;
        }


        JFrame settingsframe = new JFrame("Settings Panel");
        settingsframe.setVisible(true);
        settingsframe.setSize(558, 520);
        JTextField sizeMin, sizeMax, circularityMin, circularityMax, lowerThreshold, upperThreshold, blackBackground, distance, known, pixel, feretMin, feretMax;


        settingsframe.setLayout(null);
        JButton b = new JButton("Save");
        b.setBounds(195, 400, 120, 50);
        settingsframe.add(b);




        sizeMin = new JTextField();

        sizeMax = new JTextField();

        circularityMin = new JTextField();

        circularityMax = new JTextField();

        lowerThreshold = new JTextField();

        upperThreshold = new JTextField();

        blackBackground = new JTextField();

        distance = new JTextField();

        known = new JTextField();

        pixel = new JTextField();

        feretMin = new JTextField();
        feretMax = new JTextField();


        settingsframe.add(sizeMin);
        settingsframe.add(sizeMax);
        settingsframe.add(circularityMin);
        settingsframe.add(circularityMax);
        settingsframe.add(lowerThreshold);
        settingsframe.add(upperThreshold);
        settingsframe.add(blackBackground);
        settingsframe.add(distance);
        settingsframe.add(known);
        settingsframe.add(pixel);
        settingsframe.add(feretMin);
        settingsframe.add(feretMax);

        sizeMin.setBounds(160, 20, 200, 30);
        sizeMax.setBounds(160, 50, 200, 30);
        circularityMin.setBounds(160, 80, 200, 30);
        circularityMax.setBounds(160, 110, 200, 30);
        lowerThreshold.setBounds(160, 140, 200, 30);
        upperThreshold.setBounds(160, 170, 200, 30);
        blackBackground.setBounds(160, 200, 200, 30);
        distance.setBounds(160, 230, 200, 30);
        known.setBounds(160, 260, 200, 30);
        pixel.setBounds(160, 290, 200, 30);
        feretMin.setBounds(160, 320, 200, 30);
        feretMax.setBounds(160, 350, 200, 30);


        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String savesizemin = sizeMin.getText();
                String savedsizemax = sizeMax.getText();
                String savedcircularitymax = circularityMax.getText();
                String savedlowerThreshold = lowerThreshold.getText();
                String savedupperThreshold = upperThreshold.getText();
                String savedblackbackground = blackBackground.getText();
                String saveddistance = distance.getText();
                String savedknown = known.getText();
                String savedpixel = pixel.getText();
                String savedferetMin = feretMin.getText();
                String savedferetMax = feretMax.getText();


                System.out.println("Sizemin"+ savesizemin + "Sizemax" + savedsizemax + "CircularityMax" + savedcircularitymax +"LowerThreshold" +savedlowerThreshold + "UpperThreshold" +savedupperThreshold);
                System.out.println("Blackbackground" + savedblackbackground + "Distance" + saveddistance + "Known" + savedknown + "Pixel" + savedpixel + "feretMin" + savedferetMin + "feretMax" + savedferetMax);
            }
        });
    }
}