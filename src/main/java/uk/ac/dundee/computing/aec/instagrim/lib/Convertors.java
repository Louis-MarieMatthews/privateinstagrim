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

import java.net.URLDecoder;
import java.util.UUID;

/**
 * A class providing only static methods and constants. It only contains two
 * methods actually used by other classes besides its constants.
 * 
 * @author Andy Cobley, Louis-Marie Matthews
 * @version 1.0.1
 */
public final class Convertors
{
  public final static int DISPLAY_IMAGE = 0;
  public final static int DISPLAY_THUMB = 1;
  public final static int DISPLAY_PROCESSED = 2;
  
  
  
  /**
   * Unused.
   * 
   * @return 
   */
  public static UUID getTimeUUID()
  {
    return UUID.fromString(new com.eaio.uuid.UUID().toString());
  }
  
  
  
  /**
   * Unused.
   * 
   * @param uuid
   * @return 
   */
  public static byte[] asByteArray(UUID uuid)
  {
    long msb = uuid.getMostSignificantBits();
    long lsb = uuid.getLeastSignificantBits();
    byte[] buffer = new byte[16];

    for (int i = 0; i < 8; i++) {
      buffer[i] = (byte) (msb >>> 8 * (7 - i));
    }
    for (int i = 8; i < 16; i++) {
      buffer[i] = (byte) (lsb >>> 8 * (7 - i));
    }

    return buffer;
  }
  
  
  
  /**
   * Unused. The Long class already provide a similar method.
   * 
   * @param value
   * @return 
   */
  public static byte[] longToByteArray(long value)
  {
    byte[] buffer = new byte[8]; //longs are 8 bytes I believe
    for (int i = 7; i >= 0; i--) { //fill from the right
      buffer[i] = (byte) (value & 0x00000000000000ff); //get the bottom byte

      //System.out.print(""+Integer.toHexString((int)buffer[i])+",");
      value = value >>> 8; //Shift the value right 8 bits
    }
    return buffer;
  }
  
  
  
  /**
   * Unused and unecessary. The Byte class already provide a similar method.
   * 
   * @param buffer
   * @return 
   */
  public static long byteArrayToLong(byte[] buffer)
  {
    long value = 0;
    long multiplier = 1;
    for (int i = 7; i >= 0; i--) { //get from the right

      //System.out.println(Long.toHexString(multiplier)+"\t"+Integer.toHexString((int)buffer[i]));
      value = value + (buffer[i] & 0xff) * multiplier; // add the value * the hex mulitplier
      multiplier = multiplier << 8;
    }
    return value;
  }
  
  
  
  /**
   * Unused.
   * 
   * @param buffer 
   */
  public static void displayByteArrayAsHex(byte[] buffer)
  {
    int byteArrayLength = buffer.length;
    for (int i = 0; i < byteArrayLength; i++) {
      int val = (int) buffer[i];
      // System.out.print(Integer.toHexString(val)+",");
    }

    //System.out.println();
  }
  
  
  
  /**
   * Unused.
   * From: http://www.captain.at/howto-java-convert-binary-data.php
   */
  public static long arr2long(byte[] arr, int start)
  {
    int i = 0;
    int len = 4;
    int cnt = 0;
    byte[] tmp = new byte[len];
    for (i = start; i < (start + len); i++) {
      tmp[cnt] = arr[i];
      cnt++;
    }
    long accum = 0;
    i = 0;
    for (int shiftBy = 0; shiftBy < 32; shiftBy += 8) {
      accum |= ((long) (tmp[i] & 0xff)) << shiftBy;
      i++;
    }
    return accum;
  }
  
  
  /**
   * Explode comma-delimited tags into a String array and adds _No-Tag_ at the 
   * end. This method is so far unused in the rest of the project.
   * 
   * @param tags a list of tags separated by a comma
   * @return an array containing the tags with _No-Tag_ at the end.
   */
  public static String[] splitTags(String tags)
  {
    String[] splitPath = splitPath(tags);
    String[] args = new String[splitPath.length];
    args[args.length] = "_No-Tag_";
    return args;
  }
  
  
  /**
   * Accepts a path and tries to explode it into an array of valid filenames.
   * 
   * @param path the string supposed to represent a path
   * @return the path exploded into an array
   */
  public static String[] splitPath(String path) {
    String[] explodedPath;
    if ( path.charAt(0) == '/' ) {
      path = path.substring(1, path.length());
    }
    explodedPath = path.split("/");
    for (int i = 0; i < explodedPath.length; i++) {
      try {
        explodedPath[i] = URLDecoder.decode(explodedPath[i], "UTF-8");
      } catch (Exception et) {
        System.out.println("Bad URL Encoding" + explodedPath[i]);
      }
    }
    return explodedPath;
  }
}