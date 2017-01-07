package gui;

import data.Map;
import data.Territory;
import main.Game;
import player.Player;

import javax.swing.*;
import java.awt.*;

/**
 * Created by manuelesberger on 20/12/15.
 */
public class MapViewPanel extends JPanel {
    private Map map;
    private Game game;
    private Player winner;
    private int roundCount;

    public MapViewPanel(Game game) {
        super();
        this.map = game.getMap();
        this.game = game;
        this.roundCount = 0;
        this.winner = null;
        this.setBackground(Color.lightGray.brighter());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (winner == null) {
            paintMapPanel(g);
        } else {
            paintWinner();
        }
    }

    private void paintWinner() {
        if (roundCount != 0) {
            JLabel jlabel = new JLabel(winner.getName() + " won the game in " + roundCount + " rounds.");
            jlabel.setForeground(winner.getColor());
            jlabel.setFont(new Font("Verdana", 1, 20));
            this.add(jlabel);
            this.roundCount = 0;
        }
    }

    private void paintMapPanel(Graphics g) {
        Territory[] territories = map.getTerritoryArray();
        g.setColor(Color.black);
        for (Territory territory : territories) {
            for (Territory neighbor : territory.getNeighbors()) {
                if (territory.getCapital() != null && neighbor.getCapital() != null) {
                    g.drawLine(territory.getCapital().x(), territory.getCapital().y(), neighbor.getCapital().x(), neighbor.getCapital().y());
                }
            }
        }
        for (Territory territory : territories) {
            for (Territory subTerritory : territory.getSubterritories()) {
                int[] x = generateArray(subTerritory, true);
                int[] y = generateArray(subTerritory, false);
                Polygon polygon = new Polygon(x, y, x.length);
                g.drawPolygon(polygon);
                Color color = Color.LIGHT_GRAY;
                if (territory.getOwner() != null) {
                    color = territory.getOwner().getColor();
                }

                g.setColor(color);
                g.fillPolygon(polygon);

                g.setColor(Color.black);
                g.drawPolygon(polygon);
            }
        }
        for (Territory territory : territories) {
            g.setColor(Color.black);
            g.drawString("" + territory.troops(), territory.getCapital().x(), territory.getCapital().y());
        }
    }

    private int[] generateArray(Territory territory, boolean x) {
        int[] points = new int[territory.getCoordinates().length];
        for (int i = 0; i < points.length; i++) {
            if (x) {
                points[i] = (int) territory.getCoordinates()[i].getX();
            } else {
                points[i] = (int) territory.getCoordinates()[i].getY();
            }
        }
        return points;
    }

    public void displayWinner(Player player, int roundCount) {
        this.winner = player;
        this.roundCount = roundCount;
    }
}
