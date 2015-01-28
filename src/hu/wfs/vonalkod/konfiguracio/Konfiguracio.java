/*
 * Konfiguracio.java
 *
 * Created on 2005. janu�r 6., 18:33
 */

package hu.wfs.vonalkod.konfiguracio;

import java.io.*;
import java.util.*;

/**
 * Program konfigur�ci�s be�ll�t�sainak kezel�je.
 * @author Rendszergazda
 */
public class Konfiguracio {
	
	private static final String KONFIG_K�NYVT�R_A_USER_LIBBEN="/.wfsVonalkod";
	private static final String KONFIG_PROPS_ALAP_FAJL="/hu/wfs/vonalkod/konfiguracio/konfiguracio.properties";	
	
	/**
	 * A konfigur�ci�s f�jlok helye. Felhaszn�l�nk�nt elt�r�
	 */
	private static File libKonfig;
	
	private static File fileKonfigUser;
	private static Properties propsKonfigUser;
	private static Properties propsKonfigAlap;
	
	private static boolean isInicializ�lva=false;
	
	public static Properties getTulajdons�gok() {
		return propsKonfigUser;			
	}
	
	public static File getKonfigLib() {
		return libKonfig;
	}
	
	
	/**
	 * B�rmelyik met�dus h�v�s�t meg kell el�znie az inicilaiz�l met�dusnak.
	 * Az�rt nem ker�l ez automatikusan(egyszer) v�grehajt�sra, mivel kiv�telt
	 * is dobhat �s a felhaszn�l� k�dban c�lszer�, ha csak egyszer kell 
	 * a "fogad�s�ra" felk�sz�lni egy tr� blokkal.
	 * @throws java.lang.Throwable 
	 */
	public static void inicializ�l�s() throws Throwable  {
		init_KonfigK�nyvt�r();
		init_Konfigur�ci�Alap();
		
		String alapKonfigVerzi�=propsKonfigAlap.getProperty("verzio");
		if (alapKonfigVerzi�==null) throw new Exception("Hi�nyz� alap verzi�!");
		
		init_Konfigur�ci�User();
		
		String userKonfigVerzi�=propsKonfigUser.getProperty("verzio");
		if (
			userKonfigVerzi�==null ||
			!alapKonfigVerzi�.equals(userKonfigVerzi�)
		) konfigK�rnyzetAlaphelyzetbe();
		isInicializ�lva=true;
	}
	
	private static void konfigK�rnyzetAlaphelyzetbe() throws Throwable {
		File[] konfigF�jlok=libKonfig.listFiles();
		for(int i=0;i<konfigF�jlok.length;i++) {
			File f�jl=konfigF�jlok[i];
			f�jl.delete();
		}
		propsKonfigUser=propsKonfigAlap;
		ment�s();
	}
	
	public static void ment�s() throws Throwable {
		OutputStream out=new FileOutputStream(fileKonfigUser);
		propsKonfigUser.store(out, "-- Nincsen inf� --");
		out.flush();
		out.close();
	}
	
	private static void init_KonfigK�nyvt�r() throws Throwable {
		libKonfig=new File(System.getProperty("user.home")+KONFIG_K�NYVT�R_A_USER_LIBBEN);
		if (!libKonfig.exists())
			libKonfig.mkdirs();
	}
	
	private static void  init_Konfigur�ci�Alap() throws Throwable {
		propsKonfigAlap=new Properties();
		propsKonfigAlap.load(
				new Konfiguracio().getClass()
				.getResourceAsStream(KONFIG_PROPS_ALAP_FAJL));
	}
	
	/**
	 * A konfigur�ci�s k�nyvt�rban l�v� egyik f�jl alapj�n adja vissza az InpuStreamet.
	 * @param f�jln�v A konfig. k�nyvt�rban l�v� f�jl neve.
	 */
	public static InputStream getInputStreamFromKonfigLib(String f�jln�v) throws Throwable {
		File f�jl=new File(libKonfig,f�jln�v);
		if (!f�jl.exists())
			return null;
		return new FileInputStream(f�jl);
	}
	
	public static OutputStream getOutputStreamToKonfigLib(String f�jln�v) throws Throwable {
		File f�jl=new File(libKonfig,f�jln�v);
		if (!f�jl.exists())
			f�jl.createNewFile();
		return new FileOutputStream(f�jl);
	}
	
	private static void init_Konfigur�ci�User() throws Throwable {
		fileKonfigUser=new File(libKonfig,"konfiguracio.properties");
		if (!fileKonfigUser.exists())
			fileKonfigUser.createNewFile();
		propsKonfigUser=new Properties();
		InputStream is=new FileInputStream(fileKonfigUser);
		propsKonfigUser.load(is);
		is.close();
	}
	
	public static Object konfigur�ci�sObjektumBet�lt�s(String f�jln�v) throws Throwable {
		File f�jl=new File(libKonfig,f�jln�v);
		if (!f�jl.exists())
			return null;
		ObjectInputStream ois=new ObjectInputStream(new FileInputStream(f�jl));
		Object objektum=ois.readObject();
		ois.close();
		return objektum;
	}
	
	public static void konfigur�ci�sObjektumMent�s(Serializable objektum, String f�jln�v) throws Throwable {
		if (objektum==null||f�jln�v==null)
			return;
		File f�jl=new File(libKonfig,f�jln�v);
		if (!f�jl.exists())
			f�jl.delete();
		ObjectOutputStream os=new ObjectOutputStream(new FileOutputStream(f�jl));
		os.writeObject(objektum);
		os.flush();
		os.close();			
	}
	
	
	
}
