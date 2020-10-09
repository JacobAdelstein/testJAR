import ij.IJ;
import ij.ImagePlus;
import ij.measure.ResultsTable;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jruby.RubyProcess;
import org.jruby.javasupport.ext.JavaUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.RenderedImage;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SophionMH {

    private static String formatDate(String inputString) {
        //This function takes a string in format MMddyyyy and returns the string in format mm-dd-yyyy
        return inputString.substring(0,2) + '-' + inputString.substring(2,4) + '-' + inputString.substring(4, 8);

    }

    private static String getEncodedDate() {
        //Returns a string representing date encoded MMddyyyy
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMddyyyy");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    private static void newDateFile() {
        //Creates a new file to save batch date and writes current date to it
        try {
            FileWriter writer = new FileWriter("SophionMH-date.txt", false);
            writer.write(getEncodedDate());
            writer.close();
        } catch (IOException e) {
            gui.sysConsole.println("Could not create file", gui.sysConsole.WARNING);
        }
    }

    private static void writeDateFile(String date) {
        //Creates a new file to save batch date and writes current date to it
        try {
            FileWriter writer = new FileWriter("SophionMH-date.txt", false);
            writer.write(date);
            writer.close();
        } catch (IOException e) {
            gui.sysConsole.println("Could not create file", gui.sysConsole.WARNING);
        }
    }

    private static String readLine(String inputFile) throws IOException {
        FileReader reader = new FileReader(inputFile);
        BufferedReader bufferedReader = new BufferedReader(reader);
        return bufferedReader.readLine();
    }

    static public JPanel newDialog(inspectionProfile profile) {

        //Initialize code to read current Sophion batch date
        String batchDate = "";
        try {
            //Try to read current batch data from first line of file
            batchDate = readLine("SophionMH-date.txt");
        } catch (FileNotFoundException ex) {
            //If doesn't exist, create new file with date then try reading again
            newDateFile();
            try {
                batchDate = readLine("SophionMH-date.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (batchDate.length() != 8) {
            //If the batchDate String isn't what we're expecting, create a new file and set batchDate to current date
            gui.sysConsole.println("Error with batch Date, will reset to current date", gui.sysConsole.WARNING);
            batchDate = getEncodedDate();
            newDateFile();
        }



        JPanel returnPanel = new JPanel(new BorderLayout());


        //Create and add batchPanel
        JPanel batchPanel = new JPanel(new BorderLayout());
        JLabel currDate = new JLabel("Batch Date: " + formatDate(batchDate));
        JButton changeButton = new JButton("Change Batch Start Date");
        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame dateChangerFrame = new JFrame("Change Batch Start Date");
                dateChangerFrame.setBounds(gui.screenSize.width/2-350,gui.screenSize.height/2-200,350,110);
                dateChangerFrame.setResizable(false);

                JPanel dateChangerPanel = new JPanel(new BorderLayout());
                dateChangerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5,5,5));

                JLabel descLabel = new JLabel("Enter new batch start date in format mmddyyyy");

                JTextField newDate = new JTextField();


                JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
                JButton save = new JButton("Save");
                JButton cancel = new JButton("Cancel");
                cancel.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dateChangerFrame.dispose();
                    }
                });
                save.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String date = newDate.getText();
                        if (date.length() != 8 || !date.matches("\\d+")) {
                            //Check to ensure the length = 8 and string is all digits
                            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Invalid entry, make sure to include leading zeros for values <10");
                        } else {
                            writeDateFile(date);
                            currDate.setText("Batch Date: " + formatDate(date));

                            dateChangerFrame.dispose();
                        }
                    }
                });
                buttonPanel.add(save);
                buttonPanel.add(cancel);


                dateChangerPanel.add(newDate, BorderLayout.CENTER);
                dateChangerPanel.add(buttonPanel, BorderLayout.PAGE_END);
                dateChangerPanel.add(descLabel, BorderLayout.PAGE_START);
                dateChangerFrame.add(dateChangerPanel);
                dateChangerFrame.setVisible(true);

            }
        });

        batchPanel.add(changeButton, BorderLayout.LINE_END);
        batchPanel.add(currDate, BorderLayout.LINE_START);


        JPanel partNumPanel = new JPanel();
        partNumPanel.setLayout(new BoxLayout(partNumPanel, BoxLayout.LINE_AXIS));
        JLabel partNumLabel = new JLabel("Enter Part Number: ");
        partNumLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        partNumPanel.add(partNumLabel);
        JTextField partNumField = new JTextField();
        partNumField.setAlignmentX(Component.RIGHT_ALIGNMENT);
        partNumField.setMaximumSize(new Dimension(100,20));
        partNumPanel.add(partNumField);
        returnPanel.add(partNumPanel, BorderLayout.CENTER);

        JButton submit = new JButton("Create Measurement");
        String finalBatchDate = batchDate;
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!partNumField.getText().matches("\\d+")) {
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Invalid entry, enter integer numbers");
                } else if (gui.partExists(Integer.parseInt(partNumField.getText()))){
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Part already exists");
                } else {
                    int partNum = Integer.parseInt(partNumField.getText());
                    measurementsCol currentMeasure = new measurementsCol(partNum, finalBatchDate,  profile);
                    gui.storage.add(currentMeasure);
                    gui.tabbedPane.addTab(String.valueOf(partNum), null);
                    gui.tabbedPane.setSelectedIndex(gui.tabbedPane.getTabCount()-1);
                    guiHandler.updateTabbedPane();
                    guiHandler.newTab.dispose();
                }
            }
        });

        returnPanel.add(submit, BorderLayout.PAGE_END);
        returnPanel.add(batchPanel, BorderLayout.PAGE_START);


        return returnPanel;



    }


    public static JPanel getPanel(measurementsCol data) {
        JPanel returnPanel = new JPanel(new BorderLayout());

        returnPanel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                gui.debugChecker(e.getKeyCode());

            }
        });



        JLabel titleLbl = new JLabel("Measurement for part: " + data.partNum);
        GridLayout layout = new GridLayout(5, 1);
        JPanel frontPanel = new JPanel(layout);

