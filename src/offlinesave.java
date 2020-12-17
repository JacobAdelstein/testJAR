import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class offlinesave {

    public static void filewrite(measurementsCol data){



        BufferedWriter bw = null;
        try {
              String profiletype = "Profile Type:" + data.currentProfile.profileType;

              String profileName = "Profile Name:" + data.currentProfile.profileName;

              String submitAddress = "Submit Address:" + data.currentProfile.submitAddress;

              String sizeMin = "Size Min:" + data.currentProfile.sizeMin;

              String sizeMax = "Size Max:" + data.currentProfile.sizeMax;

              String circularityMin = "Circularity Min:" + data.currentProfile.circularityMin;

              String circularityMax = "Circularity Max:" + data.currentProfile.circularityMax;

              String distance = "Distance:" + data.currentProfile.distance;

              String known = "Known:" + data.currentProfile.known;

              String pixel = "Pixel:" + data.currentProfile.pixel;

              String enterMin = "enterMin:" + data.currentProfile.enterMin;

              String enterMax = "enterMax:" + data.currentProfile.enterMax;

              String exitMin =  "exitMin:" + data.currentProfile.exitMin;

              String exitMax = "exitMax" + data.currentProfile.exitMax;

              String lowerThreshold = "lowerThreshold" + data.currentProfile.lowerThreshold;

              String upperThreshold = "upperThreshold" + data.currentProfile.upperThreshold;

              String holeCount = "holeCount" + data.currentProfile.holeCount;


            //Specify the file name and path here
            File file = new File("C:/Users/jacob/IdeaProjects/testJAR/src/OfflineSaveResults.txt");

            /* This logic will make sure that the file
             * gets created if it is not present at the
             * specified location*/
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            bw.write(profiletype);
            bw.write(profileName);
            bw.write(submitAddress);
            bw.write(sizeMin);
            bw.write(sizeMax);
            bw.write(circularityMin);
            bw.write(circularityMax);
            bw.write(distance);
            bw.write(known);
            bw.write(pixel);
            bw.write(enterMin);
            bw.write(enterMax);
            bw.write(exitMax);
            bw.write(exitMin);
            bw.write(lowerThreshold);
            bw.write(upperThreshold);
            bw.write(holeCount);


            System.out.println("File written Successfully");

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        finally
        {
            try{
                if(bw!=null)
                    bw.close();
            }catch(Exception ex){
                System.out.println("Error in closing the BufferedWriter"+ex);
            }
        }
    }

    }












    

