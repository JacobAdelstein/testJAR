import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class guiHandler {



    public static JFrame newTab;

    static public void getLastSubmit() {
        JFrame getSubmit = new JFrame("Get latest submission from server: " + gui.currentSettings.serverURL);
        JPanel mainPanel = new JPanel();
        JComboBox profileBox = new JComboBox(gui.currentSettings.profileList);
        profileBox.setEditable(false);


        JButton goButt = new JButton("Go");
        goButt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getSubmit.dispose();
                System.out.println("Getting results for " + gui.currentSettings.inspectionProfiles.get(profileBox.getSelectedIndex()).profileName);

                CloseableHttpClient client = HttpClients.createDefault();
                String submitURL = gui.currentSettings.serverURL + gui.currentSettings.inspectionProfiles.get(profileBox.getSelectedIndex()).submitAddress;
//                HttpGet submitGet = new HttpGet(submitURL);
                HttpGet submitGet = new HttpGet("http://192.168.10.111:8000/jsonsubmit");

                CloseableHttpResponse response = null;
                try {
                    response = client.execute(submitGet);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }

                System.out.println(response);
                String responseEntity = null;

                try {
                    responseEntity = EntityUtils.toString(response.getEntity());
                    gui.sysConsole.println(responseEntity);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }

                JSONObject obj;
                try {
                    obj = new JSONObject(responseEntity);

                    System.out.println("OBJ " + obj);




                    String[] columnNames = {"Part Number", "Batch Date", "Pass", "Technician", "Submission Date"};
                    JFrame resultsFrame = new JFrame("Showing last 10 submissions for " + gui.currentSettings.inspectionProfiles.get(profileBox.getSelectedIndex()).profileName);
                    JPanel mainPanel = new JPanel();
                    JTable resultsTable = new JTable(11, 5);

                    resultsTable.setValueAt(columnNames[0], 0, 0);
                    resultsTable.setValueAt(columnNames[1], 0, 1);
                    resultsTable.setValueAt(columnNames[2], 0, 2);
                    resultsTable.setValueAt(columnNames[3], 0, 3);
                    resultsTable.setValueAt(columnNames[4], 0, 4);


                    for (int d = 1; d <= 10; d++) {
                        JSONObject subObj = obj.getJSONObject(String.valueOf(d));

                        if (subObj.getBoolean("deleted")) {
                            resultsTable.setValueAt("DELETED", d, 0);
                            resultsTable.setValueAt("DELETED", d, 1);
                            resultsTable.setValueAt("DELETED", d, 2);
                            resultsTable.setValueAt("DELETED", d, 3);
                            resultsTable.setValueAt("DELETED", d, 4);
                        } else {
                            resultsTable.setValueAt(subObj.get("partNum"), d, 0);
                            resultsTable.setValueAt(subObj.get("batchDate"), d, 1);
                            resultsTable.setValueAt(subObj.get("pass"), d, 2);
                            resultsTable.setValueAt(subObj.get("tech"), d, 3);
                            resultsTable.setValueAt(subObj.get("submissionDate"), d, 4);

                        }


                    }

                    resultsTable.setDefaultEditor(Object.class, null);
                    mainPanel.add(resultsTable);
                    resultsFrame.add(mainPanel);
                    resultsFrame.setSize(500, 250);
                    resultsFrame.setVisible(true);


                } catch (JSONException exJSON) {
                    JOptionPane.showMessageDialog(null, "JSON Exception occurred");

                }
            }
        });
        mainPanel.add(profileBox);
        mainPanel.add(goButt);
        getSubmit.add(mainPanel);
        getSubmit.setSize(500, 80);
        getSubmit.setVisible(true);
    }

    static public void addTab() {

        newTab = new JFrame("New Measurement");
        JPanel mainPanel = new JPanel();
        JPanel cardPanel = new JPanel(new CardLayout());


        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.add(Box.createVerticalGlue());
        JComboBox profileBox = new JComboBox(gui.currentSettings.profileList);
        profileBox.setEditable(false);
        profileBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Item state Changed");
                CardLayout cl = (CardLayout) cardPanel.getLayout();
                cl.show(cardPanel, profileBox.getSelectedItem().toString());
            }
        });
        mainPanel.add(profileBox);

        for (int i = 0; i < gui.currentSettings.inspectionProfiles.size(); i++) {
            if (gui.currentSettings.inspectionProfiles.get(i).profileType.equalsIgnoreCase("SophionMH")) {
                System.out.println("Setting profile type SophionMH");
                cardPanel.add(SophionMH.newDialog(gui.currentSettings.inspectionProfiles.get(i)), gui.currentSettings.inspectionProfiles.get(i).profileName);
            } else if (gui.currentSettings.inspectionProfiles.get(i).profileType.equalsIgnoreCase("test")) {
                System.out.println("Setting profile type test");
                JPanel testPanel = new JPanel();
                JLabel testLabel = new JLabel("This is a test Label for testPanel");
                testPanel.add(testLabel);
                cardPanel.add(testPanel, gui.currentSettings.inspectionProfiles.get(i).profileName);

            } else {
                System.out.println("Unable to locate inspection view for profile type: " + gui.currentSettings.inspectionProfiles.get(i).profileType);

            }
        }


