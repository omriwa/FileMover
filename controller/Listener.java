package controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import model.TransferFile;
import view.FileMoverPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.JFXPanel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author omri
 */
public class Listener implements ActionListener {

    private JFileChooser directoryChooser;
    private String srcPath, target1Path, target2Path, temp1Path, temp2Path, fileType, volume, offset;
    private ArrayList<String> targetsPath = null;
    private File files[];
    private boolean validSrc = false, validTar1 = false, validTar2 = false, validVol = false;
    private ArrayList<TransferFile> transferFilesInfo = null;

    public Listener() {
        directoryChooser = new JFileChooser();
        targetsPath = new ArrayList<>();
        transferFilesInfo = new ArrayList<TransferFile>();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean dirChosen = false;
        //if transfer not pressed
        if (!e.getSource().equals(FileMoverPanel.getFileMoverPanel().getTransferBtn())) //choose source
        {
            if (!chooseDirectory()) {
                return;
            }
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
                try {
                    setVolume();//ask the user for change the volume
                    setInput();//set the input
                    File choosedFiles[] = getFilesToTransfer(files);//get the files that need to be transfered
                    TransferFile.initFileArrInfo(choosedFiles);//
                    setVolumeGui();//set the volume of files in gui
                    updateDestination();
                    int userResult = JOptionPane.showConfirmDialog(new JFrame(), "procced?");
                    if (userResult == JOptionPane.OK_OPTION) {
                        createFolders();
                        transferFiles(choosedFiles, TransferFile.getTransferFilesInfo());
                        setFilesDate(TransferFile.getTransferFilesInfo());
                        userResult = JOptionPane.showConfirmDialog(new JFrame(), "Transfered successfuly , delete origin files?");
                        if (userResult == JOptionPane.OK_OPTION) {
                            deletOriginFiles();
                        }
                        clearUserInputs();
                    }
                } catch (Exception ez) {

                }
            } else {//announce the user
                System.out.println("not valid");
            }
        }
    }

    private boolean chooseDirectory() {
        directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = directoryChooser.showOpenDialog(new JFrame());
        if (result == JFileChooser.APPROVE_OPTION) {
            return true;
        } else {
            return false;
        }
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
        //check folder1 fill
        if (fileMoverPanel.getFolder1Name().isEmpty() && validTar1) {
            return false;
        }
        //check folder2 fill
        if (fileMoverPanel.getFolder2Name().isEmpty() && validTar2) {
            return false;
        }
        //check offset
        if (!notEmptyAndNum(fileMoverPanel.getOffset())) {
            JOptionPane.showMessageDialog(new JFrame(), 
            "intiliaze the offset with a number, if you dont want an offset enter 0");
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

    private void transferFiles(File choosedFiles[], ArrayList<TransferFile> filesInfo) throws IOException {
        String toPath , newName;
        Path to = null, from;
        for (int i = 0; i < choosedFiles.length; i++) {//copy file to the destinations
            toPath = targetsPath.get(0);
            from = Paths.get(choosedFiles[i].getAbsolutePath());
            if (toPath != null) {
                newName = filesInfo.get(i).getName()
                        + "_" + volume
                        + "_" + getFileName(choosedFiles[i].getName());
                to = Paths.get(toPath + "/" + newName + getFileEnding(choosedFiles[i]));
                Files.copy(from, to);
            }

            toPath = targetsPath.get(1);
            if (toPath != null) {//regular transfer
                newName = TransferFile.getFileNameFormatDay(filesInfo.get(i).getCreateTime()) + "_" + getFileName(choosedFiles[i].getName());
                to = Paths.get(toPath + "/" + newName + getFileEnding(choosedFiles[i]));
                from = Paths.get(srcPath + "/" + choosedFiles[i].getName());
                Files.copy(from, to);
            }
        }
    }

    private void createFolders() {
        for (int i = 0; i < targetsPath.size(); i++) {
            File f = new File(targetsPath.get(i));
            f.mkdir();
        }
    }

    /*update destination in gui*/
    private void updateDestination() {
        FileMoverPanel panel = FileMoverPanel.getFileMoverPanel();

        //update gui targets
        temp1Path = target1Path + "/" + panel.getFolder1Name();
        temp2Path = target2Path + "/" + panel.getFolder2Name();
        if(target1Path != null)
            panel.setChosenTar1(temp1Path);
        if(target2Path != null)
            panel.setChosenTar2(temp2Path);
        //add to targetsPath
        targetsPath.clear();
        targetsPath.add(temp1Path);
        targetsPath.add(temp2Path);
    }

    /*select the files to transfer*/
    private File[] getFilesToTransfer(File[] files) {
        String pattern = FileMoverPanel.getFileMoverPanel().getType();
        ArrayList<File> file2Transfer = new ArrayList<>();

        for (File file : files) {
            if (fileType.length() > 0) {//filtered by file type
                if (file.getName().endsWith(fileType)) {
                    file2Transfer.add(file);
                }
            } else {
                file2Transfer.add(file);
            }
        }
        File arrFile[] = new File[file2Transfer.size()];
        return file2Transfer.toArray(arrFile);
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
        FileMoverPanel.getFileMoverPanel().setVolumeInput(volume);
    }

    /*change the input of the volume*/
    private void setVolume() {
        String volume = "";
        volume = JOptionPane.showInputDialog(new JFrame(), "for change the volume enter a volume else press ok");
        if (!volume.isEmpty()) {
            FileMoverPanel.getFileMoverPanel().setVolumeInput(volume);
        }
    }

    private void setFileCreationDate(File newF, Date createdTime) throws IOException {
        BasicFileAttributeView attributes = Files.getFileAttributeView(Paths.get(newF.getAbsolutePath()), BasicFileAttributeView.class);
        FileTime newFileTime = FileTime.fromMillis(createdTime.getTime());
        attributes.setTimes(newFileTime, newFileTime, newFileTime);

    }

    /*getting the file ending*/
    private String getFileEnding(File f) {
        String ending = "";
        int dotIndex;
        for (dotIndex = f.getName().length() - 1; dotIndex > 0; dotIndex--) {
            if (f.getName().charAt(dotIndex) == '.') {
                break;
            }
        }
        ending = f.getName().substring(dotIndex);
        return ending;
    }

    private void setFilesDate(ArrayList<TransferFile> transferFilesInfo) throws Exception {
        for (String targetPath : targetsPath) {
            if (targetPath != null) {
                File dir = new File(targetPath);
                File dirFiles[] = dir.listFiles();
                for (int i = 0; i < transferFilesInfo.size(); i++) {
                    setFileCreationDate(dirFiles[i], transferFilesInfo.get(i).getCreateTime());
                }
            }
        }
    }

    private void deletOriginFiles() {
        for (int i = 0; i < files.length; i++) {
            try {
                Files.delete(Paths.get(files[i].getAbsolutePath()));
            } catch (IOException ex) {
                Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private String getFileName(String name) {
        String output = "";
        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) == '.') {
                output = name.substring(0, i);
                break;
            }
        }
        return output;
    }

    /*clear user inputs and refresh the paths*/
    private void clearUserInputs() {
        FileMoverPanel.getFileMoverPanel().clearInputs();//clear inputs in gui
        srcPath = "";
        target1Path = "";
        target2Path = "";
        targetsPath.clear();
        files = null;
    }
}
