package Gameplay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

import GameMaterial.CardType.Bonus;
import GameMaterial.CardType.Cloverleaf;
import GameMaterial.CardType.Fireworks;
import GameMaterial.CardType.PlusMinus;
import GameMaterial.CardType.Stop;
import GameMaterial.CardType.Straight;
import GameMaterial.CardType.x2;
import GameMaterial.Card;
import GameMaterial.Dice;


public class Game {

    public static final int numberofCards =56;

    public static final int numCloverleaf =1;
    public static final int numFirework =5;
    public static final int numStop =10;
    public static final int numStraight =5;
    public static final int numPlusMinus =5;
    public static final int numx2 =5;
    public static final int numBonus =25;
    public static final int [] BonusPoints =new int[] {200,300,400,500,600};


    public static void printWelcome(){
        System.out.println("Welcome to TUTTO!\n");
    }

    public static int numberofPlayers(){
        boolean validnPlayers=false;
        int nPlayer=0;

        while (!validnPlayers){
            try{
                System.out.println("Please enter number of players (min:2, max:4):");
                Scanner in = new Scanner(System.in);
                nPlayer = in.nextInt();
                if (nPlayer<2 || nPlayer>4){
                    System.out.println("Only 2 to 4 people can play!");
                }
                else{
                    validnPlayers=true;
                }
            }catch(Exception InputMismatchException) {
                System.out.println("Enter a number between 2-4!");
            }
        }
        return nPlayer;
    }

    public static ArrayList<Player> sortPlayers(int nPlayer){
        ArrayList<Player> listPlayer = new ArrayList<Player>();
        ArrayList<String> playerName=new ArrayList<String>();
        for (int i=0;i<nPlayer;i++){
            boolean validname=false;
            String name;
            while (!validname){
                System.out.println("Enter name for Player "+(i+1)+":");
                Scanner entry = new Scanner(System.in);
                name = entry.nextLine();
                String pName = name.substring(0,1);
                if (Character.isLetter(pName.charAt(0)) == true){
                    validname=true;
                    playerName.add(name);
                }
            }

        }
        Collections.sort(playerName);
        for(String temp: playerName){
            Player P1=new Player(temp);
            listPlayer.add(P1);
        }
        return listPlayer;
    }

    public static ArrayList<Card> Deck(){
        ArrayList<Card> deck = new ArrayList<Card>();
        for (int i = 0; i< numBonus; i++){
            Card c=new Bonus(BonusPoints[i%5]);
            deck.add(c);
        }
        for (int i = 0; i< numx2; i++){
            Card c=new x2();
            deck.add(c);
        }
        for (int i = 0; i< numStop; i++){
            Card c=new Stop();
            deck.add(c);
        }
        for (int i = 0; i< numFirework; i++){
            Card c=new Fireworks();
            deck.add(c);
        }
        for (int i = 0; i< numPlusMinus; i++){
            Card c=new PlusMinus();
            deck.add(c);
        }
        for (int i = 0; i< numCloverleaf; i++){
            Card c=new Cloverleaf();
            deck.add(c);
        }
        for (int i = 0; i< numStraight; i++){
            Card c=new Straight();
            deck.add(c);
        }
        return deck;
    }

    public static void printCurrentScore(ArrayList<Player> playerList){
        for (int i=0;i<playerList.size();i++){
            System.out.println(playerList.get(i).getName()+": "+playerList.get(i).getFinalPoints());
        }
    }
    public static ArrayList<Dice> createNewListDice(int len){
        ArrayList<Dice> lDice = new ArrayList<>();
        for (int i=0;i<len;i++ ){
            Dice d=new Dice();
            lDice.add(d);
        }
        return lDice;
    }

    public static Card getRandomCard(ArrayList<Card> listCard){
        Random rand=new Random();
        boolean notSelected=true;
        Card c=null;
        int counter=0;
        while(counter<listCard.size()-1 && listCard.get(counter).isUsed()==true){
            counter++;
        }
        if (counter==55){
            for (int i=0;i<listCard.size();i++){
                listCard.get(i).setUsed(false);
            }
        }
        while (notSelected){
            int position=rand.nextInt(56);
            if (listCard.get(position).isUsed()==false){
                listCard.get(position).setUsed(true);
                c= listCard.get(position);
                notSelected=false;
            }
        }
        return c;
    }

    public static void otherPlayers(ArrayList<Player> listPlayer, Player p) {
        int position=0;
        int score=0;
        for (int i=0;i<listPlayer.size();i++){
            if (! listPlayer.get(i).equals(p)){
                if (listPlayer.get(i).getFinalPoints()>score){
                    position=i;
                    score=listPlayer.get(i).getFinalPoints();
                }
            }

        }
        for (Player player: listPlayer){
            if (!player.equals(p) && player.getFinalPoints()==listPlayer.get(position).getFinalPoints() && !player.equals(listPlayer.get(position))){
                player.setFinalPoints(listPlayer.get(position).getFinalPoints()-1000);
            }
        }
        listPlayer.get(position).setFinalPoints(listPlayer.get(position).getFinalPoints()-1000);

    }
}
