
import com.github.sarxos.webcam.Webcam;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.ds.civil.LtiCivilDriver;
import ij.IJ;
import ij.ImagePlus;
import ij.measure.ResultsTable;
import org.netlib.util.Second;
import script.imglib.color.Green;


class settings {


    String windowTitle;
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


}

class helper extends JFrame{

    private static Object BufferedReader;

    public static void main(String args[]) throws IOException {

        settings currentSettings = new settings();


        try {
            String line;
            FileReader file = new FileReader("config.txt");
            BufferedReader reader = new BufferedReader((file));

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("[WindowTitle]")) {
                    currentSettings.windowTitle = line.substring(line.indexOf("=") + 1);
                }
                if (line.startsWith("[sizeMin]")) {
                    currentSettings.sizeMin = line.substring(line.indexOf("=") + 1);
                }
                if (line.startsWith("[sizeMax]")) {
                    currentSettings.sizeMax = line.substring(line.indexOf("=") + 1);
                }
                if (line.startsWith("[circularityMin]")) {
                    currentSettings.circularityMin = line.substring(line.indexOf("=") + 1);
                }
                if (line.startsWith("[circularityMax]")) {
                    currentSettings.circularityMax = line.substring(line.indexOf("=") + 1);
                }
                if (line.startsWith("[lowerThreshold]")) {
                    currentSettings.lowerThreshold = new Double(line.substring(line.indexOf("=") + 1));
                }
                if (line.startsWith("[upperThreshold]")) {
                    currentSettings.upperThreshold = new Double(line.substring(line.indexOf("=") + 1));
                }
                if (line.startsWith("[feretMax]")) {
                    currentSettings.feretMax = new Double(line.substring(line.indexOf("=") + 1));
                }
                if (line.startsWith("[feretMin]")) {
                    currentSettings.feretMin = new Double(line.substring(line.indexOf("=") + 1));
                }
                if (line.startsWith("[distance]")) {
                    currentSettings.distance = line.substring(line.indexOf("=") + 1);
                }
                if (line.startsWith("[known]")) {
                    currentSettings.known = line.substring(line.indexOf("=") + 1);
                }
                if (line.startsWith("[pixel]")) {
                    currentSettings.pixel = line.substring(line.indexOf("=") + 1);
                }
                if (line.startsWith("[blackBackground]")) {
                    if (line.substring(line.indexOf("=") + 1).trim().equalsIgnoreCase("TRUE")) {
                        currentSettings.blackBackground = true;
                    } else {
                        currentSettings.blackBackground = false;
                    }


                    }
            }
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

            String line;
        JFrame frame = new JFrame(currentSettings.windowTitle);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 100);
        JButton button = new JButton("Acquire Image");

        Webcam.setDriver(new LtiCivilDriver());
        System.out.println("Driver set");
        Webcam webcam = Webcam.getDefault();
        System.out.println("Webcam Acquired: " + webcam.getName());
