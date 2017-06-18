/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 *
 * @author omri
 */
public class FileMoverFrame extends JFrame{
    
    private int width = 0;
    private int height = 0;
    private final String windowLbl = "COPY/RENAME FILE UTILITY";

    public FileMoverFrame() {
        this.setScreenSize();
        this.setSize(width, height);
        this.setTitle(windowLbl);
        this.add(FileMoverPanel.getFileMoverPanel());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    
    
    /*set the screen size 1/3 and return array of int that first element is width and the other is height*/
    private void setScreenSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        this.width = (int)(width/3);
        this.height = (int)(height/3);
        
    }

    public static void main(String[] args) {
         FileMoverFrame f = new FileMoverFrame();
    }
}