//        BoxLayout layout = new BoxLayout(frontPanel, BoxLayout.PAGE_AXIS);
//        layout.
//        frontPanel.setLayout(layout);
        for (int i=1; i<=5; i++ ) {
            if (!data.measureList.get(i-1).hasImage) {
                JPanel noimgPanel = new JPanel();
                noimgPanel.add(getNoImagePanel(data.measureList.get(i-1)));
                noimgPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
                frontPanel.add(noimgPanel);
            } else {
                JPanel imgPanel = new JPanel();
                imgPanel.add(getImagePanel(data.measureList.get(i-1)));
                imgPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
                frontPanel.add(imgPanel);



            }
        }


        JPanel backPanel = new JPanel(layout);
        for (int i=6; i<=10; i++ ) {
            if (!data.measureList.get(i-1).hasImage) {
                JPanel noimgPanel = new JPanel();
                noimgPanel.add(getNoImagePanel(data.measureList.get(i-1)));
                noimgPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
                backPanel.add(noimgPanel);
            } else {

                JPanel imgPanel = new JPanel();
                imgPanel.add(getImagePanel(data.measureList.get(i-1)));
                imgPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
                backPanel.add(imgPanel);

            }
        }


        JButton submit = new JButton("Submit");
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean pass = true;
                System.out.println("Part Number" + String.valueOf(data.partNum));
                for (measurements measure : data.measureList) {
                    if (!measure.pass) {
                        System.out.println(measure.name + " has not passed");
                        pass = false;
                    }
                }

                System.out.println("Save Override: " + gui.saveOverride);

                if (pass || gui.saveOverride) {
                    try {

                        int techID = 0;
                        boolean goodID = false;
                        while (!goodID) {
                            String techString = JOptionPane.showInputDialog("Enter technician ID");
                            if (!techString.matches("\\d+")) {
                                JOptionPane.showMessageDialog(null, "Invalid input. Enter an integer number.");
                            } else if (techString.length() > 10) {
                                JOptionPane.showMessageDialog(null, "u crazy?");
                            } else {
                                goodID = true;
                                techID = Integer.valueOf(techString);
                            }
                        }



                        JSONObject jsonData = new JSONObject();
                        JSONObject jsonResults = new JSONObject();
                        jsonResults.put("pass", true);


                        for (measurements measurement : data.measureList) {

                            JSONArray array = new JSONArray();
                            for (double result : measurement.results) {
                                array.put(result);
                            }
                            System.out.println(array);
                            jsonResults.put(String.valueOf(measurement.position), array);

                        }




                        String submitURL = gui.currentSettings.serverURL + data.currentProfile.submitAddress;
                        System.out.println("Submission URL: " + submitURL);
                        System.out.println("Batch Date: " + data.batchDate);


                        //Initialize HTTP stuff
//                        System.out.println(submitURL);
                        HttpPost submitPost = new HttpPost(submitURL);
                        submitPost.setHeader("Content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");


                        MultipartEntityBuilder entity = MultipartEntityBuilder
                                .create()
                                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                                .addTextBody("partNum", String.valueOf(data.partNum))
                                .addTextBody("tech", String.valueOf(techID))
                                .addTextBody("json", String.valueOf(jsonResults))
                                .addTextBody("batchDate", data.batchDate)
                                .addTextBody("pass", String.valueOf(pass))
                                .setBoundary("----WebKitFormBoundary7MA4YWxkTrZu0gW");




//                        for (measurements measure : data.measureList) {
//                            if (measure.position == measurements.F_TopLeft) {
//                                ByteArrayOutputStream os = new ByteArrayOutputStream();
//                                ImageIO.write((RenderedImage) measure.capture, "jpg", os);
//                                entity.addBinaryBody("img1", os.toByteArray(), ContentType.DEFAULT_BINARY, "img1.jpg");
//                            } else if (measure.position == measurements.F_TopRight) {
//                                img2 = measure.capture;
//                            } else if (measure.position == measurements.F_Center) {
//                                img3 = measure.capture;
//                            } else if (measure.position == measurements.F_BotLeft) {
//                                img4 = measure.capture;
//                            } else if (measure.position == measurements.F_BotRight) {
//                                img5 = measure.capture;
//                            } else if (measure.position == measurements.B_TopLeft) {
//                                img6 = measure.capture;
//                            } else if (measure.position == measurements.B_TopRight) {
//                                img7 = measure.capture;
//                            } else if (measure.position == measurements.B_Center) {
//                                img8 = measure.capture;
//                            } else if (measure.position == measurements.B_BotRight) {
//                                img9 = measure.capture;
//                            } else if (measure.position == measurements.B_BotLeft) {
//                                img10 = measure.capture;
//                            }
//                        }

                        //Iterate through all images and add to entity body
                        for (measurements measure : data.measureList) {
                            ByteArrayOutputStream os = new ByteArrayOutputStream();
                            ImageIO.write((RenderedImage) measure.capture, "jpg", os);
                            entity.addBinaryBody("img" + String.valueOf(measure.position), os.toByteArray(), ContentType.DEFAULT_BINARY, "img" + String.valueOf(measure.position) + ".jpg");
                        }

                        //Build HTTP request
                        HttpEntity request = entity.build();

                        //Just send it
                        submitPost.setEntity(request);
                        if (httpSubmit.HTTPClient(submitPost) == 1) {
                            if (!gui.keepPanel) {
                                gui.tabbedPane.removeTabAt(gui.tabbedPane.indexOfTab(String.valueOf(data.partNum)));
                                for (measurementsCol part: gui.storage) {
                                    if (part.partNum == data.partNum) {
                                        if (gui.storage.remove(part)) {
                                            gui.sysConsole.println("Removed successfully");
                                        }
                                    }
                                }
                            }
                        }



                    } catch (JSONException ex) {
                        JOptionPane.showMessageDialog(null, "JSON Exception");
                    } catch (IOException exception) {
                        JOptionPane.showMessageDialog(null, "IO Exception");

                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Cannot submit to server because not all images have passed inspection");
                }


            }
        });




        returnPanel.add(titleLbl, BorderLayout.PAGE_START);
        returnPanel.add(frontPanel, BorderLayout.LINE_START);
        returnPanel.add(backPanel, BorderLayout.LINE_END);
        returnPanel.add(submit, BorderLayout.PAGE_END);





        return returnPanel;
    }


    private static JPanel getNoImagePanel(measurements currentMeasurement){
        //If no image exists for the measurement, then this method will be called to provide a blank panel
        JPanel returnPanel = new JPanel();
        returnPanel.setLayout(new BoxLayout(returnPanel, BoxLayout.LINE_AXIS));
        returnPanel.add(Box.createHorizontalGlue());
        returnPanel.setPreferredSize(new Dimension(450,100));
        returnPanel.setSize(450, 100);
        returnPanel.setMinimumSize(new Dimension(450, 100));
        JButton acquireBtn = new JButton("Acquire");

        acquireBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.currentCapture[0] = currentMeasurement.partNum;
                gui.currentCapture[1] = currentMeasurement.position;
                gui.camera.startLive();

            }
        });


        JLabel locLabel = new JLabel(currentMeasurement.name + currentMeasurement.position);
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

    public static JPanel getImagePanel(measurements currentMeasurement){
        //Once the image is taken, this panel is called
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
                newFrame.setBounds(500,100,650,750);
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
                guiHandler.updateTabbedPane();

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
                        object = (Double) object;
                        setText(String.valueOf(object));

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
                newFrame.setSize(300, 650);
                newJpanel.add(resultsList);

                JButton remove = new JButton("Remove");
                remove.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        resultsModel.removeElementAt(resultsList.getSelectedIndex());
                    }
                });

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

    public static void analyzeImage(measurementsCol measurement, int position, Image image) {

        System.out.println("Analyzing SOMH");

        //Do Image Analysis
        ImagePlus imp = new ImagePlus("capture", image);
        imp.changes = false;

        IJ.run(imp, "8-bit", "");
        IJ.setThreshold(imp, measurement.currentProfile.lowerThreshold, measurement.currentProfile.upperThreshold);
        IJ.setProperty("BlackBackground", measurement.currentProfile.blackBackground);
        IJ.run(imp, "Convert to Mask", "");

        String scale = ("distance=" + measurement.currentProfile.distance + " known=" + measurement.currentProfile.known + " pixel=" + measurement.currentProfile.pixel + " unit=um");
        String analysis = ("size=" + measurement.currentProfile.sizeMin + "-" + measurement.currentProfile.sizeMax + " circularity=" + measurement.currentProfile.circularityMin + "-" + measurement.currentProfile.circularityMax + " show=[Overlay Masks] clear include in-situ");
        IJ.run(imp, "Set Scale...", scale);
        IJ.run(imp, "Set Measurements...", "feret's");
//        IJ.run(imp, "Analyze Particles...", "size=800-Infinity circularity=0.70-1.00 show=[Overlay Masks] clear include in-situ");
        IJ.run(imp, "Analyze Particles...", analysis);
//                IJ.doCommand(imp, "Analyze Particles...");
        if (gui.showOverlay) {
            //Only show thresholded image if the debug setting set to true
            imp.show();
        }

//                IJ.run("Calculator Plus", "i1=capture i2=original operation=[Add: i2 = (i1+i2) x k1 + k2] k1=1 k2=0 create");
//                IJ.runPlugIn("Calculator Plus", "i1=capture i2=original operation=[Add: i2 = (i1+i2) x k1 + k2] k1=1 k2=0 create");
//                calculate(imp, org, 1, 0);
//                org.show();
//                imp.show();

        Image overlay = imp.getBufferedImage();
        gui.sysConsole.println("SCALE: " + scale);
        gui.sysConsole.println("Analysis: " + analysis);
        ResultsTable rt = ResultsTable.getResultsTable();
        double[] results = rt.getColumnAsDoubles(19);

        try {
            gui.sysConsole.println("--------------RESULTS--------------");
            gui.sysConsole.println("Number of results: " + results.length);
            for (int i = 0; i < results.length; i++) {
                gui.sysConsole.println(results[i]);
            }
            gui.sysConsole.println("--------------END RESULTS--------------");
        } catch (NullPointerException  ex) {
            JOptionPane.showMessageDialog(null, "No holes found");
            results = new double[1];
        }

        for (int i = 0; i < measurement.measureList.size(); i++){
            if (measurement.measureList.get(i).position == gui.currentCapture[1]){
                measurement.measureList.get(i).setImage(gui.camera.capture);
                measurement.measureList.get(i).setResults(results);
                measurement.measureList.get(i).overlay = overlay;
                measurement.measureList.get(i).setPass(checkPass(position, results, measurement.measureList.get(i).profile));


            }
        }



    }


    public static boolean checkPass(int position, double[] results, inspectionProfile profile) {

        System.out.println("Number of results: " + String.valueOf(results.length));
        for (double result : results) {
            System.out.println(result);
        }

        int numPass = 0;

        for (double result: results) {
            if (position < 6) {
                if (result <= profile.enterMax && result >= profile.enterMin) {
                    numPass++;
                }
            } else if (position >= 6) {
                if (result >= profile.exitMin && result <= profile.exitMax) {
                    numPass++;
                }
            }
        }

        if (numPass >= profile.holeCount) {
            return true;
        } else {
            return false;
        }


    }



}





