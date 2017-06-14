/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import view.FileMoverPanel;

/**
 *
 * @author omri
 */
public class Listener implements ActionListener {

    private JFileChooser directoryChooser;
    private String srcPath, target1Path, target2Path, fileType, volume, folder1Name = "", folder2Name = "";
    private ArrayList<String> targetsPath = null;
    private File files[];
    private boolean validSrc = false, validTar1 = false, validTar2 = false, validVol = false, validType = false;

    public Listener() {
        directoryChooser = new JFileChooser();
        targetsPath = new ArrayList<>();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //if transfer not pressed
        if (!e.getSource().equals(FileMoverPanel.getFileMoverPanel().getTransferBtn())) //choose source
        {
            this.chooseDirectory();
        }
        //src btn
        if (e.getSource().equals(FileMoverPanel.getFileMoverPanel().getSrcBtn())) {
            srcPath = directoryChooser.getSelectedFile().getAbsolutePath();//get src path
            files = directoryChooser.getSelectedFile().listFiles();//get files from directory
            //set src label in panel
            FileMoverPanel.getFileMoverPanel().setChosenSrc(srcPath);
            //valid the choose of src
            validSrc = true;
        } //target 1 btn
        else if (e.getSource().equals(FileMoverPanel.getFileMoverPanel().getTarget1Btn())) {
            //choose target 1
            target1Path = directoryChooser.getSelectedFile().getAbsolutePath();
            FileMoverPanel.getFileMoverPanel().setChosenTar1(target1Path);
            //valid the choose of target 1
            validTar1 = true;
        } //target 2 btn
        else if (e.getSource().equals(FileMoverPanel.getFileMoverPanel().getTarget2Btn())) {
            //choose target 2
            target2Path = directoryChooser.getSelectedFile().getAbsolutePath();
            FileMoverPanel.getFileMoverPanel().setChosenTar2(target2Path);
            //valid the choose of target 2
            validTar2 = true;
        } //transfer
        else {
            //validate user chooses
            if (isValidTransfer()) {//make transfer
                updateDestination();
                createFolders();
                transferFiles();
            } else {//announce the user
                System.out.println("not valid");
            }
        }
    }

    private void chooseDirectory() {
        directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        directoryChooser.showOpenDialog(new JFrame());
    }

    /*check if the transfer is valid*/
    private boolean isValidTransfer() {
        //check if src chosed and at least one target choosed
        if (validSrc && (validTar1 || validTar2)) {
            //check if input are fill
            if (isValidInput()) {
                return true;
            }
        }
        return false;
    }

    /*check if input box are filled and valid*/
    private boolean isValidInput() {
        FileMoverPanel fileMoverPanel = FileMoverPanel.getFileMoverPanel();
        //check volume
        if (!notEmptyAndNum(fileMoverPanel.getVolume())) {
            return false;
        }
        //check folder1 fill
        if (fileMoverPanel.getFolder1Name().isEmpty()) {
            return false;
        }
        //check folder2 fill
        if (fileMoverPanel.getFolder2Name().isEmpty()) {
            return false;
        }
        //check type
        if (fileMoverPanel.getType().isEmpty()) {
            return false;
        }
        //check offset
        if (!notEmptyAndNum(fileMoverPanel.getOffset())) {
            return false;
        }

        return true;
    }

    /*check if the string isnt empty and can convert to Integer*/
    private boolean notEmptyAndNum(String str) {
        if (str.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean transferFiles() {
        createFolders();//create the folders
        File choosedFiles[] = getFilesToTransfer(files);//get the files that need to be transfered
        try {
            for (File file : choosedFiles) {//copy file to the destinations
                for (String toPath : targetsPath) {
                    Path to, from;
                    from = Paths.get(srcPath + "\\" + file.getName());
                    to = Paths.get(toPath + "\\" + file.getName());
                    Files.copy(from, to);
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void createFolders() {
        for (String targetPath : targetsPath) {
            File f = new File(targetPath);
            f.mkdir();
        }
    }

    /*update destination in gui*/
    private void updateDestination() {
        FileMoverPanel panel = FileMoverPanel.getFileMoverPanel();
        //update the destination members in the listener
        if (!folder1Name.equals(panel.getFolder1Name())) {
            folder1Name = panel.getFolder1Name();
            target1Path += "\\" + panel.getFolder1Name();
        }
        if (!folder2Name.equals(panel.getFolder2Name())) {
            folder2Name = panel.getFolder2Name();
            target2Path += "\\" + panel.getFolder2Name();
        }
        //update gui targets
        panel.setChosenTar1(target1Path);
        panel.setChosenTar2(target2Path);
        //add to targetsPath
        targetsPath.clear();
        targetsPath.add(target1Path);
        targetsPath.add(target2Path);
    }

    /*select the files to transfer*/
    private File[] getFilesToTransfer(File[] files) {
        String pattern = FileMoverPanel.getFileMoverPanel().getType();
        ArrayList<File> file2Transfer = new ArrayList<>();

        for (File file : files) {
            //check for pattern
            if (file.getName().endsWith(pattern)) {
                file2Transfer.add(file);
            }
        }
        File arrFile[] = new File[file2Transfer.size()];
        return file2Transfer.toArray(arrFile);
    }
}
