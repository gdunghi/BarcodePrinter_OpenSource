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
		//t�bla.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//t�bla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}
	
	
	protected void init_OszlopLe�r�k(java.util.ArrayList oszlopLe�r�k) {
		AbsztraktTabla.OszlopLeiro ol;

		ol=new OszlopLeiro();
		ol.n�v="K�doland� sz�veg";
		ol.l�that�=true;
		ol.k�t�ttTulajd="K�doland�Sz�veg";
		ol.prefSz�less�g=new Integer(175);
		ol.rendez�siSorsz�m=new Integer(1);
		oszlopLe�r�k.add(ol);

		ol=new OszlopLeiro();
		ol.n�v="Mennyis�g";
		ol.l�that�=true;
		ol.k�t�ttTulajd="Mennyis�g";
		ol.prefSz�less�g=new Integer(80);
		ol.rendez�siSorsz�m=new Integer(1);
		oszlopLe�r�k.add(ol);
		
		ol=new OszlopLeiro();
		ol.n�v="K�dol�si szabv�ny";
		ol.l�that�=true;
		ol.k�t�ttTulajd="Szabv�ny";
		ol.prefSz�less�g=new Integer(80);
		ol.rendez�siSorsz�m=new Integer(1);
		oszlopLe�r�k.add(ol);
	}
	
	public void elemBesz�r�s(Object elem) {
		adatok.add(elem);
		t�blamodell.fireTableDataChanged();
	}
	
	public void kijel�ltekT�rl�se()  {
		adatok.removeAll(getKijel�ltek());		
		t�blamodell.fireTableDataChanged();
	}
	
	
	public ArrayList getKijel�ltek() {
		int[] sorIndexek=t�bla.getSelectedRows();
		ArrayList kijel�ltek=new ArrayList();
		for (int i=0;i<sorIndexek.length;i++) {
			Object adat=adatok.get(t�blaRendez�.modelIndex(sorIndexek[i]));
			kijel�ltek.add(adat);
		}		
		return kijel�ltek;
	}

	public void friss�t�s()  {
		t�blamodell.fireTableDataChanged();
	}
	
}
