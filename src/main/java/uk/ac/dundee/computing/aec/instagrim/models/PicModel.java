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
package uk.ac.dundee.computing.aec.instagrim.models;

/*
 * Expects a cassandra columnfamily defined as
 * use keyspace2;
 CREATE TABLE Tweets (
 user varchar,
 interaction_time timeuuid,
 tweet varchar,
 PRIMARY KEY (user,interaction_time)
 ) WITH CLUSTERING ORDER BY (interaction_time DESC);
 * To manually generate a UUID use:
 * http://www.famkruithof.net/uuid/uuidgen
 */
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;
import uk.ac.dundee.computing.aec.instagrim.exception.InvalidImageTypeException;
import uk.ac.dundee.computing.aec.instagrim.exception.NullSessionException;
import uk.ac.dundee.computing.aec.instagrim.exception.UnavailableSessionException;

import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;

//import uk.ac.dundee.computing.aec.stores.TweetStore;
/**
 * Contains methods to acess and insert images from/into the database.
 * 
 * @author Andy Cobley, Louis-Marie Matthews
 * @version 1.0.1
 */
public class PicModel
{
  public static void insertPic(byte[] b, String type, String name, String user)
    throws NullSessionException, UnavailableSessionException
  {
    try {
      String types[] = Convertors.splitPath(type);
      ByteBuffer buffer = ByteBuffer.wrap(b);
      int length = b.length;
      java.util.UUID picid = Convertors.getTimeUUID();

      //The following is a quick and dirty way of doing this, will fill the disk quickly !
      Boolean success = (new File("/var/tmp/instagrim/")).mkdirs();
      FileOutputStream output = new FileOutputStream(new File("/var/tmp/instagrim/" + picid));

      output.write(b);
      byte[] thumbb = picResize(picid.toString(), types[1]);
      int thumblength = thumbb.length;
      ByteBuffer thumbbuf = ByteBuffer.wrap(thumbb);
      byte[] processedb = picDecolour(picid.toString(), types[1]);
      ByteBuffer processedbuf = ByteBuffer.wrap(processedb);
      int processedlength = processedb.length;

      Date DateAdded = new Date();
      
      CassandraHosts.query( "INSERT INTO pics ("
        + "picid, image, thumb, processed, user, interaction_time, imagelength,"
        + "thumblength, procesedlength, type, name )"
        + "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
        picid, buffer, thumbbuf, processedbuf, user, DateAdded, length, thumblength, processedlength, type, name );
      CassandraHosts.query( "INSERT INTO userpiclist ( picid, user, pic_added ) values ( ?, ?, ? )", picid, user, DateAdded );
      
    } catch (IOException ex) {
      System.out.println("Error --> " + ex);
    }
  }
  
  
  
  public static byte[] picResize(String picid, String type)
  {
    try {
      BufferedImage bi = ImageIO.read(new File("/var/tmp/instagrim/" + picid));
      BufferedImage thumbnail = createThumbnail(bi);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ImageIO.write(thumbnail, type, baos);
      baos.flush();

      byte[] imageInByte = baos.toByteArray();
      baos.close();
      return imageInByte;
    } catch (IOException et) {

    }
    return null;
  }
  
  
  
  public static byte[] picDecolour(String picid, String type)
  {
    try {
      BufferedImage bi = ImageIO.read(new File("/var/tmp/instagrim/" + picid));
      BufferedImage processed = createProcessed(bi);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ImageIO.write(processed, type, baos);
      baos.flush();
      byte[] imageInByte = baos.toByteArray();
      baos.close();
      return imageInByte;
    } catch (IOException et) {

    }
    return null;
  }
  
  
  
  public static BufferedImage createThumbnail(BufferedImage img)
  {
    img = Scalr.resize(img, Scalr.Method.SPEED, 250, Scalr.OP_ANTIALIAS, Scalr.OP_GRAYSCALE);
    // Let's add a little border before we return result.
    return Scalr.pad(img, 2);
  }
  
  
  
  public static BufferedImage createProcessed(BufferedImage img)
  {
    int width = img.getWidth() - 1;
    img = Scalr.resize(img, Scalr.Method.SPEED, width, Scalr.OP_ANTIALIAS, Scalr.OP_GRAYSCALE);
    return Scalr.pad(img, 4);
  }
  
  
  
  public static java.util.LinkedList<Pic> getPicsForUser(String user)
    throws NullSessionException, UnavailableSessionException
  {
    java.util.LinkedList<Pic> pics = new java.util.LinkedList<>();
    ResultSet rs = CassandraHosts.query( "select picid from userpiclist where user = ?", user);
    if (rs.isExhausted()) {
      System.out.println("No Images returned");
      return null;
    }
    else {
      for (Row row : rs) {
      Pic pic = new Pic();
      java.util.UUID uuid = row.getUUID("picid");
      System.out.println("UUID" + uuid.toString());
      pic.setUUID(uuid);
      pics.add(pic);
      }
    }
    return pics;
  }
  
  
  
  /**
   * 
   * 
   * @param imageType
   * @param picId
   * @return 
   * @throws uk.ac.dundee.computing.aec.instagrim.exception.InvalidImageTypeException 
   */
  public static Pic getPic(int imageType, java.util.UUID picId)
    throws InvalidImageTypeException, NullSessionException, UnavailableSessionException
  {
    Session session = CassandraHosts.getSession();
    ByteBuffer bImage = null;
    String type = null;
    int length = 0;
    try {
      Convertors convertor = new Convertors();
      ResultSet rs;
      
      
      /**
       * TODO: What if the given type isn't correct?
       */
      if (imageType == Convertors.DISPLAY_IMAGE) {
        rs = CassandraHosts.query("SELECT image, imagelength, type FROM pics WHERE picid = ?", picId );
      }
      else if (imageType == Convertors.DISPLAY_THUMB) {
        rs = CassandraHosts.query("SELECT thumb, imagelength, thumblength, type FROM pics WHERE picid = ?", picId );
      }
      else if (imageType == Convertors.DISPLAY_PROCESSED) {
        rs = CassandraHosts.query("SELECT processed, processedlength, type FROM pics WHERE picid = ?", picId );
      } else {
        throw new InvalidImageTypeException();
      }
      
      if (rs.isExhausted()) {
        System.out.println("No Images returned");
        return null;
      }
      else {
        for (Row row : rs) {
          if (imageType == Convertors.DISPLAY_IMAGE) {
            bImage = row.getBytes("image");
            length = row.getInt("imagelength");
          }
          else if (imageType == Convertors.DISPLAY_THUMB) {
            bImage = row.getBytes("thumb");
            length = row.getInt("thumblength");
          }
          else if (imageType == Convertors.DISPLAY_PROCESSED) {
            bImage = row.getBytes("processed");
            length = row.getInt("processedlength");
          }
          type = row.getString("type");
        }
      }
    } catch (Exception et) {
      System.out.println("Can't get Pic" + et);
      return null;
    }
    Pic p = new Pic();
    p.setPic(bImage, length, type);

    return p;
  }
}