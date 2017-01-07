package gui;

import data.Dice;
import data.Dices;
import main.Game;
import player.ActionDependent;
import player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

/**
 * Created by manuelesberger on 10/01/16.
 */
public class GamePanel extends JPanel {
    private SpinnerModel spinnerModel;
    private JSpinner spinner;
    private JButton nextPhase;
    private MapViewPanel mapView;
    private DefaultMouseListener mouseListener;
    private java.util.List<ActionDependent> actionDependentList;
    private JLabel displayLabel;
    private DicesPanel dicePanel;
    private JPanel southPanel;
    private JPanel menuRowPanel;

    public GamePanel(Game game) {
        super();
        BorderLayout layout = new BorderLayout();
        this.setLayout(layout);

        this.actionDependentList = new LinkedList<>();

        mouseListener = new DefaultMouseListener(game.getMap().getTerritoryArray());
        mapView = new MapViewPanel(game);
        mapView.setPreferredSize(new Dimension(1250, 650));
        JScrollPane scrollPane = new JScrollPane(mapView);
        this.add(scrollPane, BorderLayout.CENTER);

        southPanel = new JPanel(new GridLayout(2, 1));

        spinnerModel = new SpinnerModel();
        spinner = new JSpinner(spinnerModel);
        spinner.setPreferredSize(new Dimension(50, 20));
        menuRowPanel = new JPanel(new FlowLayout(10));
        menuRowPanel.add(spinner);
        displayLabel = new JLabel();
        displayLabel.setPreferredSize(new Dimension(800, 20));
        menuRowPanel.add(displayLabel);

        nextPhase = new NextPhaseButton();
        menuRowPanel.add(nextPhase);

        dicePanel = new DicesPanel();

        southPanel.add(dicePanel);
        southPanel.add(menuRowPanel);
        this.add(southPanel, BorderLayout.SOUTH);
    }

    class DicesPanel extends JPanel {
        private Dices[] dicesList;

        public DicesPanel() {
            this.setLayout(new FlowLayout(10));
        }

        private void setDices(Dices[] dicesList) {
            this.dicesList = dicesList;
            this.removeAll();
            if (this.dicesList != null) {
                for (Dices dices : this.dicesList) {
                    Color color = dices.player().getColor();
                    for (Dice dice : dices.dices()) {
                        this.add(new DicePanel(dice, color));
                    }
                    JPanel emptyPanel = new JPanel();
                    emptyPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                    this.add(emptyPanel);
                }
            }
        }
    }

    class NextPhaseButton extends JButton implements ActionListener {
        public NextPhaseButton() {
            this.setText("done");
            this.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            actionDependentList.forEach(a -> a.buttonPressed());
        }
    }

    class SpinnerModel extends SpinnerNumberModel {
        public SpinnerModel() {
            this.setMinimum(0);
        }

        @Override
        public void setMinimum(Comparable minimum) {
            super.setMinimum(0);
        }

        @Override
        public void setValue(Object value) {
            actionDependentList.forEach(a -> a.numberCounter((int) value));
            super.setValue(value);
        }
    }

    @Override
    public void repaint() {
        super.repaint();
        revalidate();
    }

    public void addActionDependent(ActionDependent actionDependent) {
        actionDependentList.add(actionDependent);
        actionDependent.numberCounter((Integer) spinnerModel.getNumber());
        mouseListener.addActionDependent(actionDependent);
    }

    public void removeActionDependet(ActionDependent actionDependent) {
        actionDependentList.remove(actionDependent);
        mouseListener.removeActionDependent(actionDependent);
    }

    public void setDices(Dices[] dicesList) {
        dicePanel.setDices(dicesList);
        repaint();
    }

    public void setMaxNumberCounter(int numberCounter) {
        spinnerModel.setMaximum(numberCounter);
        if (numberCounter < (Integer) this.spinnerModel.getNumber()) {
            setNumberCounter(numberCounter);
        } else if (numberCounter > 0) {
            setNumberCounter(1);
        }
    }

    public void disableInput(boolean disable) {
        this.setMaxNumberCounter(0);
        nextPhase.setEnabled(!disable);
        if (disable) {
            mapView.removeMouseListener(mouseListener);
        } else {
            mapView.addMouseListener(mouseListener);
        }
    }

    public void setNumberCounter(int numberCounter) {
        this.spinnerModel.setValue(numberCounter);
    }

    public void displayMessage(String message, Player player) {
        displayLabel.setText(message);
        if (player == null) {
            displayLabel.setOpaque(false);
        } else {
            displayLabel.setOpaque(true);
            displayLabel.setBackground(player.getColor());
        }
    }

    public void displayWinner(Player player, int roundCount) {
        mapView.displayWinner(player, roundCount);
    }
}
