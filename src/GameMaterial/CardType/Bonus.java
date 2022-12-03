package GameMaterial.CardType;

import GameMaterial.Card;

public class Bonus extends Card {

    private int bonusPoints;

    public Bonus(int bp) {
        super("Bonus Card");
        this.bonusPoints = bp;
    }

    public int getBonusPoints() {
        return bonusPoints;
    }

}
