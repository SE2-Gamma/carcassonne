package at.aau.se2.gamma.core.models.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Game statistics handles the statistics for one game.
 * It should be initialized at game start by the game session.
 */
public class GameStatistic implements Serializable {
    private ArrayList<Player> players;

    public GameStatistic(ArrayList<Player> players) {
        this.players = players;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void removePlayer(Player player) {
        this.players.remove(player);
    }

    public void applyEndDetectionData(ArrayList<ClosedFieldDetectionData> endData) {
        for(ClosedFieldDetectionData data: endData) {
            data.setEndGameData(true);
            applyClosedFieldDetectionData(data);
        }
    }

    public Soldier getSoldierBySoldierData(SoldierData data) {
        Soldier soldier = null;
        for (Player player : players
        ) {
            for (Soldier playersoldier : player.getSoldiers()
            ) {
                if (data.getSoldierID() == playersoldier.getId()) {
                    soldier = playersoldier;
                }
            }
        }
        if (soldier == null) {
            throw new NoSuchElementException();
        }
        return soldier;
    }


    /**
     * Apply the calculated data from a closed area to the players statistic
     * @param closedFieldDetectionData
     */
    public void applyClosedFieldDetectionData(ClosedFieldDetectionData closedFieldDetectionData) {
        int[] soldiersPerPlayer = new int[this.players.size()];
        ArrayList<Soldier> affectedSoldiers = new ArrayList<>();

        // set soldiers to 0
        for(int i = 0; i < this.players.size(); i++) {
            soldiersPerPlayer[i] = 0;
        }

        // determine the players who receive the points
        // calculate soldiers per player
        for(GameCardSide gameCardSide: closedFieldDetectionData.getGameCardSides()) {
            for(Player player: players) {
                for(Soldier soldier: player.getSoldiers()) {
                    // check each soldierPlacement for each player
                    // and check if the cardSides are the same
                    // to calculate the right amount of players
                    SoldierPlacement soldierPlacement = soldier.getSoldierPlacement();
                    if (soldierPlacement != null) {
                        if (soldierPlacement.getGameCardSide() == gameCardSide) {
                            System.out.println("Gamestatistic:"+player.getName()+"'s Soldier found");
                            // increase by 1 the soldiersPerPlayer for player with index
                            soldiersPerPlayer[players.indexOf(player)]++;
                            affectedSoldiers.add(soldier);
                        }
                    }
                }
            }
        }

        // determine the players with the most placed soldiers
        ArrayList<Player> winningPlayers = new ArrayList<>();
        int topAmount = 1;
        for(int i = 0; i < soldiersPerPlayer.length; i++) {
            int amountOfSoldiers = soldiersPerPlayer[i];

            // if the player has more than the top amount, remove all other players, and add this one
            if (amountOfSoldiers > topAmount) {
                winningPlayers.clear();
                winningPlayers.add(players.get(i));
            } else if (amountOfSoldiers == topAmount){
                // if the player has the same amount, add the player to the winning ones
                winningPlayers.add(players.get(i));
            }
        }

        // bring back the soldiers
        for(Soldier soldier: affectedSoldiers) {
            soldier.comeBackHome();
        }

        // add points to the right players
        for(Player player: winningPlayers) {
            if (closedFieldDetectionData.isEndGameData()) {
                if (closedFieldDetectionData.isGrasType()) {
                    player.addPlayerPoints(closedFieldDetectionData.getDetectedCastles().size()*3);
                } else if (closedFieldDetectionData.isMonasteryType()) {
                    player.addPlayerPoints(closedFieldDetectionData.getGameCards().size());
                } else {
                    for(GameCard gameCard: closedFieldDetectionData.getGameCards()) {
                        int multiplier = 0;
                        for(GameCardSide side: closedFieldDetectionData.getGameCardSides()) {
                            for (GameCardSide currentCardSide: gameCard.getNeswmSides()) {
                                if (currentCardSide == side && side.getMultiplier() > 1) {
                                    multiplier++;
                                }
                            }
                        }
                        player.addPlayerPoints(1+multiplier);
                    }
                }
            } else {
                for(GameCard gameCard: closedFieldDetectionData.getGameCards()) {
                    int multiplier = 1;
                    int points = 0;
                    for(GameCardSide side: closedFieldDetectionData.getGameCardSides()) {
                        for (GameCardSide currentCardSide: gameCard.getNeswmSides()) {
                            if (currentCardSide == side ) {
                                points = side.getPoints();
                                multiplier *= side.getMultiplier();
                            }
                        }
                    }
                    player.addPlayerPoints(points*multiplier);
                }
                //player.addPlayerPoints(closedFieldDetectionData.getPoints());
            }
        }
    }
}
