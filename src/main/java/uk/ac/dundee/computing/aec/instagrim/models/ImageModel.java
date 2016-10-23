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
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.imgscalr.Scalr;
import uk.ac.dundee.computing.aec.instagrim.exception.InsufficientPermissionsException;
import uk.ac.dundee.computing.aec.instagrim.exception.InvalidImageTypeException;
import uk.ac.dundee.computing.aec.instagrim.exception.NullSessionException;
import uk.ac.dundee.computing.aec.instagrim.exception.UnavailableSessionException;

import uk.ac.dundee.computing.aec.instagrim.lib.Cassandra;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
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
  public static void insertUserImage(byte[] b, String type, String name, String user,
                                     String title)
    throws NullSessionException, UnavailableSessionException, IOException
  {
    System.out.println( "ImageModel#insertUserImage(…): called with type = " + type );
    String types[] = Convertors.splitPath(type);
    ByteBuffer buffer = ByteBuffer.wrap(b);
    int length = b.length;
    java.util.UUID imgId = Convertors.getTimeUUID();
    File file = new File( "/var/tmp/instagrim/" + imgId );
    FileOutputStream output = null;
    try {
      output = new FileOutputStream( file );
      output.write(b);
      byte[] thumbb = resizeUserImage(imgId.toString(), types[1]);
      int thumblength = thumbb.length;
      ByteBuffer thumbbuf = ByteBuffer.wrap(thumbb);
      byte[] processedb = decolourUserImage(imgId.toString(), types[1]);
      ByteBuffer processedbuf = ByteBuffer.wrap(processedb);
      int processedlength = processedb.length;

      Date dateAdded = new Date();
      
      
      Cassandra.query( "INSERT INTO images ("
        + "id, title, image, thumbnail, processed, user, interaction_time, image_length,"
        + "thumbnail_length, processed_length, type, name )"
        + "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
        imgId, title, buffer, thumbbuf, processedbuf, user, dateAdded, length, thumblength, processedlength, type, name );
      Cassandra.query( "INSERT INTO user_images ( image_id, user, image_added ) VALUES ( ?, ?, ? )", imgId, user, dateAdded );
      output.close();
    } catch (IOException ex) {
      
      System.out.println("Error --> " + ex);
      if ( output != null ) {
        output.close();
      }
      throw ex;
    }
    finally {
      if ( output != null ) {
        output.close();
      }
      if ( file.exists() ) {
        System.out.println( "ImageModel#insertUserImage(…]: file exists." );
      } else {
        System.out.println( "ImageModel#insertUserImage(…]: FILE DOES NOT EXIST." );
      }
      if ( file.delete() ) {
        System.out.println( "ImageModel#insertUserImage(…]: file has been deleted." );
      } else {
        System.out.println( "ImageModel#insertUserImage(…]: FILE HASN'T BEEN DELETED." );
      }
    }
  }
  
  
  
  public static byte[] resizeUserImage(String imgId, String type)
    throws IOException
  {
    BufferedImage bi;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();;
    try {
      bi = ImageIO.read(new File("/var/tmp/instagrim/" + imgId));
      BufferedImage thumbnail = createThumbnail(bi);
      ImageIO.write(thumbnail, type, baos);
      baos.flush();

      byte[] imageInByte = baos.toByteArray();
      baos.close();
      return imageInByte;
    } catch (IOException et) {
      if ( baos != null ) {
        baos.close();
      }
      throw et;
    }
  }
  
  
  
  public static byte[] decolourUserImage(String imageId, String type)
    throws IOException
  {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      BufferedImage bi = ImageIO.read(new File("/var/tmp/instagrim/" + imageId));
      BufferedImage processed = createProcessed(bi);
      ImageIO.write(processed, type, baos);
      baos.flush();
        baos.close();
      byte[] imageInByte = baos.toByteArray();
      return imageInByte;
    } catch (IOException et) {
      if ( baos != null ) {
        baos.close();
      }
      throw et;
    }
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
  
  
  
  /**
   * Returns the different image UUIDs for a particular user.
   * 
   * @param username the user whose image UUIDs to return
   * @return a list of their image UUIDs
   * @throws NullSessionException
   * @throws UnavailableSessionException 
   */
  public static java.util.LinkedList<java.util.UUID> getImagesUuidForUser(String username)
    throws NullSessionException, UnavailableSessionException
  {
    java.util.LinkedList<java.util.UUID> uuids = new java.util.LinkedList<>();
    ResultSet rs = Cassandra.query( "SELECT image_id FROM user_images WHERE user = ?", username);
    if (rs.isExhausted()) {
      System.out.println("ImageModel#getImagesUuidForUser(…): no images returned");
      return null;
    }
    else {
      for (Row row : rs) {
      java.util.UUID uuid = row.getUUID("image_id");
      System.out.println("ImageModel#getImagesUuidForUser: UUID found : " + uuid.toString());
      uuids.add(uuid);
      }
    }
    return uuids;
  }
  
  
  
  /**
   * Returns the image stored in the database with the specified image id.
   * 
   * @param imageType the type of the image (0: image, 1: thumbnail, 2: proccessed)
   * @param imageId the UUID of the image
   * @return the maching image
   * @throws uk.ac.dundee.computing.aec.instagrim.exception.InvalidImageTypeException 
   */
  public static UserImage getUserImage( int imageType, java.util.UUID imageId )
    throws InvalidImageTypeException, NullSessionException, UnavailableSessionException
  {
    ByteBuffer bImage = null;
    String type = null;
    String title = null;
    int length = 0;
    try {
      ResultSet rs;
      
      if (imageType == Convertors.DISPLAY_IMAGE) {
        rs = Cassandra.query("SELECT title, image, image_length, type FROM images WHERE id = ?", imageId );
      }
      else if (imageType == Convertors.DISPLAY_THUMB) {
        rs = Cassandra.query("SELECT title, thumbnail, image_length, thumbnail_length, type FROM images WHERE id = ?", imageId );
      }
      else if (imageType == Convertors.DISPLAY_PROCESSED) {
        rs = Cassandra.query("SELECT title, processed, processed_length, type FROM images WHERE id = ?", imageId );
      } else {
        throw new InvalidImageTypeException();
      }
      
      if (rs.isExhausted()) {
        System.out.println("ImageModel#getUserImage(…): no images returned");
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
          title = row.getString( "title" );
        }
      }
    } catch (Exception et) {
      System.out.println("ImageModel#getUserImage(…): " + et);
      return null;
    }
    UserImage p = new UserImage(bImage, length, type, title);
    
    return p;
  }
  
  
  public static String getTitle( java.util.UUID uuid )
    throws NullSessionException, UnavailableSessionException
  {
    ResultSet rs = Cassandra.query( "SELECT title FROM images WHERE id = ?", uuid );
    String title;
    if ( rs.isExhausted() ) {
      title = "Untitled";
    } else {
      title = rs.one().getString( "title" );
    }
    return title;
  }
  
  
  
  /**
   * Checks if the currently logged-in user (static data in LoggedIn class) is
   * the owner of the specified image.
   * 
   * @param uuid the uuid of the mentioned image
   * @return true if they are the owner, false otherwise
   */
  public static boolean isOwner( java.util.UUID uuid, String user )
    throws NullSessionException, UnavailableSessionException
  {
    System.out.println( "ImageModel#checkPermissions(…): called with user = " + user );
    ResultSet rs = Cassandra.query( "SELECT user FROM user_images WHERE image_id = ?", uuid );
    String owner = rs.one().getString( "user" );
    System.out.println( "ImageModel#checkPermissions(…): owner = " + owner );
    if ( owner.equals( user ) ) {
      System.out.println( "ImageModel#checkPermissions(…): owner and users are the same." );
      return true;
    } else {
      System.out.println( "ImageModel#checkPermissions(…): USER IS NOT THE OWNER" );
      return false;
    }
  }
  
  
  
  /**
   * Rename the designed image, provided that the current logged-in user is its 
   * owner.
   * 
   */
  public static void rename( java.util.UUID uuid, String newName,
                             String user )
    throws NullSessionException, UnavailableSessionException,
           InsufficientPermissionsException
  {
    System.out.println( "ImageModel#rename(…): called" );
    System.out.println( "ImageModel#rename(…): newName = " + newName );
    System.out.println( "ImageModel#rename(…): user" + user );
    if ( isOwner( uuid, user ) ) {
     System.out.println( "ImageModel#rename(…): is owner. Proceeding to renaming…" );
      Cassandra.query( "UPDATE images SET title = ? WHERE id = ?;", newName, uuid );
    } else {
      throw new InsufficientPermissionsException();
    }
  }
}