//        JPanel partNumPanel = new JPanel();
//        partNumPanel.setLayout(new BoxLayout(partNumPanel, BoxLayout.LINE_AXIS));
//        JLabel partNumLabel = new JLabel("Enter Part Number: ");
//        partNumLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
//        partNumPanel.add(partNumLabel);
//        JTextField partNumField = new JTextField();
//        partNumField.setAlignmentX(Component.RIGHT_ALIGNMENT);
//        partNumField.setMaximumSize(new Dimension(100,20));
//        partNumPanel.add(partNumField);
//        mainPanel.add(partNumPanel);
//        mainPanel.add(Box.createVerticalGlue());
//        JPanel profileSelectorPanel = new JPanel();
//        profileSelectorPanel.setLayout(new BoxLayout(profileSelectorPanel, BoxLayout.LINE_AXIS));
//        JLabel profileLabel = new JLabel("Select a inspection profile: ");
//        profileSelectorPanel.add(profileLabel);
//        profileBox.setMaximumSize(new Dimension(150, 20));
//        profileBox.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
////                    int partNum = Integer.parseInt(partNumField.getText());
////                    if (partExists(partNum)) {
////                        JOptionPane.showMessageDialog(mainPanel, "Measurement already exists for part number " + String.valueOf(partNum));                    } else {
////                        measurementsCol currentMeasure = new measurementsCol(partNum, currentSettings.inspectionProfiles.get(profileBox.getSelectedIndex()));
////                        storage.add(currentMeasure);
////                        tabbedPane.addTab(String.valueOf(partNum), null);
////                        guiHandler.updateTabbedPane();
////                        newTab.setVisible(false);
//
//            }
//        });
//        profileSelectorPanel.add(profileBox);
//        mainPanel.add(profileSelectorPanel);
//        mainPanel.add(Box.createVerticalGlue());
//
//
//
//
//        JButton submit = new JButton("Create Measurement");
//
//        mainPanel.add(submit);
//        mainPanel.add(Box.createVerticalGlue());

        newTab.setBounds(gui.screenSize.width/2-250,gui.screenSize.height/2-100,330,160);
        newTab.add(mainPanel, BorderLayout.PAGE_START);
        newTab.add(cardPanel, BorderLayout.CENTER);
        newTab.setVisible(true);
