package view;







/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import controller.Listener;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.*;

/**
 *
 * @author omri
 */
public class FileMoverPanel extends JPanel {

    private JButton srcBtn, target1Btn, target2Btn, transferBtn;
    private JLabel chosenSrc, trg1Lbl, trg2Lbl, volumeLbl, offsetLbl, fTypeLbl,
            chosenTrg1, chosenTrg2;
    private JTextField tar1Input, tar2Input, fTypeInput, offsetInput, volInput;
    private JPanel ePanel, wPanel, sPanel, innerSPanel;
    private static FileMoverPanel fileMoverPanel = null;

    private FileMoverPanel() {
        //panel creating
        sPanel = new JPanel();
        ePanel = new JPanel();
        innerSPanel = new JPanel();
        wPanel = new JPanel();
        //creating the memebers
        srcBtn = new JButton("Source Folder");
        target1Btn = new JButton("Target1 Folder");
        target2Btn = new JButton("Target2 Folder");
        transferBtn = new JButton("Transfer");
        chosenSrc = new JLabel("Source:");
        trg1Lbl = new JLabel("Target1:");
        trg2Lbl = new JLabel("Target2:");
        volumeLbl = new JLabel("Volume");
        fTypeLbl = new JLabel("files to copy:");
        offsetLbl = new JLabel("Offset:");
        chosenTrg1 = new JLabel("target1:");
        chosenTrg2 = new JLabel("target2:");
        tar1Input = new JTextField();
        tar2Input = new JTextField();
        offsetInput = new JTextField();
        volInput = new JTextField();
        fTypeInput = new JTextField();
        Listener listener = new Listener();
        //setting boarderlayout to panels
        wPanel.setLayout(new GridLayout(3, 1));
        ePanel.setLayout(new GridLayout(5, 1));
        innerSPanel.setLayout(new GridLayout(3, 1));
        sPanel.setLayout(new GridLayout(2, 1));
        //adding the memebers to panels
        //wPanel adding
        wPanel.add(srcBtn);
        wPanel.add(target1Btn);
        wPanel.add(target2Btn);
        //ePanel adding
        ePanel.add(volumeLbl);
        ePanel.add(volInput);
        ePanel.add(trg1Lbl);
        ePanel.add(tar1Input);
        ePanel.add(trg2Lbl);
        ePanel.add(tar2Input);
        ePanel.add(fTypeLbl);
        ePanel.add(fTypeInput);
        ePanel.add(offsetLbl);
        ePanel.add(offsetInput);
        //innerSPanel adding
        innerSPanel.add(chosenSrc);
        innerSPanel.add(chosenTrg1);
        innerSPanel.add(chosenTrg2);
        //sPanel adding
        sPanel.add(innerSPanel);
        sPanel.add(transferBtn);
        //setting the border layout
        this.setLayout(new BorderLayout());
        //adding panels
        this.add(wPanel, BorderLayout.WEST);
        this.add(ePanel, BorderLayout.EAST);
        this.add(sPanel, BorderLayout.SOUTH);
        //adding listener
        srcBtn.addActionListener(listener);
        target1Btn.addActionListener(listener);
        target2Btn.addActionListener(listener);
        transferBtn.addActionListener(listener);
    }

    /*singleton*/
    public static FileMoverPanel getFileMoverPanel() {
        if (fileMoverPanel == null) {
            fileMoverPanel = new FileMoverPanel();
        }
        return fileMoverPanel;
    }
    
    public void setChosenSrc(String txt){
        chosenSrc.setText("Source:" + txt);
    }
    public void setChosenTar1(String txt){
        chosenTrg1.setText("Target1:" + txt);
    }
    public void setChosenTar2(String txt){
        chosenTrg2.setText("Target2:" + txt);
    }

    public JButton getSrcBtn() {
        return srcBtn;
    }

    public JButton getTarget1Btn() {
        return target1Btn;
    }
    
    public JButton getTarget2Btn() {
        return target2Btn;
    }

    public JButton getTransferBtn() {
        return transferBtn;
    }
    
    public String getFolder1Name(){
        return tar1Input.getText();
    }
    
    public String getFolder2Name(){
        return tar2Input.getText();
    }
    
    public String getVolume(){
        return volInput.getText();
    }
    
    public String getOffset(){
        return offsetInput.getText();
    }
    
    public String getType(){
        return fTypeInput.getText();
    }

    public void setVolumeInput(String size) {
        volInput.setText(size);
    }
}
