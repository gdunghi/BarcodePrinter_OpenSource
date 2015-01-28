/*
 * Nyomtatando.java
 *
 * Created on 2005. április 23., 20:08
 */

package hu.wfs.vonalkod.modell;

import net.sourceforge.barbecue.Barcode;

/**
 *
 * @author Béci
 */
public class Nyomtatando {
	private  String kódolandóSzöveg;
	private int mennyiség;
	private Barcode vonalkód;
	private String szabvány;

	/**
	 * Getter for property kódolandóSzöveg.
	 * @return Value of property kódolandóSzöveg.
	 */
	public String getKódolandóSzöveg() {

		return this.kódolandóSzöveg;
	}

	/**
	 * Setter for property kódolandóSzöveg.
	 * @param kódolandóSzöveg New value of property kódolandóSzöveg.
	 */
	public void setKódolandóSzöveg(String kódolandóSzöveg) {

		this.kódolandóSzöveg = kódolandóSzöveg;
	}

	/**
	 * Getter for property mennyiség.
	 * @return Value of property mennyiség.
	 */
	public int getMennyiség() {

		return this.mennyiség;
	}

	/**
	 * Setter for property mennyiség.
	 * @param mennyiség New value of property mennyiség.
	 */
	public void setMennyiség(int mennyiség) {

		this.mennyiség = mennyiség;
	}

	/**
	 * Getter for property szabvány.
	 * @return Value of property szabvány.
	 */
	public String getSzabvány() {

		return this.szabvány;
	}

	/**
	 * Setter for property szabvány.
	 * @param szabvány New value of property szabvány.
	 */
	public void setSzabvány(String szabvány) {

		this.szabvány = szabvány;
	}

	/**
	 * Getter for property vonalkód.
	 * @return Value of property vonalkód.
	 */
	public Barcode getVonalkód() {

		return this.vonalkód;
	}

	/**
	 * Setter for property vonalkód.
	 * @param vonalkód New value of property vonalkód.
	 */
	public void setVonalkód(Barcode vonalkód) {

		this.vonalkód = vonalkód;
	}
}
