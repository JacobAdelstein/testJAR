public class inspectionProfile {
    String profileName;
    double sizeMin;
    double sizeMax;
    double circularityMin;
    double circularityMax;
    double distance;
    double known;
    double pixel;
    double feretMin;
    double feretMax;
    double lowerThreshold;
    double upperThreshold;
    Boolean blackBackground;

    @Override
    public String toString() {
        StringBuilder returnString = new StringBuilder("\n\n------------Profile: " + this.profileName + " ------------\n");
        returnString.append("Lower Threshold: " + this.lowerThreshold);
        returnString.append("\nUpper Threshold: " + this.upperThreshold);
        returnString.append("\nMinimum analysis size: " + this.sizeMin);
        returnString.append("\nMaximum analysis size: " + this.sizeMax);
        returnString.append("\nMinimum circularity: " + this.circularityMax);
        returnString.append("\nMaximum circularity: " + this.circularityMax);
        returnString.append("\nDistance: " + this.distance);
        returnString.append("\nKnown: " + this.known);
        returnString.append("\nPixel: " + this.pixel);
        returnString.append("\nMinimum Passing: " + this.feretMin);
        returnString.append("\nMaximum Passing" + this.feretMax);
        returnString.append("\nBlackBackground: " + this.blackBackground);
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

    public double getFeretMin() {
        return feretMin;
    }

    public void setFeretMin(double feretMin) {
        this.feretMin = feretMin;
    }

    public double getFeretMax() {
        return feretMax;
    }

    public void setFeretMax(double feretMax) {
        this.feretMax = feretMax;
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


}