//        newTab.getRootPane().setDefaultButton(submit);

    }











    public static JPanel getResultsPanel(measurements currentMeasurement) {

        JPanel returnPanel = new JPanel();
        returnPanel.setLayout(new BoxLayout(returnPanel, BoxLayout.PAGE_AXIS));
        returnPanel.setSize(100,60);
        returnPanel.setMaximumSize(new Dimension(100, 60));
        returnPanel.setMinimumSize(new Dimension(100, 60));

        if (currentMeasurement.pass) {
            JLabel passLabel = new JLabel("Pass", SwingConstants.CENTER);
            returnPanel.add(passLabel);
            returnPanel.setBackground(new Color(72, 245, 76));
        } else {
            JLabel fail = new JLabel("FAIL", SwingConstants.CENTER);
            returnPanel.add(fail);
            returnPanel.setBackground(new Color(245, 66, 66));
        }

        returnPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFrame newFrame = new JFrame("Editing results for " + currentMeasurement.name);
                JPanel newJpanel = new JPanel();
                newJpanel.setLayout(new BoxLayout(newJpanel, BoxLayout.PAGE_AXIS));


                DefaultListModel<Double> resultsModel = new DefaultListModel<>();
                for (int i = 0; i < currentMeasurement.results.length; i++) {
                    resultsModel.addElement(currentMeasurement.results[i]);
                }
                JList resultsList = new JList(resultsModel);
                resultsList.setCellRenderer(new DefaultListCellRenderer() {
                    @Override
                    public Component getListCellRendererComponent(JList list, Object object, int index, boolean isSelected, boolean cellHasFocus) {
                        object = (result) object;
                        setText(String.valueOf(((result) object).getResult()));

                        if (isSelected) {
                            setBackground(list.getSelectionBackground());
                            setForeground(list.getSelectionForeground());
                        } else {
                            setBackground(list.getBackground());
                            setForeground(list.getForeground());
                        }

                        return this;
                    }
                });
                newFrame.setSize(200, 650);
                newFrame.setBackground(Color.black);
                newJpanel.add(resultsList);

                JButton remove = new JButton("Remove");
                remove.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        resultsModel.removeElementAt(resultsList.getSelectedIndex());
                    }
                });
                newJpanel.setBackground(Color.white);
                newJpanel.add(remove);
                newFrame.add(newJpanel);
                newFrame.setVisible(true);


            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });


        return returnPanel;

    }


    public static JPanel getImagePanel(measurements currentMeasurement){
        //Get Scaled ImageIcon
        ImageIcon imageIcon = new ImageIcon(currentMeasurement.capture.getScaledInstance(80,45,Image.SCALE_SMOOTH));
        JPanel returnPanel = new JPanel();
        JPanel imgPanel = new JPanel();
        imgPanel.setMaximumSize(new Dimension(80,45));
        imgPanel.setLayout(new BorderLayout(10, 20));
        JLabel image = new JLabel();

        image.setIcon(imageIcon);
        image.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                JFrame newFrame = new JFrame("Showing " + currentMeasurement.name);
                JPanel newJpanel = new JPanel();
                JLabel label = new JLabel();
                label.setIcon(new ImageIcon(currentMeasurement.capture.getScaledInstance(640,360,Image.SCALE_SMOOTH)));
                newJpanel.add(label);
                JLabel overlayLabel = new JLabel();
                overlayLabel.setIcon(new ImageIcon(currentMeasurement.overlay.getScaledInstance(640,360, Image.SCALE_SMOOTH)));
                newJpanel.add(overlayLabel);
                newFrame.add(newJpanel);
                newFrame.setPreferredSize(new Dimension(640, 700));
                newFrame.setBounds(500,100,640,700);
                newFrame.setVisible(true);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        imgPanel.add(image, BorderLayout.LINE_START);

        returnPanel.setLayout(new GridLayout(1, 4));
        returnPanel.setPreferredSize(new Dimension(450,100));
        JButton removeBtn = new JButton("Remove");


        removeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentMeasurement.removeImage();
                updateTabbedPane();

            }
        });
        JLabel locLabel = new JLabel(currentMeasurement.name, SwingConstants.CENTER);
        returnPanel.add(locLabel);
        returnPanel.add(imgPanel);
        returnPanel.add(getResultsPanel(currentMeasurement));
        returnPanel.add(removeBtn);
        locLabel.setHorizontalAlignment(SwingConstants.CENTER);
        locLabel.setVerticalAlignment(SwingConstants.CENTER);
        returnPanel.setBorder(BorderFactory.createBevelBorder(EtchedBorder.RAISED));

        return returnPanel;
    }

    public static JPanel getNoImagePanel(measurements currentMeasurement){
        JPanel returnPanel = new JPanel();
        returnPanel.setLayout(new BoxLayout(returnPanel, BoxLayout.LINE_AXIS));
        returnPanel.add(Box.createHorizontalGlue());
        returnPanel.setPreferredSize(new Dimension(450,100));
        JButton acquireBtn = new JButton("Acquire");

        acquireBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.currentCapture[0] = currentMeasurement.partNum;
                gui.currentCapture[1] = currentMeasurement.position;
                gui.camera.startLive();

            }
        });


        JLabel locLabel = new JLabel(currentMeasurement.name);
        returnPanel.add(locLabel);
        returnPanel.add(Box.createHorizontalGlue());
        returnPanel.add(Box.createHorizontalGlue());
        returnPanel.add(Box.createHorizontalGlue());
        returnPanel.add(Box.createHorizontalGlue());
        returnPanel.add(Box.createHorizontalGlue());
        returnPanel.add(Box.createHorizontalGlue());
        //i know this sum bs code righ here
        returnPanel.add(acquireBtn);
        returnPanel.add(Box.createHorizontalGlue());
        locLabel.setHorizontalAlignment(SwingConstants.CENTER);
        returnPanel.setBorder(BorderFactory.createBevelBorder(EtchedBorder.RAISED));
        return returnPanel;
    }

    public static void updateTabbedPane(){
        //This function iterates through all tabbedPanes and updates them with latest data.
        //Call this function after data has changed to update the view.



        //Iterate through tabbedPanes

        int i = gui.tabbedPane.getSelectedIndex();

        int tabNum = Integer.parseInt(gui.tabbedPane.getTitleAt(i));
        //Get the partNum in the title
        gui.sysConsole.println("Updating panel at " + tabNum);

//            JPanel mainPanel = new JPanel();
//            mainPanel.setLayout(new FlowLayout());
//            mainPanel.setPreferredSize(new Dimension(500, 1000));
////            mainPanel.setSize(1000,1100);
//            JScrollPane scrollPane = new JScrollPane(mainPanel);
//            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
//            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
//            scrollPane.setPreferredSize(new Dimension(500,500));
//            for(int b = 0; b < gui.storage.size(); b++) {
//                //Iterate through the storage arrayList
//                if (tabNum == gui.storage.get(b).partNum) {
//                    gui.sysConsole.println(gui.storage.get(b).toString());
//                    for (int a = 0; a < gui.storage.get(b).measureList.size(); a++) {
//                        if (gui.storage.get(b).measureList.get(a).hasImage == true) {
//                            mainPanel.add(getImagePanel(gui.storage.get(b).measureList.get(a)));
//                        } else {
//                            mainPanel.add(getNoImagePanel(gui.storage.get(b).measureList.get(a)));
//                        }
//                    }
//
//                    JButton submit = new JButton("Submit");
//                    int finalB = b;
//                    submit.addActionListener(new ActionListener() {
//                        @Override
//                        public void actionPerformed(ActionEvent e) {
//                            try {
//                                try {
//                                    int techID = Integer.parseInt(JOptionPane.showInputDialog("Enter Technician ID"));
//                                    httpSubmit.testSubmit(gui.storage.get(finalB).partNum, techID);
//                                } catch (NumberFormatException ex) {
//                                    JOptionPane.showMessageDialog(null, "Technician ID must be a number");
//                                } catch (JSONException ex) {
//                                    JOptionPane.showMessageDialog(null, "Received a JSON Exception");
//                                }
//                            } catch (IOException ioException) {
//                                ioException.printStackTrace();
//                            }
//                        }
//                    });
//                    mainPanel.add(submit);
//                }
//            }

        for(int b = 0; b < gui.storage.size(); b++) {
            //Iterate through the storage arrayList
            if (tabNum == gui.storage.get(b).partNum) {
                System.out.print(gui.storage.get(b).currentProfile.profileType);
                if (gui.storage.get(b).currentProfile.profileType.equalsIgnoreCase("SophionMH")) {
                    gui.tabbedPane.setComponentAt(i, SophionMH.getPanel(gui.storage.get(b)));
                }
            }

        }

//            gui.tabbedPane.setComponentAt(i, scrollPane);
    }

    public static JMenuBar getMenu(boolean debug){

        //Setup menu variables
        JMenu menu, subMenu;
        JMenuItem menuItem;
        JMenuBar menuBar;

        //Setup the main menu
        menuBar = new JMenuBar();
        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menuItem = new JMenuItem("New", KeyEvent.VK_N);






        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addTab();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a Integer number");
                }
            }
        });


        menu.add(menuItem);
        menu.addSeparator();

        menuItem = new JMenuItem("Settings", KeyEvent.VK_N);


        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    settingsframe settings = new settingsframe();
                } catch (NumberFormatException ex) {

                } catch (SAXException saxException) {
                    saxException.printStackTrace();
                } catch (ParserConfigurationException parserConfigurationException) {
                    parserConfigurationException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }


            }
        });

        menu.add(menuItem);
        menu.addSeparator();

        menuItem = new JMenuItem("Take Screenshot", KeyEvent.VK_N);


        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                gui.camera.takePic();






            }
        });



        menu.add(menuItem);
        menu.addSeparator();



        menuItem = new JMenuItem("Exit", KeyEvent.VK_X);
        menuItem.addActionListener(new gui.CloseListener());
        menu.add(menuItem);
        menuBar.add(menu);




        menu = new JMenu("Action");
        menuItem = new JMenuItem("Force Update Panels");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiHandler.updateTabbedPane();
            }
        });
        menu.add(menuItem);

        menuItem = new JMenuItem("Get last submission");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Send a get request for JSON data
                getLastSubmit();

            }
        });
        menu.add(menuItem);
        menuBar.add(menu);




        menu = new JMenu("Option");
        JCheckBoxMenuItem offlineCheckBox = new JCheckBoxMenuItem("Offline Save?");
        offlineCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (offlineCheckBox.getState()) {
                    gui.sysConsole.println("Offline Mode Enabled");
                    gui.offlineSave = offlineCheckBox.getState();

                } else {
                    gui.sysConsole.println("Offline Mode Disabled");
                    gui.offlineSave = offlineCheckBox.getState();

                }

            }
        });
        menu.add(offlineCheckBox);
        menuBar.add(menu);



        if (debug) {
            gui.sysConsole.println("Adding debug");
            menu = new JMenu("Debug");

            JCheckBoxMenuItem console = new JCheckBoxMenuItem("Show Debug Console");
            console.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (console.getState() == true) {
                        gui.sysConsole.showConsole();
                    } else {
                        gui.sysConsole.hideConsole();
                    }
                }
            });
            menu.add(console);

            JCheckBoxMenuItem overlay = new JCheckBoxMenuItem("Show Overlays");
            overlay.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (overlay.getState()) {
                        gui.showOverlay = true;
                    } else {
                        gui.showOverlay = false;
                    }
                }
            });
            menu.add(overlay);

            JCheckBoxMenuItem keepPanel = new JCheckBoxMenuItem("Keep panel after submit");
            keepPanel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (keepPanel.getState()) {
                        gui.keepPanel = true;
                    } else {
                        gui.keepPanel = false;
                    }
                }
            });
            menu.add(keepPanel);


            JCheckBoxMenuItem saveOverride = new JCheckBoxMenuItem("Override save");
            saveOverride.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (saveOverride.getState()) {
                        gui.saveOverride = true;
                    } else {
                        gui.saveOverride = false;
                    }
                }
            });
            menu.add(saveOverride);





            menuBar.add(menu);
            SwingUtilities.updateComponentTreeUI(gui.main);
        }
        return menuBar;
    }
}
