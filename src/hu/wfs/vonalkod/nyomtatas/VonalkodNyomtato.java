/*
 * VonalkokNyomtato.java
 *
 * Created on 2005. április 17., 18:53
 */

package hu.wfs.vonalkod.nyomtatas;

import hu.wfs.vonalkod.modell.Nyomtatando;
import hu.wfs.vonalkod.modell.Oldalbeallitas;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sourceforge.barbecue.Barcode;


/**
 * Figyelem: az oldalszám tag 0-tól indul!
 * @author Béci
 */
public class VonalkodNyomtato {
	private   final static boolean DEBUG=false;	
	Oldalbeallitas oldalbeállítás,ob;	
	Nyomtatando[] nyomtatandók;
	Barcode[] vonalkódLista;
	PrinterJob job;
	private int összoldalszám;
	
	public VonalkodNyomtato(
		Oldalbeallitas _oldalbeállítás,
		Nyomtatando[] _nyomtatandók,
		PrinterJob _job
	) {
		oldalbeállítás=ob=_oldalbeállítás;
		nyomtatandók=_nyomtatandók;
		job=_job;
		createBarcodelista();
		setÖsszoldalszám();
	}
	
	private void createBarcodelista() {
		List lista=new ArrayList();
		for (int i=0;i<nyomtatandók.length;i++) {
			Nyomtatando nyomt=nyomtatandók[i];
			for (int y=0;y<nyomt.getMennyiség();y++) {
				lista.add(nyomt.getVonalkód());
			}
		}
		vonalkódLista=(Barcode[])lista.toArray(new Barcode[lista.size()]);
	}
	
	/**
	 * 
	 * @param oldalszám Az oldalszám nulla alapú!!!
	 */
	private Barcode getVonalkód(
		int oldalszám, 
		int sor,			
		int oszlop
	) {
		List lista=Arrays.asList(vonalkódLista);
		int egyOldalonElemekSzáma=
			ob.cimkeOszlopokSzáma*ob.cimkeSorokSzáma;
		int index=
			egyOldalonElemekSzáma*oldalszám+
			((sor-1)*ob.cimkeOszlopokSzáma+oszlop)-1;
		if (lista.size()<=index)
			return null;
		return (Barcode)lista.get(index);
	}
	
	private void setÖsszoldalszám() {
		double egyOldalonElemekSzáma=
			ob.cimkeOszlopokSzáma*ob.cimkeSorokSzáma;
		double összvonalkódszám=vonalkódLista.length;
		if (egyOldalonElemekSzáma==0) {
			összoldalszám=0;
			return;
		}
		összoldalszám=(int)Math.ceil(összvonalkódszám/egyOldalonElemekSzáma);
	}
	
	
	public synchronized void nyomtatásKezdése() throws Throwable {
		if (összoldalszám<=0) return;
		PageFormat page=job.defaultPage();
		Paper papír=new Paper();
		papír.setSize(pix(210), pix(297));
		papír.setImageableArea(pix(0),pix(0), pix(210),pix(297));
		page.setPaper(papír);
		
		
		job.setPrintable(new Printable() {
			// Egy oldal megrajzolása
			public int print(
				Graphics g, 
				PageFormat pageFormat, 
				int oldalszám
			) throws PrinterException {
				if (oldalszám>összoldalszám-1)
					return Printable.NO_SUCH_PAGE; 
				Graphics2D g2=(Graphics2D)g;
				
				if (DEBUG==true)
					hibakeresõPrint(g2, pageFormat, oldalszám);
				
				külsõ: for (int sor=1;sor<=ob.cimkeSorokSzáma;sor++) {
					for (int oszlop=1;oszlop<=ob.cimkeOszlopokSzáma;oszlop++) {
						Barcode vonalkód=getVonalkód(oldalszám, sor, oszlop);
						if (vonalkód==null)
							break külsõ;							
						Pont pont=getLocation(sor, oszlop);
						vonalkódKirajzolása(vonalkód, g2,pont);
						if (DEBUG)
							extraVonalakMegjelenítése(g2,pont);
					}
				}
				return Printable.PAGE_EXISTS;
			}
		},page);

		job.print();
	}
	
