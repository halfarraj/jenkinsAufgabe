package com.jenkins.aufgabe.jenkinsAufgabe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class create a interface and extends the JobsDetails class
 * then the user can execute the delete command in a good way
 */
public class Interface extends JobsDetails {
    public Interface() {
        super();
    }

    /**
     * Here create the JFrame and insert to it Labels
     * and Button and implement to the Button the delete
     * command like a ActionListener
     */
    public void frame() {

        JFrame jFrame = new JFrame();
        jFrame.setSize(450, 300);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLayout(null);

        Font newFont = new Font("Serif", Font.BOLD, 14);

        JLabel jLabelText1 = new JLabel();
        JLabel jLabelText2 = new JLabel();
        JLabel sign = new JLabel();

        jLabelText1.setFont(newFont);
        jLabelText1.setText("To delete the Jobs who they have more than one year ");
        jLabelText1.setBounds(20, 20, 550, 50);

        jLabelText2.setFont(newFont);
        jLabelText2.setText("  did not active Click Please on the Delete Button ");
        jLabelText2.setBounds(20, 40, 550, 50);

        sign.setFont(newFont);
        sign.setText(" Husaen Alfarraj ");
        sign.setBounds(310, 220, 150, 50);

        JButton button = new JButton("DELETE");
        button.setVisible(true);
        button.setBounds(40, 90, 90, 20);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    deleteJobs();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                JOptionPane.showMessageDialog(jFrame, "The Job/s is deleted");

            }
        });


        jFrame.add(jLabelText1);
        jFrame.add(jLabelText2);
        jFrame.add(sign);
        jFrame.add(button);


    }


}
