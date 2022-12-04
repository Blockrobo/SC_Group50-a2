package Gameplay;

import GameMaterial.Card;
import GameMaterial.CardType.*;
import GameMaterial.Dice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;


public class Player {

    private final String name;
    private int finalPoints;
    private int currentPoints;
    private int roundPoints;

    public static final int point5 = 50;
    public static final int point1 = 100;
    public static final int [] pointTriplet = new int[] {1000,200,300,400,500,600};
    public static final int pointPlusMinus =1000;
    public static final int pointStraight =2000;
    public static final int pointTuttoCloverleaf =2;

    ArrayList<Dice> diceChange = new ArrayList<Dice>();
    ArrayList<Dice> diceKeep = new ArrayList<Dice>();

    private int [] numberDice =new int[6];


    public Player(String n){
        this.name=n;
        this.currentPoints =0;
        this.finalPoints =0;
        this.roundPoints =0;
    }

    public String getName() {
        return name;
    }

    public int getFinalPoints() {
        return finalPoints;
    }

    public void setFinalPoints(int finalPoints) {
        this.finalPoints = finalPoints;
    }


    public boolean playerTurn(Card cardIs, ArrayList<Dice> lDice) throws IOException{
        System.out.print("\nYou drew: "+cardIs.getCard());
        if (cardIs instanceof Bonus){
            System.out.print(" "+((Bonus) cardIs).getBonusPoints());
        }
        System.out.println("");
        if (cardIs instanceof Stop){
            return false;
        }
        this.diceChange =lDice;
        for (Dice d : this.diceChange){
            d.rollDice();
        }
        System.out.println("");
        printDice("Dice kept:  ",this.diceKeep);
        printDice("Dice rolled: ",this.diceChange);


        System.out.println("");
        System.out.println("This round you earned: "+ roundPoints +" Points");
        System.out.println("Total Points: "+ currentPoints +"\n");

        if(checkNull(cardIs)){
            if (cardIs instanceof Fireworks){
                roundPoints += currentPoints;
                finalPoints += roundPoints;
            }
            currentPoints =0;
            roundPoints =0;
            diceKeep.clear();
            System.out.println("\nToo bad, you rolled a null! Your turn is over.");
            return false;
        }
        if (cardIs instanceof Fireworks){
            allDice();
            if (checkTutto()){
                this.diceChange = Game.createNewListDice(Dice.numberofDice);
                this.diceKeep.clear();
                return playerTurn(cardIs, diceChange);
            }
            else{
                return playerTurn(cardIs, diceChange);
            }
        }
        else if(cardIs instanceof PlusMinus || cardIs instanceof Cloverleaf || cardIs instanceof Straight){
            selectDice(cardIs,lDice);
            if (checkTutto()){

                if (cardIs instanceof Cloverleaf){
                    ((Cloverleaf)cardIs).setTimes(((Cloverleaf)cardIs).getTimes()+1);
                    if (((Cloverleaf)cardIs).getTimes()==2){
                        System.out.println("You accomplished a TUTTO twice in a row!!");
                        return false;
                    }
                    else{
                        this.diceChange = Game.createNewListDice(Dice.numberofDice);
                        diceKeep.clear();
                        return playerTurn(cardIs, diceChange);
                    }
                }
                else if (cardIs instanceof PlusMinus){
                    roundPoints += pointPlusMinus;
                    this.diceKeep.clear();
                    return ifTutto();
                }
                else if (cardIs instanceof Straight){
                    roundPoints += pointStraight;
                    this.diceKeep.clear();
                    return ifTutto();
                }
                return true;
            }
            else{
                return playerTurn(cardIs, diceChange);
            }
        }
        else{
            boolean correctLetter=false;
            while (!correctLetter){
                System.out.println("Enter R to roll the dice");
                System.out.println("Enter E to end turn");
                Scanner entry = new Scanner(System.in);
                String input ;
                input = entry.nextLine();
                String letter = input.substring(0,1);

                if (letter.length() != 1){
                    System.out.println("Enter R or E!");
                }
                else if (letter.equals("R")){
                    selectDice(cardIs,lDice);
                    if (checkTutto()){
                        if (cardIs instanceof Bonus){
                            roundPoints +=(this.currentPoints +((Bonus)cardIs).getBonusPoints());
                        }
                        else if (cardIs instanceof x2){
                            roundPoints +=(this.currentPoints *2);
                        }
                        this.currentPoints =0;
                        this.diceKeep.clear();
                        boolean keep= ifTutto();
                        if (!keep){
                            this.finalPoints +=this.roundPoints;
                            this.roundPoints =0;
                        }
                        return keep;
                    }
                    else{
                        return playerTurn(cardIs,this.diceChange);
                    }
                }
                else if (letter.equals("E")){
                    allDice();
                    if (checkTutto()){
                        if (cardIs instanceof Bonus){
                            roundPoints +=(this.currentPoints +((Bonus)cardIs).getBonusPoints());
                        }
                        else if (cardIs instanceof x2){
                            roundPoints +=(this.currentPoints *2);
                        }
                        this.currentPoints =0;
                        this.diceKeep.clear();
                    }
                    else{
                        this.finalPoints += this.currentPoints;

                    }
                    this.finalPoints += this.roundPoints;
                    this.roundPoints =0;
                    this.currentPoints =0;
                    this.diceKeep.clear();
                    return false;
                }
                else{
                    System.out.println("Enter R or E!");
                }
            }

        }
        return false;
    }


