/*
 * Utils.java
 *
 * Created on 2005. �prilis 13., 21:36
 */

package hu.wfs.vonalkod.shared;

import java.awt.Component;
import java.io.InputStream;
import javax.swing.JOptionPane;

/**
 *
 * @author B�ci
 */
public class Utils {
	
	public static void hiba�zenet(Component sz�l�Komponens,String �zenet) {
		JOptionPane.showMessageDialog(sz�l�Komponens,�zenet, "Hiba!", JOptionPane.ERROR_MESSAGE);
	}
	
	public static void �zenet(Component sz�l�Komponens,String �zenet) {
		JOptionPane.showMessageDialog(sz�l�Komponens,�zenet, "Figyelem!", JOptionPane.INFORMATION_MESSAGE);
	}	
	
	public static void hiba�zenet(Component sz�l�Komponens,String �zenet,Throwable kiv�tel) {
		kiv�tel.printStackTrace();
		hiba�zenet(sz�l�Komponens,�zenet+" Tov�bbi inf� a konzolon!");
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
