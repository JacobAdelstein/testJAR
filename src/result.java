
public class result {
    private int index;
    private double result;
    private boolean pass;


    public result(int index, double result) {
        this.index = index;
        this.result = result;
        this.pass = pass;
    }

    public int getIndex() {
        return index;
    }

    public boolean getPass() {
        return pass;
    }
    public double getResult() {
        gui.sysConsole.println("Getting result: " + result);
        return result;
    }
    public void setPass(boolean pass) {
        this.pass = pass;
    }


}
