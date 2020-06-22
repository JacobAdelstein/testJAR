import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.ds.civil.LtiCivilDriver;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.ImageIcon;
import java.io.FileWriter;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;



public class gui extends imp{
    private static boolean toplefttf;
    private static boolean toprighttf;
    private static boolean centeraccess;
    private static boolean bottomrighttf;
    private static boolean bottomlefttf;
    private static boolean checktopleft;
    private static boolean checkbottomleft;
    private static boolean checkcenteraccess;
    private static boolean checkbottomright;
    private static boolean checktopright;
    private static int passvariable;
    static JFrame resultspanel = new JFrame("Results Panel");
    static JPanel Passfail = new JPanel();
    static JLabel Below = new JLabel("The Limit is below the FeretMin!");
    public static boolean aquirebuttontf;
    public static settings currentSettings = new settings();
    static JFrame main = new JFrame("JFrame with a JPanel");

    public static void main(String[] args) throws IOException {

        Webcam.setDriver(new LtiCivilDriver());
        System.out.println("Driver set");


//First JFrame that appears
//Some Initialization


        JPanel Button1 = new JPanel();// Make a JPanel;
        Button1.setBounds(100, 30, 400, 40);
        main.setSize(875, 420);
        main.setLayout(null);

        JPanel Image = new JPanel();

        JLabel jl = new JLabel();


        InputStream in = gui.class.getResourceAsStream("logo/newpotomac.png");

        BufferedImage myImg = ImageIO.read(in);
        jl.setIcon(new ImageIcon(myImg));


        main.getContentPane().add(Image);
        Image.add(jl);
        Image.setBounds(300, 5, 198, 153);

        main.setVisible(true);

//Buttons for Main Frame
        JButton topleft = new JButton("topleft");
        topleft.setBounds(20, 150, 100, 50);
        main.getContentPane().add(topleft);

        JButton bottomleft = new JButton("bottomleft");
        bottomleft.setBounds(20, 300, 100, 50);
        main.getContentPane().add(bottomleft);


        JButton center = new JButton("center");
        center.setBounds(350, 250, 100, 50);
        main.getContentPane().add(center);

        JButton topright = new JButton("topright");
        topright.setBounds(700, 150, 100, 50);
        main.getContentPane().add(topright);

        JButton bottomright = new JButton("bottomright");
        bottomright.setBounds(700, 300, 100, 50);
        main.getContentPane().add(bottomright);




//The results panel

        resultspanel.setSize(1000, 600);
        JPanel results1 = new JPanel();// Make a JPanel;
        results1.setBounds(20, 30, 400, 40);
        JLabel Above = new JLabel("The Limit Exceeds the FeretMax!");   // Make a JLabel;
        Above.setForeground(Color.red);
        Above.setFont(new Font("Serif", Font.PLAIN, 20));
        Below.setForeground(Color.red);
        Below.setFont(new Font("Serif", Font.PLAIN, 20));
        resultspanel.getContentPane().add(Passfail);  // Add Passfail to JFrame f
        Passfail.setBounds(20, 300, 800, 500);
        resultspanel.getContentPane().add(results1);


//Top Left Button Action Listener
        topleft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //delegate to event handler method

                toplefttf = true;
                checktopleft = true;


                cameracontrol.returner();

                try {
                    cameracontrol.setsizegetcontent();
                    cameracontrol.buttonevent();
                    idk(currentSettings);


                } catch (IOException ioException) {
                    ioException.printStackTrace();

                }


            }
        });

//Top Right Button Action Listener
        topright.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //delegate to event handler method
                toprighttf = true;
                checktopright = true;
                cameracontrol.returner();


                try {

                    idk(currentSettings);


                } catch (IOException ioException) {
                    ioException.printStackTrace();

                }


            }
        });
//Center button Action Listener
        center.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //delegate to event handler method
                centeraccess = true;
                checkcenteraccess = true;

                cameracontrol.returner();

                try {

                    idk(currentSettings);


                } catch (IOException ioException) {
                    ioException.printStackTrace();

                }


            }
        });
//Bottom right button action listener
        bottomright.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //delegate to event handler method
                bottomrighttf = true;
                checkbottomright = true;

                cameracontrol.returner();

                try {

                    idk(currentSettings);


                } catch (IOException ioException) {
                    ioException.printStackTrace();

                }


            }
        });

//bottom left button action listener
        bottomleft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //delegate to event handler method
                bottomlefttf = true;
                checkbottomleft = true;

                cameracontrol.returner();


                try {

                    idk(currentSettings);


                } catch (IOException ioException) {
                    ioException.printStackTrace();

                }


            }
        });

    }


