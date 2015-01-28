/*
 * VonalkokNyomtato.java
 *
 * Created on 2005. �prilis 17., 18:53
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
 * Figyelem: az oldalsz�m tag 0-t�l indul!
 * @author B�ci
 */
public class VonalkodNyomtato {
	private   final static boolean DEBUG=false;	
	Oldalbeallitas oldalbe�ll�t�s,ob;	
	Nyomtatando[] nyomtatand�k;
	Barcode[] vonalk�dLista;
	PrinterJob job;
	private int �sszoldalsz�m;
	
	public VonalkodNyomtato(
		Oldalbeallitas _oldalbe�ll�t�s,
		Nyomtatando[] _nyomtatand�k,
		PrinterJob _job
	) {
		oldalbe�ll�t�s=ob=_oldalbe�ll�t�s;
		nyomtatand�k=_nyomtatand�k;
		job=_job;
		createBarcodelista();
		set�sszoldalsz�m();
	}
	
	private void createBarcodelista() {
		List lista=new ArrayList();
		for (int i=0;i<nyomtatand�k.length;i++) {
			Nyomtatando nyomt=nyomtatand�k[i];
			for (int y=0;y<nyomt.getMennyis�g();y++) {
				lista.add(nyomt.getVonalk�d());
			}
		}
		vonalk�dLista=(Barcode[])lista.toArray(new Barcode[lista.size()]);
	}
	
	/**
	 * 
	 * @param oldalsz�m Az oldalsz�m nulla alap�!!!
	 */
	private Barcode getVonalk�d(
		int oldalsz�m, 
		int sor,			
		int oszlop
	) {
		List lista=Arrays.asList(vonalk�dLista);
		int egyOldalonElemekSz�ma=
			ob.cimkeOszlopokSz�ma*ob.cimkeSorokSz�ma;
		int index=
			egyOldalonElemekSz�ma*oldalsz�m+
			((sor-1)*ob.cimkeOszlopokSz�ma+oszlop)-1;
		if (lista.size()<=index)
			return null;
		return (Barcode)lista.get(index);
	}
	
	private void set�sszoldalsz�m() {
		double egyOldalonElemekSz�ma=
			ob.cimkeOszlopokSz�ma*ob.cimkeSorokSz�ma;
		double �sszvonalk�dsz�m=vonalk�dLista.length;
		if (egyOldalonElemekSz�ma==0) {
			�sszoldalsz�m=0;
			return;
		}
		�sszoldalsz�m=(int)Math.ceil(�sszvonalk�dsz�m/egyOldalonElemekSz�ma);
	}
	
	
	public synchronized void nyomtat�sKezd�se() throws Throwable {
		if (�sszoldalsz�m<=0) return;
		PageFormat page=job.defaultPage();
		Paper pap�r=new Paper();
		pap�r.setSize(pix(210), pix(297));
		pap�r.setImageableArea(pix(0),pix(0), pix(210),pix(297));
		page.setPaper(pap�r);
		
		
		job.setPrintable(new Printable() {
			// Egy oldal megrajzol�sa
			public int print(
				Graphics g, 
				PageFormat pageFormat, 
				int oldalsz�m
			) throws PrinterException {
				if (oldalsz�m>�sszoldalsz�m-1)
					return Printable.NO_SUCH_PAGE; 
				Graphics2D g2=(Graphics2D)g;
				
				if (DEBUG==true)
					hibakeres�Print(g2, pageFormat, oldalsz�m);
				
				k�ls�: for (int sor=1;sor<=ob.cimkeSorokSz�ma;sor++) {
					for (int oszlop=1;oszlop<=ob.cimkeOszlopokSz�ma;oszlop++) {
						Barcode vonalk�d=getVonalk�d(oldalsz�m, sor, oszlop);
						if (vonalk�d==null)
							break k�ls�;							
						Pont pont=getLocation(sor, oszlop);
						vonalk�dKirajzol�sa(vonalk�d, g2,pont);
						if (DEBUG)
							extraVonalakMegjelen�t�se(g2,pont);
					}
				}
				return Printable.PAGE_EXISTS;
			}
		},page);

		job.print();
	}
	
