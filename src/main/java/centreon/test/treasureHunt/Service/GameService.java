package centreon.test.treasureHunt.Service;

import centreon.test.treasureHunt.Domain.*;

import java.util.List;

public interface GameService {

    boolean isGameOver(List<Adventurer> adventurers);

    void sleepTilNextTurn();

    void collectTreasure(Map m, Adventurer a);

    void fillMapWithAdventurers(Map map, List<Adventurer> adventurers);

    void move(Map m, Adventurer a);

    void logActivity(List<Adventurer> adventurers);

}