//Button ActionEvent method for Main frame buttons



    // Button ActionEvent for acquire button
    public static double[] acquirehandler(settings currentSettings) throws IOException {


        FileWriter myWriter = new FileWriter("C:/Users/Admin/IdeaProjects/testJAR/Results/savedresults.txt", true);
        resultspanel.setVisible(true);

        impmethod(currentSettings);


        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());


        for (double curr : feretCol) {


            if (curr > currentSettings.feretMax) {


                JLabel Red = new JLabel("Fails:" + curr);
                Red.setForeground(Color.red);
                Passfail.add(Red);

            }


            if (curr < currentSettings.feretMin) {

                JLabel Red2 = new JLabel("Fail:" + curr);
                Red2.setForeground(Color.red);
                resultspanel.add(Below);
                Passfail.add(Red2);

            }


            if (curr > currentSettings.feretMin & curr < currentSettings.feretMax) {

                JLabel Green = new JLabel("Pass" + curr);
                Green.setForeground(Color.green);
                Passfail.add(Green);

            }


//TOP LEFT HANDLER FOR SAVING DATA TO RESULTS -- TOP LEFT HANDLER FOR SAVING DATA TO RESULTS -- TOP LEFT HANDLER FOR SAVING DATA TO RESULTS


            if (toplefttf == true & aquirebuttontf == true) {



                myWriter.write(formatter.format(date)+  " " + "Top Left!" + "" + " " + curr +  "\n");



            }


//TOP RIGHT HANDLER FOR SAVING DATA TO RESULTS -- TOP RIGHT HANDLER FOR SAVING DATA TO RESULTS -- TOP RIGHT HANDLER FOR SAVING DATA TO RESULTS


            if (toprighttf == true & aquirebuttontf == true) {



                myWriter.write(formatter.format(date)+ " " +"Top Right!" + " " + curr + "\n");

            }


            //CENTER HANDLER FOR SAVING DATA TO RESULTS -- CENTER HANDLER FOR SAVING DATA TO RESULTS -- CENTER HANDLER FOR SAVING DATA TO RESULTS


            if (centeraccess == true & aquirebuttontf == true) {



                myWriter.write(formatter.format(date)+ " " + "Center Access" + " " + curr + "\n");

            }


            //BOTTOM RIGHT HANDLER FOR SAVING DATA TO RESULTS -- BOTTOM RIGHT FOR SAVING DATA TO RESULTS -- BOTTOM RIGHT FOR SAVING DATA TO RESULTS


            if (bottomrighttf == true & aquirebuttontf == true) {



                myWriter.write(formatter.format(date) + " " + "Top Left!" + " " + curr +  "\n");

            }


            //BOTTOM KEFT HANDLER FOR SAVING DATA TO RESULTS -- BOTTOM LEFT FOR SAVING DATA TO RESULTS -- BOTTOM LEFT FOR SAVING DATA TO RESULTS


            if (bottomlefttf == true & aquirebuttontf == true) {



                myWriter.write(formatter.format(date) + " " + "Bottom Left!" + " " + curr + "\n");


            }



        }
        myWriter.close();
        toplefttf = false;
        bottomlefttf = false;
        centeraccess = false;
        toprighttf = false;
        bottomrighttf = false;

        System.out.println(feretCol.length);
