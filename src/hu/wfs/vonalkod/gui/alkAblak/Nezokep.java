/*
 * Nezokep.java
 *
 * Created on 2005. �prilis 26., 0:29
 */

package hu.wfs.vonalkod.gui.alkAblak;

import hu.wfs.vonalkod.nyomtatas.VonalkodSeged;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import net.sourceforge.barbecue.Barcode;

/**
 *
 * @author  B�ci
 */
public class Nezokep extends javax.swing.JPanel {
	
	/** Creates new form Nezokep */
	public Nezokep() {
		initComponents();
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());

    }
    // </editor-fold>//GEN-END:initComponents
	
	public void setVonalk�d(Barcode _vonalk�d) {
		if (_vonalk�d!=null&&_vonalk�d==vonalk�d)
			return;
		vonalk�d=_vonalk�d;
		revalidate();
		repaint();
	}
	
	private  Barcode vonalk�d;

	protected void paintComponent(java.awt.Graphics g) {
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D)g;	
		if (vonalk�d!=null) {
			VonalkodSeged.rajzol�sVonalk�d(
				g2, vonalk�d, 
				(getWidth()-getWidth()*0.95d)/2d, 
				(getHeight()-getHeight()*0.85d)/2d, 
				getWidth()*0.95d, getHeight()*0.85d);
		} else {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);			
			TextLayout sz�veg=new TextLayout(
				"Nincsen kiv�lasztott elem!",
				new Font("Times New Roman",Font.BOLD,25),
				g2.getFontRenderContext());
			GeneralPath sz�vegPath=(GeneralPath)sz�veg.getOutline(new AffineTransform());
			double maxW=sz�vegPath.getBounds2D().getWidth()/0.8d;
			double maxH=sz�vegPath.getBounds2D().getHeight()/0.8d;
			double ar�nyX=1;
			double ar�nyY=1;
			if (maxW>getWidth()) {
				ar�nyX=(double)getWidth()/maxW;
				ar�nyY=ar�nyX;
			}
			AffineTransform aff=new AffineTransform();
			aff.scale(ar�nyX, ar�nyY);
			sz�vegPath.transform(aff);
			aff=new AffineTransform();
			aff.translate(
				(getWidth()-sz�vegPath.getBounds2D().getWidth())/2-sz�vegPath.getBounds2D().getMinX(),
				(getHeight()-sz�vegPath.getBounds2D().getHeight())/2-sz�vegPath.getBounds2D().getMinY());
			sz�vegPath.transform(aff);
			g2.fill(sz�vegPath);
		}
	}

	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
	
}