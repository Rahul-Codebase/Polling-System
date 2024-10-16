package com.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminFrame extends JFrame {

    public AdminFrame(String prn) {
        setTitle("Admin Panel");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JButton castVoteButton = new JButton("Cast a Vote");
        castVoteButton.setBounds(50, 50, 150, 25);
        add(castVoteButton);

        JButton viewResultsButton = new JButton("View Results");
        viewResultsButton.setBounds(50, 90, 150, 25);
        add(viewResultsButton);

        // Action listener for "Cast a Vote" button (admin can vote too)
        castVoteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VotingFrame(prn);
                dispose();
            }
        });

        // Action listener for "View Results" button to open ResultsFrame
        viewResultsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ResultsFrame();  // Open ResultsFrame to view the voting results
                dispose();  // Close the Admin frame
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminFrame("admin").setVisible(true));
    }
}
