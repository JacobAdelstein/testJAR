import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

public class captureWorker extends SwingWorker {
    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * <p>
     * Note that this method is executed only once.
     *
     * <p>
     * Note: this method is executed in a background thread.
     *
     * @return the computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    protected Image doInBackground() throws Exception {
//        Thread.sleep(5000);
        CountDownLatch latch = new CountDownLatch(1);
        final Image[] capture = new Image[1];

        gui.camera.startLive();
        JButton acquireButton = new JButton("Acquire");
        JFrame acquireFrame = new JFrame("AcquireButton");
        acquireFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        acquireFrame.setSize(400, 70);
        acquireButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.camera.stopLive();
//                capture[0] = gui.camera.getImage();
//
//                JFrame ImpShow = new JFrame();
//                ImpShow.setSize(800, 600);
//                JLabel impshowlabel = new JLabel();
//                ImpShow.getContentPane().add(impshowlabel);
//                impshowlabel.setIcon(new ImageIcon(capture[0]));
//                ImpShow.setVisible(true);

                latch.countDown();
            }
        });
        acquireFrame.add(acquireButton);
        acquireFrame.setVisible(true);
        latch.wait();
        return capture[0];



    }

    protected void done()
    {
        try
        {
            JOptionPane.showMessageDialog(null, get());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}
