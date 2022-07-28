package centreon.test.treasureHunt.Service.impl;

import centreon.test.treasureHunt.Domain.Adventurer;
import centreon.test.treasureHunt.Domain.Coordinate;
import centreon.test.treasureHunt.Service.AdventurerService;
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
public class AdventurerServiceImpl implements AdventurerService {

    @Value("${adventurersTxtFilePath}")
    private String adventurersTxtFilePath;

    /**
     * Initialization of the adventurers from txt file
     * @return List of adventurers
     */
    public List<Adventurer> initAdventurers() {
        List<Adventurer> adventurers = new ArrayList<>();
        // On lit les infos depuis le fichier adventurers.txt
        try (BufferedReader br = new BufferedReader(new FileReader(this.adventurersTxtFilePath))) {
            String line;
            // Tant qu'il y a des lignes dans le fichier
            while ((line = br.readLine()) != null) {
                // On les découpe pour récupérer les informations relative aux aventuriers
                String[] info = line.split(" ");
                // Nom
                String name = info[0];
                // Coordonnées de départ
                Coordinate startingPosition = Coordinate.builder()
                        .x(Integer.parseInt(info[1].substring(0, 1)) - 1)
                        .y(Integer.parseInt(info[1].substring(2, 3)) - 1)
                        .build();
                // Orientation de départ
                Adventurer.Orientation startingOrientation = Adventurer.Orientation.valueOf(info[2]);
                // Séquence de mouvements
                String[] sequence = info[3].split("");
                List<Adventurer.Action> actions = new ArrayList<>();
                for (String action : sequence) {
                    actions.add(Adventurer.Action.valueOf(action));
                }
                // Chemin emprunté
                List<Coordinate> pathTaken = new ArrayList<>();
                pathTaken.add(startingPosition);
                // Build de l'aventurier
                Adventurer adv = Adventurer.builder()
                        .name(name)
                        .startingPosition(startingPosition)
                        .currentPosition(startingPosition)
                        .pathTaken(pathTaken)
                        .startingOrientation(startingOrientation)
                        .currentOrientation(startingOrientation)
                        .actions(actions)
                        .currentActionIndex(0)
                        .build();
                // Ajout à la liste
                adventurers.add(adv);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return adventurers;
    }

}
