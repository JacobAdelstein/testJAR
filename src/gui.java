import org.xml.sax.SAXException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;


public class gui {

    private static int passvariable;
    static JFrame resultspanel = new JFrame("Results Panel");
    static JPanel Passfail = new JPanel();
    static JLabel Below = new JLabel("The Limit is below the FeretMin!");
    public static settings currentSettings;

    static {
        try {
            currentSettings = new settings();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

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




    //TOP LEFT - 1
    //TOP RIGHT - 2

    static void addTab(JTabbedPane tabbedPane, Integer partNumber) {

        measurementsCol currentMeasure = new measurementsCol(partNumber);
        storage.add(currentMeasure);
        tabbedPane.addTab(String.valueOf(partNumber), null);
        guiHandler.updateTabbedPane();



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

    static Boolean partExists(Integer partNum) {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            if (Integer.parseInt(tabbedPane.getTitleAt(i)) == partNum) {
                return true;
            }
        }
        return false;
    }




    public static void main(String[] args) throws IOException {

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
                    String response = JOptionPane.showInputDialog(null, "Enter your part number: ", "Enter your name", JOptionPane.QUESTION_MESSAGE);
                    int partNum = Integer.parseInt(response);
                    if (partExists(partNum)) {
                        JOptionPane.showMessageDialog(null, "Part number already exists");
                    } else {
                        addTab(tabbedPane, partNum);
                    }
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
        main.setSize(875, 420);
        main.setLayout(new GridLayout(1,1));
        main.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        main.getContentPane().add(tabbedPane);
        main.setVisible(true);







//        tabbedPane.addTab("Tab 1", null, panel1, "Does nothing");
//        tabbedPane.addTab("tab2", null, panel2, "blah");






    }
}





























