public class inspectionProfile {
    String profileType;     //Type of profile, used to select inspection type
    String profileName;     //Friendly name of profile
    String submitAddress;   //Server subaddress
    double sizeMin;         //The minimum size recognized by the analysis algorithm
    double sizeMax;         //The maximum size recognized by the analysis algorithm
    double circularityMin;  //The minimum circularity recognized by the analysis algorithm
    double circularityMax;  //The maximum circularity recognized by the analysis algorithm
    double distance;
    double known;
    double pixel;
    double enterMin;
    double enterMax;


    double exitMin;
    double exitMax;
    double lowerThreshold;
    double upperThreshold;
    int holeCount;
    Boolean blackBackground;

    @Override
    public String toString() {
        StringBuilder returnString = new StringBuilder("\n\n------------Profile: " + this.profileName + " ------------\n");
        returnString.append("Profile Type: " + this.profileType);
        returnString.append("\nLower Threshold: " + this.lowerThreshold);
        returnString.append("\nUpper Threshold: " + this.upperThreshold);
        returnString.append("\nMinimum analysis size: " + this.sizeMin);
        returnString.append("\nMaximum analysis size: " + this.sizeMax);
        returnString.append("\nMinimum circularity: " + this.circularityMin);
        returnString.append("\nMaximum circularity: " + this.circularityMax);
        returnString.append("\nDistance: " + this.distance);
        returnString.append("\nKnown: " + this.known);
        returnString.append("\nPixel: " + this.pixel);
        returnString.append("\nMinimum exit size: " + this.exitMin);
        returnString.append("\nMaximum exit size: " + this.exitMax);
        returnString.append("\nMinimum enter size: " + this.exitMin);
        returnString.append("\nMaximum enter size: " + this.enterMax);
        returnString.append("\nBlackBackground: " + this.blackBackground);
        returnString.append("\nHole Count: " + this.holeCount);
        returnString.append("\nSubmit Address: " + this.submitAddress);

        returnString.append("\n<-------------------------------------------->\n\n");
        return returnString.toString();
    }





    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public double getSizeMin() {
        return sizeMin;
    }

    public void setSizeMin(double sizeMin) {
        this.sizeMin = sizeMin;
    }

    public double getSizeMax() {
        return sizeMax;
    }

    public void setSizeMax(double sizeMax) {
        this.sizeMax = sizeMax;
    }

    public double getCircularityMin() {
        return circularityMin;
    }

    public void setCircularityMin(double circularityMin) {
        this.circularityMin = circularityMin;
    }

    public double getCircularityMax() {
        return circularityMax;
    }

    public void setCircularityMax(double circularityMax) {
        this.circularityMax = circularityMax;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getKnown() {
        return known;
    }

    public void setKnown(double known) {
        this.known = known;
    }

    public double getPixel() {
        return pixel;
    }

    public void setPixel(double pixel) {
        this.pixel = pixel;
    }

    public double getEnterMin() {
        return enterMin;
    }

    public void setEnterMin(double enterMin) {
        this.enterMin = enterMin;
    }

    public double getEnterMax() {
        return enterMax;
    }

    public void setEnterMax(double enterMax) {
        this.enterMax = enterMax;
    }

    public double getExitMin() {
        return exitMin;
    }

    public void setExitMin(double exitMin) {
        this.exitMin = exitMin;
    }

    public double getExitMax() {
        return exitMax;
    }

    public void setExitMax(double exitMax) {
        this.exitMax = exitMax;
    }

    public double getLowerThreshold() {
        return lowerThreshold;
    }

    public void setLowerThreshold(double lowerThreshold) {
        this.lowerThreshold = lowerThreshold;
    }

    public double getUpperThreshold() {
        return upperThreshold;
    }

    public void setUpperThreshold(double upperThreshold) {
        this.upperThreshold = upperThreshold;
    }

    public Boolean getBlackBackground() {
        return blackBackground;
    }

    public void setBlackBackground(Boolean blackBackground) {
        this.blackBackground = blackBackground;
    }

    public inspectionProfile(String name) {
        profileName = name;
    }

    public int getHoleCount() {
        return holeCount;
    }

    public void setHoleCount(int holeCount) {
        this.holeCount = holeCount;
    }

    public void setProfileType(String profileType) {
        this.profileType = profileType;

    }

    public String getProfileType() {
        return profileType;
    }

    public void setsubmitAddress(String address) {
        this.submitAddress = address;

    }

    public String getsubmitAddress() {
        return submitAddress;
    }


}
