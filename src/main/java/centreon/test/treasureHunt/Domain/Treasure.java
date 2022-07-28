package centreon.test.treasureHunt.Domain;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Treasure {

    private Coordinate coordinate;

    private int number;
}