return feretCol;
    }

    public static void idk(settings currentSettings) throws IOException {

        guiextension.imageaction(currentSettings);

    }

    //This method handles the graphic checkbox and x-box on the main Jframe
    public static double[] checkxbox(settings currentSettings) throws IOException {
         double[] ph = {0};
         double[] phtl = {0};
         double[] phbl = {0};

        //Panel for pass fail check mark graphics
//Top left
        JPanel Icon1 = new JPanel();

        JLabel check1 = new JLabel();

        main.getContentPane().add(Icon1);
        Icon1.add(check1);
        Icon1.setBounds(120, 145, 80, 60);

//Bottom left
        JPanel Icon2 = new JPanel();

        JLabel check2 = new JLabel();

        main.getContentPane().add(Icon2);
        Icon2.add(check2);
        Icon2.setBounds(120, 300, 80, 60);


//Top right
        JPanel Icon3 = new JPanel();

        JLabel check3 = new JLabel();

        main.getContentPane().add(Icon3);
        Icon3.add(check3);
        Icon3.setBounds(620, 145, 80, 60);


//Bottom right

        JPanel Icon4 = new JPanel();

        JLabel check4 = new JLabel();

        main.getContentPane().add(Icon4);
        Icon4.add(check4);
        Icon4.setBounds(620, 300, 80, 60);


//Center
        JPanel Icon5 = new JPanel();

        JLabel check5 = new JLabel();

        main.getContentPane().add(Icon5);
        Icon5.add(check5);
        Icon5.setBounds(362, 300, 80, 60);



//iterate through curr


        for (passvariable = 0; passvariable <= feretCol.length; passvariable++) {
            for (double curr : feretCol) {

// set variables to count whether or not there are holes outside paramaters

                int lessthan = 0;
                int greaterthan = 0;


                if (curr > currentSettings.feretMax) {


                    if (greaterthan > 0) {

//if the button has been clicked on and part is outside parameters, show a red x
                        if (checktopleft == true) {

                            InputStream in = gui.class.getResourceAsStream("pictures/redchecmark.png");

                            BufferedImage myImg = ImageIO.read(in);

                            check1.setIcon(new ImageIcon(myImg));

                            ph = new double[passvariable + 1];
                            ph[passvariable] = curr;


                        }
                        if (checkbottomleft == true) {
                            InputStream in = gui.class.getResourceAsStream("pictures/redchecmark.png");

                            BufferedImage myImg = ImageIO.read(in);

                            check2.setIcon(new ImageIcon(myImg));

                            ph = new double[passvariable + 1];
                            ph[passvariable] = curr;


                        }
                        if (checkcenteraccess == true) {
                            InputStream in = gui.class.getResourceAsStream("pictures/redchecmark.png");

                            BufferedImage myImg = ImageIO.read(in);

                            check5.setIcon(new ImageIcon(myImg));

                            ph = new double[passvariable + 1];
                            ph[passvariable] = curr;

                        }
                        if (checktopright == true) {
                            InputStream in = gui.class.getResourceAsStream("pictures/redchecmark.png");

                            BufferedImage myImg = ImageIO.read(in);

                            check3.setIcon(new ImageIcon(myImg));

                            ph = new double[passvariable + 1];
                            ph[passvariable] = curr;


                        }
                        if (checkbottomright == true) {
                            InputStream in = gui.class.getResourceAsStream("pictures/redchecmark.png");

                            BufferedImage myImg = ImageIO.read(in);

                            check4.setIcon(new ImageIcon(myImg));

                            ph = new double[passvariable + 1];
                            ph[passvariable] = curr;


                        }
                    }

                } else if (curr < currentSettings.feretMin) {
                    lessthan++;


                    if (lessthan > 0) {
//if the button has been clicked on and part is outside parameters, show a red x
                        if (checktopleft == true) {
                            InputStream in = gui.class.getResourceAsStream("pictures/redcheckmark.png");
                            BufferedImage myImg = ImageIO.read(in);
                            check1.setIcon(new ImageIcon(myImg));

                            ph = new double[passvariable + 1];
                            ph[passvariable] = curr;


                        }
                        if (checkbottomleft == true) {
                            InputStream in = gui.class.getResourceAsStream("pictures/redcheckmark.png");
                            BufferedImage myImg = ImageIO.read(in);
                            check2.setIcon(new ImageIcon(myImg));

                            ph = new double[passvariable + 1];
                            ph[passvariable] = curr;


                        }
                        if (checkcenteraccess == true) {
                            InputStream in = gui.class.getResourceAsStream("pictures/redcheckmark.png");
                            BufferedImage myImg = ImageIO.read(in);
                            check5.setIcon(new ImageIcon(myImg));

                            ph = new double[passvariable + 1];
                            ph[passvariable] = curr;


                        }
                        if (checktopright == true) {
                            InputStream in = gui.class.getResourceAsStream("pictures/redcheckmark.png");
                            BufferedImage myImg = ImageIO.read(in);
                            check3.setIcon(new ImageIcon(myImg));

                            ph = new double[passvariable + 1];
                            ph[passvariable] = curr;

                        }
                        if (checkbottomright == true) {
                            InputStream in = gui.class.getResourceAsStream("pictures/redcheckmark.png");
                            BufferedImage myImg = ImageIO.read(in);
                            check4.setIcon(new ImageIcon(myImg));

                            ph = new double[passvariable + 1];
                            ph[passvariable] = curr;


                        }


                    }
                } else if (lessthan == 0 & greaterthan == 0) {
//if the button has been clicked on and part is within parameters,
//
// a green checkmark

                    if (checktopleft == true) {
                        InputStream in = gui.class.getResourceAsStream("pictures/greencheckmark.png");
                        BufferedImage myImg = ImageIO.read(in);
                        check1.setIcon(new ImageIcon(myImg));

                        ph = new double[passvariable + 1];
                        ph[passvariable] = curr;



                    }

                    if (checkbottomleft == true) {

                        InputStream in = gui.class.getResourceAsStream("pictures/greencheckmark.png");
                        BufferedImage myImg = ImageIO.read(in);
                        check2.setIcon(new ImageIcon(myImg));


                        ph = new double[passvariable + 1];
                        ph[passvariable] = curr;


                    }
                    if (checkcenteraccess == true) {
                        InputStream in = gui.class.getResourceAsStream("pictures/greencheckmark.png");
                        BufferedImage myImg = ImageIO.read(in);
                        check5.setIcon(new ImageIcon(myImg));

                        ph = new double[passvariable + 1];
                        ph[passvariable] = curr;

                    }
                    if (checktopright == true) {

                        InputStream in = gui.class.getResourceAsStream("pictures/greencheckmark.png");
                        BufferedImage myImg = ImageIO.read(in);
                        check3.setIcon(new ImageIcon(myImg));



                        ph = new double[passvariable + 1];
                        ph[passvariable] = curr;


                    }
                    if (checkbottomright == true) {
                        InputStream in = gui.class.getResourceAsStream("pictures/greencheckmark.png");
                        BufferedImage myImg = ImageIO.read(in);
                        check4.setIcon(new ImageIcon(myImg));

                        ph = new double[passvariable + 1];
                        ph[passvariable] = curr;


                    }

                }

            }

        }


        return feretCol;
    }




}





























