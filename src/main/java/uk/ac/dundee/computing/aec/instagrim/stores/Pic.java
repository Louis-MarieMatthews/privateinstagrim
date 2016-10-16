/*
 * The java code follows the Java Programming Style Guidelines 7.0 from 
 * Geotechnical Software Services available at this address:
 * http://geosoft.no/development/javastyle.html .
 * Some rules are still not applied yet.
 * However, some rules won't be followed:
 * 1. No underscore suffix at the end of private variables (r8)
 * 2. No space between a function and its parenthesis (r74)
 * 3. Class and package names (can't be changed, lead to problems) (r3)
 * 4. Abbreviations and the use of init is okay (r17, r24)
 * 5. Statements and variable declarations don't need to be aligned (r77, r78)
 */
package uk.ac.dundee.computing.aec.instagrim.stores;

import com.datastax.driver.core.utils.Bytes;
import java.nio.ByteBuffer;

/**
 *
 * @author Administrator
 */
public class Pic
{
  private ByteBuffer bImage = null;
  private int length;
  private String type;
  private java.util.UUID UUID = null;
  
  
  
  public void Pic()
  {
  }
  
  
  
  public void setUUID(java.util.UUID UUID)
  {
    this.UUID = UUID;
  }
  
  
  
  public String getSUUID()
  {
    return UUID.toString();
  }
  
  
  
  public void setPic(ByteBuffer bImage, int length, String type)
  {
    this.bImage = bImage;
    this.length = length;
    this.type = type;
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