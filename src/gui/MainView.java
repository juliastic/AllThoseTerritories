package gui;

import data.Dices;
import main.Game;
import player.ActionDependent;
import player.JuliaPlayer;
import player.ManuelPlayer;
import player.Player;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

/**
 * Created by manuelesberger on 20/12/15.
 */
public class MainView extends JFrame implements GUI {
    private IntroViewPanel introPanel;
    private GamePanel gamePanel;
    private Game game;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public MainView(Game game) {
        super("AllThoseTerritories");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(1250, 650));
        this.setBackground(Color.black);

        this.game = game;

        introPanel = new IntroViewPanel(this, game);
        gamePanel = new GamePanel(game);

        this.cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.add(introPanel);
        cardPanel.add(gamePanel);

        this.setContentPane(cardPanel);

        this.disableInput(true);
        this.setMaxNumberCounter(0);
    }

    public void view() {
        this.pack();
        this.setVisible(true);
    }

    public void initGame(IntroViewPanel.PlayerEntry[] players) {
        java.util.List<Player> playersList = new LinkedList<>();
        for (IntroViewPanel.PlayerEntry player : players) {
            if (player.isHuman) {
                playersList.add(new ManuelPlayer(player.name, player.color, game));
            } else {
                playersList.add(new JuliaPlayer(player.name, player.color, game));
            }
        }
        if (playersList.size() > 1) {
            game.initPlayers(playersList);
            cardLayout.next(cardPanel);
        }
    }

    @Override
    public void addActionDependent(ActionDependent actionDependent) {
        gamePanel.addActionDependent(actionDependent);
    }

    @Override
    public void removeActionDependent(ActionDependent actionDependent) {
        gamePanel.removeActionDependet(actionDependent);
    }

    @Override
    public void setMaxNumberCounter(int numberCounter) {
        gamePanel.setMaxNumberCounter(numberCounter);
    }

    @Override
    public void setNumberCounter(int numberCounter) {
        gamePanel.setNumberCounter(numberCounter);
    }

    @Override
    public void disableInput(boolean disable) {
        gamePanel.disableInput(disable);
    }

    @Override
    public void displayMessage(String message, Player player) {
        gamePanel.displayMessage(message, player);
    }

    @Override
    public void displayDices(Dices[] dicesList) {
        gamePanel.setDices(dicesList);
    }

    @Override
    public void displayWinner(Player player, int roundCount) {
        gamePanel.displayWinner(player, roundCount);
    }
}
