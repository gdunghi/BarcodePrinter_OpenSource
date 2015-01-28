/*
 * HasznIngTabla.java
 *
 * Created on 2004. december 30., 9:20
 */

package hu.wfs.vonalkod.gui.tablak;

import hu.wfs.vonalkod.gui.tablak.AbsztraktTabla.DinamikusOszlopLeiro;
import hu.wfs.vonalkod.gui.tablak.AbsztraktTabla.OszlopLeiro;
import hu.wfs.vonalkod.gui.tablak.AbsztraktTabla.Tabla;
import hu.wfs.vonalkod.gui.tablak.AbsztraktTabla.TablaModell;
import hu.wfs.vonalkod.shared.TableSorter;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;


/**
 *
 * @author  Rendszergazda
 */
public abstract class AbsztraktTabla  {
	
	protected Tabla t�bla;
	protected TablaModell t�blamodell;
	protected int T�BLA_ALAPSORMAGASS�G=15;
	protected ArrayList oszlopLe�r�k=new ArrayList();
	protected ArrayList dinamikusOszlopLe�r�k=new ArrayList();
	protected ArrayList adatok=new ArrayList();
	protected TableSorter t�blaRendez�;
	
	public void storeOszlopbe�ll�t�sok(OutputStream os) throws Throwable {
		dinOszle�r�k�sRendez�sSzinkroniz�l�sa();
		DinOszle�r�k�sOszlopokSzinkroniz�l�sa();
		t�blaModellAktualiz�l�sa();
		ObjectOutputStream oos=new ObjectOutputStream(os);
		oos.writeObject(dinamikusOszlopLe�r�k);
		oos.flush();
		oos.close();
	}
	
	public void loadOszlopbe�ll�t�sok(InputStream is) throws Throwable {
		ObjectInputStream oos=new ObjectInputStream(is);
		ArrayList csonkaDinLe�r�k=(ArrayList)oos.readObject();
		oos.close();
		if (csonkaDinLe�r�k==null) throw new Exception("Hiba a bet�lt�skor!");
		Iterator itr=csonkaDinLe�r�k.iterator();
		while(itr.hasNext()) {
			DinamikusOszlopLeiro le�r�=(DinamikusOszlopLeiro)itr.next();
			le�r�.oszlopLe�r�=getAlapOszle�r�ByN�v(le�r�.n�v);
		}
		if (csonkaDinLe�r�k.size()>0) {
			dinamikusOszlopLe�r�k=csonkaDinLe�r�k;
			t�blaModellAktualiz�l�sa();
			init_OszlopBe�ll�t�sokMen�();
		}
	}
	
	
	private void dinamikusOszlople�r�kAlaphelyzetbe() {
		dinamikusOszlopLe�r�k.clear();
		Iterator itr=oszlopLe�r�k.iterator();
		while(itr.hasNext()) {
			OszlopLeiro alapLe�r�=(OszlopLeiro)itr.next();
			DinamikusOszlopLeiro dinamikusLe�r�=new DinamikusOszlopLeiro();
			dinamikusLe�r�.l�that�=alapLe�r�.l�that�;
			dinamikusLe�r�.sz�less�g=alapLe�r�.prefSz�less�g;
			dinamikusLe�r�.rendez�siSorsz�m=alapLe�r�.rendez�siSorsz�m;
			dinamikusLe�r�.rendez�siIr�ny=alapLe�r�.rendez�siIr�ny;
			dinamikusLe�r�.oszlopLe�r�=alapLe�r�;
			dinamikusLe�r�.n�v=alapLe�r�.n�v;
			dinamikusOszlopLe�r�k.add(dinamikusLe�r�);
			alapLe�r�.dinamikusOszlople�r�=dinamikusLe�r�;
		}
	}
	
	public List getAdatok() {
		return adatok;
	}
	
	
	protected DinamikusOszlopLeiro
			getDinamikusOszlopLe�r�(OszlopLeiro alapLe�r�) {
		Iterator itr=dinamikusOszlopLe�r�k.iterator();
		while(itr.hasNext()) {
			DinamikusOszlopLeiro le�r�=(DinamikusOszlopLeiro)itr.next();
			if (le�r�.oszlopLe�r�.equals(alapLe�r�))
				return le�r�;
		}
		return null;
	}
	
	public Tabla getT�blaKomponens() {
		return t�bla;
	}
	
