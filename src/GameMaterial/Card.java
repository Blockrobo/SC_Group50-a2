package GameMaterial;

public abstract class Card {

    private boolean used;
    private String name;

    public Card(String name){
        this.used=false;
        this.name=name;
    }

    public String getCard() {
        return name;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public boolean isUsed() {
        return used;
    }

}
