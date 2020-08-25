import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
                            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Invalid entry");
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
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!partNumField.getText().matches("\\d+")) {
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Invalid entry, enter integer numbers");
                } else if (gui.partExists(Integer.parseInt(partNumField.getText()))){
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Part already exists");
                } else {
                    int partNum = Integer.parseInt(partNumField.getText());
                    measurementsCol currentMeasure = new measurementsCol(partNum, profile);
                    gui.storage.add(currentMeasure);
                    gui.tabbedPane.addTab(String.valueOf(partNum), null);
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
        JPanel returnPanel = new JPanel();
        JLabel lbl = new JLabel(String.valueOf(data.partNum));
        returnPanel.add(lbl);
        return returnPanel;
    }



}
