import com.github.sarxos.webcam.*;
import com.github.sarxos.webcam.ds.civil.LtiCivilDriver;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


interface cameraListener {
    void imageTaken();
}

public class cameraControl extends gui implements WebcamDiscoveryListener, MouseListener, WindowListener, WebcamListener {
    Webcam webcam;
    static WebcamPanel panel;
    Integer position;
    Image capture;
    private List<cameraListener> listeners = new ArrayList<cameraListener>();

    public void addListener(cameraListener toAdd) {
        listeners.add(toAdd);
    }


    public cameraControl() throws InterruptedException {

        Webcam.setDriver(new LtiCivilDriver());
        picker();
//        webcam.open();
//        panel = new WebcamPanel(webcam);

    }

    public String toString() {
        return webcam.getName();
    }



    public void startLive() {
        JFrame window = new JFrame("Live View");
        window.add(panel);
        window.setResizable(true);
        window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        window.pack();
        window.setVisible(true);
        window.setSize(1360, 768);

        panel.resume();
        panel.setFPSDisplayed(true);
        panel.setDisplayDebugInfo(true);
        panel.setImageSizeDisplayed(true);
        panel.setMirrored(false);
        window.pack();
        window.setSize(1360, 768);

        JButton acquireButton = new JButton("Acquire");
        JFrame acquireFrame = new JFrame("Acquire Button");
        acquireFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        acquireFrame.setSize(400, 70);


        acquireButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.sysConsole.println("Acquire Clicked");
                camera.getImage();
                acquireFrame.setVisible(false);
                window.setVisible(false);

            }
        });
        acquireFrame.add(acquireButton);
        window.setVisible(true);
        acquireFrame.setVisible(true);


    }

    public void stopLive() {
        panel.pause();
    }

    public void getImage() {

        panel.pause();
        capture = panel.getImage();
        panel.resume();
        for (cameraListener hl : listeners)
            hl.imageTaken();


    }

    public void takePic() {

        JFrame clayscodewindow = new JFrame("Live View");
        clayscodewindow.add(panel);
        clayscodewindow.setResizable(true);
        clayscodewindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        clayscodewindow.pack();
        clayscodewindow.setVisible(true);
        clayscodewindow.setSize(1360, 768);

        panel.resume();
        panel.setFPSDisplayed(true);
        panel.setDisplayDebugInfo(true);
        panel.setImageSizeDisplayed(true);
        panel.setMirrored(false);
        clayscodewindow.pack();
        clayscodewindow.setSize(1360, 768);

        JButton acquireButton = new JButton("Acquire");
        JFrame acquireFrame = new JFrame("Acquire Button");
        acquireFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        acquireFrame.setSize(400, 70);


        acquireButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.sysConsole.println("Acquire Clicked");
//                panel.pause();

                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.setMultiSelectionEnabled(false);
                chooser.setAcceptAllFileFilterUsed(false);
                chooser.setFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        if (f.getName().toLowerCase(Locale.ROOT).endsWith("jpg")) {
                            return true;
                        }
                        if (f.isDirectory()) {
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public String getDescription() {
                        return ".JPG file";
                    }
                });
                chooser.setDialogTitle("Select a save location");
                int option = chooser.showSaveDialog(gui.main);
                if (option == JFileChooser.APPROVE_OPTION) {
//                    if (!chooser.getSelectedFile().getAbsolutePath().toLowerCase().endsWith("jpg")) {
//                        chooser.setSelectedFile(new File(chooser.getSelectedFile().getAbsolutePath() + ".jpg"));
//                    }

                    File imgFile = new File(chooser.getSelectedFile() + ".jpg");
                    System.out.println(chooser.getSelectedFile());
                    try {
                        ImageIO.write((RenderedImage) panel.getImage(), "jpg", imgFile);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    acquireFrame.dispose();
                    clayscodewindow.dispose();

                }


                panel.resume();


            }
        });
        acquireFrame.add(acquireButton);
        clayscodewindow.setVisible(true);
        acquireFrame.setVisible(true);


    }

    public void picker() {

        JButton b = new JButton("Submit");
        WebcamPicker picker = new WebcamPicker();

        List<Webcam> camList = Webcam.getWebcams();

        if (camList.size() == 0) {
            JOptionPane.showMessageDialog(null, "No Webcam Detected");
            System.out.println("No Webcam detected, exiting...");
            System.exit(0);
        }

        JFrame cameraselectionframe = new JFrame();
        cameraselectionframe.add(picker);
        if(camList.size()==1){
            webcam = Webcam.getDefault();
            webcam.open();
            panel = new WebcamPanel(webcam);
            main.setVisible(true);
            gui.loadMain();




        }


        if (camList.size() > 1) {
            cameraselectionframe.setVisible(true);
        }

        cameraselectionframe.getContentPane().setLayout(new FlowLayout());
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                webcam = picker.getSelectedWebcam();
                webcam.open();
                panel = new WebcamPanel(webcam);
                cameraselectionframe.dispose();
                main.setVisible(true);
                gui.loadMain();






            }
        });

        cameraselectionframe.add(b);
        cameraselectionframe.setTitle("Select Webcam");
        cameraselectionframe.setSize(600, 100);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();

        cameraselectionframe.setBounds((int) ((width - cameraselectionframe.getWidth()) / 2), (int) ((height - cameraselectionframe.getHeight()) / 2), 600, 100);

    }











    @Override
    public void webcamFound(WebcamDiscoveryEvent event) {

    }

    @Override
    public void webcamGone(WebcamDiscoveryEvent event) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {


    }

    @Override
    public void mouseReleased(MouseEvent e) {
        startLive();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    @Override
    public void webcamOpen(WebcamEvent we) {

    }

    @Override
    public void webcamClosed(WebcamEvent we) {

    }

    @Override
    public void webcamDisposed(WebcamEvent we) {

    }

    @Override
    public void webcamImageObtained(WebcamEvent we) {

    }
}

