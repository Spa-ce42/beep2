import org.millburn.e24feik.beep2.Beep;

import org.millburn.e24feik.beep2.util.Point;
import demo.*;

public class Demo {
    public static void main(String[] args) {
        Beep.init();

        //region students will implement their own ways of loading setup files
        SpaceContextLoader scl = new SpaceContextLoader("beesetup3.txt");
        SpaceContext sc = scl.load();
        scl.close();
        Space s = new Space(sc);
        //endregion

        //Specify the size
        Beep.setSize(
            sc.w,
            sc.h,
            sc.l
        );

        //region students will add the positions of their bees, exits, and obstacles to Beep
        for(Point p : sc.getBees()) {
            Beep.addBee(p.x, p.y, p.z);
        }

        for(Point p : sc.getExits()) {
            Beep.addExit(p.x, p.y, p.z);
        }

        for(Point p : sc.getObstacles()) {
            Beep.addObstacle(p.x, p.y, p.z);
        }
        //endregion

        Beep.show();

        Beep.addPoint(0, 0, 0, 1, 0, 0, 1);
        Beep.flushPoints();
    }
}
