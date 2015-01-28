/*
 * Oldalbeallitas.java
 *
 * Created on 2005. április 15., 19:19
 */

package hu.wfs.vonalkod.modell;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 
 * @author Béci
 */
public class Oldalbeallitas implements Serializable {

	public static final int FELBONTÁS_DPI=72;
	
	public double getPixelByMM(double mm) {
		return mm/10/2.54*FELBONTÁS_DPI;
	} 
	
	
	/**
	 * Az érték egysége: mm
	 */
	public double papírszélesség;

	public double papírszélességInPixel() {
		return getPixelByMM(papírszélesség);
	}
	/**
	 * Az érték egysége: mm
	 */
	public double papírMagasság;
	public double papírMagasságInPixel() {
		return getPixelByMM(papírMagasság);
	}	
	/**
	 * Az érték egysége: mm
	 */
	public double margóBal;
	public double margóBalInPixel() {
		return getPixelByMM(margóBal);
	}	
	/**
	 * Az érték egysége: mm
	 */
	public double margóJobb;
	public double margóJobbInPixel() {
		return getPixelByMM(margóJobb);
	}	
	/**
	 * Az érték egysége: mm
	 */
	public double margóAlsó;
	public double margóAlsóInPixel() {
		return getPixelByMM(margóAlsó);
	}	
	/**
	 * Az érték egysége: mm
	 */
	public double margóFelsõ;
	public double margóFelsõInPixel() {
		return getPixelByMM(margóFelsõ);
	}	
	/**
	 * Az érték egysége: mm
	 */
	public double cimkeSzélesség;
	public double cimkeSzélességInPixel() {
		return getPixelByMM(cimkeSzélesség);
	}	
	/**
	 * Az érték egysége: mm
	 */
	public double cimkeMagasság;
	public double cimkeMagasságInPixel() {
		return getPixelByMM(cimkeMagasság);
	}	
	/**
	 * Az érték egysége: mm
	 */
	public double cimkeFüggõlegesKöz;
	public double cimkeFüggõlegesKözInPixel() {
		return getPixelByMM(cimkeFüggõlegesKöz);
	}	
	/**
	 * Az érték egysége: mm
	 */
	public double cimkeVízszintesKöz;
	public double cimkeVízszintesKözInPixel() {
		return getPixelByMM(cimkeVízszintesKöz);
	}	
	/**
	 * Az érték egysége: db
	 */
	public int cimkeOszlopokSzáma;
	/**
	 * Az érték egysége: db
	 */
	public int cimkeSorokSzáma;
	
	public void mentés(File fájl) throws Throwable {
		ObjectOutputStream os=new ObjectOutputStream(new FileOutputStream(fájl));
		os.writeObject(this);
		os.flush();
		os.close();
	}
	
	public static Oldalbeallitas betöltés(File fájl) throws Throwable {
		ObjectInputStream ois=new ObjectInputStream(new FileInputStream(fájl));
		Oldalbeallitas oldalbeallitas=(Oldalbeallitas)ois.readObject();;
		ois.close();
		return oldalbeallitas;
	}
	
	public boolean isÉrvényes() {
		return isÉrvényesFüggõlegesen()&&isÉrvényesVízszintesen();
	}
	
	public boolean isÉrvényesFüggõlegesen() {
		double nyomtatandóMagasság=
			margóFelsõ+
			cimkeMagasság*cimkeSorokSzáma+
			cimkeFüggõlegesKöz*(cimkeSorokSzáma-1);
		return nyomtatandóMagasság<=papírMagasság;
	}
	
	public boolean isÉrvényesVízszintesen() {
		double nyomtatandóSzélesség=
			margóBal+
			cimkeSzélesség*cimkeOszlopokSzáma+
			cimkeVízszintesKöz*(cimkeOszlopokSzáma-1);
		return nyomtatandóSzélesség<=papírszélesség;
	}
}
