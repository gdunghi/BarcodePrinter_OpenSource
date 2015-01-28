/*
 * VonalkodSeged.java
 *
 * Created on 2005. április 17., 19:12
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
 * @author Béci
 */
public class VonalkodSeged {
	
	//private static String SZÖVEG_A_HIRDETÉS_UTÁN="for MGE UPS SYS HUN";
	private static String SZÖVEG_A_HIRDETÉS_UTÁN="";
	//private static String SZÖVEG_A_HIRDETÉS_UTÁN=" - DEMÓ VERZIÓ";
	
	public static String ELSÕDLEGES_SZABVÁNY="Code128";
	
	public static Barcode createBarcodeByNév(String név,String karakterlánc) throws Throwable {
		Barcode vonalkód=null;
		if (név.equalsIgnoreCase("2of7"))
			vonalkód=BarcodeFactory.create2of7(karakterlánc);
		if (név.equalsIgnoreCase("3of9"))
			vonalkód=BarcodeFactory.create3of9(karakterlánc,false);
		if (név.equalsIgnoreCase("3of9 ellenõrzõ összeggel"))
			vonalkód=BarcodeFactory.create3of9(karakterlánc,true);		
		if (név.equalsIgnoreCase("Codabar"))
			vonalkód=BarcodeFactory.createCodabar(karakterlánc);
		if (név.equalsIgnoreCase(ELSÕDLEGES_SZABVÁNY))
			vonalkód= BarcodeFactory.createCode128(karakterlánc);
		if (név.equalsIgnoreCase("Code128A"))
			vonalkód= BarcodeFactory.createCode128A(karakterlánc);
		if (név.equalsIgnoreCase("Code128B"))
			vonalkód= BarcodeFactory.createCode128B(karakterlánc);
		if (név.equalsIgnoreCase("Code128C"))
			vonalkód= BarcodeFactory.createCode128C(karakterlánc);
		if (név.equalsIgnoreCase("Code39"))
			vonalkód= BarcodeFactory.createCode39(karakterlánc,false);		
		if (név.equalsIgnoreCase("Code39 ellenõrzõ összeggel"))
			vonalkód= BarcodeFactory.createCode39(karakterlánc,true);
		if (név.equalsIgnoreCase("EAN128"))
			vonalkód= BarcodeFactory.createEAN128(karakterlánc);
		if (név.equalsIgnoreCase("GlobalTradeItemNumber"))
			vonalkód= BarcodeFactory.createGlobalTradeItemNumber(karakterlánc);
		if (név.equalsIgnoreCase("Monarch"))
			vonalkód= BarcodeFactory.createMonarch(karakterlánc);
		if (név.equalsIgnoreCase("NW7"))
			vonalkód= BarcodeFactory.createNW7(karakterlánc);
		if (név.equalsIgnoreCase("PDF417"))
			vonalkód= BarcodeFactory.createPDF417(karakterlánc);
		if (név.equalsIgnoreCase("SCC14ShippingCode"))
			vonalkód= BarcodeFactory.createSCC14ShippingCode(karakterlánc);
		if (név.equalsIgnoreCase("SSCC18"))
			vonalkód= BarcodeFactory.createSSCC18(karakterlánc);
		if (név.equalsIgnoreCase("ShipmentIdentificationNumber"))
			vonalkód= BarcodeFactory.createShipmentIdentificationNumber(karakterlánc);
		if (név.equalsIgnoreCase("USD3"))
			vonalkód= BarcodeFactory.createUSD3(karakterlánc,false);
		if (név.equalsIgnoreCase("USD3 ellenõrzõ összeggel"))
			vonalkód= BarcodeFactory.createUSD3(karakterlánc,true);		
		if (név.equalsIgnoreCase("USD4"))
			vonalkód= BarcodeFactory.createUSD4(karakterlánc);		
		if (név.equalsIgnoreCase("USPS"))
			vonalkód= BarcodeFactory.createUSPS(karakterlánc);	
		vonalkód.getWidth();
		return vonalkód;
	}
	
	public static String[] getTámogatottSzabványok() {
		return new String[] {
			"2of7",
			"3of9",
			"3of9 ellenõrzõ összeggel",
			"Codabar",
			"Code128",
			"Code128A",
			"Code128B",
			"Code128C",
			"Code39",					
			"Code39 ellenõrzõ összeggel",
			"EAN128",
			"GlobalTradeItemNumber",
			"Monarch",
			"NW7",
			"PDF417",
			"SCC14ShippingCode",
			"SSCC18",
			"ShipmentIdentificationNumber",
			"USD3",
			"USD3 ellenõrzõ összeggel",
			"USD4",
			"USPS"
		};
	}

	private static boolean DEBUG=false;

