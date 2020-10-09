import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.measure.ResultsTable;
import ij.process.ImageProcessor;

import javax.swing.*;
import java.awt.*;


public class imgAnalyzer implements cameraListener {



    public imgAnalyzer() {
        gui.sysConsole.println("Image Analysis Loaded");

    }

    @Override

    public void imageTaken() {
        gui.sysConsole.println("An Image was taken");
        Image overlay = null;
//        JFrame newFrame = new JFrame("Showing Top Left");
//        JPanel newJpanel = new JPanel();
//        JLabel label = new JLabel();
//        label.setIcon(new ImageIcon(gui.camera.capture));
//        newJpanel.add(label);
//        newFrame.add(newJpanel);
//        newFrame.setVisible(true);
//        measurements currentMeasure = new measurements(gui.currentCapture[1], gui.camera.capture, gui.currentCapture[0]);

//        double[] results = new double[1];
        //First we determine which tab took the image
        for (measurementsCol measurement : gui.storage) {
            if (measurement.partNum == gui.currentCapture[0]) {

                if (measurement.currentProfile.getProfileType().equalsIgnoreCase("SophionMH")) {
                    SophionMH.analyzeImage(measurement, gui.currentCapture[1], gui.camera.capture);

                } else {
                    //Do Image Analysis
                    ImagePlus imp = new ImagePlus("capture", gui.camera.capture);
                    ImagePlus org = new ImagePlus("original", gui.camera.capture);
                    imp.changes = false;

                    IJ.run(imp, "8-bit", "");
                    IJ.setThreshold(imp, measurement.currentProfile.lowerThreshold, measurement.currentProfile.upperThreshold);
                    IJ.setProperty("BlackBackground", measurement.currentProfile.blackBackground);
                    IJ.run(imp, "Convert to Mask", "");

                    String scale = ("distance=" + measurement.currentProfile.distance + " known=" + measurement.currentProfile.known + " pixel=" + measurement.currentProfile.pixel + " unit=um");
                    String analysis = ("size=" + measurement.currentProfile.sizeMin + "-" + measurement.currentProfile.sizeMax + " circularity=" + measurement.currentProfile.circularityMin + "-" + measurement.currentProfile.circularityMax + " show=[Overlay Masks] clear include in-situ");
                    IJ.run(imp, "Set Scale...", scale);
                    IJ.run(imp, "Set Measurements...", "feret's");
//        IJ.run(imp, "Analyze Particles...", "size=800-Infinity circularity=0.70-1.00 show=[Overlay Masks] clear include in-situ");
                    IJ.run(imp, "Analyze Particles...", analysis);
//                IJ.doCommand(imp, "Analyze Particles...");
                    imp.show();

//                IJ.run("Calculator Plus", "i1=capture i2=original operation=[Add: i2 = (i1+i2) x k1 + k2] k1=1 k2=0 create");
//                IJ.runPlugIn("Calculator Plus", "i1=capture i2=original operation=[Add: i2 = (i1+i2) x k1 + k2] k1=1 k2=0 create");
//                calculate(imp, org, 1, 0);
//                org.show();
//                imp.show();

                    overlay = (Image) imp.getBufferedImage();

                    gui.sysConsole.println("SCALE: " + scale);
                    gui.sysConsole.println("Analysis: " + analysis);
                    ResultsTable rt = ResultsTable.getResultsTable();
                    double[] results = rt.getColumnAsDoubles(19);

                    try {
                        gui.sysConsole.println("--------------RESULTS--------------");
                        gui.sysConsole.println("Number of results: " + results.length);
                        for (int i = 0; i < results.length; i++) {
                            gui.sysConsole.println(results[i]);
                        }
                        gui.sysConsole.println("--------------END RESULTS--------------");
                    } catch (NullPointerException  ex) {
                        JOptionPane.showMessageDialog(null, "No holes found");
                        results = new double[1];
                    }

                    for (int i = 0; i < measurement.measureList.size(); i++){
                        if (measurement.measureList.get(i).position == gui.currentCapture[1]){
                            measurement.measureList.get(i).setImage(gui.camera.capture);
                            measurement.measureList.get(i).results = results;
                            measurement.measureList.get(i).overlay = overlay;


                        }
                    }



//                    for (int i = 0; i < measurement.measureList.size(); i++){
//                        if (measurement.measureList.get(i).position == gui.currentCapture[1]){
//                            measurement.measureList.get(i).setImage(gui.camera.capture);
////                        measurement.measureList.get(i).results = results;
//                            measurement.measureList.get(i).overlay = overlay;
//                            result[] resultsArray = new result[results.length];
//                            for (int k = 0; k < results.length; k++) {
//                                resultsArray[k] = new result(k, results[k]);
//                            }
//                            measurement.measureList.get(i).results = resultsArray;
//                        }
//                    }

                }







            }
        }















        //Save image and analysis results
//        for (measurementsCol measurement : gui.storage) {
//            if (measurement.partNum == gui.currentCapture[0]) {
//                for (int i = 0; i < measurement.measureList.size(); i++){
//                    if (measurement.measureList.get(i).position == gui.currentCapture[1]){
//                        measurement.measureList.get(i).setImage(gui.camera.capture);
////                        measurement.measureList.get(i).results = results;
//                        measurement.measureList.get(i).overlay = overlay;
//                        result[] resultsArray = new result[results.length];
//                        for (int k = 0; k < results.length; k++) {
//                            resultsArray[k] = new result(k, results[k]);
//                        }
//                        measurement.measureList.get(i).results = resultsArray;
//                    }
//                }
//
//            }
//        }



//        for (int i = 1; i <= gui.tabbedPane.getComponentCount(); i++) {
//            gui.tabbedPane.getComponentAt(i);
//
//
//
//        }

        guiHandler.updateTabbedPane();


    }



