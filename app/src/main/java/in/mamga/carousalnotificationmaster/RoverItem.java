package in.mamga.carousalnotificationmaster;

import java.util.List;

/**
 * Created by Shailesh on 09/01/17.
 */

public class RoverItem {

    /**
     * id : 102685
     * sol : 1004
     * camera : {"id":20,"name":"FHAZ","rover_id":5,"full_name":"Front Hazard Avoidance Camera"}
     * img_src : http://mars.jpl.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/01004/opgs/edr/fcam/FLB_486615455EDR_F0481570FHAZ00323M_.JPG
     * earth_date : 2015-06-03
     * rover : {"id":5,"name":"Curiosity","landing_date":"2012-08-06","launch_date":"2011-11-26","status":"active","max_sol":1573,"max_date":"2017-01-08","total_photos":295745,"cameras":[{"name":"FHAZ","full_name":"Front Hazard Avoidance Camera"},{"name":"NAVCAM","full_name":"Navigation Camera"},{"name":"MAST","full_name":"Mast Camera"},{"name":"CHEMCAM","full_name":"Chemistry and Camera Complex"},{"name":"MAHLI","full_name":"Mars Hand Lens Imager"},{"name":"MARDI","full_name":"Mars Descent Imager"},{"name":"RHAZ","full_name":"Rear Hazard Avoidance Camera"}]}
     */

    private int id;
    private int sol;
    /**
     * id : 20
     * name : FHAZ
     * rover_id : 5
     * full_name : Front Hazard Avoidance Camera
     */

    private CameraBean camera;
    private String img_src;
    private String earth_date;
    /**
     * id : 5
     * name : Curiosity
     * landing_date : 2012-08-06
     * launch_date : 2011-11-26
     * status : active
     * max_sol : 1573
     * max_date : 2017-01-08
     * total_photos : 295745
     * cameras : [{"name":"FHAZ","full_name":"Front Hazard Avoidance Camera"},{"name":"NAVCAM","full_name":"Navigation Camera"},{"name":"MAST","full_name":"Mast Camera"},{"name":"CHEMCAM","full_name":"Chemistry and Camera Complex"},{"name":"MAHLI","full_name":"Mars Hand Lens Imager"},{"name":"MARDI","full_name":"Mars Descent Imager"},{"name":"RHAZ","full_name":"Rear Hazard Avoidance Camera"}]
     */

    private RoverBean rover;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSol() {
        return sol;
    }

    public void setSol(int sol) {
        this.sol = sol;
    }

    public CameraBean getCamera() {
        return camera;
    }

    public void setCamera(CameraBean camera) {
        this.camera = camera;
    }

    public String getImg_src() {
        return img_src;
    }

    public void setImg_src(String img_src) {
        this.img_src = img_src;
    }

    public String getEarth_date() {
        return earth_date;
    }

    public void setEarth_date(String earth_date) {
        this.earth_date = earth_date;
    }

    public RoverBean getRover() {
        return rover;
    }

    public void setRover(RoverBean rover) {
        this.rover = rover;
    }

    public static class CameraBean {
        private int id;
        private String name;
        private int rover_id;
        private String full_name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getRover_id() {
            return rover_id;
        }

        public void setRover_id(int rover_id) {
            this.rover_id = rover_id;
        }

        public String getFull_name() {
            return full_name;
        }

        public void setFull_name(String full_name) {
            this.full_name = full_name;
        }
    }

    public static class RoverBean {
        private int id;
        private String name;
        private String landing_date;
        private String launch_date;
        private String status;
        private int max_sol;
        private String max_date;
        private int total_photos;
        /**
         * name : FHAZ
         * full_name : Front Hazard Avoidance Camera
         */

        private List<CamerasBean> cameras;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLanding_date() {
            return landing_date;
        }

        public void setLanding_date(String landing_date) {
            this.landing_date = landing_date;
        }

        public String getLaunch_date() {
            return launch_date;
        }

        public void setLaunch_date(String launch_date) {
            this.launch_date = launch_date;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getMax_sol() {
            return max_sol;
        }

        public void setMax_sol(int max_sol) {
            this.max_sol = max_sol;
        }

        public String getMax_date() {
            return max_date;
        }

        public void setMax_date(String max_date) {
            this.max_date = max_date;
        }

        public int getTotal_photos() {
            return total_photos;
        }

        public void setTotal_photos(int total_photos) {
            this.total_photos = total_photos;
        }

        public List<CamerasBean> getCameras() {
            return cameras;
        }

        public void setCameras(List<CamerasBean> cameras) {
            this.cameras = cameras;
        }

        public static class CamerasBean {
            private String name;
            private String full_name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getFull_name() {
                return full_name;
            }

            public void setFull_name(String full_name) {
                this.full_name = full_name;
            }
        }
    }
}
