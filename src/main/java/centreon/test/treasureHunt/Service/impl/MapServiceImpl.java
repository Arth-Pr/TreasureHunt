package centreon.test.treasureHunt.Service.impl;

import centreon.test.treasureHunt.Domain.Coordinate;
import centreon.test.treasureHunt.Domain.Map;
import centreon.test.treasureHunt.Domain.Mountain;
import centreon.test.treasureHunt.Domain.Treasure;
import centreon.test.treasureHunt.Service.MapService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Configuration
public class MapServiceImpl implements MapService {

    @Value("${mapTxtFilePath}")
    private String mapTxtFilePath;

    /**
     * Initialization of the map from txt file
     * @return A Map
     */
    public Map initMap() {
        Map map = new Map();
        // On lit les infos depuis le fichier map.txt
        try (BufferedReader br = new BufferedReader(new FileReader(this.mapTxtFilePath))) {
            String line;
            // Tant qu'il y a des lignes dans le fichier
            while ((line = br.readLine()) != null) {
                // On lit la ligne courante
                switch (line.charAt(0)) {
                    // Si c'est un C, on récupère largeur+hauteur de la map
                    case 'C':
                        map.setWidth(Character.getNumericValue(line.charAt(2)));
                        map.setHeight(Character.getNumericValue(line.charAt(4)));
                        map.setOccupiedPositions(new ArrayList<>());
                        break;
                    // Si c'est un T, on créé la liste de trésors
                    case 'T':
                        Treasure t = Treasure.builder()
                                .coordinate(new Coordinate(Character.getNumericValue(line.charAt(2)) - 1, Character.getNumericValue(line.charAt(4)) - 1))
                                .number(Character.getNumericValue(line.charAt(6)))
                                .build();
                        map.getTreasures().add(t);
                        break;
                    // Si c'est un M, on créé la liste de montagnes
                    case 'M':
                        Mountain m = Mountain.builder()
                                .coordinate(new Coordinate(Character.getNumericValue(line.charAt(2)) - 1, Character.getNumericValue(line.charAt(4)) - 1))
                                .build();
                        map.getMountains().add(m);
                        break;
                    default:
                        System.out.println("Syntax error in map text file. Each line should start with C, T or M.");
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * Updates the occupied positions list of the map
     * @param map Map to update
     * @param currentPosition Position to change
     * @param newPosition Value to save
     */
    public void updateOccupiedPositions(Map map, Coordinate currentPosition, Coordinate newPosition) {
        List<Coordinate> updatedMap = new ArrayList<>();
        for(Coordinate occupied : map.getOccupiedPositions()) {
            // Hormis la position courante
            if (occupied != currentPosition){
                // On copie la liste de positions occupée
                updatedMap.add(occupied);
            }
        }
        // Et on ajoute la nouvelle position
        updatedMap.add(newPosition);
        map.setOccupiedPositions(updatedMap);
    }

    /**
     * Checks for treasures
     * @param m The map
     * @param c Coordinate to check
     * @return Boolean
     */
    public boolean isThereTreasure(Map m, Coordinate c) {
        // Si dans la liste des trésors
        for (Treasure t : m.getTreasures()) {
            // L'un d'eux partage ses coordonnées avec l'aventurier
            if (t.getCoordinate().getX() == c.getX() && t.getCoordinate().getY() == c.getY() && t.getNumber()>0) {
                // Alors on renvoie vrai
                return true;
            }
        }
        // Sinon faux
        return false;
    }

    /**
     * Checks for mountains
     * @param m The map
     * @param c Coordinate to check
     * @return Boolean
     */
    public boolean isThereMountain(Map m, Coordinate c) {
        // Si dans la liste des montagnes
        for (Mountain mountain : m.getMountains()) {
            // L'une d'elles partage ses coordonnées avec l'aventurier
            if (mountain.getCoordinate().getX() == c.getX() && mountain.getCoordinate().getY() == c.getY()) {
                // Alors on renvoie vrai
                return true;
            }
        }
        // Sinon faux
        return false;
    }

    /**
     * Checks for players
     * @param m The map
     * @param c Coordinate to check
     * @return Boolean
     */
    public boolean isOccupied(Map m, Coordinate c) {
        // Est-ce que la coordonnée c est occupée sur la carte m ?
        return m.getOccupiedPositions().contains(c);
    }

}
