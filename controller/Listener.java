/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import view.FileMoverPanel;

/**
 *
 * @author omri
 */
public class Listener implements ActionListener {

    private JFileChooser directoryChooser;
    private String srcPath, target1Path, target2Path, fileType, volume;
    private File files[];
    private boolean validSrc = false, validTar1 = false, validTar2 = false, validVol = false, validType = false;

    public Listener() {
        directoryChooser = new JFileChooser();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //choose source
        this.chooseDirectory();
        //src btn
        if (e.getSource().equals(FileMoverPanel.getFileMoverPanel().getSrcBtn())) {
            srcPath = directoryChooser.getSelectedFile().getAbsolutePath();//get src path
            files = directoryChooser.getSelectedFile().listFiles();//get files from directory
            //set src label in panel
            FileMoverPanel.getFileMoverPanel().setChosenSrc(srcPath);
        } //target 1 btn
        else if (e.getSource().equals(FileMoverPanel.getFileMoverPanel().getTarget1Btn())) {
            //choose target 1
            target1Path = directoryChooser.getSelectedFile().getAbsolutePath();
            FileMoverPanel.getFileMoverPanel().setChosenTar1(target1Path);
        } //target 2 btn
        else if (e.getSource().equals(FileMoverPanel.getFileMoverPanel().getTarget2Btn())) {
            //choose target 2
            target2Path = directoryChooser.getSelectedFile().getAbsolutePath();
            FileMoverPanel.getFileMoverPanel().setChosenTar2(target2Path);
        } //transfer
        else {
            //validate user chooses
            //make transfer
        }
    }

    private void chooseDirectory() {
        directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        directoryChooser.showOpenDialog(new JFrame());
    }

}
