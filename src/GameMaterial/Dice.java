package GameMaterial;

import java.util.concurrent.ThreadLocalRandom;

public class Dice {
    public static final int numberofDice = 6;

    private int dice;

    public void rollDice(){
        this.dice = ThreadLocalRandom.current().nextInt(1, 6 + 1);
    }

    public int getDice(){
        return this.dice;
    }

    public Dice(int num) {
        this.dice = num;
    }

    public Dice() {
    }
}
