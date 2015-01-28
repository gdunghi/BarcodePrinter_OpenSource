/*
 * TelkekTabla.java
 *
 * Created on 2004. december 30., 18:03
 */

package hu.wfs.vonalkod.gui.alkAblak;

import java.util.ArrayList;
import hu.wfs.vonalkod.gui.tablak.*;




/**
 *
 * @author  Rendszergazda
 */
public class Nyomtatandok_Tbl extends AbsztraktTabla {
	
	public Nyomtatandok_Tbl() {
		super();
		//tábla.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//tábla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}
	
	
	protected void init_OszlopLeírók(java.util.ArrayList oszlopLeírók) {
		AbsztraktTabla.OszlopLeiro ol;

		ol=new OszlopLeiro();
		ol.név="Kódolandó szöveg";
		ol.látható=true;
		ol.kötöttTulajd="KódolandóSzöveg";
		ol.prefSzélesség=new Integer(175);
		ol.rendezésiSorszám=new Integer(1);
		oszlopLeírók.add(ol);

		ol=new OszlopLeiro();
		ol.név="Mennyiség";
		ol.látható=true;
		ol.kötöttTulajd="Mennyiség";
		ol.prefSzélesség=new Integer(80);
		ol.rendezésiSorszám=new Integer(1);
		oszlopLeírók.add(ol);
		
		ol=new OszlopLeiro();
		ol.név="Kódolási szabvány";
		ol.látható=true;
		ol.kötöttTulajd="Szabvány";
		ol.prefSzélesség=new Integer(80);
		ol.rendezésiSorszám=new Integer(1);
		oszlopLeírók.add(ol);
	}
	
	public void elemBeszúrás(Object elem) {
		adatok.add(elem);
		táblamodell.fireTableDataChanged();
	}
	
	public void kijelöltekTörlése()  {
		adatok.removeAll(getKijelöltek());		
		táblamodell.fireTableDataChanged();
	}
	
	
	public ArrayList getKijelöltek() {
		int[] sorIndexek=tábla.getSelectedRows();
		ArrayList kijelöltek=new ArrayList();
		for (int i=0;i<sorIndexek.length;i++) {
			Object adat=adatok.get(táblaRendezõ.modelIndex(sorIndexek[i]));
			kijelöltek.add(adat);
		}		
		return kijelöltek;
	}

	public void frissítés()  {
		táblamodell.fireTableDataChanged();
	}
	
}
