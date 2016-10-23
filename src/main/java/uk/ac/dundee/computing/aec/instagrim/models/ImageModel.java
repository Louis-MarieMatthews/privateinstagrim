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

import uk.ac.dundee.computing.aec.instagrim.lib.Cassandra;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.stores.UserImage;

//import uk.ac.dundee.computing.aec.stores.TweetStore;
/**
 * Contains methods to acess and insert images from/into the database.
 * 
 * @author Andy Cobley, Louis-Marie Matthews
 * @version 1.0.1
 */
public class ImageModel
{
  public static void insertUserImage(byte[] b, String type, String name, String user)
    throws NullSessionException, UnavailableSessionException
  {
    try {
      String types[] = Convertors.splitPath(type);
      ByteBuffer buffer = ByteBuffer.wrap(b);
      int length = b.length;
      java.util.UUID imgId = Convertors.getTimeUUID();

      //The following is a quick and dirty way of doing this, will fill the disk quickly !
      Boolean success = (new File("/var/tmp/instagrim/")).mkdirs();
      FileOutputStream output = new FileOutputStream(new File("/var/tmp/instagrim/" + imgId));

      output.write(b);
      byte[] thumbb = resizeUserImage(imgId.toString(), types[1]);
      int thumblength = thumbb.length;
      ByteBuffer thumbbuf = ByteBuffer.wrap(thumbb);
      byte[] processedb = decolourUserImage(imgId.toString(), types[1]);
      ByteBuffer processedbuf = ByteBuffer.wrap(processedb);
      int processedlength = processedb.length;

      Date dateAdded = new Date();
      
      System.out.println( "ImageModel#insertUserImage(…): thumbbuf = " + thumbbuf );
      System.out.println( "ImageModel#insertUserImage(…): " );
      System.out.println( "ImageModel#insertUserImage(…): " );
      System.out.println( "ImageModel#insertUserImage(…): " );
      System.out.println( "ImageModel#insertUserImage(…): " );
      System.out.println( "ImageModel#insertUserImage(…): " );
      System.out.println( "ImageModel#insertUserImage(…): " );
      System.out.println( "ImageModel#insertUserImage(…): " );
      
      
      Cassandra.query( "INSERT INTO images ( id, image , thumbnail, processed ) VALUES ( ?, ?, ?, ? )", imgId, buffer, thumbbuf, processedbuf );
      
      
      Cassandra.query( "INSERT INTO images ("
        + "id, image, thumbnail, processed, user, interaction_time, image_length,"
        + "thumbnail_length, processed_length, type, name )"
        + "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
        imgId, buffer, thumbbuf, processedbuf, user, dateAdded, length, thumblength, processedlength, type, name );
      Cassandra.query( "INSERT INTO user_images ( image_id, user, image_added ) VALUES ( ?, ?, ? )", imgId, user, dateAdded );
      
    } catch (IOException ex) {
      System.out.println("Error --> " + ex);
    }
  }
  
  
  
  public static byte[] resizeUserImage(String imgId, String type)
  {
    try {
      BufferedImage bi = ImageIO.read(new File("/var/tmp/instagrim/" + imgId));
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
  
  
  
  public static byte[] decolourUserImage(String imageId, String type)
  {
    try {
      BufferedImage bi = ImageIO.read(new File("/var/tmp/instagrim/" + imageId));
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
  
  
  
  public static java.util.LinkedList<UserImage> getImagesForUser(String user)
    throws NullSessionException, UnavailableSessionException
  {
    java.util.LinkedList<UserImage> imgs = new java.util.LinkedList<>();
    ResultSet rs = Cassandra.query( "SELECT image_id FROM user_images WHERE user = ?", user);
    if (rs.isExhausted()) {
      System.out.println("No Images returned");
      return null;
    }
    else {
      for (Row row : rs) {
      UserImage img = new UserImage();
      java.util.UUID uuid = row.getUUID("image_id");
      System.out.println("UUID" + uuid.toString());
      img.setUuid(uuid);
      imgs.add(img);
      }
    }
    return imgs;
  }
  
  
  
  /**
   * 
   * 
   * @param imageType
   * @param imageId
   * @return 
   * @throws uk.ac.dundee.computing.aec.instagrim.exception.InvalidImageTypeException 
   */
  public static UserImage getUserImage(int imageType, java.util.UUID imageId)
    throws InvalidImageTypeException, NullSessionException, UnavailableSessionException
  {
    Session session = Cassandra.getSession();
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
        rs = Cassandra.query("SELECT image, image_length, type FROM images WHERE id = ?", imageId );
      }
      else if (imageType == Convertors.DISPLAY_THUMB) {
        rs = Cassandra.query("SELECT thumbnail, image_length, thumbnail_length, type FROM images WHERE id = ?", imageId );
      }
      else if (imageType == Convertors.DISPLAY_PROCESSED) {
        rs = Cassandra.query("SELECT processed, processed_length, type FROM images WHERE id = ?", imageId );
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
            length = row.getInt("image_length");
          }
          else if (imageType == Convertors.DISPLAY_THUMB) {
            bImage = row.getBytes("thumbnail");
            length = row.getInt("thumbnail_length");
          }
          else if (imageType == Convertors.DISPLAY_PROCESSED) {
            bImage = row.getBytes("processed");
            length = row.getInt("processed_length");
          }
          type = row.getString("type");
        }
      }
    } catch (Exception et) {
      System.out.println("Can't get img " + et);
      return null;
    }
    UserImage p = new UserImage();
    p.set(bImage, length, type);

    return p;
  }
}