package at.aau.se2.gamma.core.factories;

import at.aau.se2.gamma.core.models.impl.GameCard;

import java.util.LinkedList;

public class GameCardFactory {
    private GameCardFactory() {}

public static LinkedList<GameCard> getDeck(int multfaktor){
        LinkedList<GameCard> deck=new LinkedList<>();

    for (int i = 0; i < multfaktor; i++) {
        deck.add(A());
        deck.add(A());
        deck.add(B());
        deck.add(B());
        deck.add(B());
        deck.add(B());
        deck.add(C());
        deck.add(D());
        deck.add(D());
        deck.add(D());
        deck.add(E());
        deck.add(E());
        deck.add(E());
        deck.add(E());
        deck.add(E());
        deck.add(F());
        deck.add(F());
        deck.add(G());
        deck.add(H());
        deck.add(H());
        deck.add(H());
        deck.add(I());
        deck.add(I());
        deck.add(J());
        deck.add(J());
        deck.add(J());
        deck.add(K());
        deck.add(K());
        deck.add(K());
        deck.add(L());
        deck.add(L());
        deck.add(L());
        deck.add(M());
        deck.add(M());
        deck.add(N());
        deck.add(N());
        deck.add(N());
        deck.add(O());
        deck.add(O());
        deck.add(P());
        deck.add(P());
        deck.add(P());
        deck.add(Q());
        deck.add(R());
        deck.add(R());
        deck.add(R());
        deck.add(S());
        deck.add(S());
        deck.add(T());
        deck.add(U());
        deck.add(U());
        deck.add(U());
        deck.add(U());
        deck.add(U());
        deck.add(U());
        deck.add(U());
        deck.add(U());
        deck.add(V());
        deck.add(V());
        deck.add(V());
        deck.add(V());
        deck.add(V());
        deck.add(V());
        deck.add(V());
        deck.add(V());
        deck.add(V());
        deck.add(W());
        deck.add(W());
        deck.add(W());
        deck.add(W());
        deck.add(X());
    }


        return deck;
}
        //todo: implement wappen on cards

    public static GameCard createMonasteryGrassGrassGrassStreet() {
        return new GameCard(
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createClosedStreetSide(), null, "A");
    }
    public static GameCard createMonasteryGrassGrassGrassGrass() {
        return new GameCard(
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createMonasteryMidSide(),
                GameCard.SpecialType.MONASTERY,
                "B");
    }
    public static GameCard createCastleCastleCastleCastle() {
        return new GameCard(
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createCastleSide(),

                null, "C");
    }
    public static GameCard createGrassStreetCcastleStreet() {
        return new GameCard(
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createStreetSide(),
                GameCardSideFactory.createClosedCastleSide(),
                GameCardSideFactory.createStreetSide(),
               null, "D");
    }
    public static GameCard createGrassCcastleGrassGrass() {
        return new GameCard(
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createClosedCastleSide(),
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createGrasSide(),



                null, "E");
    }
    public static GameCard createCastleCgrassCastleCgrass() {
        return new GameCard(
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createClosedGrasSide(),
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createClosedGrasSide(),

                null, "F");
    }
    public static GameCard createCgrassCastleCgrassCastle() {
        return new GameCard(
                GameCardSideFactory.createClosedGrasSide(),
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createClosedGrasSide(),
                GameCardSideFactory.createCastleSide(),



                null, "G");
    }
    public static GameCard createGrassCcastleGrassCcastle() {
        return new GameCard(
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createClosedCastleSide(),
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createClosedCastleSide(),



                null, "H");
    }
    public static GameCard createGrassGrassCcastleCcastle() {
        return new GameCard(
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createClosedCastleSide(),
                GameCardSideFactory.createClosedCastleSide(),

                null, "I");
    }
    public static GameCard createGrassCcastleStreetStreet() {
        return new GameCard(
                GameCardSideFactory.createClosedGrasSide(),
                GameCardSideFactory.createClosedCastleSide(),
                GameCardSideFactory.createStreetSide(),
                GameCardSideFactory.createStreetSide(),

                null, "J");
    }
    public static GameCard createStreetStreetCcastleGrass() {
        return new GameCard(
                GameCardSideFactory.createStreetSide(),
                GameCardSideFactory.createStreetSide(),
                GameCardSideFactory.createClosedCastleSide(),
                GameCardSideFactory.createGrasSide(),


                null, "K");
    }
    public static GameCard createCstreetCstreetCcastleCstreet() {
        return new GameCard(
                GameCardSideFactory.createClosedStreetSide(),
                GameCardSideFactory.createClosedStreetSide(),
                GameCardSideFactory.createClosedCastleSide(),
                GameCardSideFactory.createClosedStreetSide(),



                null, "L");
    }

