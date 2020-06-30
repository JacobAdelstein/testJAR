import java.awt.*;

public class measurementsCol {

    measurements TL;
    measurements TR;
    measurements C;
    measurements BL;
    measurements BR;

    public String Test(){
        return "Test";
    }

    public void add(Integer position, Image image){
        switch (position) {
            case 1: {
                TL.add(image);
                System.out.println("Added Position 1");
            }
            case 2: {
                TR.add(image);
                System.out.println("Added Position 2");
            }
            case 3: {
                C.add(image);
                System.out.println("Added Position 3");
            }
            case 4: {
                BL.add(image);
                System.out.println("Added Position 4");
            }
            case 5: {
                BR.add(image);
                System.out.println("Added Position 5");
            }
        }

    }

}
