import java.awt.*;
import java.util.ArrayList; // import the ArrayList class

public class measurementsCol {

    Integer partNum;

    ArrayList<measurements> measureList;
//    measurements TL;
//    measurements TR;
//    measurements C;
//    measurements BL;
//    measurements BR;


    public measurementsCol(Integer partNumber) {
        partNum = partNumber;
        measureList = new ArrayList<measurements>();
        System.out.println("MeasureList Size: " + measureList.size());

        for (int i=0; i < 5; i++) {
            measureList.add(new measurements(i+1, partNumber));
        }

        for (int i=0; i < measureList.size(); i++) {
            System.out.println("Setting measurement for partNum: " + partNum + " and Position: " + i+1);
        }

        System.out.println("Printing toString: ");
        System.out.println(this.toString());
    }


    public String toString() {
        System.out.println("Generating toString: ");

        StringBuilder returnString = new StringBuilder();
        for (int i=0; i < measureList.size(); i++) {
            returnString.append(measureList.get(i).toString() + "\n");
        }
        return returnString.toString();
    }

    public String Test(){
        return "Test";
    }

//    public void add(Integer position, Image image){
//        switch (position) {
//            case 1: {
//                TL.add(image);
//                System.out.println("Added Position 1");
//            }
//            case 2: {
//                TR.add(image);
//                System.out.println("Added Position 2");
//            }
//            case 3: {
//                C.add(image);
//                System.out.println("Added Position 3");
//            }
//            case 4: {
//                BL.add(image);
//                System.out.println("Added Position 4");
//            }
//            case 5: {
//                BR.add(image);
//                System.out.println("Added Position 5");
//            }
//        }
//
//    }

}
