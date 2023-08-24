/*
 * KONSTANTINOS KIKIDIS (4387) 
 * CHRISTOS KROKIDAS (4399) 
 * KONSTANTINOS TSAMPIRAS (4508)
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class Plot extends Frame {
	
	private ArrayList<Cdata> correctList = new ArrayList<Cdata>(); 
	private ArrayList<Cdata> wrongList = new ArrayList<Cdata>();
	
    public Plot(ArrayList<Cdata> corr,ArrayList<Cdata> wrong){
        this.correctList = corr;
        this.wrongList = wrong;
        prepareGUI();
    }

    private void prepareGUI(){
        setSize(800,800);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
            System.exit(0);
            }        
        }); 
    }    

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        Font font = new Font("Serif", Font.PLAIN, 15);
        
        
        g2.setFont(font);
        g2.translate(0, getHeight());
        g2.scale(1.0, -1.0);
        
        for(int i = 0; i < correctList.size(); i++) {
            float t1 = (float)((correctList.get(i).getX1() + 2)/4);
            float t2 = (float)((correctList.get(i).getX2() + 2)/4);
            float x = (1 - t1)*10 + t1*800;
            float y = (1 - t2)*10 + t2*800;
            switch(correctList.get(i).getC()) {
                case "C1": 
                    g2.setPaint(Color.magenta);
                    break; 
                case "C2": 
                    g2.setPaint(Color.green);
                    break; 
                case "C3": 
                    g2.setPaint(Color.red);  
                    break; 
            }
            
            g2.drawString("+", x, y);
        }
        for(int i = 0; i < wrongList.size(); i++) {
            float t1 = (float)((wrongList.get(i).getX1() + 2)/4);
            float t2 = (float)((wrongList.get(i).getX2() + 2)/4);
            float x = (1 - t1)*10 + t1*800;
            float y = (1 - t2)*10 + t2*800;
            g2.setPaint(Color.BLACK);
            g2.drawString("-", x, y);
        }
    }

}
