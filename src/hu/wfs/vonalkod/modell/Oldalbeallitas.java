/*
 * Oldalbeallitas.java
 *
 * Created on 2005. �prilis 15., 19:19
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
 * @author B�ci
 */
public class Oldalbeallitas implements Serializable {

	public static final int FELBONT�S_DPI=72;
	
	public double getPixelByMM(double mm) {
		return mm/10/2.54*FELBONT�S_DPI;
	} 
	
	
	/**
	 * Az �rt�k egys�ge: mm
	 */
	public double pap�rsz�less�g;

	public double pap�rsz�less�gInPixel() {
		return getPixelByMM(pap�rsz�less�g);
	}
	/**
	 * Az �rt�k egys�ge: mm
	 */
	public double pap�rMagass�g;
	public double pap�rMagass�gInPixel() {
		return getPixelByMM(pap�rMagass�g);
	}	
	/**
	 * Az �rt�k egys�ge: mm
	 */
	public double marg�Bal;
	public double marg�BalInPixel() {
		return getPixelByMM(marg�Bal);
	}	
	/**
	 * Az �rt�k egys�ge: mm
	 */
	public double marg�Jobb;
	public double marg�JobbInPixel() {
		return getPixelByMM(marg�Jobb);
	}	
	/**
	 * Az �rt�k egys�ge: mm
	 */
	public double marg�Als�;
	public double marg�Als�InPixel() {
		return getPixelByMM(marg�Als�);
	}	
	/**
	 * Az �rt�k egys�ge: mm
	 */
	public double marg�Fels�;
	public double marg�Fels�InPixel() {
		return getPixelByMM(marg�Fels�);
	}	
	/**
	 * Az �rt�k egys�ge: mm
	 */
	public double cimkeSz�less�g;
	public double cimkeSz�less�gInPixel() {
		return getPixelByMM(cimkeSz�less�g);
	}	
	/**
	 * Az �rt�k egys�ge: mm
	 */
	public double cimkeMagass�g;
	public double cimkeMagass�gInPixel() {
		return getPixelByMM(cimkeMagass�g);
	}	
	/**
	 * Az �rt�k egys�ge: mm
	 */
	public double cimkeF�gg�legesK�z;
	public double cimkeF�gg�legesK�zInPixel() {
		return getPixelByMM(cimkeF�gg�legesK�z);
	}	
	/**
	 * Az �rt�k egys�ge: mm
	 */
	public double cimkeV�zszintesK�z;
	public double cimkeV�zszintesK�zInPixel() {
		return getPixelByMM(cimkeV�zszintesK�z);
	}	
	/**
	 * Az �rt�k egys�ge: db
	 */
	public int cimkeOszlopokSz�ma;
	/**
	 * Az �rt�k egys�ge: db
	 */
	public int cimkeSorokSz�ma;
	
	public void ment�s(File f�jl) throws Throwable {
		ObjectOutputStream os=new ObjectOutputStream(new FileOutputStream(f�jl));
		os.writeObject(this);
		os.flush();
		os.close();
	}
	
	public static Oldalbeallitas bet�lt�s(File f�jl) throws Throwable {
		ObjectInputStream ois=new ObjectInputStream(new FileInputStream(f�jl));
		Oldalbeallitas oldalbeallitas=(Oldalbeallitas)ois.readObject();;
		ois.close();
		return oldalbeallitas;
	}
	
	public boolean is�rv�nyes() {
		return is�rv�nyesF�gg�legesen()&&is�rv�nyesV�zszintesen();
	}
	
	public boolean is�rv�nyesF�gg�legesen() {
		double nyomtatand�Magass�g=
			marg�Fels�+
			cimkeMagass�g*cimkeSorokSz�ma+
			cimkeF�gg�legesK�z*(cimkeSorokSz�ma-1);
		return nyomtatand�Magass�g<=pap�rMagass�g;
	}
	
	public boolean is�rv�nyesV�zszintesen() {
		double nyomtatand�Sz�less�g=
			marg�Bal+
			cimkeSz�less�g*cimkeOszlopokSz�ma+
			cimkeV�zszintesK�z*(cimkeOszlopokSz�ma-1);
		return nyomtatand�Sz�less�g<=pap�rsz�less�g;
	}
}
