package at.aau.se2.gamma.core.models.impl;

import at.aau.se2.gamma.core.factories.GameCardFactory;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Random;

public class Deck implements Serializable {


    LinkedList<GameCard>deck;
    public void printDeck(){
        for (GameCard card:deck
        ) {
            System.out.print("//"+card.getCardId()+"//");

        }
        System.out.print("//decksize: "+deck.size()+"//");
    }
    public GameCard drawCard() throws NoSuchElementException{
        GameCard card= deck.pop();
        System.out.print("//card "+card.getCardId()+" drawn. remaining cards:"+deck.size()+"//");
            return card;
    }
    public void putBackCard(GameCard card){
        deck.addLast(card);
    }
    public Deck(int multfaktor) {
        deck= GameCardFactory.getDeck(multfaktor);

        GameCard[] arr=new GameCard[deck.size()];
        deck.toArray(arr);
        arrayShuffle(arr);
        deck.clear();
        deck.addAll(Arrays.asList(arr));
        deck.addFirst(GameCardFactory.J());


    }
    static void arrayShuffle(Object[] arr)
    {
        Random rand = new Random();
        for (int i = 0; i < arr.length; i++) {

            // select index randomly
            int index = rand.nextInt(i + 1);

            // swapping between i th term and the index th
            // term
            Object g = arr[index];
            arr[index] = arr[i];
            arr[i] = g;
        }
    }

}
