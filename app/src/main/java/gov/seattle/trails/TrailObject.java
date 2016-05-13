package gov.seattle.trails;

/**
 * Created by Quinn on 5/13/16.
 */
public class TrailObject {

    String pma_name;
    String canopy;
    String condition;
    int gradePercent;
    String gradeType;
    String surfaceType;
    int pmaid;
    long[] coordinates;
    long gisLength;

    public TrailObject(int pmaid, String name, String surfaceType) {
        this.pma_name = name;
        this.pmaid = pmaid;
        this.surfaceType = surfaceType;
    }


}