    private void printDice(String name,ArrayList<Dice> ld){
        System.out.print(name+"  ");
        for (Dice d : ld){
            System.out.print(d.getDice()+"  ");

        }
        System.out.println("");
    }


    private boolean checkNull(Card c){
        for (int i=0;i<6;i++){
            numberDice[i]=0;
        }
        for (int i = 0; i< diceChange.size(); i++){
            numberDice[diceChange.get(i).getDice()-1]++;
        }
        if (c instanceof Straight){
            if (diceKeep.size()==0){
                return false;
            }
            boolean nullthrow=true;
            for (int i = 0; i<this.diceChange.size(); i++){
                nullthrow=false;
                for (int j = 0; j< diceKeep.size(); j++){
                    if (diceChange.get(i).getDice()== diceKeep.get(j).getDice()){
                        nullthrow=true;
                    }
                }
                if (nullthrow==false){
                    return nullthrow;
                }
            }
            return nullthrow;
        }
        else{
            for (int i = 0; i< numberDice.length; i++){
                if (numberDice[i]>=3 || numberDice[0]!=0 || numberDice[4]!=0){
                    return false;
                }
            }
            return true;
        }
    }

    private void allDice() {
        for (int i = 0; i< numberDice.length; i++){
            if (numberDice[i]>=3){
                for (int j=0;j<3;j++){
                    Dice d=new Dice(i+1);
                    diceKeep.add(d);
                    rerollDice(d);
                }
                currentPoints += pointTriplet[i];
                numberDice[i]-=3;
            }
            if (numberDice[i]==3){
                for (int j = 0; j< numberDice[i]; j++){
                    Dice d=new Dice(i+1);
                    diceKeep.add(d);
                    rerollDice(d);
                }
                currentPoints += pointTriplet[i];
                numberDice[i]-=3;
            }
            else if (i != 0 && i != 4){
                numberDice[i]-= numberDice[i];
            }
        }
        if (numberDice[0]!=0){
            for (int k = 0; k< numberDice[0]; k++){
                Dice d=new Dice(1);
                diceKeep.add(d);
                rerollDice(d);
                currentPoints += point1;
            }
            numberDice[0]-= numberDice[0];
        }
        if (numberDice[4]!=0){
            for (int k = 0; k< numberDice[4]; k++){
                Dice d=new Dice(5);
                diceKeep.add(d);
                rerollDice(d);
                currentPoints += point5;
            }
            numberDice[4]-= numberDice[4];
        }

    }

