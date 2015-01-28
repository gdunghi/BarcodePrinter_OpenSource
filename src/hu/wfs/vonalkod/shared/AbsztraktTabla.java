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
	
	protected Tabla tábla;
	protected TablaModell táblamodell;
	protected int TÁBLA_ALAPSORMAGASSÁG=15;
	protected ArrayList oszlopLeírók=new ArrayList();
	protected ArrayList dinamikusOszlopLeírók=new ArrayList();
	protected ArrayList adatok=new ArrayList();
	protected TableSorter táblaRendezõ;
	
	public void storeOszlopbeállítások(OutputStream os) throws Throwable {
		dinOszleírókÉsRendezésSzinkronizálása();
		DinOszleírókÉsOszlopokSzinkronizálása();
		táblaModellAktualizálása();
		ObjectOutputStream oos=new ObjectOutputStream(os);
		oos.writeObject(dinamikusOszlopLeírók);
		oos.flush();
		oos.close();
	}
	
	public void loadOszlopbeállítások(InputStream is) throws Throwable {
		ObjectInputStream oos=new ObjectInputStream(is);
		ArrayList csonkaDinLeírók=(ArrayList)oos.readObject();
		oos.close();
		if (csonkaDinLeírók==null) throw new Exception("Hiba a betöltéskor!");
		Iterator itr=csonkaDinLeírók.iterator();
		while(itr.hasNext()) {
			DinamikusOszlopLeiro leíró=(DinamikusOszlopLeiro)itr.next();
			leíró.oszlopLeíró=getAlapOszleíróByNév(leíró.név);
		}
		if (csonkaDinLeírók.size()>0) {
			dinamikusOszlopLeírók=csonkaDinLeírók;
			táblaModellAktualizálása();
			init_OszlopBeállításokMenü();
		}
	}
	
	
	private void dinamikusOszlopleírókAlaphelyzetbe() {
		dinamikusOszlopLeírók.clear();
		Iterator itr=oszlopLeírók.iterator();
		while(itr.hasNext()) {
			OszlopLeiro alapLeíró=(OszlopLeiro)itr.next();
			DinamikusOszlopLeiro dinamikusLeíró=new DinamikusOszlopLeiro();
			dinamikusLeíró.látható=alapLeíró.látható;
			dinamikusLeíró.szélesség=alapLeíró.prefSzélesség;
			dinamikusLeíró.rendezésiSorszám=alapLeíró.rendezésiSorszám;
			dinamikusLeíró.rendezésiIrány=alapLeíró.rendezésiIrány;
			dinamikusLeíró.oszlopLeíró=alapLeíró;
			dinamikusLeíró.név=alapLeíró.név;
			dinamikusOszlopLeírók.add(dinamikusLeíró);
			alapLeíró.dinamikusOszlopleíró=dinamikusLeíró;
		}
	}
	
	public List getAdatok() {
		return adatok;
	}
	
	
	protected DinamikusOszlopLeiro
			getDinamikusOszlopLeíró(OszlopLeiro alapLeíró) {
		Iterator itr=dinamikusOszlopLeírók.iterator();
		while(itr.hasNext()) {
			DinamikusOszlopLeiro leíró=(DinamikusOszlopLeiro)itr.next();
			if (leíró.oszlopLeíró.equals(alapLeíró))
				return leíró;
		}
		return null;
	}
	
	public Tabla getTáblaKomponens() {
		return tábla;
	}
	
	public void frissítés() {};
	
	/** Creates new form HasznIngTabla */
	public AbsztraktTabla()   {
		init_OszlopLeírók(oszlopLeírók);
		dinamikusOszlopleírókAlaphelyzetbe();
		tábla=new Tabla();
		táblaModellAktualizálása();
		
		
		tábla.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				fireListSelectionListenerValueChanged(e);
			}
		});
		init_OszlopMegjÁllító();
		init_OszlopBeállításokMenü();
	}
	
	public Object getKiválasztott() {
		int kivSor=tábla.getSelectedRow();
		if (kivSor==-1) return null;
		return adatok.get(táblaRendezõ.modelIndex(kivSor));
	}
	
	protected abstract void init_OszlopLeírók(ArrayList oszlopLeírók);
	
	protected JPopupMenu popupMenu_OszlopMegj=new JPopupMenu();
	
	protected void init_OszlopBeállításokMenü() {
		popupMenu_OszlopMegj.removeAll();
		Iterator itr=oszlopLeírók.iterator();
		while(itr.hasNext()) {
			OszlopLeiro alapLeíró=
					(OszlopLeiro)itr.next();
			JCheckBoxMenuItem menüElem=new JCheckBoxMenuItem();
			menüElem.setText(alapLeíró.név);
			menüElem.setSelected(getDinOszleíróByNév(alapLeíró.név).látható);
			menüElem.addItemListener(elemFigyelõ_OszlopMegj);
			DinamikusOszlopLeiro dinLeíró=
					getDinOszleíróByNév(alapLeíró.név);
			menüElem.putClientProperty("OszlopLeiro", dinLeíró);
			popupMenu_OszlopMegj.add(menüElem);
		}
		popupMenu_OszlopMegj.add(new JSeparator());
		JMenuItem alaphelyzet=new JMenuItem("Alaphelyzet");
		alaphelyzet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				oszlopBeállításokAlaphelyzetbe();
			}
		});
		popupMenu_OszlopMegj.add(alaphelyzet);
	}
	
	private void oszlopBeállításokAlaphelyzetbe() {
		dinamikusOszlopleírókAlaphelyzetbe();
		táblaModellAktualizálása();
		init_OszlopBeállításokMenü();
	}
	
	private void dinLeírókRendezésiInfóinakTörlése() {
		Iterator itr=dinamikusOszlopLeírók.iterator();
		while(itr.hasNext()) {
			DinamikusOszlopLeiro leíró=(DinamikusOszlopLeiro)itr.next();
			leíró.rendezésiSorszám=null;
			leíró.rendezésiIrány=0;
		}
	}
	
	private void dinOszleírókÉsRendezésSzinkronizálása() {
		dinLeírókRendezésiInfóinakTörlése();
		java.util.List direktívák=táblaRendezõ.getRendezésiDirektívák();
		Iterator itr=direktívák.iterator();
		int rendezésiSorszám=1;
		while(itr.hasNext()) {
			TableSorter.Directive direktíva=(TableSorter.Directive)itr.next();
			TableColumn oszlop=tábla.getColumnModel().getColumn(direktíva.column);
			DinamikusOszlopLeiro oszlopLeíró=getDinOszleíróByNév(oszlop.getHeaderValue().toString());
			oszlopLeíró.rendezésiSorszám=new Integer(
					rendezésiSorszám++);
			oszlopLeíró.rendezésiIrány=direktíva.direction;
		}
	}
	
	protected void OszlopMegjVáltBeköv(ItemEvent e) {
		JCheckBoxMenuItem menüElem=(JCheckBoxMenuItem)e.getSource();
		DinamikusOszlopLeiro dinOszLeíró=
				(DinamikusOszlopLeiro)menüElem.getClientProperty("OszlopLeiro");
		if (getLáthatóDinamikusOszlopleírók().size()==1&&menüElem.isSelected()==false) {
			menüElem.setSelected(true);
			JOptionPane.showMessageDialog(tábla,"Figyelem! Minimum egy oszlopnak láthatónak kell lennie!");
			return;
		}
		
		dinOszleírókÉsRendezésSzinkronizálása();
		DinOszleírókÉsOszlopokSzinkronizálása();
		
		dinOszLeíró.látható=menüElem.isSelected();
		if (dinOszLeíró.látható==true) {
			int pozicio=oszlopLeírók.indexOf(dinOszLeíró.oszlopLeíró);
			dinamikusOszlopLeírók.remove(dinOszLeíró);
			dinamikusOszlopLeírók.add(pozicio, dinOszLeíró);
		} else {
			TableColumn oszlop=getOszlopByNév(dinOszLeíró.oszlopLeíró.név);
			dinOszLeíró.rendezésiSorszám=null;
			dinOszLeíró.rendezésiIrány=0;
		}
		táblaModellAktualizálása();
	}
	
	private void táblaModellAktualizálása() {
		táblamodell=new TablaModell();
		tábla.setModell(táblamodell);
		tábla.setRowHeight(getTáblasorMagasság());
	}
	
	private void DinOszleírókÉsOszlopokSzinkronizálása() {
		ArrayList újDinOszleírók=new ArrayList();
		int oszlopszám=tábla.getColumnModel().getColumnCount();
		for(int i=0;i<oszlopszám;i++) {
			TableColumn oszlop=tábla.getColumnModel().getColumn(i);
			String oszlopNév=oszlop.getHeaderValue().toString();
			DinamikusOszlopLeiro dinLeíró=getDinOszleíróByNév(oszlopNév);
			dinLeíró.szélesség=new Integer(oszlop.getWidth());
			újDinOszleírók.add(dinLeíró);
		}
		dinamikusOszlopLeírók.removeAll(újDinOszleírók);
		újDinOszleírók.addAll(dinamikusOszlopLeírók);
		dinamikusOszlopLeírók=újDinOszleírók;
	}
	
	private TableColumn getOszlopByNév(String név) {
		int oszlopszám=tábla.getColumnModel().getColumnCount();
		for(int i=0;i<oszlopszám;i++) {
			TableColumn oszlop=tábla.getColumnModel().getColumn(i);
			String oszlopNév=oszlop.getHeaderValue().toString();
			if (oszlopNév.equals(név))
				return oszlop;
		}
		return null;
	}
	
	private OszlopLeiro getAlapOszleíróByNév(String név) {
		Iterator itr=oszlopLeírók.iterator();
		while(itr.hasNext()) {
			OszlopLeiro leíró=(OszlopLeiro)itr.next();
			if (leíró.név.equals(név))
				return leíró;
		}
		return null;
	}
	
	private DinamikusOszlopLeiro getDinOszleíróByNév(String név) {
		Iterator itr=dinamikusOszlopLeírók.iterator();
		while(itr.hasNext()) {
			DinamikusOszlopLeiro leíró=(DinamikusOszlopLeiro)itr.next();
			if (leíró.oszlopLeíró.név.equals(név))
				return leíró;
		}
		return null;
	}
	
	
	private TableColumn getTáblaOszlopByNév(String név) {
		TableColumn oszlop;
		int oszlopszám=tábla.getColumnCount();
		for (int i=0;i<oszlopszám;i++) {
			oszlop=tábla.getColumnModel().getColumn(i);
			if (
					oszlop.getHeaderValue()!=null&&
					oszlop.getHeaderValue().toString().equals(név)
					) return oszlop;
		}
		return null;
	}
	
	
	protected int getTáblasorMagasság() {
		return TÁBLA_ALAPSORMAGASSÁG;
	}
	
	protected ItemListener elemFigyelõ_OszlopMegj=new ItemListener() {
		public void itemStateChanged(ItemEvent e) {
			OszlopMegjVáltBeköv(e);
		}
	};
	
	/**
	 * Utility field used by event firing mechanism.
	 */
	private javax.swing.event.EventListenerList listenerList =  null;
	
	protected void init_OszlopMegjÁllító() {
		tábla.getTableHeader().addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent e) {
				EgérEseményATáblaFejlécenKezelõ(e);
			}
			public void mousePressed(MouseEvent e) {
				EgérEseményATáblaFejlécenKezelõ(e);
			}
		});
	}
	
	protected void EgérEseményATáblaFejlécenKezelõ(MouseEvent e){
		if (e.isPopupTrigger()) {
			popupMenu_OszlopMegj.show(e.getComponent(), e.getX(),e.getY());
		}
	}
	
	
	/**
	 * @return láthatóOszlopLeírók
	 */
	protected  ArrayList getLáthatóDinamikusOszlopleírók() {
		ArrayList láthatóDinamikusOszlopleírók=new ArrayList();
		Iterator itr=dinamikusOszlopLeírók.iterator();
		while(itr.hasNext()) {
			DinamikusOszlopLeiro leíró=(DinamikusOszlopLeiro)itr.next();
			if (leíró.látható==true)
				láthatóDinamikusOszlopleírók.add(leíró);
		}
		return láthatóDinamikusOszlopleírók;
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
	
	private void setRendezettség() {
		ArrayList leírók=getLáthatóDinamikusOszlopleírók();
		ArrayList rendezõk=new ArrayList();
		Iterator itr=leírók.iterator();
		while(itr.hasNext()) {
			DinamikusOszlopLeiro leíró=(DinamikusOszlopLeiro)itr.next();
			if (
					leíró.rendezésiSorszám!=null&&
					leíró.rendezésiIrány!=0
					) rendezõk.add(leíró);
		}
		Comparator rendezõ=new Comparator() {
			public int compare(Object o1, Object o2) {
				DinamikusOszlopLeiro ALeíró=
						(DinamikusOszlopLeiro) o1;
				DinamikusOszlopLeiro BLeíró=
						(DinamikusOszlopLeiro) o2;
				return ALeíró.rendezésiSorszám.compareTo(
						BLeíró.rendezésiSorszám);
			}
		};
		Collections.sort(rendezõk,rendezõ);
		itr=rendezõk.iterator();
		while(itr.hasNext()) {
			DinamikusOszlopLeiro leíró=(DinamikusOszlopLeiro)itr.next();
			TableColumn oszlop=getOszlopByNév(leíró.oszlopLeíró.név);
			táblaRendezõ.setSortingStatus(
					oszlop.getModelIndex(), leíró.rendezésiIrány);
		}
	}
	
	public static class DinamikusOszlopLeiro implements Serializable {
		public boolean látható=false;
		public String név=null;
		public Integer szélesség=null;
		public Integer rendezésiSorszám=null;
		public int rendezésiIrány=0;
		public transient OszlopLeiro oszlopLeíró=null;
	}
	
	public  static class OszlopLeiro {
		public String név=null;
		public boolean látható=true;
		public Class osztály=String.class;
		public TableCellRenderer renderelõ=null;
		public TableCellEditor editor=null;
		public boolean szerkeszthetõ=false;
		public Integer prefSzélesség=null;
		public Integer minSzélesség=null;
		public Integer maxSzélesség=null;
		public String kötöttTulajd=null;
		public Class kötöttTulajdOsztály=String.class;
		public DinamikusOszlopLeiro dinamikusOszlopleíró=null;
		public Integer rendezésiSorszám=null;
		public int rendezésiIrány=0;
	}
	
	
	public class Tabla extends JTable {
		public TablaModell táblaModell;
		
		public void setModell(TablaModell táblaModell) {
			táblaRendezõ=new TableSorter(táblaModell);
			setModel(táblaRendezõ);
			táblaRendezõ.setTableHeader(getTableHeader());
			init_Oszlopok();
			init_Tooltip();
			setRendezettség();
		}
		
		
		
		private void init_Tooltip() {
			// Tooltip beállítása
			getTableHeader().setToolTipText(
					"<HTML>" +
					"<UL>" +
					"<li>Klikk a rendezéshez</li>"+
					"<li>Shift+Klikk a fordított rendezéshez</li>"+
					"<li>CTRL+'Klikk másik oszlopon' a további alrendezésekhez</li>"+
					"</UL>" +
					"</HTML>"
					);
		}
		
		public void init_Oszlopok() {
			Iterator itr=getLáthatóDinamikusOszlopleírók().iterator();
			int sorszám=0;
			while(itr.hasNext()) {
				DinamikusOszlopLeiro dinOszLeíró=(DinamikusOszlopLeiro)itr.next();
				TableColumn oszlop=getColumnModel().getColumn(sorszám);
				if (dinOszLeíró.oszlopLeíró.renderelõ!=null)
					oszlop.setCellRenderer(dinOszLeíró.oszlopLeíró.renderelõ);
				if (dinOszLeíró.szélesség!=null)
					oszlop.setPreferredWidth(
							dinOszLeíró.szélesség.intValue());
				//		if (oszlopLeíró.minSzélesség!=null)
				//		    oszlop.setMinWidth(
				//			oszlopLeíró.minSzélesség.intValue());
				//		if (oszlopLeíró.maxSzélesség!=null)
				//		    oszlop.setMaxWidth(
				//			oszlopLeíró.maxSzélesség.intValue());
				sorszám++;
			}
		}
	}
	
	
	protected class TablaModell extends AbstractTableModel {
		
		public Object getValueAt(int rowIndex, int cIndex) {
			Object adat=adatok.get(rowIndex);
			DinamikusOszlopLeiro dinLeíró=(DinamikusOszlopLeiro)
			getLáthatóDinamikusOszlopleírók().get(cIndex);
			if (dinLeíró.oszlopLeíró.kötöttTulajd!=null) {
				Method lekérdezõMetódus;
				try {
					if (dinLeíró.oszlopLeíró.kötöttTulajdOsztály.equals(Boolean.class)) {
						lekérdezõMetódus=adat.getClass().getMethod(
								"is"+dinLeíró.oszlopLeíró.kötöttTulajd, new Class[0]);
					} else {
						lekérdezõMetódus=adat.getClass().getMethod(
								"get"+dinLeíró.oszlopLeíró.kötöttTulajd, new Class[0]);
					}
					Object érték=lekérdezõMetódus.invoke(adat, new Object[0]);
					return érték;
				} catch (Throwable kiv) {
					kiv.printStackTrace();
					return null;
				}
			}
			return "???";
		}
		
		public int getColumnCount() {
			return getLáthatóDinamikusOszlopleírók().size();
		}
		
		public int getRowCount() {
			return adatok.size();
		}
		
		public String getColumnName(int cIndex) {
			DinamikusOszlopLeiro dinLeíró=(DinamikusOszlopLeiro)
			getLáthatóDinamikusOszlopleírók().get(cIndex);
			return dinLeíró.oszlopLeíró.név;
		}
		
		public Class getColumnClass(int cIndex) {
			DinamikusOszlopLeiro dinLeíró=(DinamikusOszlopLeiro)
			getLáthatóDinamikusOszlopleírók().get(cIndex);
			return dinLeíró.oszlopLeíró.osztály;
		}
	}
	
	public static class TablaOszlop extends TableColumn {
		public OszlopLeiro oszlopLeíró=null;
	}
}