    public static GameCard createCcastleCcastleGrassGrassW() {  //todo checkclosingsides
        return new GameCard(
                GameCardSideFactory.createClosedCastleSide(),
                GameCardSideFactory.createClosedCastleSide(),
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createGrasSide(),



                null, "M");
    }
    public static GameCard createCcastleCcastleGrassGrass() {
        return new GameCard(
                GameCardSideFactory.createClosedCastleSide(),
                GameCardSideFactory.createClosedCastleSide(),
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createGrasSide(),



                null, "N");
    }
    public static GameCard createCcastleCcastleStreetStreetW() {
        return new GameCard(
                GameCardSideFactory.createClosedCastleSide(),
                GameCardSideFactory.createClosedCastleSide(),
                GameCardSideFactory.createStreetSide(),
                GameCardSideFactory.createStreetSide(),



                null, "O");
    }
    public static GameCard createCcastleCcastleStreetStreet() {
        return new GameCard(
                GameCardSideFactory.createClosedCastleSide(),
                GameCardSideFactory.createClosedCastleSide(),
                GameCardSideFactory.createStreetSide(),
                GameCardSideFactory.createStreetSide(),



                null, "P");
    }
    public static GameCard createCastleCastleCastleCgrassW() {
        return new GameCard(
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createGrasSide(),



                null, "Q");
    }
    public static GameCard createCastleCastleCastleCgrass() {
        return new GameCard(
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createGrasSide(),


                null, "R");
    }
    public static GameCard createCastleCastleCastleStreetW() {
        return new GameCard(
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createStreetSide(),


                null, "S");
    }
    public static GameCard createCastleCastleCastleStreet() {
        return new GameCard(
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createStreetSide(),


                null, "T");
    }
    public static GameCard createCgrassStreetCgrassStreet() {
        return new GameCard(
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createStreetSide(),
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createStreetSide(),


                null, "U");
    }
    public static GameCard createStreetGrassGrassStreet() {
        return new GameCard(
                GameCardSideFactory.createStreetSide(),
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createStreetSide(),


                null, "V");
    }
    public static GameCard createCstreetCGrassCstreetCstreet() {
        return new GameCard(
                GameCardSideFactory.createClosedStreetSide(),
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createClosedStreetSide(),
                GameCardSideFactory.createClosedStreetSide(),

                null, "W");
    }
    public static GameCard createCstreetCstreetCstreetCstreet() {
        return new GameCard(

                GameCardSideFactory.createClosedStreetSide(),
                GameCardSideFactory.createClosedStreetSide(),
                GameCardSideFactory.createClosedStreetSide(),
                GameCardSideFactory.createClosedStreetSide(),


                null, "X");
    }
    public static GameCard createCastleCastleGrasStreet() {
        return new GameCard(
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createStreetSide(), null, "V");
    }

