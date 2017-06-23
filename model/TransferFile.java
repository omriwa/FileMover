package model;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import view.FileMoverPanel;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import controller.Listener;

/**
 *
 * @author omri
 */
public class TransferFile {

    private static ArrayList<TransferFile> transferFilesInfo = new ArrayList<>();
    private static File choosedFiles[];
    private String name;
    private Date createTime;

    public static ArrayList<TransferFile> getTransferFilesInfo(){
        return transferFilesInfo;
    }
    
    public String getName() {
        return name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public static void initFileArrInfo(File files[]) {
        transferFilesInfo.clear();
        choosedFiles = files;
        for (File f : files) {
            transferFilesInfo.add(new TransferFile());
        }
        initFilesDate();
        initFilesName();
    }

    private static void initFilesName() {
        //get file creation date
        //format from date
        //init the the file name
        for(TransferFile fileInfo : transferFilesInfo){
            Date d = fileInfo.getCreateTime();
            fileInfo.setName(getFileNameFormat(d));
        }
    }

    private static void initFilesDate() {
        try {
            for (int i = 0; i < choosedFiles.length; i++) {
                //getFile creation time and set the offset
                Date createdTime = getFileCreateTime(choosedFiles[i].getAbsolutePath().toString());
                //init the date
                transferFilesInfo.get(i).setCreateTime(createdTime);
            }
        } catch (Exception e) {
            //handle
        }
    }
    /*get String of the creation date of the file as yyyyMMdd-hhmm*/
    private static String getFileNameFormat(Date date) {
        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd-hhmm");
        return formater.format(date);
    }

    private static Date getFileCreateTime(String url) throws IOException {
        Path path = Paths.get(url);
        try {
            BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
            getCreationTime(attr.creationTime().toMillis());
            long newTime = setOffset(attr.creationTime().toMillis());//set offset
            Date d = getCreationTime(newTime);
            return d;
        } catch (Exception ex) {
            throw new IOException();
        }
    }

    /*set the offset for the current time*/
    private static Long setOffset(long curTime) {
        long miliOffset = 0;
        int sec = Integer.parseInt(FileMoverPanel.getFileMoverPanel().getOffset());
        miliOffset = (long) ((Math.pow(10, 3)) * sec);//convert sec to mili
        curTime += miliOffset;
        return curTime;
    }
    /*return the date from a mili date*/
    private static Date getCreationTime(Long date) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        String dateCreated = df.format(date);
        return new Date(dateCreated);
    }
}
