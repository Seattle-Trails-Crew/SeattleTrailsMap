package gov.seattle.trails.entity;

import java.io.Serializable;

/**
 * Created by randy.thedford on 4/17/16.
 */
public class TrailEntity implements Serializable{

    private String canopy;
    private String condition;
    private String gis_edt_dt;
    private String gis_length;
    private String grade_perc;
    private String grade_type;
    private String pma_name;
    private String pmaid;
    private String shape_len;
    private String surface_ty;
    private GeoPathEntity the_geom;
    private String trail_clas;
    private String trail_id;
    private String trail_num;
    private String width;


}
//Json Example
    /*
    {
    "canopy": "High",
    "condition": "Good",
    "gis_edt_dt": "2016-03-10T00:00:00.000Z",
    "gis_length": "30.3516717",
    "grade_perc": "2",
    "grade_type": "Flat",
    "pma_name": "Ravenna Park",
    "pmaid": "391",
    "shape_len": "0",
    "surface_ty": "Gravel",
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
    "trail_clas": "0",
    "trail_id": "391-91",
    "trail_num": "91",
    "width": "5"
  }
     */
