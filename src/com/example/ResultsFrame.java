package com.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultsFrame extends JFrame {

    private JTable resultsTable;
    private JLabel maxVotesLabel;

    public ResultsFrame() {
        setTitle("Results Panel");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create the table to display the vote counts
        resultsTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Create the label to display the party with the maximum votes
        maxVotesLabel = new JLabel();
        maxVotesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(maxVotesLabel, BorderLayout.SOUTH);

        loadResults();
        setVisible(true);  // Ensure the ResultsFrame is visible
    }

    private void loadResults() {
        try (Connection con = DatabaseConnection.getConnection()) {
            // Query to get the vote counts for each party
            String query = "SELECT party, COUNT(*) AS vote_count FROM votes GROUP BY party ORDER BY vote_count DESC";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            // Create a table model to hold the results
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Party");
            model.addColumn("Vote Count");

            // Populate the table with vote counts
            while (rs.next()) {
                String party = rs.getString("party");
                int count = rs.getInt("vote_count");
                model.addRow(new Object[]{party, count});
            }

            resultsTable.setModel(model);

            // Query to get the party with the maximum votes
            String maxVotesQuery = "SELECT party FROM votes GROUP BY party ORDER BY COUNT(*) DESC LIMIT 1";
            pst = con.prepareStatement(maxVotesQuery);
            rs = pst.executeQuery();

            if (rs.next()) {
                String maxParty = rs.getString("party");
                maxVotesLabel.setText("Maximum Votes: " + maxParty);
            } else {
                maxVotesLabel.setText("No votes cast yet.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading results.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ResultsFrame().setVisible(true));
    }
}
