/*
 * tobbszaluHibaertesito.java
 *
 * Created on 2005. �prilis 18., 17:52
 */

package hu.wfs.vonalkod.shared;
import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
/**
 *
 * @author B�ci
 */
public class TobbszaluHibaertesito extends Thread {

		 public  static int �zenet(
			Component _komponens,
			String _�zenet
		) {
			TobbszaluHibaertesito �rtes�t�=new TobbszaluHibaertesito();
			�rtes�t�.komponens=_komponens;
			�rtes�t�.�zenet=_�zenet;
			try {
				SwingUtilities.invokeAndWait(�rtes�t�);					
			} catch (Throwable kiv) {
				return -1;
			}			
			return �rtes�t�.v�lasz;
		}
		
		private Component komponens;
		private String �zenet;
		private int v�lasz;
		
		public void run() {
			TobbszaluHibaertesito.this.v�lasz=
				JOptionPane.showConfirmDialog(
					komponens, 
					�zenet, 
					"Figyelem!", 
					JOptionPane.YES_NO_OPTION);				

		}
	 }