    public void calculate(ImagePlus i1, ImagePlus i2, double k1, double k2) {
        boolean createWindow = true;
        boolean rgbPlanes = false;
        final int SCALE=0, ADD=1, SUBTRACT=2, MULTIPLY=3, DIVIDE=4;
        String[] ops = {"Scale: i2 = i1 x k1 + k2", "Add: i2 = (i1+i2) x k1 + k2", "Subtract: i2 = (i1-i2) x k1 + k2",
                "Multiply: i2 = (i1*i2) x k1 + k2", "Divide: i2 = (i1/i2) x k1 + k2"};
        int operation = SCALE;


        double v1, v2=0, r1, g1, b1, r2, g2, b2;
        int iv1, iv2, r, g=0, b=0;
        int width  = i1.getWidth();
        int height = i1.getHeight();
        ImageProcessor ip1, ip2;
        int slices1 = i1.getStackSize();
        int slices2 = i2.getStackSize();
        float[] ctable1 = i1.getCalibration().getCTable();
        float[] ctable2 = i2.getCalibration().getCTable();
        ImageStack stack1 = i1.getStack();
        ImageStack stack2 = i2.getStack();
        int currentSlice = i2.getCurrentSlice();

        for (int n=1; n<=slices2; n++) {
            ip1 = stack1.getProcessor(n<=slices1?n:slices1);
            ip2 = stack2.getProcessor(n);
            ip1.setCalibrationTable(ctable1);
            ip2.setCalibrationTable(ctable2);
            if (rgbPlanes == true) {
                for (int x=0; x<width; x++) {
                    for (int y=0; y<height; y++) {
                        iv1 = ip1.getPixel(x,y);
                        iv2 = ip2.getPixel(x,y);
                        r1 = (double) ((iv1 & 0xff0000)>>16);
                        g1 = (double) ((iv1 & 0x00ff00)>>8);
                        b1 = (double) ( iv1 & 0x0000ff);
                        r2 = (double) ((iv2 & 0xff0000)>>16);
                        g2 = (double) ((iv2 & 0x00ff00)>>8);
                        b2 = (double) ( iv2 & 0x0000ff);
                        switch (operation) {
                            case SCALE: r2 = r1; g2 = g1; b2 = b1; break;
                            case ADD: r2 += r1; g2 += g1; b2 += b1; break;
                            case SUBTRACT: r2 = r1-r2; g2 = g1-g2; b2 = b1-b2; break;
                            case MULTIPLY: r2 *= r1; g2 *= g1; b2 *= b1; break;
                            case DIVIDE: r2 = r2!=0.0?r1/r2:0.0; g2 = g2!=0.0?g1/g2:0.0; b2 = b2!=0.0?b1/b2:0.0; break;
                        }
                        r2 = r2 * k1 + k2;
                        g2 = g2 * k1 + k2;
                        b2 = b2 * k1 + k2;

                        r = (int) Math.floor((r2>255.0?255:(r2<0.0?0:r2))+.5);
                        g = (int) Math.floor((g2>255.0?255:(g2<0.0?0:g2))+.5);
                        b = (int) Math.floor((b2>255.0?255:(b2<0.0?0:b2))+.5);

                        ip2.putPixel(x, y,((r & 0xff)<<16)+((g & 0xff)<<8)+(b & 0xff));
                    }
                }
            }
            else{
                for (int x=0; x<width; x++) {
                    for (int y=0; y<height; y++) {
                        v1 = ip1.getPixelValue(x,y);
                        v2 = ip2.getPixelValue(x,y);
                        switch (operation) {
                            case SCALE: v2 = v1; break;
                            case ADD: v2 += v1; break;
                            case SUBTRACT: v2 = v1-v2; break;
                            case MULTIPLY: v2 *= v1; break;
                            case DIVIDE: v2 = v2!=0.0?v1/v2:0.0; break;
                        }
                        v2 = v2*k1 + k2;
                        ip2.putPixelValue(x, y, v2);
                    }
                }
            }

            if (n==currentSlice) {
                i2.getProcessor().resetMinAndMax();
                i2.updateAndDraw();
            }
            IJ.showProgress((double)n/slices2);
            IJ.showStatus(n+"/"+slices2);
        }
    }

}
