package org.gcube.common.utils.encryption;

import javax.crypto.Cipher;

import org.apache.xml.security.utils.Base64;
import java.security.Key;

/**
 * Encrypter for {@link String} objects
 * @author Manuele Simi (CNR)
 *
 */
public class StringEncrypter implements Encrypter<String>{

	private static StringEncrypter singleton;

	StringEncrypter() {}
	
	/**
	 * Gets the Encrypter for {@link String}
	 * @return the encrypter
	 */
	public static StringEncrypter getEncrypter(){
		if (singleton == null) singleton = new StringEncrypter();
		return singleton;
	}
	
	/**
	 * Encrypts the string with the given key
	 * @param key the key for encrypting
	 * @return the encrypted string in a Base64 encoding
	 * @throws Exception
	 */
	public String encrypt(String string, Key ... key) throws Exception {
		Key ekey = (key!=null && key.length>0)? key[0] : SymmetricKey.getKey();
	    Cipher cipher = Cipher.getInstance(ekey.getAlgorithm());
	    cipher.init(Cipher.ENCRYPT_MODE, ekey);
	    return new String(Base64.encode(cipher.doFinal(string.getBytes())));
	}
	
	/**
	 * Decrypts the string with the given key
	 * @param key the key to use for decrypting
	 * @return the decripted string
	 * @throws Exception
	 */
	public String decrypt(String string, Key ... key) throws Exception {
		Key dkey = (key!=null && key.length>0)? key[0] : SymmetricKey.getKey();
	    Cipher cipher = Cipher.getInstance(dkey.getAlgorithm());
	    cipher.init(Cipher.DECRYPT_MODE, dkey);
	    return new String(cipher.doFinal(Base64.decode(string.getBytes())));
	}

}
