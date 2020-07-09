import org.jruby.RubyProcess;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class guiHandler {


    public static JPanel getImagePanel(measurements currentMeasurement){
        //Get Scaled ImageIcon
        ImageIcon imageIcon = new ImageIcon(currentMeasurement.capture.getScaledInstance(80,45,Image.SCALE_SMOOTH));




        JPanel returnPanel = new JPanel();
        JPanel imgPanel = new JPanel();
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
        returnPanel.setLayout(new BorderLayout(10, 20));
        returnPanel.setPreferredSize(new Dimension(450,100));
        JButton removeBtn = new JButton("Remove");


        removeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentMeasurement.removeImage();
                updateTabbedPane();

            }
        });


        returnPanel.add(removeBtn, BorderLayout.LINE_END);
        returnPanel.add(imgPanel, BorderLayout.CENTER);
        JLabel locLabel = new JLabel(currentMeasurement.name);
        locLabel.setHorizontalAlignment(SwingConstants.CENTER);
        returnPanel.add(locLabel, BorderLayout.LINE_START);
        returnPanel.setBorder(BorderFactory.createBevelBorder(EtchedBorder.RAISED));
        return returnPanel;
    }

    public static JPanel getNoImagePanel(measurements currentMeasurement){
        JPanel returnPanel = new JPanel();
        returnPanel.setLayout(new BorderLayout(10, 20));
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





        returnPanel.add(acquireBtn, BorderLayout.LINE_END);
        JLabel locLabel = new JLabel(currentMeasurement.name);
        locLabel.setHorizontalAlignment(SwingConstants.CENTER);
        returnPanel.add(locLabel, BorderLayout.LINE_START);
        returnPanel.setBorder(BorderFactory.createBevelBorder(EtchedBorder.RAISED));
        return returnPanel;
    }

    public static void updateTabbedPane(){
        for (int i = 0; i < gui.tabbedPane.getTabCount(); i++) {

            int tabNum = Integer.parseInt(gui.tabbedPane.getTitleAt(i));
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
                if (tabNum == gui.storage.get(b).partNum) {
                    System.out.println(gui.storage.get(b).toString());
                    for (int a = 0; a < gui.storage.get(b).measureList.size(); a++) {
                        if (gui.storage.get(b).measureList.get(a).hasImage == true) {
                            mainPanel.add(getImagePanel(gui.storage.get(b).measureList.get(a)));
                        } else {
                            mainPanel.add(getNoImagePanel(gui.storage.get(b).measureList.get(a)));
                        }
                    }
                }
            }

//            mainPanel.add(getRowPanel())


//            topLeft.




            gui.tabbedPane.setComponentAt(i, scrollPane);



//            gui.tabbedPane.remove(i);


        }

    }


}
