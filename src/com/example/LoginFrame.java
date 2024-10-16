package com.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private JTextField prnField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Login Panel");
        setSize(350, 180);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel prnLabel = new JLabel("PRN:");
        prnLabel.setBounds(30, 20, 80, 25);
        add(prnLabel);

        prnField = new JTextField();
        prnField.setBounds(120, 20, 150, 25);
        add(prnField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(30, 50, 80, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(120, 50, 150, 25);
        add(passwordField);

        JButton loginButton = new JButton("LOGIN");
        loginButton.setBounds(170, 90, 100, 25);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        add(loginButton);

        JButton registerButton = new JButton("REGISTER");
        registerButton.setBounds(30, 90, 100, 25);
        registerButton.addActionListener(e -> new RegisterFrame());
        add(registerButton);

        setVisible(true);
    }

    private void login() {
        String prn = prnField.getText();
        String password = new String(passwordField.getPassword());

        if (prn.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "PRN and Password cannot be empty.");
            return;
        }

        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "SELECT role, has_voted FROM users WHERE prn = ? AND password = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, prn);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                boolean hasVoted = rs.getBoolean("has_voted");

//                JOptionPane.showMessageDialog(LoginFrame.this, "Login successful!");

                if (role.equalsIgnoreCase("admin")) {
                    new AdminFrame(prn); // Open AdminFrame for admin
                } else {
//                    JOptionPane.showMessageDialog(this, "You have already voted!");
                    new VotingFrame(prn); // Open VotingFrame for users
                }
                dispose(); // Close the login frame after successful login
            } else {
                JOptionPane.showMessageDialog(LoginFrame.this, "Invalid PRN or password.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(LoginFrame.this, "Error connecting to the database.");
        }
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}
