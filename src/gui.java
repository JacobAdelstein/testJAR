import com.github.sarxos.webcam.WebcamLockException;
import com.itextpdf.text.Jpeg;
import org.xml.sax.SAXException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import javax.swing.border.EmptyBorder;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;

import static org.python.modules.time.Time.sleep;


public class gui {

    private static int passvariable;
    static JFrame resultspanel = new JFrame("Results Panel");
    static JPanel Passfail = new JPanel();
    static JLabel Below = new JLabel("The Limit is below the FeretMin!");
    public static settings currentSettings;
    static int debugCount = 0;
    static boolean debugMode = false;


    static JFrame main = new JFrame("Potomac Inspect");
    public static measurementsCol submission;
    static cameraControl camera;
//    static Image latestImage;
    static Integer[] currentCapture = new Integer[2];
    static ArrayList<measurementsCol> storage = new ArrayList<measurementsCol>();
    static JTabbedPane tabbedPane;


    //currentCapture array is intended to indicate which image we're currently capturing
        //[0] = partNum
        //[1] = imgPos

    private static class CloseListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            //Exit the program
            System.exit(0);
        }
    }

    public static class AcquireListener implements ActionListener {


        @Override
        public void actionPerformed(ActionEvent e) {
            //This method is called when the Acquire button is clicked
        }
    }

    static public void debugChecker(int keyPressed) {
        int[] code = {38, 38, 40, 40, 37, 39, 37, 39, 66, 10};
        if (keyPressed == code[debugCount]) {
            debugCount++;
            if (debugCount == code.length) {
                System.out.println("DEBUG MODE ENABLED");
                debugMode = true;
                JOptionPane.showMessageDialog(null, "Debug mode enabled");
                debugCount = 0;



            }
        } else {
            debugCount = 0;
        }
    }




    static boolean partExists(Integer partNum) {
        for (int i = 0; i < gui.storage.size(); i++) {
            if (gui.storage.get(i).partNum == partNum) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    static void addTab(JTabbedPane tabbedPane) {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame newTab = new JFrame("New Measurement");
        JPanel mainPanel = new JPanel();


        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.add(Box.createVerticalGlue());
        JPanel partNumPanel = new JPanel();
        partNumPanel.setLayout(new BoxLayout(partNumPanel, BoxLayout.LINE_AXIS));
        JLabel partNumLabel = new JLabel("Enter Part Number: ");
        partNumLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        partNumPanel.add(partNumLabel);
        JTextField partNumField = new JTextField();
        partNumField.setAlignmentX(Component.RIGHT_ALIGNMENT);
        partNumField.setMaximumSize(new Dimension(100,20));
        partNumPanel.add(partNumField);
        mainPanel.add(partNumPanel);
        mainPanel.add(Box.createVerticalGlue());
        JPanel profileSelectorPanel = new JPanel();
        profileSelectorPanel.setLayout(new BoxLayout(profileSelectorPanel, BoxLayout.LINE_AXIS));
        JLabel profileLabel = new JLabel("Select a inspection profile: ");
        profileSelectorPanel.add(profileLabel);
        JComboBox profileBox = new JComboBox(gui.currentSettings.profileList);
        profileBox.setMaximumSize(new Dimension(150, 20));
        profileSelectorPanel.add(profileBox);
        mainPanel.add(profileSelectorPanel);
        mainPanel.add(Box.createVerticalGlue());


        JButton submit = new JButton("Create Measurement");
        submit.setAlignmentX(Component.CENTER_ALIGNMENT);
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int partNum = Integer.parseInt(partNumField.getText());
                    if (partExists(partNum)) {
                        JOptionPane.showMessageDialog(mainPanel, "Measurement already exists for part number " + String.valueOf(partNum));                    } else {
                        measurementsCol currentMeasure = new measurementsCol(partNum, currentSettings.inspectionProfiles.get(profileBox.getSelectedIndex()));
                        storage.add(currentMeasure);
                        tabbedPane.addTab(String.valueOf(partNum), null);
                        guiHandler.updateTabbedPane();
                        newTab.setVisible(false);
                    }
                } catch (NumberFormatException exception) {
                    JOptionPane.showMessageDialog(mainPanel, "Please enter an integer number");
                }
            }
        });
        mainPanel.add(submit);
        mainPanel.add(Box.createVerticalGlue());

        newTab.setBounds(screenSize.width/2-250,screenSize.height/2-100,500,200);
        newTab.add(mainPanel);
        newTab.setVisible(true);
        newTab.getRootPane().setDefaultButton(submit);