	public void friss�t�s() {};
	
	/** Creates new form HasznIngTabla */
	public AbsztraktTabla()   {
		init_OszlopLe�r�k(oszlopLe�r�k);
		dinamikusOszlople�r�kAlaphelyzetbe();
		t�bla=new Tabla();
		t�blaModellAktualiz�l�sa();
		
		
		t�bla.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				fireListSelectionListenerValueChanged(e);
			}
		});
		init_OszlopMegj�ll�t�();
		init_OszlopBe�ll�t�sokMen�();
	}
	
	public Object getKiv�lasztott() {
		int kivSor=t�bla.getSelectedRow();
		if (kivSor==-1) return null;
		return adatok.get(t�blaRendez�.modelIndex(kivSor));
	}
	
	protected abstract void init_OszlopLe�r�k(ArrayList oszlopLe�r�k);
	
	protected JPopupMenu popupMenu_OszlopMegj=new JPopupMenu();
	
	protected void init_OszlopBe�ll�t�sokMen�() {
		popupMenu_OszlopMegj.removeAll();
		Iterator itr=oszlopLe�r�k.iterator();
		while(itr.hasNext()) {
			OszlopLeiro alapLe�r�=
					(OszlopLeiro)itr.next();
			JCheckBoxMenuItem men�Elem=new JCheckBoxMenuItem();
			men�Elem.setText(alapLe�r�.n�v);
			men�Elem.setSelected(getDinOszle�r�ByN�v(alapLe�r�.n�v).l�that�);
			men�Elem.addItemListener(elemFigyel�_OszlopMegj);
			DinamikusOszlopLeiro dinLe�r�=
					getDinOszle�r�ByN�v(alapLe�r�.n�v);
			men�Elem.putClientProperty("OszlopLeiro", dinLe�r�);
			popupMenu_OszlopMegj.add(men�Elem);
		}
		popupMenu_OszlopMegj.add(new JSeparator());
		JMenuItem alaphelyzet=new JMenuItem("Alaphelyzet");
		alaphelyzet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				oszlopBe�ll�t�sokAlaphelyzetbe();
			}
		});
		popupMenu_OszlopMegj.add(alaphelyzet);
	}
	
	private void oszlopBe�ll�t�sokAlaphelyzetbe() {
		dinamikusOszlople�r�kAlaphelyzetbe();
		t�blaModellAktualiz�l�sa();
		init_OszlopBe�ll�t�sokMen�();
	}
	
	private void dinLe�r�kRendez�siInf�inakT�rl�se() {
		Iterator itr=dinamikusOszlopLe�r�k.iterator();
		while(itr.hasNext()) {
			DinamikusOszlopLeiro le�r�=(DinamikusOszlopLeiro)itr.next();
			le�r�.rendez�siSorsz�m=null;
			le�r�.rendez�siIr�ny=0;
		}
	}
	
	private void dinOszle�r�k�sRendez�sSzinkroniz�l�sa() {
		dinLe�r�kRendez�siInf�inakT�rl�se();
		java.util.List direkt�v�k=t�blaRendez�.getRendez�siDirekt�v�k();
		Iterator itr=direkt�v�k.iterator();
		int rendez�siSorsz�m=1;
		while(itr.hasNext()) {
			TableSorter.Directive direkt�va=(TableSorter.Directive)itr.next();
			TableColumn oszlop=t�bla.getColumnModel().getColumn(direkt�va.column);
			DinamikusOszlopLeiro oszlopLe�r�=getDinOszle�r�ByN�v(oszlop.getHeaderValue().toString());
			oszlopLe�r�.rendez�siSorsz�m=new Integer(
					rendez�siSorsz�m++);
			oszlopLe�r�.rendez�siIr�ny=direkt�va.direction;
		}
	}
	
	protected void OszlopMegjV�ltBek�v(ItemEvent e) {
		JCheckBoxMenuItem men�Elem=(JCheckBoxMenuItem)e.getSource();
		DinamikusOszlopLeiro dinOszLe�r�=
				(DinamikusOszlopLeiro)men�Elem.getClientProperty("OszlopLeiro");
		if (getL�that�DinamikusOszlople�r�k().size()==1&&men�Elem.isSelected()==false) {
			men�Elem.setSelected(true);
			JOptionPane.showMessageDialog(t�bla,"Figyelem! Minimum egy oszlopnak l�that�nak kell lennie!");
			return;
		}
		
		dinOszle�r�k�sRendez�sSzinkroniz�l�sa();
		DinOszle�r�k�sOszlopokSzinkroniz�l�sa();
		
		dinOszLe�r�.l�that�=men�Elem.isSelected();
		if (dinOszLe�r�.l�that�==true) {
			int pozicio=oszlopLe�r�k.indexOf(dinOszLe�r�.oszlopLe�r�);
			dinamikusOszlopLe�r�k.remove(dinOszLe�r�);
			dinamikusOszlopLe�r�k.add(pozicio, dinOszLe�r�);
		} else {
			TableColumn oszlop=getOszlopByN�v(dinOszLe�r�.oszlopLe�r�.n�v);
			dinOszLe�r�.rendez�siSorsz�m=null;
			dinOszLe�r�.rendez�siIr�ny=0;
		}
		t�blaModellAktualiz�l�sa();
	}
	
	private void t�blaModellAktualiz�l�sa() {
		t�blamodell=new TablaModell();
		t�bla.setModell(t�blamodell);
		t�bla.setRowHeight(getT�blasorMagass�g());
	}
	
	private void DinOszle�r�k�sOszlopokSzinkroniz�l�sa() {
		ArrayList �jDinOszle�r�k=new ArrayList();
		int oszlopsz�m=t�bla.getColumnModel().getColumnCount();
		for(int i=0;i<oszlopsz�m;i++) {
			TableColumn oszlop=t�bla.getColumnModel().getColumn(i);
			String oszlopN�v=oszlop.getHeaderValue().toString();
			DinamikusOszlopLeiro dinLe�r�=getDinOszle�r�ByN�v(oszlopN�v);
			dinLe�r�.sz�less�g=new Integer(oszlop.getWidth());
			�jDinOszle�r�k.add(dinLe�r�);
		}
		dinamikusOszlopLe�r�k.removeAll(�jDinOszle�r�k);
		�jDinOszle�r�k.addAll(dinamikusOszlopLe�r�k);
		dinamikusOszlopLe�r�k=�jDinOszle�r�k;
	}
	
	private TableColumn getOszlopByN�v(String n�v) {
		int oszlopsz�m=t�bla.getColumnModel().getColumnCount();
		for(int i=0;i<oszlopsz�m;i++) {
			TableColumn oszlop=t�bla.getColumnModel().getColumn(i);
			String oszlopN�v=oszlop.getHeaderValue().toString();
			if (oszlopN�v.equals(n�v))
				return oszlop;
		}
		return null;
	}
	
	private OszlopLeiro getAlapOszle�r�ByN�v(String n�v) {
		Iterator itr=oszlopLe�r�k.iterator();
		while(itr.hasNext()) {
			OszlopLeiro le�r�=(OszlopLeiro)itr.next();
			if (le�r�.n�v.equals(n�v))
				return le�r�;
		}
		return null;
	}
	
	private DinamikusOszlopLeiro getDinOszle�r�ByN�v(String n�v) {
		Iterator itr=dinamikusOszlopLe�r�k.iterator();
		while(itr.hasNext()) {
			DinamikusOszlopLeiro le�r�=(DinamikusOszlopLeiro)itr.next();
			if (le�r�.oszlopLe�r�.n�v.equals(n�v))
				return le�r�;
		}
		return null;
	}
	
	
	private TableColumn getT�blaOszlopByN�v(String n�v) {
		TableColumn oszlop;
		int oszlopsz�m=t�bla.getColumnCount();
		for (int i=0;i<oszlopsz�m;i++) {
			oszlop=t�bla.getColumnModel().getColumn(i);
			if (
					oszlop.getHeaderValue()!=null&&
					oszlop.getHeaderValue().toString().equals(n�v)
					) return oszlop;
		}
		return null;
	}
	
	
	protected int getT�blasorMagass�g() {
		return T�BLA_ALAPSORMAGASS�G;
	}
	
	protected ItemListener elemFigyel�_OszlopMegj=new ItemListener() {
		public void itemStateChanged(ItemEvent e) {
			OszlopMegjV�ltBek�v(e);
		}
	};
	
	/**
	 * Utility field used by event firing mechanism.
	 */
	private javax.swing.event.EventListenerList listenerList =  null;
	
	protected void init_OszlopMegj�ll�t�() {
		t�bla.getTableHeader().addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent e) {
				Eg�rEsem�nyAT�blaFejl�cenKezel�(e);
			}
			public void mousePressed(MouseEvent e) {
				Eg�rEsem�nyAT�blaFejl�cenKezel�(e);
			}
		});
	}
	
	protected void Eg�rEsem�nyAT�blaFejl�cenKezel�(MouseEvent e){
		if (e.isPopupTrigger()) {
			popupMenu_OszlopMegj.show(e.getComponent(), e.getX(),e.getY());
		}
	}
	
	
	/**
	 * @return l�that�OszlopLe�r�k
	 */
	protected  ArrayList getL�that�DinamikusOszlople�r�k() {
		ArrayList l�that�DinamikusOszlople�r�k=new ArrayList();
		Iterator itr=dinamikusOszlopLe�r�k.iterator();
		while(itr.hasNext()) {
			DinamikusOszlopLeiro le�r�=(DinamikusOszlopLeiro)itr.next();
			if (le�r�.l�that�==true)
				l�that�DinamikusOszlople�r�k.add(le�r�);
		}
		return l�that�DinamikusOszlople�r�k;
	}
	
	/**
	 * Registers ListSelectionListener to receive events.
	 * @param listener The listener to register.
	 */
	public synchronized void addListSelectionListener(javax.swing.event.ListSelectionListener listener) {
		if (listenerList == null ) {
			listenerList = new javax.swing.event.EventListenerList();
		}
		listenerList.add(javax.swing.event.ListSelectionListener.class, listener);
	}
	
	/**
	 * Removes ListSelectionListener from the list of listeners.
	 * @param listener The listener to remove.
	 */
	public synchronized void removeListSelectionListener(javax.swing.event.ListSelectionListener listener) {
		listenerList.remove(javax.swing.event.ListSelectionListener.class, listener);
	}
	
	/**
	 * Notifies all registered listeners about the event.
	 *
	 * @param event The event to be fired
	 */
	private void fireListSelectionListenerValueChanged(javax.swing.event.ListSelectionEvent event) {
		if (listenerList == null) return;
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==javax.swing.event.ListSelectionListener.class) {
				((javax.swing.event.ListSelectionListener)listeners[i+1]).valueChanged(event);
			}
		}
	}
	
	private void setRendezetts�g() {
		ArrayList le�r�k=getL�that�DinamikusOszlople�r�k();
		ArrayList rendez�k=new ArrayList();
		Iterator itr=le�r�k.iterator();
		while(itr.hasNext()) {
			DinamikusOszlopLeiro le�r�=(DinamikusOszlopLeiro)itr.next();
			if (
					le�r�.rendez�siSorsz�m!=null&&
					le�r�.rendez�siIr�ny!=0
					) rendez�k.add(le�r�);
		}
		Comparator rendez�=new Comparator() {
			public int compare(Object o1, Object o2) {
				DinamikusOszlopLeiro ALe�r�=
						(DinamikusOszlopLeiro) o1;
				DinamikusOszlopLeiro BLe�r�=
						(DinamikusOszlopLeiro) o2;
				return ALe�r�.rendez�siSorsz�m.compareTo(
						BLe�r�.rendez�siSorsz�m);
			}
		};
		Collections.sort(rendez�k,rendez�);
		itr=rendez�k.iterator();
		while(itr.hasNext()) {
			DinamikusOszlopLeiro le�r�=(DinamikusOszlopLeiro)itr.next();
			TableColumn oszlop=getOszlopByN�v(le�r�.oszlopLe�r�.n�v);
			t�blaRendez�.setSortingStatus(
					oszlop.getModelIndex(), le�r�.rendez�siIr�ny);
		}
	}
	
	public static class DinamikusOszlopLeiro implements Serializable {
		public boolean l�that�=false;
		public String n�v=null;
		public Integer sz�less�g=null;
		public Integer rendez�siSorsz�m=null;
		public int rendez�siIr�ny=0;
		public transient OszlopLeiro oszlopLe�r�=null;
	}
	
	public  static class OszlopLeiro {
		public String n�v=null;
		public boolean l�that�=true;
		public Class oszt�ly=String.class;
		public TableCellRenderer renderel�=null;
		public TableCellEditor editor=null;
		public boolean szerkeszthet�=false;
		public Integer prefSz�less�g=null;
		public Integer minSz�less�g=null;
		public Integer maxSz�less�g=null;
		public String k�t�ttTulajd=null;
		public Class k�t�ttTulajdOszt�ly=String.class;
		public DinamikusOszlopLeiro dinamikusOszlople�r�=null;
		public Integer rendez�siSorsz�m=null;
		public int rendez�siIr�ny=0;
	}
	
	
	public class Tabla extends JTable {
		public TablaModell t�blaModell;
		
		public void setModell(TablaModell t�blaModell) {
			t�blaRendez�=new TableSorter(t�blaModell);
			setModel(t�blaRendez�);
			t�blaRendez�.setTableHeader(getTableHeader());
			init_Oszlopok();
			init_Tooltip();
			setRendezetts�g();
		}
		
		
		
		private void init_Tooltip() {
			// Tooltip be�ll�t�sa
			getTableHeader().setToolTipText(
					"<HTML>" +
					"<UL>" +
					"<li>Klikk a rendez�shez</li>"+
					"<li>Shift+Klikk a ford�tott rendez�shez</li>"+
					"<li>CTRL+'Klikk m�sik oszlopon' a tov�bbi alrendez�sekhez</li>"+
					"</UL>" +
					"</HTML>"
					);
		}
		
		public void init_Oszlopok() {
			Iterator itr=getL�that�DinamikusOszlople�r�k().iterator();
			int sorsz�m=0;
			while(itr.hasNext()) {
				DinamikusOszlopLeiro dinOszLe�r�=(DinamikusOszlopLeiro)itr.next();
				TableColumn oszlop=getColumnModel().getColumn(sorsz�m);
				if (dinOszLe�r�.oszlopLe�r�.renderel�!=null)
					oszlop.setCellRenderer(dinOszLe�r�.oszlopLe�r�.renderel�);
				if (dinOszLe�r�.sz�less�g!=null)
					oszlop.setPreferredWidth(
							dinOszLe�r�.sz�less�g.intValue());
				//		if (oszlopLe�r�.minSz�less�g!=null)
				//		    oszlop.setMinWidth(
				//			oszlopLe�r�.minSz�less�g.intValue());
				//		if (oszlopLe�r�.maxSz�less�g!=null)
				//		    oszlop.setMaxWidth(
				//			oszlopLe�r�.maxSz�less�g.intValue());
				sorsz�m++;
			}
		}
	}
	
	
	protected class TablaModell extends AbstractTableModel {
		
		public Object getValueAt(int rowIndex, int cIndex) {
			Object adat=adatok.get(rowIndex);
			DinamikusOszlopLeiro dinLe�r�=(DinamikusOszlopLeiro)
			getL�that�DinamikusOszlople�r�k().get(cIndex);
			if (dinLe�r�.oszlopLe�r�.k�t�ttTulajd!=null) {
				Method lek�rdez�Met�dus;
				try {
					if (dinLe�r�.oszlopLe�r�.k�t�ttTulajdOszt�ly.equals(Boolean.class)) {
						lek�rdez�Met�dus=adat.getClass().getMethod(
								"is"+dinLe�r�.oszlopLe�r�.k�t�ttTulajd, new Class[0]);
					} else {
						lek�rdez�Met�dus=adat.getClass().getMethod(
								"get"+dinLe�r�.oszlopLe�r�.k�t�ttTulajd, new Class[0]);
					}
					Object �rt�k=lek�rdez�Met�dus.invoke(adat, new Object[0]);
					return �rt�k;
				} catch (Throwable kiv) {
					kiv.printStackTrace();
					return null;
				}
			}
			return "???";
		}
		
		public int getColumnCount() {
			return getL�that�DinamikusOszlople�r�k().size();
		}
		
		public int getRowCount() {
			return adatok.size();
		}
		
		public String getColumnName(int cIndex) {
			DinamikusOszlopLeiro dinLe�r�=(DinamikusOszlopLeiro)
			getL�that�DinamikusOszlople�r�k().get(cIndex);
			return dinLe�r�.oszlopLe�r�.n�v;
		}
		
		public Class getColumnClass(int cIndex) {
			DinamikusOszlopLeiro dinLe�r�=(DinamikusOszlopLeiro)
			getL�that�DinamikusOszlople�r�k().get(cIndex);
			return dinLe�r�.oszlopLe�r�.oszt�ly;
		}
	}
	
	public static class TablaOszlop extends TableColumn {
		public OszlopLeiro oszlopLe�r�=null;
	}
}




