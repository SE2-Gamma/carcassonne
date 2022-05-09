package at.aau.se2.gamma.core.models.impl;

import at.aau.se2.gamma.core.factories.GameCardFactory;

import java.util.LinkedList;

public class Deck {
    LinkedList<GameCard>deck=new LinkedList<>();

    public Deck(int multfaktor) {
        deck= GameCardFactory.getDeck(multfaktor);
    }

}
