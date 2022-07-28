package centreon.test.treasureHunt;

import centreon.test.treasureHunt.Domain.Adventurer;
import centreon.test.treasureHunt.Domain.Map;
import centreon.test.treasureHunt.Service.AdventurerService;
import centreon.test.treasureHunt.Service.GameService;
import centreon.test.treasureHunt.Service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringBootApplication
public class TreasureHuntApplication {

    @Autowired
    private MapService mapService;
    @Autowired
    private AdventurerService adventurerService;
    @Autowired
    private GameService gameService;

    public static void main (String[]args) {
        SpringApplication.run(TreasureHuntApplication.class, args);
    }

    @PostConstruct
    public void startGame() {

        // Initialisation de la carte et des aventuriers depuis les fichiers txt
        Map map = mapService.initMap();
        List<Adventurer> adventurers = adventurerService.initAdventurers();

        // Remplissage de la carte avec les aventuriers
        gameService.fillMapWithAdventurers(map, adventurers);

        // Boucle de jeu
        while(!gameService.isGameOver(adventurers)) {
            System.out.println("NEW TURN\n");
            // Pour chaque aventurier en jeu
            for(Adventurer adv : adventurers) {
                // On regarde s'il est sur une case avec un trésor
                if (mapService.isThereTreasure(map, adv.getCurrentPosition())){
                    // Si c'est le cas, il collecte
                    gameService.collectTreasure(map, adv);
                    // Sinon, si la séquence de déplacement n'est pas terminée
                } else if (!adv.isSequenceOver()) {
                    // On poursuit le déplacement
                    gameService.move(map, adv);
                    // Sinon, on stoppe la collecte
                } else {
                    adv.setCollectingTreasure(false);
                }
            }
            System.out.println("\nEND OF TURN\n__________________");
            // Sleep d'une seconde avant le prochain tour
            gameService.sleepTilNextTurn();
        }
        // Log des résultats dans le fichier results.txt
        gameService.logActivity(adventurers);
    }

}
