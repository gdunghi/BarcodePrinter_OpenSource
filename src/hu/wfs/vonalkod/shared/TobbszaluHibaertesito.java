/*
 * tobbszaluHibaertesito.java
 *
 * Created on 2005. április 18., 17:52
 */

package hu.wfs.vonalkod.shared;
import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
/**
 *
 * @author Béci
 */
public class TobbszaluHibaertesito extends Thread {

		 public  static int üzenet(
			Component _komponens,
			String _üzenet
		) {
			TobbszaluHibaertesito értesítõ=new TobbszaluHibaertesito();
			értesítõ.komponens=_komponens;
			értesítõ.üzenet=_üzenet;
			try {
				SwingUtilities.invokeAndWait(értesítõ);					
			} catch (Throwable kiv) {
				return -1;
			}			
			return értesítõ.válasz;
		}
		
		private Component komponens;
		private String üzenet;
		private int válasz;
		
		public void run() {
			TobbszaluHibaertesito.this.válasz=
				JOptionPane.showConfirmDialog(
					komponens, 
					üzenet, 
					"Figyelem!", 
					JOptionPane.YES_NO_OPTION);				

		}
	 }
