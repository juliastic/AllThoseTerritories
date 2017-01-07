package gui;

/**
 * Created by Gril on 10/01/2016.
 */

import data.Map;
import main.Game;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class IntroViewPanel extends JPanel {
    private Map map;
    private Game game;
    java.util.List<PlayerEntry> players;
    private static final int MAX_PLAYER = 6;
    private JScrollPane scrollTable;
    private JTable table;

    public IntroViewPanel(MainView view, Game game) {
        super();
        this.game = game;
        this.map = game.getMap();

        BorderLayout layout = new BorderLayout(5, 5);

        FlowLayout southLayout = new FlowLayout(10);
        JPanel southPanel = new JPanel(southLayout);
        JButton plusButton = new PlusMinusButton(true);
        JButton minusButton = new PlusMinusButton(false);
        southPanel.add(plusButton);
        southPanel.add(minusButton);
        JLabel emptyLabel = new JLabel();
        emptyLabel.setBorder(BorderFactory.createEmptyBorder(0,20,0,0));
        southPanel.add(emptyLabel);
        southPanel.add(new PlayButton(view));

        this.setLayout(layout);
        table = getTable();
        table.getTableHeader().setReorderingAllowed(false);
        this.scrollTable = new JScrollPane(table);
        this.add(this.scrollTable, BorderLayout.CENTER);
        this.add(southPanel, BorderLayout.SOUTH);
    }

    @Override
    public void repaint() {
        super.repaint();
        if(table != null) table.revalidate();
        if(scrollTable != null)scrollTable.repaint();
    }

    private JTable getTable() {
        PlayerTableModel model = new PlayerTableModel();
        JTable table = new JTable(model);
        table.getColumn("Color").setCellRenderer(new ColorButtonRenderer());
        table.getColumn("Color").setCellEditor(new ColorButtonEditor(new JTextField()));
        return table;
    }

    private Color randomColor(){
        Random rand = new Random();

        // Java 'Color' class takes 3 floats, from 0 to 1.
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();

        Color randomColor = new Color(r, g, b);
        randomColor.brighter();
        for(PlayerEntry player : players){
            if(player.color.equals(randomColor)){
                randomColor = randomColor();
            }
        }
        return randomColor;
    }

    private class PlayButton extends JButton implements ActionListener{
        MainView view;

        public PlayButton(MainView view){
            this.view = view;
            this.setText("Let's play");
            this.addActionListener(this);
            this.setPreferredSize(new Dimension(100,40));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
             view.initGame(players.toArray(new PlayerEntry[players.size()]));
        }
    }

    private class PlusMinusButton extends JButton implements ActionListener{
        boolean plus;

        public PlusMinusButton(boolean plus){
            this.plus = plus;
            this.setPreferredSize(new Dimension(30,30));
            this.setText(plus ? "+" : "-");
            this.setVisible(true);
            this.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(plus){
                if(players.size() < MAX_PLAYER) {
                    players.add(new PlayerEntry(randomColor(), "Player " + (players.size() + 1), false));
                }
            } else {
                if(players.size()>2){
                    players.remove(players.size()-1);
                }
            }
            IntroViewPanel.this.repaint();
        }
    }

    private class PlayerTableModel extends AbstractTableModel {

        public PlayerTableModel() {
            players = new ArrayList<>();
            players.add(new PlayerEntry(randomColor(), "Player 1", true));
            players.add(new PlayerEntry(randomColor(), "Player 2", false));
        }

        @Override
        public int getRowCount() {
            return players.size();
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public String getColumnName(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return "Color";
                case 1:
                    return "Name";
                case 2:
                    return "Computer";
                default:
                    return "";
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return String.class;
                case 1:
                    return String.class;
                case 2:
                    return Boolean.class;
                default:
                    return null;
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            //if (columnIndex == 0) return false;
            return true;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            PlayerEntry player = players.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return "";
                case 1:
                    return player.name;
                case 2:
                    return !player.isHuman;
                default:
                    return null;
            }
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            PlayerEntry player = players.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    break;
                case 1:
                    player.name = (String) aValue;
                    break;
                case 2:
                    player.isHuman = !((Boolean) aValue);
                    break;
            }
        }
    }

    class PlayerEntry {
        Color color;
        String name;
        boolean isHuman;

        public PlayerEntry(Color color, String name, boolean isHuman) {
            this.color = color;
            this.name = name;
            this.isHuman = isHuman;
        }
    }


    class ColorButtonRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            component.setBackground(players.get(row).color);
            ((JLabel) component).setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED));
            return component;
        }
    }

    class ColorButtonEditor extends DefaultCellEditor {

        public ColorButtonEditor(JTextField textField) {
            super(textField);
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            new ColorChooserFrame(players.get(row));
            return null;
        }

        class ColorChooserFrame extends JFrame {
            public ColorChooserFrame(PlayerEntry player) {
                JColorChooser colorChooser = new JColorChooser(player.color);
                colorChooser.setPreferredSize(new Dimension(500, 300));
                colorChooser.getSelectionModel().addChangeListener(new ColorChangeListener(player, colorChooser));
                this.setContentPane(colorChooser);
                this.pack();
                this.setVisible(true);
            }

            class ColorChangeListener implements ChangeListener {
                PlayerEntry player;
                JColorChooser colorChooser;

                public ColorChangeListener(PlayerEntry player, JColorChooser colorChooser) {
                    this.player = player;
                    this.colorChooser = colorChooser;
                }

                @Override
                public void stateChanged(ChangeEvent e) {
                    this.player.color = this.colorChooser.getColor();
                }
            }
        }
    }
}
