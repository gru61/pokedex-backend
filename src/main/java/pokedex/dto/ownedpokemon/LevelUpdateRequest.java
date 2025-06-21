package pokedex.dto.ownedpokemon;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class LevelUpdateRequest {

    @Min(value = 1, message = "Why so weak!? (min lvl = 1)")
    @Max(value = 100, message = "Steroide sind nicht gut fürs Pokemon (max lvl = 100)")
    private int level;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
