import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import org.apache.xpath.objects.XNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;


//Actionevent for bringing up image

public class guiextension extends gui {

    public static JFrame imageaction(JFrame main, settings currentSettings,  JLabel check1, JLabel check2, JLabel check3, JLabel check4, JLabel check5, JFrame aquirebutton, JFrame resultspanel, JPanel Passfail, JLabel Below) throws IOException {

        //topleftimagebutton
        JButton topleftimage = new JButton("top right image");
        topleftimage.setOpaque(false);
        topleftimage.setContentAreaFilled(false);
        topleftimage.setBorderPainted(false);
        topleftimage.setBounds(135, 147, 50, 50);
        main.getContentPane().add(topleftimage);


        //bottomleftimage
        JButton bottomleftimage = new JButton("bottom left image");
        bottomleftimage.setOpaque(false);
        bottomleftimage.setContentAreaFilled(false);
        bottomleftimage.setBounds(135, 300, 50, 50);
        bottomleftimage.setBorderPainted(false);
        main.getContentPane().add(bottomleftimage);


        //centerimage
        JButton centerimage = new JButton("center image");
        centerimage.setOpaque(false);
        centerimage.setContentAreaFilled(false);
        centerimage.setBorderPainted(false);
        centerimage.setBounds(362, 300, 50, 50);
        main.getContentPane().add(centerimage);

        //toprightimage
        JButton toprightimage = new JButton("top right image");
        toprightimage.setOpaque(false);
        toprightimage.setContentAreaFilled(false);
        toprightimage.setBorderPainted(false);
        toprightimage.setBounds(620, 147, 50, 50);
        main.getContentPane().add(toprightimage);


        //bottomrightimage
        JButton bottomrightimage = new JButton("top right image");
        bottomrightimage.setOpaque(false);
        bottomrightimage.setContentAreaFilled(false);
        bottomrightimage.setBorderPainted(false);
        bottomrightimage.setBounds(620, 300, 50, 50);
        main.getContentPane().add(bottomrightimage);

        JFrame resultspage = new JFrame("Results Page");

        resultspage.setSize(875, 420);








        //topleftimageactionlistener


        topleftimage.addActionListener(new ActionListener() {


            @Override
            public void actionPerformed(ActionEvent b) {
                //delegate to event handler method

                try {

                    imageactioner(resultspage);



                  double ph[] = checkxbox(currentSettings,check1, check2,  check3, check4, check5,  aquirebutton,  resultspanel, Passfail, Below);

                    JLabel displaytl = new JLabel("Results" + "" + Arrays.toString(ph));   // Make a JLabel;
                    displaytl.setBounds(20, 100, 198, 153);
                    resultspage.getContentPane().add(displaytl);







                } catch (IOException e) {
                    e.printStackTrace();
                }

                }


        });

        //bottomleftimageactionlistener
        bottomleftimage.addActionListener(new ActionListener() {


            @Override
            public void actionPerformed(ActionEvent b) {
                //delegate to event handler method

                try {

                    resultspage.getContentPane().removeAll();
                    resultspage.repaint();

                    imageactioner(resultspage);



                    double ph[] =   checkxbox(currentSettings,check1, check2,  check3, check4, check5,  aquirebutton,  resultspanel, Passfail, Below);



                    JLabel displaybl = new JLabel(Arrays.toString(ph));   // Make a JLabel;

                    displaybl.setBounds(50, 200, 500, 500);
                    resultspage.getContentPane().add(displaybl);




                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
        //center image action listener
        centerimage.addActionListener(new ActionListener() {


            @Override
            public void actionPerformed(ActionEvent b) {
                //delegate to event handler method

                try {

                    imageactioner(resultspage);

                    double ph[] = checkxbox(currentSettings,check1, check2,  check3, check4, check5,  aquirebutton,  resultspanel, Passfail, Below);

                    JLabel displayci = new JLabel("Results" + "" + Arrays.toString(ph));   // Make a JLabel;
                    displayci.setBounds(20, 300, 198, 153);
                    resultspage.getContentPane().add(displayci);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
        //top right image action listener
        toprightimage.addActionListener(new ActionListener() {


            @Override
            public void actionPerformed(ActionEvent b) {
                //delegate to event handler method

                try {

                    imageactioner(resultspage);
                    double ph[] = checkxbox(currentSettings,check1, check2,  check3, check4, check5,  aquirebutton,  resultspanel, Passfail, Below);

                    JLabel displaytr= new JLabel("Results" + "" + Arrays.toString(ph));   // Make a JLabel;
                    displaytr.setBounds(20, 400, 198, 153);
                    resultspage.getContentPane().add(displaytr);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

        // bottom right image action listener
        bottomrightimage.addActionListener(new ActionListener() {


            @Override
            public void actionPerformed(ActionEvent b) {
                //delegate to event handler method

                try {

                    imageactioner(resultspage);

                    double ph[] = checkxbox(currentSettings,check1, check2,  check3, check4, check5,  aquirebutton,  resultspanel, Passfail, Below);

                    JLabel displaybr = new JLabel("Results" + "" + Arrays.toString(ph));   // Make a JLabel;
                    displaybr.setBounds(20, 100, 198, 153);
                    resultspage.getContentPane().add(displaybr);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });


       return resultspage;

    }




        public static void imageactioner (JFrame resultspage) throws IOException {


            resultspage.setVisible(true);




    }

}















