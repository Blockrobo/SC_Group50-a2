package GameMaterial;

import java.util.concurrent.ThreadLocalRandom;

public class Dice {
    public static final int numberofDice = 6;

    private int die;

    public void rollDice(){
        this.die = ThreadLocalRandom.current().nextInt(1, 6 + 1);
    }

    public int getDie(){
        return this.die;
    }

    public Dice(int num) {
        this.die = num;
    }

    public Dice() {
    }
}
