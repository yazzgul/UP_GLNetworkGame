package ru.itis.game.components;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Final extends JFrame {

    public Final(int var) {
        JLabel label = null;
        if (var == 1) {
            label = new JLabel("Первый игрок проиграл. Второй игрок автоматически становится победителем!");
        } else if (var == 2) {
            label = new JLabel("Второй игрок проиграл. Первый игрок автоматически становится победителем!");
        } else if (var == 3) {
            label = new JLabel("Вы проиграли!");
        } else if (var == 4) {
            label = new JLabel("Первый игрок победил!");
        } else if (var == 5) {
            label = new JLabel("Второй игрок победил!");
        } else if (var == 6) {
            label = new JLabel("Никто не победил!");
        } else if (var == 7) {
            label = new JLabel("Вы проиграли!");
        } else if (var == 8) {
            label = new JLabel("Вы победили!");
        }

        label.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(label);

        setSize(700, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);

        Timer timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                System.exit(0);
            }
        });

        timer.setRepeats(true);
        timer.start();

    }
}