//        measurementsCol currentMeasure = new measurementsCol(partNumber);
//        storage.add(currentMeasure);
//        tabbedPane.addTab(String.valueOf(partNumber), null);
//        JLabel label = new JLabel(String.valueOf(partNumber));
//        JButton topLeft = new JButton("Top Left");
//        JPanel panel = new JPanel();
//        panel.add(label);
//        panel.add(topLeft);
//
//        topLeft.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//                currentCapture[0] = partNumber;
//                currentCapture[1] = 1;
//                camera.startLive();
//
//            }
//        });
//
//
//
//        System.out.println(tabbedPane.getComponentCount());
//        JButton button2 = new JButton("Unclicked");
//        panel.add(button2);
//        button2.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String response = JOptionPane.showInputDialog(null, "Enter your part number: ", "Enter your part", JOptionPane.QUESTION_MESSAGE);
//                button2.setText(response);
//            }
//        });
//        JButton button3 = new JButton("Print what you typed");
//        button3.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                System.out.println(button2.getText());
//                guiHandler.updateTabbedPane();
//
//            }
//        });
//        panel.add(button3);
//
//        JButton button4 = new JButton("Show Top Left");
//        button4.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                //Action listener attached to btn4
//
//                JFrame newFrame = new JFrame("Showing Top Left");
//                JPanel newJpanel = new JPanel();
//                JLabel label = new JLabel();
//                newJpanel.add(label);
//                newFrame.add(newJpanel);
//                newFrame.setVisible(true);
//
//            }
//        });
//        panel.add(button4);
//
//        JButton button5 = new JButton("Close tabPane");
//        button5.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                tabbedPane.remove(tabbedPane.getSelectedIndex());
//            }
//        });
//        panel.add(button5);
//        tabbedPane.addTab(String.valueOf(partNumber), panel);


    }





    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {

        //Initialize program settings
        currentSettings = new settings();
        System.out.println("Upload server URL: " + currentSettings.serverURL);

        System.out.println("Profiles loaded: " + currentSettings.listProfiles());

        //Setup camera control
        camera = new cameraControl();


        //Setup menu variables
        JMenu menu, subMenu;
        JMenuItem menuItem;
        JMenuBar menuBar;

        //Setup tabs
        tabbedPane = new JTabbedPane();
//        tabbedPane.setBounds(50,50, 500, 600);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        //Setup the main menu
        menuBar = new JMenuBar();
        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menuItem = new JMenuItem("New", KeyEvent.VK_N);

        //Setup Image analysis listener
        imgAnalyzer analyzer = new imgAnalyzer();
        camera.addListener(analyzer);



        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addTab(tabbedPane);
                } catch (NumberFormatException exception) {
                    JOptionPane.showMessageDialog(null, "Please enter a Integer number");
                }
            }
        });

        menu.add(menuItem);
        menu.addSeparator();

        menuItem = new JMenuItem("Exit", KeyEvent.VK_X);
        menuItem.addActionListener(new CloseListener());

        menu.add(menuItem);


        menuBar.add(menu);

        menu = new JMenu("Action");
        menuItem = new JMenuItem("Force Update");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiHandler.updateTabbedPane();
            }
        });
        menu.add(menuItem);
        menuBar.add(menu);
        main.setJMenuBar(menuBar);
        main.setSize(600, 900);
        main.setLayout(new GridLayout(1,1));
        main.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        main.getContentPane().add(tabbedPane);
        main.setFocusable(true);
        main.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                System.out.println(e.getKeyCode() + " key released");
                debugChecker(e.getKeyCode());

            }
        });
        main.setVisible(true);

//        tabbedPane.addTab("Tab 1", null, panel1, "Does nothing");
//        tabbedPane.addTab("tab2", null, panel2, "blah");
    }
}





























