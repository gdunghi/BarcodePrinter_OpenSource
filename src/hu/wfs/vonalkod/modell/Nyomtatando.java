/*
 * Nyomtatando.java
 *
 * Created on 2005. �prilis 23., 20:08
 */

package hu.wfs.vonalkod.modell;

import net.sourceforge.barbecue.Barcode;

/**
 *
 * @author B�ci
 */
public class Nyomtatando {
	private  String k�doland�Sz�veg;
	private int mennyis�g;
	private Barcode vonalk�d;
	private String szabv�ny;

	/**
	 * Getter for property k�doland�Sz�veg.
	 * @return Value of property k�doland�Sz�veg.
	 */
	public String getK�doland�Sz�veg() {

		return this.k�doland�Sz�veg;
	}

	/**
	 * Setter for property k�doland�Sz�veg.
	 * @param k�doland�Sz�veg New value of property k�doland�Sz�veg.
	 */
	public void setK�doland�Sz�veg(String k�doland�Sz�veg) {

		this.k�doland�Sz�veg = k�doland�Sz�veg;
	}

	/**
	 * Getter for property mennyis�g.
	 * @return Value of property mennyis�g.
	 */
	public int getMennyis�g() {

		return this.mennyis�g;
	}

	/**
	 * Setter for property mennyis�g.
	 * @param mennyis�g New value of property mennyis�g.
	 */
	public void setMennyis�g(int mennyis�g) {

		this.mennyis�g = mennyis�g;
	}

	/**
	 * Getter for property szabv�ny.
	 * @return Value of property szabv�ny.
	 */
	public String getSzabv�ny() {

		return this.szabv�ny;
	}

	/**
	 * Setter for property szabv�ny.
	 * @param szabv�ny New value of property szabv�ny.
	 */
	public void setSzabv�ny(String szabv�ny) {

		this.szabv�ny = szabv�ny;
	}

	/**
	 * Getter for property vonalk�d.
	 * @return Value of property vonalk�d.
	 */
	public Barcode getVonalk�d() {

		return this.vonalk�d;
	}

	/**
	 * Setter for property vonalk�d.
	 * @param vonalk�d New value of property vonalk�d.
	 */
	public void setVonalk�d(Barcode vonalk�d) {

		this.vonalk�d = vonalk�d;
	}
}
