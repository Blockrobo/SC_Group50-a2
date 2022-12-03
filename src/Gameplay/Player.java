package Gameplay;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import GameMaterial.CardType.Bonus;
import GameMaterial.CardType.Cloverleaf;
import GameMaterial.CardType.Fireworks;
import GameMaterial.CardType.PlusMinus;
import GameMaterial.CardType.Stop;
import GameMaterial.CardType.Straight;
import GameMaterial.CardType.x2;
import GameMaterial.Card;
import GameMaterial.Dice;


public class Player {

    private String name;
    private int finalPoints;
    private int currentPoints;
    private int roundPoints;

    public static final int point5 = 50;
    public static final int point1 = 100;
    public static final int [] pointTriplet = new int[] {1000,200,300,400,500,600};
    public static final int pointPlusMinus =1000;
    public static final int pointStraight =2000;
    //public static final int DEDUCTIONPLUSMINUS=1000;
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
        //Game.printIndex(diceChange.size());


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
                    //FALLA LA RESTA DE PUNTOS
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
                //Dice [] lnewDice=Utils.createNewListDice(6-dicesIn.size());
                return playerTurn(cardIs, diceChange);
            }
        }
        else{
            boolean correctLetter=false;
            while (!correctLetter){
                //System.out.println("Please Select one of the options:");
                System.out.println("Enter R to roll the dice");
                System.out.println("Enter E to end turn");
                Scanner entry = new Scanner(System.in);
                String input ;
                input = entry.nextLine();
                String letter = input.substring(0,1);

                if (letter.length() != 1){
                    System.out.println("Enter R or E!");
                }
                else if (letter.equals("R")){ //CONTINUE
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
                else if (letter.equals("E")){ //END THE TURN
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
            System.out.print(d.getDie()+"  ");

        }
        System.out.println("");
    }


    private boolean checkNull(Card c){
        for (int i=0;i<6;i++){
            numberDice[i]=0;
        }
        for (int i = 0; i< diceChange.size(); i++){
            numberDice[diceChange.get(i).getDie()-1]++;
        }
        if (c instanceof Straight){
            if (diceKeep.size()==0){
                return false;
            }
            boolean nullthrow=true;
            for (int i = 0; i<this.diceChange.size(); i++){
                nullthrow=false;
                for (int j = 0; j< diceKeep.size(); j++){
                    if (diceChange.get(i).getDie()== diceKeep.get(j).getDie()){
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
            if (numberDice[i]>=3){  //CASE YOU THROW 3 OR MORE DICES WITH THE SAME NUMBER
                for (int j=0;j<3;j++){
                    Dice d=new Dice(i+1);
                    diceKeep.add(d);
                    rerollDice(d);
                }
                currentPoints += pointTriplet[i];
                numberDice[i]-=3;
            }
            if (numberDice[i]==3){  //CASE YOU THROW 6 DICES WITH THE SAME NUMBER
                for (int j = 0; j< numberDice[i]; j++){
                    Dice d=new Dice(i+1);
                    diceKeep.add(d);
                    rerollDice(d);
                }
                currentPoints += pointTriplet[i];
                numberDice[i]-=3;
            }
            else if (i != 0 && i != 4){ //CASE YOU THROW 1-2 or 4-5 DICES WITH THE SAME NUMBER
                numberDice[i]-= numberDice[i];
            }
        }
        if (numberDice[0]!=0){ //CASE DICE NUMBER IS 1
            for (int k = 0; k< numberDice[0]; k++){
                Dice d=new Dice(1);
                diceKeep.add(d);
                rerollDice(d);
                currentPoints += point1;
            }
            numberDice[0]-= numberDice[0];
        }
        if (numberDice[4]!=0){ //CASE DICE NUMBER IS 5
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
        ArrayList<Integer> listOptions = new ArrayList<Integer>();
        while (!selected){
            try{
                listOptions.clear();
                System.out.println("Enter position of dice you want to keep (Ex. '2' to keep the second die from the left): ");
                Scanner tec = new Scanner(System.in);
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String  lines = br.readLine();
                String[] strs = lines.trim().split("\\s+");

                for (int i = 0; i < strs.length; i++) {
                    listOptions.add(Integer.parseInt(strs[i]));
                }
                //System.out.println(listOptions);


                if (c instanceof Straight){
                    selected= checkSelectionOrder(listOptions);
                }
                else{
                    selected= checkDiceSelection(listOptions);
                }
            }
            catch (Exception NumberFormatException){
                System.out.println("Enter a number between 1-6 for dice position!");
            }


        }
        for (int values: listOptions){
            Dice d=new Dice(this.diceChange.get(values-1).getDie());
            this.diceKeep.add(d);
            if (!(c instanceof Straight) && !(c instanceof PlusMinus) && !(c instanceof Cloverleaf)){
                if (numberDice[diceChange.get(values-1).getDie()-1]>=3){
                    this.currentPoints += pointTriplet[diceChange.get(values-1).getDie()-1];
                    numberDice[diceChange.get(values-1).getDie()-1]-=3;
                }
                else if (diceChange.get(values-1).getDie()==1 && numberDice[diceChange.get(values-1).getDie()-1]!=0){
                    this.currentPoints += point1;
                    numberDice[diceChange.get(values-1).getDie()-1]--;
                }
                else if (diceChange.get(values-1).getDie()==5 && numberDice[diceChange.get(values-1).getDie()-1]!=0){
                    this.currentPoints += point5;
                    numberDice[diceChange.get(values-1).getDie()-1]--;
                }
            }
        }
        for (int i=0;i<listOptions.size();i++){
            diceChange.remove(0);
        }
    }

    private boolean checkTutto() {
        if (diceKeep.size()==Dice.numberofDice){
            System.out.println("TUTTO!");
            return true;
        }
        return false;
    }

    private void rerollDice(Dice d) {
        for (int i = 0; i< diceChange.size(); i++){
            if (diceChange.get(i).getDie()==d.getDie()){
                diceChange.remove(i);
                return;
            }

        }
    }

    private boolean checkDiceSelection(ArrayList<Integer> listOptions) {
        int [] provisionalList=new int[6];
        for (int value : listOptions){
            if (numberDice[diceChange.get(value-1).getDie()-1]<3 && diceChange.get(value-1).getDie() !=1 && diceChange.get(value-1).getDie() !=5){
                System.out.println("Wrong input! Try again");
                return false;
            }
            else{
                provisionalList[diceChange.get(value-1).getDie()-1]++;
            }

        }
        for (int i=0;i<6;i++){
            if(provisionalList[i]!=3 && provisionalList[i]!=0 && i+1!=1 && i+1!=5){
                System.out.println("Wrong input! Try again");
                return false;
            }
        }

        return true;
    }

    private boolean checkSelectionOrder(ArrayList<Integer> listOptions) {
        for (int value: listOptions){
            for (int value2: listOptions){
                if (diceChange.get(value-1).getDie()== diceChange.get(value2-1).getDie() && value!=value2){
                    System.out.println("Wrong input! Try again");
                    return false;
                }
            }
            for (Dice dice: diceKeep){
                if (dice.getDie()== diceChange.get(value-1).getDie()){
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
