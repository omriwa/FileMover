/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import view.FileMoverPanel;

/**
 *
 * @author omri
 */
public class Listener implements ActionListener {

    private JFileChooser directoryChooser;
    private String srcPath, target1Path, target2Path, fileType, volume, folder1Name = "", folder2Name = "", offset;
    private ArrayList<String> targetsPath = null;
    private File files[];
    private boolean validSrc = false, validTar1 = false, validTar2 = false, validVol = false, validType = false;

    public Listener() {
        directoryChooser = new JFileChooser();
        targetsPath = new ArrayList<>();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean dirChosen = false;
        //if transfer not pressed
        if (!e.getSource().equals(FileMoverPanel.getFileMoverPanel().getTransferBtn())) //choose source
        {
            if(!chooseDirectory())
                return;
        }
        //src btn
        if (e.getSource().equals(FileMoverPanel.getFileMoverPanel().getSrcBtn())) {
            srcPath = directoryChooser.getSelectedFile().getAbsolutePath();//get src path
            files = directoryChooser.getSelectedFile().listFiles();//get files from directory
            setVolumeGui();//set the volume of files in gui
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
                File choosedFiles[] = getFilesToTransfer(files);//get the files that need to be transfered
                setVolumeGui();//set the volume of files in gui
                changeVol();//ask the user for change the volume
                updateDestination();
                createFolders();
                setInput();//set the input
                transferFiles(choosedFiles);
                setOffsetDate(choosedFiles);
            } else {//announce the user
                System.out.println("not valid");
            }
        }
    }

    private boolean chooseDirectory() {
        directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = directoryChooser.showOpenDialog(new JFrame());
        if(result == JFileChooser.APPROVE_OPTION)
            return true;
        else
            return false;
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

    private void transferFiles(File choosedFiles[]) {
        try {
            for (File file : choosedFiles) {//copy file to the destinations
                for (String toPath : targetsPath) {
                    Path to, from;
                    from = Paths.get(srcPath + "\\" + file.getName());
                    String newName = getNewFileName(file);
                    to = Paths.get(toPath + "\\" + newName + "." + fileType);
                    Files.copy(from, to);
                }
            }
        } catch (Exception e) {
            //announce user
        }
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

    private String getNewFileName(File file) {
        String format = "YYYYMMdd-hhmm";
        String newFileName = "";
        long date = 0;
        SimpleDateFormat parser = null;
        try {
            BasicFileAttributes attr = Files.readAttributes(Paths.get(file.getAbsolutePath()), BasicFileAttributes.class);
            date = attr.creationTime().to(TimeUnit.MILLISECONDS);
            parser = new SimpleDateFormat(format);
        } catch (IOException ex) {
            Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
        }
        Date fileDate = new Date(date);
        newFileName = parser.format(fileDate);
        return newFileName;
    }

    private void setOffsetDate(File files[]) {
        for (File file : files) {

        }
    }

    /*set the input of the user to the right variables*/
    private void setInput() {
        FileMoverPanel panel = FileMoverPanel.getFileMoverPanel();
        fileType = panel.getType();
        offset = panel.getOffset();
        volume = panel.getVolume();
    }

    /*set the volume of the source in gui*/
    private void setVolumeGui() {
        int size = 0;
        for (File f : files) {//summing the volume of each file
            size += f.length();
        }
        FileMoverPanel.getFileMoverPanel().setVolumeInput(Integer.toString(size / 10));
    }

    /*change the input of the volume*/
    private void changeVol() {
        String volume = "";
        volume = JOptionPane.showInputDialog(new JFrame(), "for change the volume enter a number else press ok");
        if (!volume.isEmpty()) {
            FileMoverPanel.getFileMoverPanel().setVolumeInput(volume);
        }
    }

    public void setOffsetDate() {
        File choosedFiles [] = getFilesToTransfer(files);
        //changing the offset of the created time of the file
        for (int i = 0 ; i < choosedFiles.length ; i++) {
            //get the source Files
            if(choosedFiles[i].getName().equals(files[i].getName())){
                try {
                    setFileCreationDate(files[i].getAbsolutePath());
                } catch (IOException ex) {
                    Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void setFileCreationDate(String filePath) throws IOException {
        BasicFileAttributeView attributes = Files.getFileAttributeView(Paths.get(filePath), BasicFileAttributeView.class);
        FileTime time = FileTime.fromMillis(getFileCreateTime(filePath) - ((10^6) * Integer.parseInt(offset)));
        attributes.setTimes(time, time, time);
    }
    
    private Long getFileCreateTime(String url){
        BasicFileAttributes attr = null;
        try {
            attr = Files.readAttributes(Paths.get(url), BasicFileAttributes.class);
        } catch (IOException ex) {
            Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
        }
        return attr.creationTime().toMillis();
    }
}
