package com.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VotingFrame extends JFrame {
    private String prn;
    private JRadioButton option1, option2, option3, option4, nota;

    public VotingFrame(String prn) {
        this.prn = prn;
        setTitle("Voting Panel");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel label = new JLabel("Choose a party:");
        label.setBounds(50, 20, 200, 25);
        add(label);

        option1 = new JRadioButton("Option 1");
        option1.setBounds(50, 60, 150, 25);
        add(option1);

        option2 = new JRadioButton("Option 2");
        option2.setBounds(50, 90, 150, 25);
        add(option2);

        option3 = new JRadioButton("Option 3");
        option3.setBounds(50, 120, 150, 25);
        add(option3);

        option4 = new JRadioButton("Option 4");
        option4.setBounds(50, 150, 150, 25);
        add(option4);

        nota = new JRadioButton("NOTA");
        nota.setBounds(50, 180, 150, 25);
        add(nota);

        ButtonGroup group = new ButtonGroup();
        group.add(option1);
        group.add(option2);
        group.add(option3);
        group.add(option4);
        group.add(nota);

        JButton voteButton = new JButton("Cast Vote");
        voteButton.setBounds(50, 220, 150, 25);
        voteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                castVote();
            }
        });
        add(voteButton);

        setVisible(true);
    }

    private void castVote() {
        String selectedOption = null;
        if (option1.isSelected()) selectedOption = "Option 1";
        else if (option2.isSelected()) selectedOption = "Option 2";
        else if (option3.isSelected()) selectedOption = "Option 3";
        else if (option4.isSelected()) selectedOption = "Option 4";
        else if (nota.isSelected()) selectedOption = "NOTA";

        if (selectedOption == null) {
            JOptionPane.showMessageDialog(this, "Please select a party to vote.");
            return;
        }

        try (Connection con = DatabaseConnection.getConnection()) {
            // First, check if the user has already voted
            String checkVoteQuery = "SELECT * FROM votes WHERE prn = ?";
            PreparedStatement checkPst = con.prepareStatement(checkVoteQuery);
            checkPst.setString(1, prn);
            ResultSet rs = checkPst.executeQuery();

            if (rs.next()) {
                // User has already voted
                JOptionPane.showMessageDialog(this, "You have already voted.");
                dispose(); // Close the voting frame
                new LoginFrame(); // Redirect to login frame
                return;
            }

            // If user has not voted yet, insert the vote
            String query = "INSERT INTO votes (prn, party) VALUES (?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, prn);
            pst.setString(2, selectedOption);
            pst.executeUpdate();

            // Update user's voting status in the users table
            query = "UPDATE users SET has_voted = TRUE WHERE prn = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, prn);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Your vote has been cast successfully.");
            dispose(); // Close the voting frame after voting
            new LoginFrame(); // Redirect to login frame
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error while casting vote.");
        }
    }
}
