package centreon.test.treasureHunt.Service;

import centreon.test.treasureHunt.Domain.*;

public interface MapService {

    Map initMap();

    void updateOccupiedPositions(Map map, Coordinate currentPosition, Coordinate newPosition);

    boolean isThereTreasure(Map m, Coordinate c);

    boolean isThereMountain(Map m, Coordinate c);

    boolean isOccupied(Map m, Coordinate c);

}
