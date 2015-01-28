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
    private byte[] b�jtok=new byte[0];
        
    public void append(byte[] t�mb) {
	b�jtok=byteArray�sszef�z�s(b�jtok,t�mb);
    }
    
    public byte[] getBytes() {
	return b�jtok;
    }
    
    public void append(byte[] t�mb, int start, int hossz) {    
	byte[] tmp=new byte[hossz];
	System.arraycopy(t�mb, start, tmp, 0, hossz);
	b�jtok=byteArray�sszef�z�s(b�jtok,tmp);
    }

    private static byte[] byteArray�sszef�z�s(byte[] a,byte[] b) {
	byte[] tmp=new byte[a.length+b.length];
	System.arraycopy(a, 0, tmp, 0, a.length);
	System.arraycopy(b, 0, tmp, a.length, b.length);
	return tmp;
    }    
}
