/*
 * ByteArrayBuffer.java
 *
 * Created on 2004. december 6., 19:12
 */

package hu.wfs.vonalkod.shared;

/**
 *
 * @author  Rendszergazda
 */
public class ByteArrayBuffer {
    private byte[] bájtok=new byte[0];
        
    public void append(byte[] tömb) {
	bájtok=byteArrayÖsszefûzés(bájtok,tömb);
    }
    
    public byte[] getBytes() {
	return bájtok;
    }
    
    public void append(byte[] tömb, int start, int hossz) {    
	byte[] tmp=new byte[hossz];
	System.arraycopy(tömb, start, tmp, 0, hossz);
	bájtok=byteArrayÖsszefûzés(bájtok,tmp);
    }

    private static byte[] byteArrayÖsszefûzés(byte[] a,byte[] b) {
	byte[] tmp=new byte[a.length+b.length];
	System.arraycopy(a, 0, tmp, 0, a.length);
	System.arraycopy(b, 0, tmp, a.length, b.length);
	return tmp;
    }    
}
