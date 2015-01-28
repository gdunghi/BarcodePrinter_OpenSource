/*
 * Konfiguracio.java
 *
 * Created on 2005. január 6., 18:33
 */

package hu.wfs.vonalkod.konfiguracio;

import java.io.*;
import java.util.*;

/**
 * Program konfigurációs beállításainak kezelõje.
 * @author Rendszergazda
 */
public class Konfiguracio {
	
	private static final String KONFIG_KÖNYVTÁR_A_USER_LIBBEN="/.wfsVonalkod";
	private static final String KONFIG_PROPS_ALAP_FAJL="/hu/wfs/vonalkod/konfiguracio/konfiguracio.properties";	
	
	/**
	 * A konfigurációs fájlok helye. Felhasználónként eltérõ
	 */
	private static File libKonfig;
	
	private static File fileKonfigUser;
	private static Properties propsKonfigUser;
	private static Properties propsKonfigAlap;
	
	private static boolean isInicializálva=false;
	
	public static Properties getTulajdonságok() {
		return propsKonfigUser;			
	}
	
	public static File getKonfigLib() {
		return libKonfig;
	}
	
	
	/**
	 * Bármelyik metódus hívását meg kell elõznie az inicilaizál metódusnak.
	 * Azért nem kerül ez automatikusan(egyszer) végrehajtásra, mivel kivételt
	 * is dobhat és a felhasználó kódban célszerû, ha csak egyszer kell 
	 * a "fogadására" felkészülni egy trí blokkal.
	 * @throws java.lang.Throwable 
	 */
	public static void inicializálás() throws Throwable  {
		init_KonfigKönyvtár();
		init_KonfigurációAlap();
		
		String alapKonfigVerzió=propsKonfigAlap.getProperty("verzio");
		if (alapKonfigVerzió==null) throw new Exception("Hiányzó alap verzió!");
		
		init_KonfigurációUser();
		
		String userKonfigVerzió=propsKonfigUser.getProperty("verzio");
		if (
			userKonfigVerzió==null ||
			!alapKonfigVerzió.equals(userKonfigVerzió)
		) konfigKörnyzetAlaphelyzetbe();
		isInicializálva=true;
	}
	
	private static void konfigKörnyzetAlaphelyzetbe() throws Throwable {
		File[] konfigFájlok=libKonfig.listFiles();
		for(int i=0;i<konfigFájlok.length;i++) {
			File fájl=konfigFájlok[i];
			fájl.delete();
		}
		propsKonfigUser=propsKonfigAlap;
		mentés();
	}
	
	public static void mentés() throws Throwable {
		OutputStream out=new FileOutputStream(fileKonfigUser);
		propsKonfigUser.store(out, "-- Nincsen infó --");
		out.flush();
		out.close();
	}
	
	private static void init_KonfigKönyvtár() throws Throwable {
		libKonfig=new File(System.getProperty("user.home")+KONFIG_KÖNYVTÁR_A_USER_LIBBEN);
		if (!libKonfig.exists())
			libKonfig.mkdirs();
	}
	
	private static void  init_KonfigurációAlap() throws Throwable {
		propsKonfigAlap=new Properties();
		propsKonfigAlap.load(
				new Konfiguracio().getClass()
				.getResourceAsStream(KONFIG_PROPS_ALAP_FAJL));
	}
	
	/**
	 * A konfigurációs könyvtárban lévõ egyik fájl alapján adja vissza az InpuStreamet.
	 * @param fájlnév A konfig. könyvtárban lévõ fájl neve.
	 */
	public static InputStream getInputStreamFromKonfigLib(String fájlnév) throws Throwable {
		File fájl=new File(libKonfig,fájlnév);
		if (!fájl.exists())
			return null;
		return new FileInputStream(fájl);
	}
	
	public static OutputStream getOutputStreamToKonfigLib(String fájlnév) throws Throwable {
		File fájl=new File(libKonfig,fájlnév);
		if (!fájl.exists())
			fájl.createNewFile();
		return new FileOutputStream(fájl);
	}
	
	private static void init_KonfigurációUser() throws Throwable {
		fileKonfigUser=new File(libKonfig,"konfiguracio.properties");
		if (!fileKonfigUser.exists())
			fileKonfigUser.createNewFile();
		propsKonfigUser=new Properties();
		InputStream is=new FileInputStream(fileKonfigUser);
		propsKonfigUser.load(is);
		is.close();
	}
	
	public static Object konfigurációsObjektumBetöltés(String fájlnév) throws Throwable {
		File fájl=new File(libKonfig,fájlnév);
		if (!fájl.exists())
			return null;
		ObjectInputStream ois=new ObjectInputStream(new FileInputStream(fájl));
		Object objektum=ois.readObject();
		ois.close();
		return objektum;
	}
	
	public static void konfigurációsObjektumMentés(Serializable objektum, String fájlnév) throws Throwable {
		if (objektum==null||fájlnév==null)
			return;
		File fájl=new File(libKonfig,fájlnév);
		if (!fájl.exists())
			fájl.delete();
		ObjectOutputStream os=new ObjectOutputStream(new FileOutputStream(fájl));
		os.writeObject(objektum);
		os.flush();
		os.close();			
	}
	
	
	
}
