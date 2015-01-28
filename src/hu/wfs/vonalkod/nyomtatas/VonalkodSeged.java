/*
 * VonalkodSeged.java
 *
 * Created on 2005. �prilis 17., 19:12
 */

package hu.wfs.vonalkod.nyomtatas;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;

/**
 *
 * @author B�ci
 */
public class VonalkodSeged {
	
	//private static String SZ�VEG_A_HIRDET�S_UT�N="for MGE UPS SYS HUN";
	private static String SZ�VEG_A_HIRDET�S_UT�N="";
	//private static String SZ�VEG_A_HIRDET�S_UT�N=" - DEM� VERZI�";
	
	public static String ELS�DLEGES_SZABV�NY="Code128";
	
	public static Barcode createBarcodeByN�v(String n�v,String karakterl�nc) throws Throwable {
		Barcode vonalk�d=null;
		if (n�v.equalsIgnoreCase("2of7"))
			vonalk�d=BarcodeFactory.create2of7(karakterl�nc);
		if (n�v.equalsIgnoreCase("3of9"))
			vonalk�d=BarcodeFactory.create3of9(karakterl�nc,false);
		if (n�v.equalsIgnoreCase("3of9 ellen�rz� �sszeggel"))
			vonalk�d=BarcodeFactory.create3of9(karakterl�nc,true);		
		if (n�v.equalsIgnoreCase("Codabar"))
			vonalk�d=BarcodeFactory.createCodabar(karakterl�nc);
		if (n�v.equalsIgnoreCase(ELS�DLEGES_SZABV�NY))
			vonalk�d= BarcodeFactory.createCode128(karakterl�nc);
		if (n�v.equalsIgnoreCase("Code128A"))
			vonalk�d= BarcodeFactory.createCode128A(karakterl�nc);
		if (n�v.equalsIgnoreCase("Code128B"))
			vonalk�d= BarcodeFactory.createCode128B(karakterl�nc);
		if (n�v.equalsIgnoreCase("Code128C"))
			vonalk�d= BarcodeFactory.createCode128C(karakterl�nc);
		if (n�v.equalsIgnoreCase("Code39"))
			vonalk�d= BarcodeFactory.createCode39(karakterl�nc,false);		
		if (n�v.equalsIgnoreCase("Code39 ellen�rz� �sszeggel"))
			vonalk�d= BarcodeFactory.createCode39(karakterl�nc,true);
		if (n�v.equalsIgnoreCase("EAN128"))
			vonalk�d= BarcodeFactory.createEAN128(karakterl�nc);
		if (n�v.equalsIgnoreCase("GlobalTradeItemNumber"))
			vonalk�d= BarcodeFactory.createGlobalTradeItemNumber(karakterl�nc);
		if (n�v.equalsIgnoreCase("Monarch"))
			vonalk�d= BarcodeFactory.createMonarch(karakterl�nc);
		if (n�v.equalsIgnoreCase("NW7"))
			vonalk�d= BarcodeFactory.createNW7(karakterl�nc);
		if (n�v.equalsIgnoreCase("PDF417"))
			vonalk�d= BarcodeFactory.createPDF417(karakterl�nc);
		if (n�v.equalsIgnoreCase("SCC14ShippingCode"))
			vonalk�d= BarcodeFactory.createSCC14ShippingCode(karakterl�nc);
		if (n�v.equalsIgnoreCase("SSCC18"))
			vonalk�d= BarcodeFactory.createSSCC18(karakterl�nc);
		if (n�v.equalsIgnoreCase("ShipmentIdentificationNumber"))
			vonalk�d= BarcodeFactory.createShipmentIdentificationNumber(karakterl�nc);
		if (n�v.equalsIgnoreCase("USD3"))
			vonalk�d= BarcodeFactory.createUSD3(karakterl�nc,false);
		if (n�v.equalsIgnoreCase("USD3 ellen�rz� �sszeggel"))
			vonalk�d= BarcodeFactory.createUSD3(karakterl�nc,true);		
		if (n�v.equalsIgnoreCase("USD4"))
			vonalk�d= BarcodeFactory.createUSD4(karakterl�nc);		
		if (n�v.equalsIgnoreCase("USPS"))
			vonalk�d= BarcodeFactory.createUSPS(karakterl�nc);	
		vonalk�d.getWidth();
		return vonalk�d;
	}
	
	public static String[] getT�mogatottSzabv�nyok() {
		return new String[] {
			"2of7",
			"3of9",
			"3of9 ellen�rz� �sszeggel",
			"Codabar",
			"Code128",
			"Code128A",
			"Code128B",
			"Code128C",
			"Code39",					
			"Code39 ellen�rz� �sszeggel",
			"EAN128",
			"GlobalTradeItemNumber",
			"Monarch",
			"NW7",
			"PDF417",
			"SCC14ShippingCode",
			"SSCC18",
			"ShipmentIdentificationNumber",
			"USD3",
			"USD3 ellen�rz� �sszeggel",
			"USD4",
			"USPS"
		};
	}

	private static boolean DEBUG=false;