    private void selectDice(Card c, ArrayList<Dice> ld) throws IOException{
        boolean selected=false;
        ArrayList<Integer> selectedDice = new ArrayList<Integer>();
        while (!selected){
            try{
                selectedDice.clear();
                System.out.println("Enter position of dice you want to keep (Ex. '2' to keep the second dice from the left): ");
                Scanner tec = new Scanner(System.in);
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String  lines = br.readLine();
                String[] strs = lines.trim().split("\\s+");

                for (int i = 0; i < strs.length; i++) {
                    selectedDice.add(Integer.parseInt(strs[i]));
                }

                if (c instanceof Straight){
                    selected = checkSelectionOrder(selectedDice);
                }
                else{
                    selected = checkDiceSelection(selectedDice);
                }
            }
            catch (Exception NumberFormatException){
                System.out.println("Enter a number between 1-6 for dice position!");
            }


        }
        for (int values: selectedDice){
            Dice d=new Dice(this.diceChange.get(values-1).getDice());
            this.diceKeep.add(d);
            if (!(c instanceof Straight) && !(c instanceof PlusMinus) && !(c instanceof Cloverleaf)){
                if (numberDice[diceChange.get(values-1).getDice()-1]>=3){
                    this.currentPoints += pointTriplet[diceChange.get(values-1).getDice()-1];
                    numberDice[diceChange.get(values-1).getDice()-1]-=3;
                }
                else if (diceChange.get(values-1).getDice()==1 && numberDice[diceChange.get(values-1).getDice()-1]!=0){
                    this.currentPoints += point1;
                    numberDice[diceChange.get(values-1).getDice()-1]--;
                }
                else if (diceChange.get(values-1).getDice()==5 && numberDice[diceChange.get(values-1).getDice()-1]!=0){
                    this.currentPoints += point5;
                    numberDice[diceChange.get(values-1).getDice()-1]--;
                }
            }
        }
        for (int i=0;i<selectedDice.size();i++){
            diceChange.remove(0);
        }
    }

    private boolean checkTutto() {
        if (diceKeep.size()==Dice.numberofDice){
            System.out.println("TUTTO!!!");
            return true;
        }
        return false;
    }

    private void rerollDice(Dice d) {
        for (int i = 0; i< diceChange.size(); i++){
            if (diceChange.get(i).getDice()==d.getDice()){
                diceChange.remove(i);
                return;
            }

        }
    }

    private boolean checkDiceSelection(ArrayList<Integer> selectedDice) {
        int [] bufferList=new int[6];
        for (int value : selectedDice){
            if (numberDice[diceChange.get(value-1).getDice()-1]<3 && diceChange.get(value-1).getDice() !=1 && diceChange.get(value-1).getDice() !=5){
                System.out.println("Wrong input! Try again");
                return false;
            }
            else{
                bufferList[diceChange.get(value-1).getDice()-1]++;
            }

        }
        for (int i=0;i<6;i++){
            if(bufferList[i]!=3 && bufferList[i]!=0 && i+1!=1 && i+1!=5){
                System.out.println("Wrong input! Try again");
                return false;
            }
        }

        return true;
    }

    private boolean checkSelectionOrder(ArrayList<Integer> selectedDice) {
        for (int value: selectedDice){
            for (int value2: selectedDice){
                if (diceChange.get(value-1).getDice()== diceChange.get(value2-1).getDice() && value!=value2){
                    System.out.println("Wrong input! Try again");
                    return false;
                }
            }
            for (Dice dice: diceKeep){
                if (dice.getDice()== diceChange.get(value-1).getDice()){
                    System.out.println("Wrong input! Try again");
                    return false;
                }
            }
        }
        return true;
    }

    private boolean ifTutto() {
        boolean playing=true;
        while (playing){
            System.out.println("Enter C to continue and draw a new card");
            System.out.println("End E to end turn");
            Scanner teclado = new Scanner(System.in);
            String letter=teclado.nextLine();
            if (letter.length() != 1){
                System.out.println("Wrong input! Try again");
            }
            else{
                if (!letter.equals("C") && !letter.equals("E")){
                    System.out.println("Enter C or E!");
                }
                else if (letter.equals("C")){
                    return true;
                }
                else if (letter.equals("E")){
                    this.finalPoints +=this.roundPoints;
                    this.roundPoints =0;
                    return false;
                }
            }
        }
        return false;
    }

}
