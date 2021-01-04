import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class settingsframe {
    ArrayList<inspectionProfile> inspectionProfiles;
    ArrayList<settingsPanel> panelList = new ArrayList<>(0);
    String[] profileList;



    public static void main(String args[]) throws ParserConfigurationException, SAXException, IOException {
        System.out.println("settings");
        new settingsframe();


    }



    public settingsframe() throws IOException, SAXException, ParserConfigurationException {
        settings newSettings = new settings(); //Initialize settings object and read values in

        JFrame settingsframe = new JFrame("Settings Panel");
        settingsframe.setSize(650,600);
        settingsframe.setTitle("Settings");
        settingsframe.setLayout(new GridLayout(2,1));
        JPanel generalPanel = new JPanel();

        generalPanel.setLayout(new GridLayout(11,2));

        JTabbedPane tp = new JTabbedPane();
        tp.setBounds(20, 20, 580, 300);
        tp.setBorder(new LineBorder(Color.BLACK, 0));

        tp.add("General", generalPanel);
        settingsEntry URLField = new settingsEntry("Server Address", newSettings.serverURL);
        generalPanel.add(URLField);

        JPanel browsePanel = new JPanel();
        browsePanel.setLayout(new GridLayout(1,2));


        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new GridLayout(1,2));
        JPanel filePanel = new JPanel();
        filePanel.setLayout(new GridLayout(1,2));
        JLabel fileLabel = new JLabel("Offline Save Location");
        JTextField fileField = new JTextField(newSettings.offlineSaveDirectory,1);
        JButton browseButton = new JButton("Browse");

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser(newSettings.offlineSaveDirectory);
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int option = chooser.showOpenDialog(settingsframe);
                if (option == JFileChooser.APPROVE_OPTION) {
                    fileField.setText(chooser.getSelectedFile().getAbsolutePath());
                    System.out.println("New path: " + newSettings.offlineSaveDirectory);
                }
            }
        });


        labelPanel.add(fileLabel);
        browsePanel.add(fileField);
        browsePanel.add(browseButton);


        filePanel.add(labelPanel);
        filePanel.add(browsePanel);
      


        generalPanel.add(filePanel);

        for (inspectionProfile profile : newSettings.inspectionProfiles) {
            System.out.println(profile.profileName);
//            tp.add(profile.profileName, panel(profile, newSettings));

//            tp.add(panel(profile));
            settingsPanel panel = new settingsPanel(profile);
            panelList.add(panel);
            tp.add(profile.profileName, panel);


        }

//        settingsframe.add(savepanel(profile, newSettings));
        JPanel savePanel = new JPanel();
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("URL IS: " + URLField.getText());
                newSettings.serverURL = URLField.getText();
                newSettings.offlineSaveDirectory = fileField.getText();


                for(settingsPanel panel : panelList) {

                    inspectionProfile saveProfile = panel.saveProfile();
                    for (inspectionProfile profile: newSettings.inspectionProfiles) {
                        if (profile.profileName.equals(saveProfile.profileName)) {
                            profile = saveProfile;


                        }
                    }
                }

                try {
                    writeFile(newSettings);
                } catch (ParserConfigurationException parserConfigurationException) {
                    parserConfigurationException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (SAXException saxException) {
                    saxException.printStackTrace();
                } catch (TransformerException transformerException) {
                    transformerException.printStackTrace();
                }
                settingsframe.dispose();
            }
        });

        savePanel.add(saveButton);




//        tp.add("One", settings.panel());
//        tp.add("Two", settings.panel());
//        tp.add("Three", settings.panel());

        settingsframe.add(tp);
        settingsframe.add(savePanel);


        settingsframe.setVisible(true);



    }
















    public void writeFile(settings newSettings) throws ParserConfigurationException, IOException, SAXException, TransformerException {





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
                                    globalSettingsNL.item(k).setTextContent(newSettings.serverURL);
                                }

                                if (globalSettingsNL.item(k).getNodeName().equals("offlineSaveDirectory")) {
                                    globalSettingsNL.item(k).setTextContent(newSettings.offlineSaveDirectory);
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
                                for (inspectionProfile profile: newSettings.inspectionProfiles) {
                                    if (profile.profileName.equalsIgnoreCase(inspectionProfileNS.item(k).getNodeName())) {
                                        System.out.println("Writing: " + profile.profileName);
                                        Element currElement = (Element) inspectionProfileNS.item(k);
                                        NodeList IPSettingsNS = inspectionProfileNS.item(k).getChildNodes();
                                        //IPSettingsNS = Inspection Profile Settings NodeList
                                        for (int h = 0; h < IPSettingsNS.getLength(); h++) {
                                            if (IPSettingsNS.item(h).getNodeType() == Node.ELEMENT_NODE) {
                                                //Here's where we'll read each individual setting element
                                                if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("lowerthreshold")) {
                                                    IPSettingsNS.item(h).setTextContent(String.valueOf(profile.getLowerThreshold()));
                                                    System.out.println("Writing: lt");

                                                }
                                                if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("upperthreshold")) {
                                                    IPSettingsNS.item(h).setTextContent(String.valueOf(profile.upperThreshold));
                                                }
                                                if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("sizemin")) {
                                                    IPSettingsNS.item(h).setTextContent(String.valueOf(profile.sizeMin));
                                                }
                                                if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("sizemax")) {
                                                    IPSettingsNS.item(h).setTextContent(String.valueOf(profile.sizeMax));
                                                }
                                                if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("circularitymin")) {
                                                    IPSettingsNS.item(h).setTextContent(String.valueOf(profile.circularityMin));
                                                }
                                                if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("circularitymax")) {
                                                    IPSettingsNS.item(h).setTextContent(String.valueOf(profile.circularityMax));
                                                }
                                                if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("distance")) {
                                                    IPSettingsNS.item(h).setTextContent(String.valueOf(profile.distance));
                                                }
                                                if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("known")){
                                                    IPSettingsNS.item(h).setTextContent(String.valueOf(profile.known));
                                                }
                                                if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("pixel")) {
                                                    IPSettingsNS.item(h).setTextContent(String.valueOf(profile.pixel));
                                                }



                                                if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("blackbackground")) {
                                                    if (profile.blackBackground) {
                                                        IPSettingsNS.item(h).setTextContent("true");
                                                    } else {
                                                        IPSettingsNS.item(h).setTextContent("false");

                                                    }
                                                }
                                                if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("holecount")) {
                                                    IPSettingsNS.item(h).setTextContent(String.valueOf(profile.holeCount));
                                                }

                                                if (IPSettingsNS.item(h).getNodeName().equalsIgnoreCase("submitaddress")) {
                                                    IPSettingsNS.item(h).setTextContent(String.valueOf(profile.submitAddress));
                                                }
//
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            //Otherwise throw an exception
        }


        //Write the file
        TransformerFactory trf = TransformerFactory.newInstance();
        Transformer transformer = trf.newTransformer();
        StreamResult result = new StreamResult(new FileOutputStream( "settings.xml" ));
        DOMSource source = new DOMSource(document);
        transformer.transform(source, result);


    }



}