	public void hibakeresõPrint(
		Graphics2D g2, 
		PageFormat pageFormat, 
		int oldalszám
	) throws PrinterException {
		// Segédek felrajzolása
		System.out.println("Papír szélesség:"+pageFormat.getWidth());
		System.out.println("Papír magasság:"+pageFormat.getHeight());

		System.out.println("Rajzolható x.:"+(int) pageFormat.getImageableX());
		System.out.println("Rajzolható y.:"+(int) pageFormat.getImageableY());			

		System.out.println("Rajzolható sz.:"+(int) pageFormat.getImageableWidth());
		System.out.println("Rajzolható m.:"+(int) pageFormat.getImageableHeight());

		System.out.println("Oldalszám:"+oldalszám);

		g2.drawLine(150,0,150,100);
		g2.drawLine(200,20,200,100);
		g2.drawRect(
			(int) pageFormat.getImageableX(),
			(int) pageFormat.getImageableY(), 
			(int) pageFormat.getImageableWidth(),
			(int) pageFormat.getImageableHeight()
		);
	}
	
	
	private void vonalkódKirajzolása(Barcode vonalkód,Graphics2D g2, Pont pont) {
		double befoglalókeretSzélesség=oldalbeállítás.cimkeSzélességInPixel()-pix(3);
		double befoglalókeretMagasság=oldalbeállítás.cimkeMagasságInPixel()-pix(3);
		double belsõmargóJobb=(ob.cimkeSzélességInPixel()-befoglalókeretSzélesség)/2;
		double belsõmargóFelsõ=(ob.cimkeMagasságInPixel()-befoglalókeretMagasság)/2;
		double vonalkódX=pont.x+belsõmargóJobb;
		double vonalkódY=pont.y+belsõmargóFelsõ;
				
		VonalkodSeged.rajzolásVonalkód(
			g2, 
			vonalkód, 
			vonalkódX, 
			vonalkódY,
			befoglalókeretSzélesség,
			befoglalókeretMagasság);
	}
	
	private void extraVonalakMegjelenítése(
		Graphics2D g2, 
		Pont pont
	) {
		int x=(int)pont.x;
		int y=(int)pont.y;			
		String szöveg=x +":"+y;
		System.out.println(szöveg);
		g2.drawString(szöveg,x,y);
		Stroke vonal=g2.getStroke();
		g2.setStroke(new BasicStroke(0.5f,BasicStroke.CAP_ROUND,
			BasicStroke.JOIN_MITER,10.0f, new float[] {2.0f},0.0f));		
		g2.drawRect(
			x,
			y,
			(int)ob.cimkeSzélességInPixel(),
			(int)ob.cimkeMagasságInPixel());
		g2.setStroke(vonal);
	}
	
	private Pont getLocation(int sor,int oszlop) {
		double x=
			ob.margóBal+
			(ob.cimkeSzélesség+ob.cimkeVízszintesKöz)*(oszlop-1);
		double y=
			ob.margóFelsõ+
			(ob.cimkeMagasság+ob.cimkeFüggõlegesKöz)*(sor-1);
		return new Pont(
			getPixelByMM(x),
			getPixelByMM(y));
	}
	
	public static final int FELBONTÁS_DPI=72;
	
	public double getPixelByMM(double mm) {
		return mm/10d/2.54d*(double)FELBONTÁS_DPI;
	} 
		
	public double pix(double mm) {
		return getPixelByMM(mm);
	} 
	
	public int pixAsInt(double mm) {
		return Math.round((float)(getPixelByMM(mm)));
	} 
	
	private static class Pont {
		/**
		 * értéke mértékegysége: pixel
		 */
		double x;
		/**
		 * értéke mértékegysége: pixel
		 */
		double y;
		public Pont(double _x,double _y) {
			x=_x;
			y=_y;
		}
	}
}
