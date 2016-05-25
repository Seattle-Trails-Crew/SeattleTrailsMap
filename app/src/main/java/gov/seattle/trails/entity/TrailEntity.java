package gov.seattle.trails.entity;

import java.io.Serializable;

/**
 * Created by randy.thedford on 4/17/16.
 */
public class TrailEntity implements Serializable {

    private String canopy;
    private String condition;
    private String gis_edt_dt; //edit data not needed
    private String gis_length;
    private String grade_perc;
    private String grade_type;
    private String pma_name;
    private String pmaid;
    private String shape_len;
    private String surface_ty;
    private GeoPathEntity the_geom; //how to pass data to this
    private String trail_clas;
    private String trail_id;
    private String trail_num;
    private String width;

    public TrailEntity() {
        //default constructor
    }

    public String getCanopy() {
        if (canopy == null) {
            return "";
        } else {
            return canopy;
        }
    }

    public String getCondition() {
        if (condition == null) {
            return "";
        } else {
            return condition;
        }
    }

    public String getGis_length() {
        if (gis_length == null) {
            return "";
        } else {
            return gis_length;
        }
    }

    public String getGrade_perc() {
        if (grade_perc == null) {
            return "";
        } else {
            return grade_perc;
        }
    }

    public String getGrade_type() {
        if (grade_type == null) {
            return "";
        } else {
            return grade_type;
        }
    }

    public String getPma_name() {
        if (pma_name == null) {
            return "";
        } else {
            return pma_name;
        }
    }

    public String getPmaid() {
        if (pmaid == null) {
            return "";
        } else {
            return pmaid;
        }
    }

    public String getShape_len() {
        if (shape_len == null) {
            return "";
        } else {
            return shape_len;
        }
    }

    public String getSurface_ty() {
        if (surface_ty == null) {
            return "";
        } else {
            return surface_ty;
        }
    }

    public GeoPathEntity getThe_geom() {

        return the_geom;
    } //TODO: null handler?

    public String getTrail_clas() {
        if (trail_clas == null) {
            return "";
        } else {
            return trail_clas;
        }
    }

    public String getTrail_id() {
        if (trail_id == null) {
            return "";
        } else {
            return trail_id;
        }
    }

    public String getTrail_num() {
        if (trail_num == null) {
            return "";
        } else {
            return trail_num;
        }
    }

    public String getWidth() {
        if (width == null) {
            return "";
        } else {
            return width;
        }
    }

    public String getGis_edt_dt() {

        return gis_edt_dt;
    }//TODO: null handler?

    public void setCanopy(String canopy) {
        this.canopy = canopy;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setGis_edt_dt(String gis_edt_dt) {
        this.gis_edt_dt = gis_edt_dt;
    }

    public void setGis_length(String gis_length) {
        this.gis_length = gis_length;
    }

    public void setGrade_perc(String grade_perc) {
        this.grade_perc = grade_perc;
    }

    public void setGrade_type(String grade_type) {
        this.grade_type = grade_type;
    }

    public void setPma_name(String pma_name) {
        this.pma_name = pma_name;
    }

    public void setPmaid(String pmaid) {
        this.pmaid = pmaid;
    }

    public void setShape_len(String shape_len) {
        this.shape_len = shape_len;
    }

    public void setSurface_ty(String surface_ty) {
        this.surface_ty = surface_ty;
    }

    public void setThe_geom(GeoPathEntity the_geom) {
        this.the_geom = the_geom;
    }

    public void setTrail_clas(String trail_clas) {
        this.trail_clas = trail_clas;
    }

    public void setTrail_id(String trail_id) {
        this.trail_id = trail_id;
    }

    public void setTrail_num(String trail_num) {
        this.trail_num = trail_num;
    }

    public void setWidth(String width) {
        this.width = width;
    }


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
