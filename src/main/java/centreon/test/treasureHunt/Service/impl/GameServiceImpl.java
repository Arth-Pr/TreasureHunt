package centreon.test.treasureHunt.Service.impl;

import centreon.test.treasureHunt.Domain.Adventurer;
import centreon.test.treasureHunt.Domain.Coordinate;
import centreon.test.treasureHunt.Domain.Map;
import centreon.test.treasureHunt.Domain.Treasure;
import centreon.test.treasureHunt.Service.GameService;
import centreon.test.treasureHunt.Service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Configuration
@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private MapService mapService;

    @Value("${resultsTxtFilePath}")
    private String resultsTxtFilePath;

    /**
     * Checks if the game is over
     * @param adventurers Adventurers to check
     * @return Boolean
     */
    public boolean isGameOver(List<Adventurer> adventurers) {
        for (Adventurer a : adventurers) {
            // Si un seul des aventuriers n'a pas terminé sa séquence, ou qu'il est en train de collecter
            if (!a.isSequenceOver() || a.isCollectingTreasure())
                // Alors la partie n'est pas terminée
                return false;
        }
        // Sinon, partie terminée
        System.out.println("Every one has finished his sequence and is no more collecting treasures !");
        return true;
    }

    /**
     * Pauses the game for one second
     */
    public void sleepTilNextTurn() {
        try {
            // Pause d'une seconde entre chaque tour
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * Fills the map with the adventurers
     * @param map The map
     * @param adventurers Adventurers to add on the map occupied positions list
     */
    public void fillMapWithAdventurers(Map map, List<Adventurer> adventurers) {
        System.out.println();
        // Pour chaque aventurier
        for (Adventurer a : adventurers) {
            // Si ses coordonnées de départ ne sont pas présentes dans la liste des positions occupées de la carte
            if (!map.getOccupiedPositions().contains(a.getStartingPosition())) {
                // La position est désormais occupée
                map.getOccupiedPositions().add(a.getStartingPosition());
                System.out.println(a.getName()+" starts on "+a.getCurrentPosition());
            } else {
                // Si déjà occupée, on stop l'exécution pour correction dans le txt
                System.out.println("Two adventurers can't start on the same point !");
                System.exit(1);
            }
        }
        System.out.println();
    }


    /**
     * An adventurer collects
     * @param m The map
     * @param a The adventurer that collects
     */
    public void collectTreasure(Map m, Adventurer a) {
        for (Treasure t : m.getTreasures()) {
            // Le trésor à la postion de l'aventurier
            if (t.getCoordinate().getX() == a.getCurrentPosition().getX() && t.getCoordinate().getY() == a.getCurrentPosition().getY()) {
                // Perd un compteur
                t.setNumber(t.getNumber()-1);
                // Et le compteur de trésor de l'aventurier s'incrémente
                a.setCollectedTreasures(a.getCollectedTreasures()+1);
                System.out.println(a.getName()+" is collecting! He has "+a.getCollectedTreasures()+" treasure(s).");
            }
        }
    }


    /**
     * Moves an adventurer on the map
     * @param m The map
     * @param a The adventurer to move
     */
    public void move(Map m, Adventurer a) {
        // Un aventurier qui se déplace ne collecte pas
        a.setCollectingTreasure(false);
        // Si la séquence de l'aventurier n'est pas terminée
        if (!a.isSequenceOver()) {
            Adventurer.Action currentAction = a.getActions().get(a.getCurrentActionIndex());
            // On exécute l'action en cours
            switch (currentAction) {
                // Avancer
                case A:
                    switch (a.getCurrentOrientation()) {
                        // En fonction de l'orientation
                        case E:
                            Coordinate towardsEast = new Coordinate(a.getCurrentPosition().getX() + 1, a.getCurrentPosition().getY());
                            advancing(m, a, towardsEast);
                            break;
                        case S:
                            Coordinate towardsSouth = new Coordinate(a.getCurrentPosition().getX(), a.getCurrentPosition().getY() + 1);
                            advancing(m, a, towardsSouth);
                            break;
                        case W:
                            Coordinate towardsWest = new Coordinate(a.getCurrentPosition().getX() - 1, a.getCurrentPosition().getY());
                            advancing(m, a, towardsWest);
                            break;
                        case N:
                            Coordinate towardsNorth = new Coordinate(a.getCurrentPosition().getX(), a.getCurrentPosition().getY() - 1);
                            advancing(m, a, towardsNorth);
                            break;
                    }
                    break;
                // Tourner sur la droite
                case D:
                    switch (a.getCurrentOrientation()) {
                        // En fonction de l'orientation
                        case E:
                            a.setCurrentOrientation(Adventurer.Orientation.valueOf("S"));
                            System.out.println(a.getName()+" is rotating South.");
                            break;
                        case S:
                            a.setCurrentOrientation(Adventurer.Orientation.valueOf("W"));
                            System.out.println(a.getName()+" is rotating West.");
                            break;
                        case W:
                            a.setCurrentOrientation(Adventurer.Orientation.valueOf("N"));
                            System.out.println(a.getName()+" is rotating North.");
                            break;
                        case N:
                            a.setCurrentOrientation(Adventurer.Orientation.valueOf("E"));
                            System.out.println(a.getName()+" is rotating East.");
                            break;
                    }
                    break;
                // Tourner sur la gauche
                case G:
                    switch (a.getCurrentOrientation()) {
                        // En fonction de l'orientation
                        case E:
                            a.setCurrentOrientation(Adventurer.Orientation.valueOf("N"));
                            System.out.println(a.getName()+" is rotating North.");
                            break;
                        case S:
                            a.setCurrentOrientation(Adventurer.Orientation.valueOf("E"));
                            System.out.println(a.getName()+" is rotating East.");
                            break;
                        case W:
                            a.setCurrentOrientation(Adventurer.Orientation.valueOf("S"));
                            System.out.println(a.getName()+" is rotating South.");
                            break;
                        case N:
                            a.setCurrentOrientation(Adventurer.Orientation.valueOf("W"));
                            System.out.println(a.getName()+" is rotating West.");
                            break;
                    }
                    break;
            }
            // S'il reste des actions dans la séquence, on incrémente l'action courante
            if (a.getCurrentActionIndex() < a.getActions().size() - 1) {
                a.setCurrentActionIndex(a.getCurrentActionIndex() + 1);
            } else {
                // Sinon, la séquence de l'aventurer est terminée
                a.setSequenceOver(true);
                System.out.println(a.getName()+" has finished his sequence.");
            }
            // Si il y'a un trésor sur la position actuelle
            if (mapService.isThereTreasure(m, a.getCurrentPosition())) {
                // L'aventurier collecte
                a.setCollectingTreasure(true);
            }

        }
    }

    /**
     * Updates to be done when an adventurer moves
     * @param m The map
     * @param a The adventurer that advances
     * @param towardsC Targeted coordinates
     */
    private void advancing(Map m, Adventurer a, Coordinate towardsC) {
        // Si il n'y a pas de montagne ou de joueur sur la case visée
        if (!mapService.isThereMountain(m, towardsC) && !mapService.isOccupied(m, towardsC)) {
            // On change la position de l'aventurier
            a.setCurrentPosition(towardsC);
            // On sauvegarde son chemin
            a.getPathTaken().add(towardsC);
            // On met à jour les emplacements occupés sur la carte
            mapService.updateOccupiedPositions(m, a.getCurrentPosition(), towardsC);
            System.out.println(a.getName()+" moved on "+ towardsC);
        } else {
            System.out.println("Something is on the way of "+a.getName()+"!");
        }
    }

    /**
     * Logs the adventurers activity in txt file
     * @param adventurers Adventurers to log
     */
    public void logActivity(List<Adventurer> adventurers) {
        try {
            // On écrit dans le fichier results.txt
            FileWriter myWriter = new FileWriter(this.resultsTxtFilePath);
            // les infos sauvegardées de chaque aventurier :
            for (Adventurer adv : adventurers) {
                // Nom
                myWriter.write(adv.getName()+" ");
                // Chemin emprunté
                for (Coordinate c : adv.getPathTaken()) {
                    int x = c.getX()+1;
                    int y = c.getY()+1;
                    myWriter.write("[" + x + "-" + y + "]");
                }
                // Nb de trésors collectés
                myWriter.write(" "+adv.getCollectedTreasures()+System.lineSeparator());
            }
            myWriter.close();
            System.out.println("Successfully wrote results to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