	public static void rajzol�sVonalk�d(
		Graphics2D g2,
		Barcode vonalk�d, 
		double x, 
		double y,
		double sz�less�g,
		double magass�g
	) {
		vonalk�d.setDrawingText(false);
		vonalk�d.setBarHeight(magass�g*2d);
		AffineTransform at=g2.getTransform();
		double ar�nyX=1;
		double ar�nyY=1;		
		if (vonalk�d.getWidth()>sz�less�g)
			ar�nyX=sz�less�g/(double)vonalk�d.getWidth();
		ar�nyY=(magass�g-5)/(double)vonalk�d.getHeight();		
		g2.scale(ar�nyX, ar�nyY);
		int vonalk�dPozX=(int)(Math.round(x/ar�nyX));
		if (ar�nyX==1)
			vonalk�dPozX=(int)(Math.round(x+(sz�less�g-vonalk�d.getWidth())/2));
		int vonalk�dPozY=(int)(Math.round(y/ar�nyY));
		vonalk�d.draw(g2, vonalk�dPozX, vonalk�dPozY+1);
		g2.setTransform(at);
		
		/*************************************
		 * Hidet�s megjelen�t�se
		 *************************************/		
		
		// Vonalk�d adat megjelen�t�se 
		String hirdet�s="http://vonalkod.webfocus.hu "+SZ�VEG_A_HIRDET�S_UT�N;		
		double keret=1;
		Font font=new Font("Arial",Font.PLAIN,8);
		FontRenderContext frc=g2.getFontRenderContext();
		TextLayout sz�veg=new TextLayout(hirdet�s,font,frc);
		GeneralPath sz�vegPath=
			(GeneralPath)sz�veg.getOutline(new AffineTransform());
		
		// finom �lek bekapcsol�sa
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// esetleges �tm�retez�s
		if (sz�vegPath.getBounds2D().getWidth()+keret*2>sz�less�g) {
			AffineTransform af=new AffineTransform();
			af.scale(sz�less�g/(sz�vegPath.getBounds2D().getWidth()+keret*2), 1);
			sz�vegPath.transform(af);
		}
		
		// Sz�veg pozici�j�nak meghat�roz�sa
		AffineTransform aff_Sz�vPathPoz=new AffineTransform();
		aff_Sz�vPathPoz.translate(
			x+(sz�less�g-sz�vegPath.getBounds2D().getWidth())/2-sz�vegPath.getBounds2D().getMinX(), 
			y-sz�vegPath.getBounds2D().getMinY()+keret);
		sz�vegPath.transform(aff_Sz�vPathPoz);
		
		// H�tt�r kirajzol�sa
		g2.setColor(Color.WHITE);
		double befoglal�Sz�less�ge=sz�vegPath.getBounds2D().getWidth()+keret*2;
		g2.fillRect(
				Math.round((float)(x+(sz�less�g-befoglal�Sz�less�ge)/2)),
				Math.round((float)(y)),
				Math.round((float)(befoglal�Sz�less�ge)),
				Math.round((float)(sz�vegPath.getBounds2D().getHeight()+keret*2)));
		
		
		g2.setStroke(new BasicStroke(.2f));
		
		// 
		g2.setColor(Color.BLACK);
		g2.fill(sz�vegPath);
		g2.drawRect(
				Math.round((float)((x+(sz�less�g-befoglal�Sz�less�ge)/2))),
				Math.round((float)(y)),
				Math.round((float)(sz�vegPath.getBounds2D().getWidth()+keret*2)),
				Math.round((float)(sz�vegPath.getBounds2D().getHeight()+keret*2)));		

		if (DEBUG) {
			Stroke stroke=g2.getStroke();
			g2.setColor(Color.BLACK);
			g2.setStroke(new BasicStroke(.2f));
			g2.drawRect(
				Math.round((float)((x+(sz�less�g-befoglal�Sz�less�ge)/2))),
				Math.round((float)(y)),
				Math.round((float)(sz�less�g)),
				Math.round((float)(magass�g)));
			g2.setStroke(stroke);
		}		
		
		
		/*************************************
		 * Vonalk�d adat�nak megjelen�t�se
		 *************************************/
		
		// Vonalk�d adat megjelen�t�se 
		String k�doltAdat=vonalk�d.getData();		
		keret=2;
		font=new Font("Arial",Font.PLAIN,11);
		frc=g2.getFontRenderContext();
		sz�veg=new TextLayout(k�doltAdat,font,frc);
		sz�vegPath=
			(GeneralPath)sz�veg.getOutline(new AffineTransform());
			
		// esetleges �tm�retez�s
		if (sz�vegPath.getBounds2D().getWidth()+keret*2>sz�less�g) {
			AffineTransform af=new AffineTransform();
			af.scale(sz�less�g/(sz�vegPath.getBounds2D().getWidth()+keret*2), 1);
			sz�vegPath.transform(af);
		}
		
		// Sz�veg pozici�j�nak meghat�roz�sa
		aff_Sz�vPathPoz=new AffineTransform();
		aff_Sz�vPathPoz.translate(
			x+
			((sz�less�g-(sz�vegPath.getBounds2D().getWidth()))/2)-sz�vegPath.getBounds2D().getMinX(), 
			y+magass�g-sz�vegPath.getBounds2D().getHeight()-sz�vegPath.getBounds2D().getMinY()-keret);
		sz�vegPath.transform(aff_Sz�vPathPoz);
		
		// H�tt�r kirajzol�sa
		g2.setColor(Color.WHITE);
		g2.fillRect(
				Math.round((float)(x+((sz�less�g-(sz�vegPath.getBounds2D().getWidth()+keret*2))/2))),
				Math.round((float)(y+magass�g-(sz�vegPath.getBounds2D().getHeight()+keret*2d))),
				Math.round((float)(sz�vegPath.getBounds2D().getWidth()+keret*2)),
				Math.round((float)(sz�vegPath.getBounds2D().getHeight()+keret*2d)));
		
		
		g2.setStroke(new BasicStroke(.2f));
		
		// 
		g2.setColor(Color.BLACK);
		g2.fill(sz�vegPath);
	}

	
	
}
