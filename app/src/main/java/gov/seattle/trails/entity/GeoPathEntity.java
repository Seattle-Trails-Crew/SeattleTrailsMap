package gov.seattle.trails.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by randy.thedford on 4/17/16.
 */
public class GeoPathEntity implements Serializable {

    private String type;
    public List<float[]> coordinates = new ArrayList<>();

    public void setCoordinates(List<float[]> coordinates) {

        this.coordinates = coordinates;
  }

    public List<float[]> getCoordinates() {

        return this.coordinates;
    }
}
    //Json Example
    /*
    "the_geom": {
      "type": "LineString",
      "coordinates": [
        [
          -122.30378643028492,
          47.67267506799566
        ],
        [
          -122.30374984701724,
          47.6726554908045
        ],
        [
          -122.30372767352452,
          47.672642923516186
        ],
        [
          -122.30370034840142,
          47.672627725713994
        ],
        [
          -122.30369106494473,
          47.67262240295352
        ]
      ]
    },
     */
