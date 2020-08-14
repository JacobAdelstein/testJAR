import net.imagej.ops.Ops;
import org.jruby.RubyProcess;
import org.json.JSONException;
import org.python.indexer.Def;
import org.yecht.IoFileRead;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class guiHandler {

    public static JPanel getResultsPanel(measurements currentMeasurement) {

        JPanel returnPanel = new JPanel();
        returnPanel.setLayout(new BoxLayout(returnPanel, BoxLayout.PAGE_AXIS));
        returnPanel.setSize(100,60);
        returnPanel.setMaximumSize(new Dimension(100, 60));
        returnPanel.setMinimumSize(new Dimension(100, 60));
        boolean pass = currentMeasurement.checkPass();

        if (pass) {
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


                DefaultListModel<result> resultsModel = new DefaultListModel<>();
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



        for (int i = 0; i < gui.tabbedPane.getTabCount(); i++) {
            //Iterate through tabbedPanes

            int tabNum = Integer.parseInt(gui.tabbedPane.getTitleAt(i));
            //Get the partNum in the title
            gui.sysConsole.println("Updating panel at " + tabNum);
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
                    gui.sysConsole.println(gui.storage.get(b).toString());
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
                                try {
                                    int techID = Integer.parseInt(JOptionPane.showInputDialog("Enter Technician ID"));
                                    httpSubmit.testSubmit(gui.storage.get(finalB).partNum, techID);
                                } catch (NumberFormatException ex) {
                                    JOptionPane.showMessageDialog(null, "Technician ID must be a number");
                                } catch (JSONException ex) {
                                    JOptionPane.showMessageDialog(null, "Received a JSON Exception");
                                }
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
                    gui.addTab(gui.tabbedPane);
                } catch (NumberFormatException exception) {
                    JOptionPane.showMessageDialog(null, "Please enter a Integer number");
                }
            }
        });

        menu.add(menuItem);
        menu.addSeparator();
        menuItem = new JMenuItem("Exit", KeyEvent.VK_X);
        menuItem.addActionListener(new gui.CloseListener());
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
        if (debug) {
            gui.sysConsole.println("Adding debug");
            menu = new JMenu("Debug");
            JCheckBoxMenuItem menuItemCheck = new JCheckBoxMenuItem("Offline Save");
            menuItemCheck.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (menuItemCheck.getState() == true) {

                    } else {

                    }

                }
            });
            menu.add(menuItemCheck);

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

            menuBar.add(menu);
            SwingUtilities.updateComponentTreeUI(gui.main);



        }

        return menuBar;
    }



}