    public static GameCard createGrasCastleGrasStreet() {
        return new GameCard(
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createStreetSide(), null, "1");
    }
    public static GameCard A() {
        return new GameCard(
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createClosedStreetSide(),
                GameCardSideFactory.createMonasteryMidSide(),
                GameCard.SpecialType.MONASTERY, "A");
    }
    public static GameCard B() {
        return new GameCard(
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createMonasteryMidSide(),
                GameCard.SpecialType.MONASTERY, "B");
    }
    public static GameCard C() {
        return new GameCard(
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createCastleSide(),

                null, "C");
    }
    public static GameCard D() {
        return new GameCard(
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createStreetSide(),
                GameCardSideFactory.createClosedCastleSide(),
                GameCardSideFactory.createStreetSide(),
                null, "D");
    }
    public static GameCard E() {
        return new GameCard(
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createClosedCastleSide(),
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createGrasSide(),



                null, "E");
    }
    public static GameCard F() {
        return new GameCard(
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createClosedGrasSide(),
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createClosedGrasSide(),

                null, "F");
    }
    public static GameCard G() {
        return new GameCard(
                GameCardSideFactory.createClosedGrasSide(),
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createClosedGrasSide(),
                GameCardSideFactory.createCastleSide(),

                null, "G");
    }
    public static GameCard H() {
        return new GameCard(
                GameCardSideFactory.createClosedCastleSide(),
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createClosedCastleSide(),
                GameCardSideFactory.createGrasSide(),

                null, "H");
    }
    public static GameCard I() {
        return new GameCard(
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createClosedCastleSide(),
                GameCardSideFactory.createClosedCastleSide(),

                null, "I");
    }
    public static GameCard J() {
        return new GameCard(
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createClosedCastleSide(),
                GameCardSideFactory.createStreetSide(),
                GameCardSideFactory.createStreetSide(),

                null, "J");
    }
    public static GameCard K() {
        return new GameCard(
                GameCardSideFactory.createStreetSide(),
                GameCardSideFactory.createStreetSide(),
                GameCardSideFactory.createClosedCastleSide(),
                GameCardSideFactory.createGrasSide(),


                null, "K");
    }
    public static GameCard L() {
        return new GameCard(
                GameCardSideFactory.createClosedStreetSide(),
                GameCardSideFactory.createClosedStreetSide(),
                GameCardSideFactory.createClosedCastleSide(),
                GameCardSideFactory.createClosedStreetSide(),



                null, "L");
    }

    public static GameCard M() {  //todo checkclosingsides
        return new GameCard(
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createGrasSide(),



                null, "M");
    }
    public static GameCard N() {
        return new GameCard(
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createGrasSide(),



                null, "N");
    }
    public static GameCard O() {
        return new GameCard(
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createStreetSide(),
                GameCardSideFactory.createStreetSide(),



                null, "O");
    }
    public static GameCard P() {
        return new GameCard(
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createStreetSide(),
                GameCardSideFactory.createStreetSide(),



                null, "P");
    }
    public static GameCard Q() {
        return new GameCard(
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createClosedGrasSide(),



                null, "Q");
    }
    public static GameCard R() {
        return new GameCard(
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createClosedGrasSide(),


                null, "R");
    }
    public static GameCard S() {
        return new GameCard(
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createClosedStreetSide(),


                null, "S");
    }
    public static GameCard T() {
        return new GameCard(
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createClosedStreetSide(),


                null, "T");
    }
    public static GameCard U() {
        return new GameCard(
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createStreetSide(),
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createStreetSide(),


                null, "U");
    }
    public static GameCard V() {
        return new GameCard(
                GameCardSideFactory.createStreetSide(),
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createStreetSide(),


                null, "V");
    }
    public static GameCard W() {
        return new GameCard(
                GameCardSideFactory.createClosedStreetSide(),
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createClosedStreetSide(),
                GameCardSideFactory.createClosedStreetSide(),

                null, "W");
    }
    public static GameCard X() {
        return new GameCard(

                GameCardSideFactory.createClosedStreetSide(),
                GameCardSideFactory.createClosedStreetSide(),
                GameCardSideFactory.createClosedStreetSide(),
                GameCardSideFactory.createClosedStreetSide(),


                null, "X");
    }
}