	public static void rajzolásVonalkód(
		Graphics2D g2,
		Barcode vonalkód, 
		double x, 
		double y,
		double szélesség,
		double magasság
	) {
		vonalkód.setDrawingText(false);
		vonalkód.setBarHeight(magasság*2d);
		AffineTransform at=g2.getTransform();
		double arányX=1;
		double arányY=1;		
		if (vonalkód.getWidth()>szélesség)
			arányX=szélesség/(double)vonalkód.getWidth();
		arányY=(magasság-5)/(double)vonalkód.getHeight();		
		g2.scale(arányX, arányY);
		int vonalkódPozX=(int)(Math.round(x/arányX));
		if (arányX==1)
			vonalkódPozX=(int)(Math.round(x+(szélesség-vonalkód.getWidth())/2));
		int vonalkódPozY=(int)(Math.round(y/arányY));
		vonalkód.draw(g2, vonalkódPozX, vonalkódPozY+1);
		g2.setTransform(at);
		
		/*************************************
		 * Hidetés megjelenítése
		 *************************************/		
		
		// Vonalkód adat megjelenítése 
		String hirdetés="http://vonalkod.webfocus.hu "+SZÖVEG_A_HIRDETÉS_UTÁN;		
		double keret=1;
		Font font=new Font("Arial",Font.PLAIN,8);
		FontRenderContext frc=g2.getFontRenderContext();
		TextLayout szöveg=new TextLayout(hirdetés,font,frc);
		GeneralPath szövegPath=
			(GeneralPath)szöveg.getOutline(new AffineTransform());
		
		// finom élek bekapcsolása
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// esetleges átméretezés
		if (szövegPath.getBounds2D().getWidth()+keret*2>szélesség) {
			AffineTransform af=new AffineTransform();
			af.scale(szélesség/(szövegPath.getBounds2D().getWidth()+keret*2), 1);
			szövegPath.transform(af);
		}
		
		// Szöveg poziciójának meghatározása
		AffineTransform aff_SzövPathPoz=new AffineTransform();
		aff_SzövPathPoz.translate(
			x+(szélesség-szövegPath.getBounds2D().getWidth())/2-szövegPath.getBounds2D().getMinX(), 
			y-szövegPath.getBounds2D().getMinY()+keret);
		szövegPath.transform(aff_SzövPathPoz);
		
		// Háttér kirajzolása
		g2.setColor(Color.WHITE);
		double befoglalóSzélessége=szövegPath.getBounds2D().getWidth()+keret*2;
		g2.fillRect(
				Math.round((float)(x+(szélesség-befoglalóSzélessége)/2)),
				Math.round((float)(y)),
				Math.round((float)(befoglalóSzélessége)),
				Math.round((float)(szövegPath.getBounds2D().getHeight()+keret*2)));
		
		
		g2.setStroke(new BasicStroke(.2f));
		
		// 
		g2.setColor(Color.BLACK);
		g2.fill(szövegPath);
		g2.drawRect(
				Math.round((float)((x+(szélesség-befoglalóSzélessége)/2))),
				Math.round((float)(y)),
				Math.round((float)(szövegPath.getBounds2D().getWidth()+keret*2)),
				Math.round((float)(szövegPath.getBounds2D().getHeight()+keret*2)));		

		if (DEBUG) {
			Stroke stroke=g2.getStroke();
			g2.setColor(Color.BLACK);
			g2.setStroke(new BasicStroke(.2f));
			g2.drawRect(
				Math.round((float)((x+(szélesség-befoglalóSzélessége)/2))),
				Math.round((float)(y)),
				Math.round((float)(szélesség)),
				Math.round((float)(magasság)));
			g2.setStroke(stroke);
		}		
		
		
		/*************************************
		 * Vonalkód adatának megjelenítése
		 *************************************/
		
		// Vonalkód adat megjelenítése 
		String kódoltAdat=vonalkód.getData();		
		keret=2;
		font=new Font("Arial",Font.PLAIN,11);
		frc=g2.getFontRenderContext();
		szöveg=new TextLayout(kódoltAdat,font,frc);
		szövegPath=
			(GeneralPath)szöveg.getOutline(new AffineTransform());
			
		// esetleges átméretezés
		if (szövegPath.getBounds2D().getWidth()+keret*2>szélesség) {
			AffineTransform af=new AffineTransform();
			af.scale(szélesség/(szövegPath.getBounds2D().getWidth()+keret*2), 1);
			szövegPath.transform(af);
		}
		
		// Szöveg poziciójának meghatározása
		aff_SzövPathPoz=new AffineTransform();
		aff_SzövPathPoz.translate(
			x+
			((szélesség-(szövegPath.getBounds2D().getWidth()))/2)-szövegPath.getBounds2D().getMinX(), 
			y+magasság-szövegPath.getBounds2D().getHeight()-szövegPath.getBounds2D().getMinY()-keret);
		szövegPath.transform(aff_SzövPathPoz);
		
		// Háttér kirajzolása
		g2.setColor(Color.WHITE);
		g2.fillRect(
				Math.round((float)(x+((szélesség-(szövegPath.getBounds2D().getWidth()+keret*2))/2))),
				Math.round((float)(y+magasság-(szövegPath.getBounds2D().getHeight()+keret*2d))),
				Math.round((float)(szövegPath.getBounds2D().getWidth()+keret*2)),
				Math.round((float)(szövegPath.getBounds2D().getHeight()+keret*2d)));
		
		
		g2.setStroke(new BasicStroke(.2f));
		
		// 
		g2.setColor(Color.BLACK);
		g2.fill(szövegPath);
	}

	
	
}
