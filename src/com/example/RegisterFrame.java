package com.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class RegisterFrame extends JFrame {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JPasswordField passwordField;
    private JTextField prnField;

    public RegisterFrame() {
        setTitle("Registration Panel");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setBounds(30, 20, 100, 25);
        add(firstNameLabel);

        firstNameField = new JTextField();
        firstNameField.setBounds(150, 20, 150, 25);
        add(firstNameField);

        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setBounds(30, 60, 100, 25);
        add(lastNameLabel);

        lastNameField = new JTextField();
        lastNameField.setBounds(150, 60, 150, 25);
        add(lastNameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(30, 100, 100, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 100, 150, 25);
        add(passwordField);

        JLabel prnLabel = new JLabel("PRN:");
        prnLabel.setBounds(30, 140, 100, 25);
        add(prnLabel);

        prnField = new JTextField();
        prnField.setBounds(150, 140, 150, 25);
        prnField.setEditable(false);
        add(prnField);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(150, 180, 100, 25);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });
        add(registerButton);

        setVisible(true);
    }

    private void register() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String password = new String(passwordField.getPassword());

        if (firstName.isEmpty() || lastName.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        String prn = generateUniquePRN();
        prnField.setText(prn);

        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO users (prn, first_name, last_name, password, role) VALUES (?, ?, ?, ?, 'user')";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, prn);
            pst.setString(2, firstName);
            pst.setString(3, lastName);
            pst.setString(4, password);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Registration successful! Your PRN is " + prn);
            dispose(); // Close the register frame after registration
//            new LoginFrame(); // Redirect to login frame
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error during registration.");
        }
    }

    private String generateUniquePRN() {
        Random random = new Random();
        int part1 = random.nextInt(9000) + 1000;
        int part2 = random.nextInt(9000) + 1000;
        int part3 = random.nextInt(9000) + 1000;
        return part1 + "-" + part2 + "-" + part3;
    }
}
