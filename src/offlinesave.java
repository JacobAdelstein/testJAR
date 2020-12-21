import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class offlinesave {






    public static void filewrite(measurementsCol data, int techid) throws IOException {

        //Specify the file name and path here
        File file = new File("C:/Users/jacob/IdeaProjects/testJAR/src/OfflineSaveResults.txt");

        /* This logic will make sure that the file
         * gets created if it is not present at the
         * specified location*/
        if (!file.exists()) {
            file.createNewFile();
        }



        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);

        try {
            bw.write("TechID:"+ techid);
            bw.write("\nDate:"+ LocalDate.now() + " "+ "Time:" + LocalTime.now());
              bw.write("\nProfile Type:" + data.currentProfile.profileType);

              bw.write("\nProfile Name:" + data.currentProfile.profileName);

              bw.write("\nSubmit Address:" + data.currentProfile.submitAddress);

              bw.write("\nSize Min:" + data.currentProfile.sizeMin);

              bw.write("\nSize Max:" + data.currentProfile.sizeMax);

              bw.write("\nCircularity Min:" + data.currentProfile.circularityMin);

              bw.write("\nCircularity Max:" + data.currentProfile.circularityMax);

              bw.write("\nDistance:" + data.currentProfile.distance);

              bw.write( "\nKnown:" + data.currentProfile.known);

              bw.write("\nPixel:" + data.currentProfile.pixel);

              bw.write( "\nenterMin:" + data.currentProfile.enterMin);

              bw.write("\nenterMax:" + data.currentProfile.enterMax);

              bw.write( "\nexitMin:" + data.currentProfile.exitMin);

              bw.write( "\nexitMax:" + data.currentProfile.exitMax);

              bw.write( "\nlowerThreshold:" + data.currentProfile.lowerThreshold);

              bw.write( "\nupperThreshold:" + data.currentProfile.upperThreshold);

              bw.write( "\nholeCount:" + data.currentProfile.holeCount);



              for (measurements measurement : data.measureList) {
                  bw.write("\n\nData for hole: " + measurement.positionname());
                  for (double result : measurement.results) {
                      bw.write("\n  " + result);
                  }
              }



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












    

