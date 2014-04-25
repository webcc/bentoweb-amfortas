package org.bentoweb.amfortas.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.BASE64Encoder;

public final class EncryptionService
{
	private static Log log = LogFactory.getLog(EncryptionService.class);
	private static EncryptionService instance;

	private EncryptionService()
	{
	}

	public synchronized String encrypt(String plaintext)
	{
		MessageDigest md = null;
		try
		{
			md = MessageDigest.getInstance("SHA"); 
		}
		catch(NoSuchAlgorithmException ex)
		{
			log.debug(ex.getMessage());
		}
		try
		{
			md.update(plaintext.getBytes("UTF-8")); 
		}
		catch(UnsupportedEncodingException ex)
		{
			log.debug(ex.getMessage());
		}

		byte raw[] = md.digest(); 
	    String hash = (new BASE64Encoder()).encode(raw);
	    //BASE64 uses  A-Z, a-z, 0-9, +, /, = 
	    //'+' in URL breaks the process
	    hash = hash.replace("+","A"); //
		return hash;
	}
  
	public static synchronized EncryptionService getInstance() 
	{
		if(instance == null)
		{
			instance = new EncryptionService(); 
		} 
		return instance;
  }
}