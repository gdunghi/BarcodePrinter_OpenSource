/*
 * Utils.java
 *
 * Created on 2005. április 13., 21:36
 */

package hu.wfs.vonalkod.shared;

import java.awt.Component;
import java.io.InputStream;
import javax.swing.JOptionPane;

/**
 *
 * @author Béci
 */
public class Utils {
	
	public static void hibaÜzenet(Component szülõKomponens,String üzenet) {
		JOptionPane.showMessageDialog(szülõKomponens,üzenet, "Hiba!", JOptionPane.ERROR_MESSAGE);
	}
	
	public static void üzenet(Component szülõKomponens,String üzenet) {
		JOptionPane.showMessageDialog(szülõKomponens,üzenet, "Figyelem!", JOptionPane.INFORMATION_MESSAGE);
	}	
	
	public static void hibaÜzenet(Component szülõKomponens,String üzenet,Throwable kivétel) {
		kivétel.printStackTrace();
		hibaÜzenet(szülõKomponens,üzenet+" További infó a konzolon!");
	}
	
	public static byte[] inputStreamToByteArray(InputStream is) throws Throwable {
		int blokk=1024;
		int hossz=0;
		byte[] buffer=new byte[blokk];
		ByteArrayBuffer bab=new ByteArrayBuffer();
		do {
			while((hossz=is.read(buffer))==blokk)
				bab.append(buffer);
			if (hossz!=-1)
				bab.append(buffer,0,hossz);
		} while(hossz!=-1);
		return bab.getBytes();
	}
	

}
