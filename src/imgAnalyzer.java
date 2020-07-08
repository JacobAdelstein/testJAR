import ij.IJ;
import ij.ImagePlus;
import ij.measure.ResultsTable;

import javax.swing.*;


public class imgAnalyzer implements cameraListener {
    public imgAnalyzer() {
        System.out.println("Image Analysis Loaded");

    }

    @Override

    public void imageTaken() {
        System.out.println("An Image was taken");
        JFrame newFrame = new JFrame("Showing Top Left");
        JPanel newJpanel = new JPanel();
        JLabel label = new JLabel();
        label.setIcon(new ImageIcon(gui.camera.capture));
        newJpanel.add(label);
        newFrame.add(newJpanel);
        newFrame.setVisible(true);

//        measurements currentMeasure = new measurements(gui.currentCapture[1], gui.camera.capture, gui.currentCapture[0]);



        //Do Image Analysis
        ImagePlus imp = new ImagePlus("capture", gui.camera.capture);
        imp.changes = false;

        IJ.run(imp, "8-bit", "");
        IJ.setThreshold(imp, 60, 255);
        IJ.setProperty("BlackBackground", true);
        IJ.run(imp, "Convert to Mask", "");
        StringBuilder scale = new StringBuilder();
        scale.append("distance=" + 1 + " known=" + 1 + " pixel=" + 1 + " unit=um");
        StringBuilder analysis = new StringBuilder();
        analysis.append("size=" + 0 + "-" + 100 + " circularity=" + .5 + "-" + 1 + " show=[Overlay Masks] clear include in-situ");


        IJ.run(imp, "Set Scale...", scale.toString());
        IJ.run(imp, "Set Measurements...", "feret's");
//        IJ.run(imp, "Analyze Particles...", "size=800-Infinity circularity=0.70-1.00 show=[Overlay Masks] clear include in-situ");
        IJ.run(imp, "Analyze Particles...", analysis.toString());



        ResultsTable rt = ResultsTable.getResultsTable();
        double[] results = rt.getColumnAsDoubles(19);










        //Save image and analysis results
        for (measurementsCol measurement : gui.storage) {
            if (measurement.partNum == gui.currentCapture[0]) {
                for (int i = 0; i < measurement.measureList.size(); i++){
                    if (measurement.measureList.get(i).position == gui.currentCapture[1]){
                        measurement.measureList.get(i).setImage(gui.camera.capture);
                        //lol good luck trying to decipher this bullshit
                    }
                }

            }
        }



//        for (int i = 1; i <= gui.tabbedPane.getComponentCount(); i++) {
//            gui.tabbedPane.getComponentAt(i);
//
//
//
//        }

        guiHandler.updateTabbedPane();


    }
}