//        webcam.setViewSize(new Dimension(1920, 1080));
        System.out.println("Resolution Set");
        webcam.open();

        WebcamPanel panel = new WebcamPanel(webcam);
        panel.setFPSDisplayed(true);
        panel.setDisplayDebugInfo(true);
        panel.setImageSizeDisplayed(true);
        panel.setMirrored(false);


        JFrame window = new JFrame("Live View");
        window.add(panel);
        window.setResizable(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setVisible(true);
        window.setSize(1360, 768);


        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //delegate to event handler method

                try {
                    acquireAction(e, webcam, currentSettings, panel);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }


            }
        });

        frame.getContentPane().add(button); // Adds Button to content pane of frame
        frame.setLocation(1000, 100);
        frame.setVisible(true);
    }





    public static void SecondFrame ( settings currentSettings) throws IOException {
        String line;


        //assigning values

        // ____________________________________________________________________________________________________________________________________________________//

        FileReader file = new FileReader("config.txt");
        BufferedReader = new BufferedReader((file));
        //Setting up JFrame values
        JFrame f = new JFrame("JFrame with a JPanel");

        JLabel L = new JLabel("The Limit Exceeds the FeretMax!");   // Make a JLabel;
        L.setForeground(Color.red);
        L.setFont(new Font("Serif", Font.PLAIN, 20));
        JLabel L2 = new JLabel("The Limit is below the FeretMin!");   // Make a JLabel;
        L2.setForeground(Color.red);
        JPanel P = new JPanel();// Make a JPanel;
        f.setSize(2000, 1000);
        f.setLayout(null);
        P.setBounds(100, 30, 400, 40);
        // Add L to JPanel P




        f.getContentPane().add(P);  // Add P to JFrame f

        //Set the JFrame F to visible




//JPanel for values in green & red

        StringBuilder scale = new StringBuilder();
        scale.append("distance=" + currentSettings.distance + " known=" + currentSettings.known + " pixel=" + currentSettings.pixel + " unit=um");

        StringBuilder analysis = new StringBuilder();
        analysis.append("size=" + currentSettings.sizeMin + "-" + currentSettings.sizeMax + " circularity=" + currentSettings.circularityMin + "-" + currentSettings.circularityMax + " show=[Overlay Masks] clear include in-situ");


        JPanel Passfail = new JPanel();
        f.getContentPane().add(Passfail);  // Add Passfail to JFrame f
        Passfail.setBounds(20, 80, 800, 500 );

        //Print out settings
        JPanel programsettings = new JPanel();
        f.getContentPane().add(programsettings); // Add Settings to JFrame f
        programsettings.setBounds(100, 700, 1000, 200);

        JLabel SettingsList = new JLabel("LTH:" + " " + currentSettings.lowerThreshold + " "+ " "+ "UTH:" + " "+  currentSettings.upperThreshold + " "+ " "+ "BB:" + " "+  currentSettings.blackBackground + " "+ " "+ "Scale:" + " "+  scale + " "+ " "+ "Analysis:" + " "+  analysis );
        SettingsList.setForeground(Color.black);
        programsettings.add(SettingsList);
        f.setVisible(true);
        //Make button to bring up a new window



        JButton result = new JButton("Save");

        JFrame secondFrame = new JFrame("JFrame with a JPanel");
        result.setBounds(169, 750, 193, 77);
        f.getContentPane().add(result);
        result.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                secondFrame.setVisible(true);
                secondFrame.setSize(2000, 1000);
            }
        });



        //____________________________________________________________________________________________________________________________________________________//
        //This is to iterate through each method and display red or green if the value is passing


            }



    //____________________________________________________________________________________________________________________________________________________//














    public static void acquireAction(ActionEvent e, Webcam webcam, settings currentSettings, WebcamPanel panel) throws IOException  {
        //Do something







//        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "shit");


//        JFrame frame = new JFrame("RAW IMAGE");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(1360, 768);
//
//        frame.add(new JLabel(new ImageIcon(getScaledImage(capture, 1360, 768))));


//run("Set Scale...", "distance=3.1746 known=1 pixel=1 unit=µm");


        IJ.setProperty("BlackBackground", currentSettings.blackBackground);


        StringBuilder scale = new StringBuilder();
        scale.append("distance=" + currentSettings.distance + " known=" + currentSettings.known + " pixel=" + currentSettings.pixel + " unit=um");

        StringBuilder analysis = new StringBuilder();
        analysis.append("size=" + currentSettings.sizeMin + "-" + currentSettings.sizeMax + " circularity=" + currentSettings.circularityMin + "-" + currentSettings.circularityMax + " show=[Overlay Masks] clear include in-situ");

        System.out.println("ScaleSTR= " + scale.toString());
        System.out.println("AnalysisSTR= " + analysis.toString());
        System.out.println("LTH " + currentSettings.lowerThreshold);
        System.out.println("UTH " + currentSettings.upperThreshold);
        System.out.println("BB " + currentSettings.blackBackground);

//        IJ.run(imp, "Analyze Particles...", "size=800-Infinity circularity=0.70-1.00 show=[Overlay Masks] clear include in-situ");




//        rt.reset();
//        rt = ResultsTable.getResultsTable();


//        System.out.println(rt.getColumnIndex("Feret"));
//
//        String[] headings = rt.getHeadings();
//        for(String head: headings) {
//            System.out.println(head);
//        }
        StringBuilder message = new StringBuilder();
        message.delete(0, message.length());
        message.append("Results: \n");





        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), message);
        panel.resume();
        SecondFrame(currentSettings);



    }


        }






