package main;

import data.Map;
import data.Territory;
import gui.GUI;
import gui.MainView;
import phases.*;
import player.PathFinding;
import player.Player;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by manuelesberger on 20/12/15.
 */
public class Game implements Winable, Runnable {
    private Map map;
    private GUI gui;
    private Collection<Player> players;
    private Player currentPlayer;
    private Iterator<Player> playerIterator;
    private Collection<Phase> phases;
    private Iterator<Phase> phaseIterator;
    private Phase currentPhase;
    private PreparePhase beginningPhase;
    private Player winner;
    private int roundCount;

    public Game(Map map) {
        this.map = map;
        this.gui = new MainView(this);
        this.roundCount = 0;
        initialize();

        for(Territory territory : map.getTerritoryArray()){
            PathFinding.addTerritory(territory);
        }

//        System.out.println(map.getTerritoryArray()[0].getName() + " to " + map.getTerritoryArray()[10].getName() + " is "+ PathFinding.distance(map.getTerritoryArray()[0], map.getTerritoryArray()[10]));
//        System.out.println(PathFinding.getNeighbour(map.getTerritoryArray()[10], map.getTerritoryArray()[0], false).getName());
    }

    @Override
    public void run(){
        if(players == null){
            return;
        }

        //first acquisition phase
        gui.disableInput(false);
        beginningPhase.execute(players.toArray(new Player[players.size()]));

        //main phases
        while (winner == null) {
            do {
                nextPlayer();
            }while (hasLost(currentPlayer));
            roundCount++;
            do {
                nextPhase();
                gui().disableInput(false);
                currentPhase.execute(currentPlayer);
                gui.disableInput(true);
                gui().repaint();
                if(winner != null) break;
            } while(!isPhaseEnd());
        }
    }

    private void initialize(){
        beginningPhase = new PreparePhase(gui, getMap().getTerritoryArray());
        phases = new LinkedList<>();
        phases.add(new AcquisitionPhase(gui));
        phases.add(new AttackPhase(this));
        phases.add(new EndPhase(gui));
    }

    public Map getMap() {
        return map;
    }

    public Player[] getPlayers() {
        return players.toArray(new Player[players.size()]);
    }

    public Player getTurn() {
        return currentPlayer;
    }

    public Player nextPlayer(){
        if(players != null) {
            if (playerIterator != null && players.size() != 0 && playerIterator.hasNext() ) {
                currentPlayer = playerIterator.next();
                return currentPlayer;
            } else {
                playerIterator = players.iterator();
                return nextPlayer();
            }
        }
        return null;
    }

    public GUI gui() {
        return gui;
    }

    public boolean isPhaseEnd(){
        if(phaseIterator!=null && !phaseIterator.hasNext()){
             return true;
        }
        return false;
    }

    public void nextPhase(){
        if(phases != null){
            if(phaseIterator != null && phases.size() != 0 && phaseIterator.hasNext()) {
               currentPhase = phaseIterator.next();
            } else {
                phaseIterator = phases.iterator();
                nextPhase();
            }
        }
    }

    @Override
    public void won(Player player) {
        this.winner = player;
        gui.disableInput(true);
        gui.displayWinner(player, roundCount);
    }

    public void initPlayers(Collection<Player> players){
        this.players = players;
        new Thread(this).start();
    }

    private boolean hasLost(Player player){
        return player.getTerritories().length <= 0;
    }
}
