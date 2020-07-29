import org.jruby.RubyProcess;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

public class guiHandler {
    private static boolean checkPass(double[] results, inspectionProfile currentProfile){
        if (results.length == 0) {
            return false;
        }
        for (int i=0; i < results.length; i++) {
            if (results[i] < currentProfile.feretMin || results[i] > currentProfile.feretMax) {
                return false;
            }
        }
        return true;
    }

    public static JPanel getResultsPanel(measurements currentMeasurement) {

        JPanel returnPanel = new JPanel();
        returnPanel.setLayout(new BoxLayout(returnPanel, BoxLayout.PAGE_AXIS));
        returnPanel.setSize(100,60);
        returnPanel.setMaximumSize(new Dimension(100, 60));
        returnPanel.setMinimumSize(new Dimension(100, 60));
        boolean pass = checkPass(currentMeasurement.results, currentMeasurement.profile);

        if (pass) {
            returnPanel.setBackground(new Color(72, 245, 76));
        } else {
            JLabel fail = new JLabel("FAIL", SwingConstants.CENTER);
            returnPanel.add(fail);
            returnPanel.setBackground(new Color(245, 66, 66));
        }

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
                newFrame.add(newJpanel);
                newFrame.setPreferredSize(new Dimension(640, 360));
                newFrame.setBounds(500,100,640,360);
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



        for (int i = 0; i < gui.tabbedPane.getTabCount(); i++) {
            //Iterate through tabbedPanes

            int tabNum = Integer.parseInt(gui.tabbedPane.getTitleAt(i));
            //Get the partNum in the title
            System.out.println("Updating panel at " + tabNum);
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new FlowLayout());
            mainPanel.setPreferredSize(new Dimension(500, 1000));
//            mainPanel.setSize(1000,1100);
            JScrollPane scrollPane = new JScrollPane(mainPanel);
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane.setPreferredSize(new Dimension(500,500));
            for(int b = 0; b < gui.storage.size(); b++) {
                //Iterate through the storage arrayList


                if (tabNum == gui.storage.get(b).partNum) {
                    System.out.println(gui.storage.get(b).toString());
                    for (int a = 0; a < gui.storage.get(b).measureList.size(); a++) {
                        if (gui.storage.get(b).measureList.get(a).hasImage == true) {
                            mainPanel.add(getImagePanel(gui.storage.get(b).measureList.get(a)));
                        } else {
                            mainPanel.add(getNoImagePanel(gui.storage.get(b).measureList.get(a)));
                        }
                    }

                    JButton submit = new JButton("Submit");
                    int finalB = b;
                    submit.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                httpSubmit.testSubmit(gui.storage.get(finalB).partNum);
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }


                        }
                    });
                    mainPanel.add(submit);




                }
            }




            gui.tabbedPane.setComponentAt(i, scrollPane);





        }

    }


}
