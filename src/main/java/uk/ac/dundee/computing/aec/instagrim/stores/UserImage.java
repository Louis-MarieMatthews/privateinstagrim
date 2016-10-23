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
package uk.ac.dundee.computing.aec.instagrim.stores;

import com.datastax.driver.core.utils.Bytes;
import java.nio.ByteBuffer;

/**
 * @author Andy Cobley, Louis-Marie Matthews
 * @version 1.0.1
 */
public class UserImage
{
  private ByteBuffer bImage = null;
  private int length;
  private String type;
  private java.util.UUID uuid = null;
  
  
  
  
  public UserImage()
  {
  }
  
  
  
  
  public UserImage(ByteBuffer bImage, int length, String type)
  {
    this.bImage = bImage;
    this.length = length;
    this.type = type;
  }
  
  
  
  public void setUuid(java.util.UUID uuid)
  {
    this.uuid = uuid;
  }
  
  
  
  public String getStringUuid()
  {
    return uuid.toString();
  }
  
  
  
  public ByteBuffer getBuffer()
  {
    return bImage;
  }
  
  
  
  public int getLength()
  {
    return length;
  }
  
  
  
  public String getType()
  {
    return type;
  }
  
  
  
  public byte[] getBytes()
  {
    byte image[] = Bytes.getArray(bImage);
    return image;
  }
}