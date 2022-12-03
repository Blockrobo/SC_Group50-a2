package GameMaterial.CardType;

import GameMaterial.Card;

public class Cloverleaf extends Card {
    private int times;

    public Cloverleaf() {
        super("Cloverleaf Card");
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

}
