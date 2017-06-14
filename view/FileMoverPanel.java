/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Listener;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.*;

/**
 *
 * @author omri
 */
public class FileMoverPanel extends JPanel {
    
    private JButton srcBtn , target1Btn , target2Btn , transferBtn;
    private JLabel srcLbl , trg1Lbl, trg2Lbl , volumeLbl , offsetLbl , fTypeLbl,
            chosenTrg1 , chosenTrg2;
    private JTextField tar1Input , tar2Input , fTypeInput , offsetInput , volInput;
    private JPanel ePanel,wPanel , sPanel,innerSPanel;
    private static FileMoverPanel fileMoverPanel = null;
    
    private FileMoverPanel(){
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
        srcLbl = new JLabel("Source:");
        trg1Lbl = new JLabel("Target1:");
        trg2Lbl = new JLabel("Target2:");
        volumeLbl = new JLabel("Volume:");
        fTypeLbl = new JLabel("files to copy:");
        offsetLbl = new JLabel("Offset:");
        chosenTrg1 = new JLabel("target1:");
        chosenTrg2 = new JLabel("target2:");
        tar1Input = new JTextField();
        tar2Input = new JTextField();
        offsetInput = new JTextField();
        volInput = new JTextField();
        fTypeInput = new JTextField();
        controller.Listener listener = new Listener();
        //setting boarderlayout to panels
        wPanel.setLayout(new GridLayout(3, 1));
        ePanel.setLayout(new GridLayout(5,1));
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
        innerSPanel.add(srcLbl);
        innerSPanel.add(chosenTrg1);
        innerSPanel.add(chosenTrg2);
        //sPanel adding
        sPanel.add(innerSPanel);
        sPanel.add(transferBtn);
        //setting the border layout
        this.setLayout(new BorderLayout());
        //adding panels
        this.add(wPanel , BorderLayout.WEST);
        this.add(ePanel , BorderLayout.EAST);
        this.add(sPanel , BorderLayout.SOUTH);
        //adding listener
        srcBtn.addActionListener(listener);
        target1Btn.addActionListener(listener);
        target2Btn.addActionListener(listener);
        transferBtn.addActionListener(listener);
    }
    
    /*singleton*/
    public static FileMoverPanel getFileMoverPanel(){
        if(fileMoverPanel == null){
            fileMoverPanel = new FileMoverPanel();
        }
        return fileMoverPanel;
    }

    public void setSrcBtn(JButton srcBtn) {
        this.srcBtn = srcBtn;
    }

    public void setTarget1Btn(JButton target1Btn) {
        this.target1Btn = target1Btn;
    }

    public void setTarget2Btn(JButton target2Btn) {
        this.target2Btn = target2Btn;
    }

    public void setTransferBtn(JButton transferBtn) {
        this.transferBtn = transferBtn;
    }

    public void setSrcLbl(String txt) {
        srcLbl.setText(txt);
    }

    public void setTrg1Lbl(String txt) {
        trg1Lbl.setText(txt);
    }

    public void setTrg2Lbl(String txt) {
        trg2Lbl.setText(txt);
    }

    public void setVolumeLbl(JLabel volumeLbl) {
        this.volumeLbl = volumeLbl;
    }

    public void setOffsetLbl(JLabel offsetLbl) {
        this.offsetLbl = offsetLbl;
    }

    public void setfTypeLbl(JLabel fTypeLbl) {
        this.fTypeLbl = fTypeLbl;
    }

    public void setChosenTrg1(JLabel chosenTrg1) {
        this.chosenTrg1 = chosenTrg1;
    }

    public void setChosenTrg2(JLabel chosenTrg2) {
        this.chosenTrg2 = chosenTrg2;
    }

    public void setTar1Input(JTextField tar1Input) {
        this.tar1Input = tar1Input;
    }

    public void setTar2Input(JTextField tar2Input) {
        this.tar2Input = tar2Input;
    }

    public void setfTypeInput(JTextField fTypeInput) {
        this.fTypeInput = fTypeInput;
    }

    public void setOffsetInput(JTextField offsetInput) {
        this.offsetInput = offsetInput;
    }

    public void setVolInput(JTextField volInput) {
        this.volInput = volInput;
    }

    public void setePanel(JPanel ePanel) {
        this.ePanel = ePanel;
    }

    public void setwPanel(JPanel wPanel) {
        this.wPanel = wPanel;
    }

    public void setsPanel(JPanel sPanel) {
        this.sPanel = sPanel;
    }

    public void setInnerSPanel(JPanel innerSPanel) {
        this.innerSPanel = innerSPanel;
    }

    public static void setFileMoverPanel(FileMoverPanel fileMoverPanel) {
        FileMoverPanel.fileMoverPanel = fileMoverPanel;
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

    public JLabel getSrcLbl() {
        return srcLbl;
    }

    public JLabel getTrg1Lbl() {
        return trg1Lbl;
    }

    public JLabel getTrg2Lbl() {
        return trg2Lbl;
    }

    public JLabel getVolumeLbl() {
        return volumeLbl;
    }

    public JLabel getOffsetLbl() {
        return offsetLbl;
    }

    public JLabel getfTypeLbl() {
        return fTypeLbl;
    }

    public JLabel getChosenTrg1() {
        return chosenTrg1;
    }

    public JLabel getChosenTrg2() {
        return chosenTrg2;
    }

    public JTextField getTar1Input() {
        return tar1Input;
    }

    public JTextField getTar2Input() {
        return tar2Input;
    }

    public JTextField getfTypeInput() {
        return fTypeInput;
    }

    public JTextField getOffsetInput() {
        return offsetInput;
    }

    public JTextField getVolInput() {
        return volInput;
    }

    public JPanel getePanel() {
        return ePanel;
    }

    public JPanel getwPanel() {
        return wPanel;
    }

    public JPanel getsPanel() {
        return sPanel;
    }

    public JPanel getInnerSPanel() {
        return innerSPanel;
    }
   
}
