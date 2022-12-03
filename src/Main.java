import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;

import GameMaterial.Card;
import GameMaterial.CardType.Cloverleaf;
import GameMaterial.CardType.PlusMinus;
import GameMaterial.CardType.Stop;
import GameMaterial.Dice;
import Gameplay.Player;
import Gameplay.Game;


public class Main {

    public static final int MaxPoints = 6000;

    public static void main(String[] args) throws IOException {
        Game.printWelcome();

        int playerNum= Game.numberofPlayers();

        //ARRAY WITH THE PLAYERS ORDERED
        ArrayList<Player> listPlayer= Game.sortPlayers(playerNum);

        //DECK OF CARDS
        ArrayList<Card> deckCards = Game.Deck();

        //SET OF DICES
        ArrayList<Dice> dice = new ArrayList<Dice>();

        //PLAY TUTTO GAME
        boolean playerWon=false;

        while(!playerWon){
            for (int i=0;i<listPlayer.size();i++){
                System.out.println("\nIt's your turn "+listPlayer.get(i).getName()+"!");
                // System.out.println("\nIt's your turn Player "+(i+1)+": "+listPlayer.get(i).getName());
                boolean errorStart=true;
                String input="";

                while (errorStart){
                    System.out.println("Enter R to roll the dice");
                    System.out.println("Enter D to display the scores");
                    Scanner entry = new Scanner(System.in);
                    input = entry.nextLine();

                    if (input.length() != 1){
                        System.out.println("Enter R or D!");
                    }
                    else{
                        if (!input.equals("R") && !input.equals("D")){
                            System.out.println("Enter R or D!");
                        }
                        else if (input.equals("R")){
                            Card card= Game.getRandomCard(deckCards);
                            dice= Game.createNewListDice(Dice.numberofDice);
                            while (listPlayer.get(i).playerTurn(card, dice)){
                                if (card instanceof PlusMinus){ //WRONG
                                    Game.restFirstPlayer(listPlayer,listPlayer.get(i));
                                }
                                dice= Game.createNewListDice(Dice.numberofDice);
                                card= Game.getRandomCard(deckCards);
                            }
                            if (card instanceof Stop){
                                System.out.println("Tough luck! You flipped a Stop Card. Your turn is over.");
                            }

                            else if (card instanceof Cloverleaf){
                                if (((Cloverleaf)card).getTimes()==2){
                                    playerWon=true;
                                }
                            }
                            errorStart=false;
                        }
                        else{
                            Game.printCurrentScore(listPlayer);
                        }
                    }
                }
                if (listPlayer.get(i).getFinalPoints() >= MaxPoints){
                    playerWon=true;
                }
                if (playerWon){
                    System.out.println("\nCongrats, you won!!!" + listPlayer.get(i).getName());
                    System.out.println("Scoreboard:");
                    Game.printCurrentScore(listPlayer);
                    //System.out.println("GAME OVER");
                    break;
                }


            }
        }

    }

}
