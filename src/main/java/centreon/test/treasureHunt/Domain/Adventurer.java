package centreon.test.treasureHunt.Domain;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Adventurer {

    private String name;

    private int collectedTreasures;

    private boolean isCollectingTreasure = false;

    private Coordinate startingPosition;

    private Coordinate currentPosition;

    private List<Coordinate> pathTaken = new ArrayList<>();

    private Orientation startingOrientation;

    private Orientation currentOrientation;

    private List<Action> actions = new ArrayList<>();

    private int currentActionIndex;

    private boolean isSequenceOver = false;

    public enum Orientation {
        N,
        E,
        W,
        S;
    }

    public enum Action {
        A,
        G,
        D;
    }
}
