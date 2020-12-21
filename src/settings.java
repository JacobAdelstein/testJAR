import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class settings {


    ArrayList<inspectionProfile> inspectionProfiles;
    String[] profileList;
    String serverURL;
    boolean debug = false;
    boolean offlineSave = false;


    public String toString(){
        StringBuilder sb = new StringBuilder("\n\nPrinting settings: \n");
        sb.append("Server URL: " + serverURL + "\n");
        for (inspectionProfile profile: inspectionProfiles) {
            sb.append(profile.toString());
        }
        return sb.toString();
    }


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


                                        if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("entermin")) {
                                            currentProfile.setEnterMin(Double.parseDouble(IPSettingsNS.item(h).getTextContent()));
                                        }
                                        if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("entermax")) {
                                            currentProfile.setEnterMax(Double.parseDouble(IPSettingsNS.item(h).getTextContent()));
                                        }
                                        if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("exitmin")) {
                                            currentProfile.setExitMin(Double.parseDouble(IPSettingsNS.item(h).getTextContent()));
                                        }
                                        if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("exitmax")) {
                                            currentProfile.setExitMax(Double.parseDouble(IPSettingsNS.item(h).getTextContent()));
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


    }





    public static void settingsframe() throws IOException, SAXException, ParserConfigurationException {
        settings newSettings = new settings();


        JFrame settingsframe = new JFrame("Settings Panel");

        settingsframe.setSize(630, 450);


        settingsframe.setTitle("Settings");

        JTabbedPane tp = new JTabbedPane();
        tp.setBounds(20, 20, 580, 450);

        for (inspectionProfile profile : newSettings.inspectionProfiles) {
            System.out.println(profile.profileName);
//            tp.add(profile.profileName, panel(profile, newSettings));
        }


//        tp.add("One", settings.panel());
//        tp.add("Two", settings.panel());
//        tp.add("Three", settings.panel());

        settingsframe.add(tp);

        settingsframe.setVisible(true);



    }


    public void writeFile(settings newSettings) throws ParserConfigurationException, IOException, SAXException {



        String serverURL;

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
                                            IPSettingsNS.item(h).setTextContent(String.valueOf(currentProfile.getLowerThreshold()));
                                        }
                                        if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("upperthreshold")) {
                                            IPSettingsNS.item(h).setTextContent(String.valueOf(currentProfile.upperThreshold));
                                        }
                                        if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("sizemin")) {
                                            IPSettingsNS.item(h).setTextContent(String.valueOf(currentProfile.sizeMin));
                                        }
                                        if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("sizemax")) {
                                            IPSettingsNS.item(h).setTextContent(String.valueOf(currentProfile.sizeMax));
                                        }
                                        if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("circularitymin")) {
                                            IPSettingsNS.item(h).setTextContent(String.valueOf(currentProfile.circularityMin));
                                        }
                                        if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("circularitymax")) {
                                            IPSettingsNS.item(h).setTextContent(String.valueOf(currentProfile.circularityMax));
                                        }
                                        if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("distance")) {
                                            IPSettingsNS.item(h).setTextContent(String.valueOf(currentProfile.distance));
                                        }
                                        if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("known")){
                                            IPSettingsNS.item(h).setTextContent(String.valueOf(currentProfile.known));
                                        }
                                        if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("pixel")) {
                                            IPSettingsNS.item(h).setTextContent(String.valueOf(currentProfile.pixel));
                                        }


                                        if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("entermin")) {
                                            currentProfile.setEnterMin(Double.parseDouble(IPSettingsNS.item(h).getTextContent()));
                                        }
                                        if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("entermax")) {
                                            currentProfile.setEnterMax(Double.parseDouble(IPSettingsNS.item(h).getTextContent()));
                                        }
                                        if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("exitmin")) {
                                            currentProfile.setExitMin(Double.parseDouble(IPSettingsNS.item(h).getTextContent()));
                                        }
                                        if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("exitmax")) {
                                            currentProfile.setExitMax(Double.parseDouble(IPSettingsNS.item(h).getTextContent()));
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


    }


    private JPanel panel(inspectionProfile profile, settings newSettings)  {



        JPanel newpanel = new JPanel();


        newpanel.setLayout(new GridLayout(13, 2));
        JTextField sizeMin, sizeMax, circularityMin, circularityMax, lowerThreshold, upperThreshold, blackBackground, distance, known, pixel;

        JLabel sizeminlabel = new JLabel("Size Min:");
        JLabel sizemaxlabel = new JLabel("Size Max:");
        JLabel circularityminlabel = new JLabel("Circularity Min:");
        JLabel circularitymaxlabel = new JLabel("Circularity Max:");
        JLabel lowerthresholdlabel = new JLabel("Lower Threshold:");
        JLabel upperthresholdlabel = new JLabel("Upper Threshold:");
        JLabel blackbackgroundlabel = new JLabel("Black Background:");
        JLabel distancelabel = new JLabel("Distance:");
        JLabel knownlabel = new JLabel("Known Label:");
        JLabel pixellabel = new JLabel("Pixel Label:");
        JLabel empty = new JLabel("");
        JButton b = new JButton("Save");


        sizeMin = new JTextField(String.valueOf(profile.sizeMin));

        sizeMax = new JTextField(String.valueOf(profile.sizeMax));

        circularityMin = new JTextField(String.valueOf(profile.circularityMin));

        circularityMax = new JTextField(String.valueOf(profile.circularityMax));

        lowerThreshold = new JTextField(String.valueOf(profile.lowerThreshold));

        upperThreshold = new JTextField(String.valueOf(profile.upperThreshold));

        blackBackground = new JTextField(String.valueOf(profile.blackBackground));

        distance = new JTextField(String.valueOf(profile.distance));

        known = new JTextField(String.valueOf(profile.known));

        pixel = new JTextField(String.valueOf(profile.pixel));


        newpanel.add(sizeminlabel);
        newpanel.add(sizeMin);
        newpanel.add(sizemaxlabel);
        newpanel.add(sizeMax);
        newpanel.add(circularityminlabel);
        newpanel.add(circularityMin);
        newpanel.add(circularitymaxlabel);
        newpanel.add(circularityMax);
        newpanel.add(lowerthresholdlabel);
        newpanel.add(lowerThreshold);
        newpanel.add(upperthresholdlabel);
        newpanel.add(upperThreshold);
        newpanel.add(blackbackgroundlabel);
        newpanel.add(blackBackground);
        newpanel.add(distancelabel);
        newpanel.add(distance);
        newpanel.add(knownlabel);
        newpanel.add(known);
        newpanel.add(pixellabel);
        newpanel.add(pixel);
        newpanel.add(empty);
        newpanel.add(b);


        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {

                profile.setSizeMax(Double.parseDouble(sizeMax.getText()));
                profile.setCircularityMin(Double.parseDouble(circularityMin.getText()));
                profile.setCircularityMax(Double.parseDouble(circularityMax.getText()));
                profile.setLowerThreshold(Double.parseDouble(lowerThreshold.getText()));
                profile.setUpperThreshold(Double.parseDouble(upperThreshold.getText()));
                profile.setBlackBackground(Boolean.parseBoolean(blackBackground.getText()));
                profile.setDistance(Double.parseDouble(distance.getText()));
                profile.setKnown(Double.parseDouble(known.getText()));
                profile.setPixel(Double.parseDouble(pixel.getText()));

                try {
                    writeFile(newSettings);
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }


            }
        });

//        sizeMin.setBounds(160, 40, 200, 30);
//        sizeMax.setBounds(160, 70, 200, 30);
//        circularityMin.setBounds(160, 90, 200, 30);
//        circularityMax.setBounds(160, 120, 200, 30);
//        lowerThreshold.setBounds(160, 150, 200, 30);
//        upperThreshold.setBounds(160, 180, 200, 30);
//        blackBackground.setBounds(160, 210, 200, 30);
//        distance.setBounds(160, 240, 200, 30);
//        known.setBounds(160, 270, 200, 30);
//        pixel.setBounds(160, 300, 200, 30);
//        feretMin.setBounds(160, 330, 200, 30);
//        feretMax.setBounds(160, 360, 200, 30);


        return newpanel;

    }

}

