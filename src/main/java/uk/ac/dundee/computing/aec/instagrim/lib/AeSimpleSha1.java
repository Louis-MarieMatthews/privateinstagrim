/*
 * The java code follows the Java Programming Style Guidelines 7.0 from 
 * Geotechnical Software Services available at this address:
 * http://geosoft.no/development/javastyle.html .
 * Some rules are still not applied yet.
 * However, some rules won't be followed:
 * 1. No underscore suffix at the end of private variables (r8)
 * 2. No space between a function and its parenthesis (r74). Instead, parenthesis
 * may be wrapped around space. So function ( parameter ) instead of 
 * function (parameter).
 * 4. Abbreviations and the use of init is okay (r17, r24)
 * 5. Statements and variable declarations don't need to be aligned (r77, r78)
 * 6. Class names don't have to be nouns (would make some class' names long and
 * poorly representative for servlets and filters).
 */
package uk.ac.dundee.computing.aec.instagrim.lib;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Andy Cobley, Louis-Marie Matthews
 * @version 1.0.1
 */
public class AeSimpleSha1
{
  private static String convertToHex(byte[] data)
  {
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < data.length; i++) {
      int halfbyte = (data[i] >>> 4) & 0x0F;
      int two_halfs = 0;
      do {
        if ((0 <= halfbyte) && (halfbyte <= 9)) {
          buf.append((char) ('0' + halfbyte));
        } else {
          buf.append((char) ('a' + (halfbyte - 10)));
        }
        halfbyte = data[i] & 0x0F;
      } while (two_halfs++ < 1);
    }
    return buf.toString();
  }
  
  
  
  public static String sha1(String string)
    throws NoSuchAlgorithmException, UnsupportedEncodingException
  {
    MessageDigest md;
    md = MessageDigest.getInstance("SHA-1");
    byte[] sha1hash = new byte[40];
    md.update(string.getBytes("iso-8859-1"), 0, string.length());
    sha1hash = md.digest();
    return convertToHex(sha1hash);
  }
}