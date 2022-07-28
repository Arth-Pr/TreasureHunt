package centreon.test.treasureHunt.Domain;

import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Map {

    private int width;

    private int height;

    private List<Treasure> treasures = new ArrayList<>();

    private List<Mountain> mountains = new ArrayList<>();

    private List<Coordinate> occupiedPositions = new ArrayList<>();

}