	public void hibakeres�Print(
		Graphics2D g2, 
		PageFormat pageFormat, 
		int oldalsz�m
	) throws PrinterException {
		// Seg�dek felrajzol�sa
		System.out.println("Pap�r sz�less�g:"+pageFormat.getWidth());
		System.out.println("Pap�r magass�g:"+pageFormat.getHeight());

		System.out.println("Rajzolhat� x.:"+(int) pageFormat.getImageableX());
		System.out.println("Rajzolhat� y.:"+(int) pageFormat.getImageableY());			

		System.out.println("Rajzolhat� sz.:"+(int) pageFormat.getImageableWidth());
		System.out.println("Rajzolhat� m.:"+(int) pageFormat.getImageableHeight());

		System.out.println("Oldalsz�m:"+oldalsz�m);

		g2.drawLine(150,0,150,100);
		g2.drawLine(200,20,200,100);
		g2.drawRect(
			(int) pageFormat.getImageableX(),
			(int) pageFormat.getImageableY(), 
			(int) pageFormat.getImageableWidth(),
			(int) pageFormat.getImageableHeight()
		);
	}
	
	
	private void vonalk�dKirajzol�sa(Barcode vonalk�d,Graphics2D g2, Pont pont) {
		double befoglal�keretSz�less�g=oldalbe�ll�t�s.cimkeSz�less�gInPixel()-pix(3);
		double befoglal�keretMagass�g=oldalbe�ll�t�s.cimkeMagass�gInPixel()-pix(3);
		double bels�marg�Jobb=(ob.cimkeSz�less�gInPixel()-befoglal�keretSz�less�g)/2;
		double bels�marg�Fels�=(ob.cimkeMagass�gInPixel()-befoglal�keretMagass�g)/2;
		double vonalk�dX=pont.x+bels�marg�Jobb;
		double vonalk�dY=pont.y+bels�marg�Fels�;
				
		VonalkodSeged.rajzol�sVonalk�d(
			g2, 
			vonalk�d, 
			vonalk�dX, 
			vonalk�dY,
			befoglal�keretSz�less�g,
			befoglal�keretMagass�g);
	}
	
	private void extraVonalakMegjelen�t�se(
		Graphics2D g2, 
		Pont pont
	) {
		int x=(int)pont.x;
		int y=(int)pont.y;			
		String sz�veg=x +":"+y;
		System.out.println(sz�veg);
		g2.drawString(sz�veg,x,y);
		Stroke vonal=g2.getStroke();
		g2.setStroke(new BasicStroke(0.5f,BasicStroke.CAP_ROUND,
			BasicStroke.JOIN_MITER,10.0f, new float[] {2.0f},0.0f));		
		g2.drawRect(
			x,
			y,
			(int)ob.cimkeSz�less�gInPixel(),
			(int)ob.cimkeMagass�gInPixel());
		g2.setStroke(vonal);
	}
	
	private Pont getLocation(int sor,int oszlop) {
		double x=
			ob.marg�Bal+
			(ob.cimkeSz�less�g+ob.cimkeV�zszintesK�z)*(oszlop-1);
		double y=
			ob.marg�Fels�+
			(ob.cimkeMagass�g+ob.cimkeF�gg�legesK�z)*(sor-1);
		return new Pont(
			getPixelByMM(x),
			getPixelByMM(y));
	}
	
	public static final int FELBONT�S_DPI=72;
	
	public double getPixelByMM(double mm) {
		return mm/10d/2.54d*(double)FELBONT�S_DPI;
	} 
		
	public double pix(double mm) {
		return getPixelByMM(mm);
	} 
	
	public int pixAsInt(double mm) {
		return Math.round((float)(getPixelByMM(mm)));
	} 
	
	private static class Pont {
		/**
		 * �rt�ke m�rt�kegys�ge: pixel
		 */
		double x;
		/**
		 * �rt�ke m�rt�kegys�ge: pixel
		 */
		double y;
		public Pont(double _x,double _y) {
			x=_x;
			y=_y;
		}
	}
}
