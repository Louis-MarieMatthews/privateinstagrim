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

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import uk.ac.dundee.computing.aec.instagrim.exception.NullSessionException;
import uk.ac.dundee.computing.aec.instagrim.exception.UnavailableSessionException;
import uk.ac.dundee.computing.aec.instagrim.exception.UsernameNotAsciiException;
import uk.ac.dundee.computing.aec.instagrim.exception.UsernameTakenException;
import uk.ac.dundee.computing.aec.instagrim.exception.WrongLoginDetailsException;
import uk.ac.dundee.computing.aec.instagrim.lib.AeSimpleSha1;
import uk.ac.dundee.computing.aec.instagrim.lib.Cassandra;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;

/**
 * A model class to acess database-stored users.
 * 
 * @author Andy Cobley, Louis-Marie Matthews
 * @version 1.0.1
 */
public class User
{
  public final static String DEFAULT_USERNAME = "majed";
  
  
  
  public static boolean registerUser(String username, String password)
    throws UsernameNotAsciiException, UsernameTakenException, NullSessionException,
           UnavailableSessionException
  {
    if ( ! Charset.forName("US-ASCII").newEncoder().canEncode(username)) {
      throw new UsernameNotAsciiException();
    } else if ( userExists(username) ) {
      throw new UsernameTakenException();
    }
    String encodedPassword = null;
    try {
      encodedPassword = AeSimpleSha1.sha1(password);
    } catch (UnsupportedEncodingException | NoSuchAlgorithmException et) {
      System.out.println("Can't check your password");
      return false;
    }
    Cassandra.query("INSERT INTO user_profiles (login, password) VALUES ( ?, ? )",
                         username, encodedPassword);
    //We are assuming this always works.  Also a transaction would be good here !

    return true;
  }
  
  
  
  public static boolean isValidUser(String username, String password)
    throws NullSessionException, UnavailableSessionException
  {
    String encodedPassword = null;
    try {
      encodedPassword = AeSimpleSha1.sha1(password);
    } catch (UnsupportedEncodingException | NoSuchAlgorithmException et) {
      System.out.println("Can't check your password");
      return false;
    }
    ResultSet rs = Cassandra.query( "SELECT password FROM user_profiles WHERE login = ?", username );
    if (rs.isExhausted()) {
      System.out.println("No Images returned");
      return false;
    }
    else {
      for (Row row : rs) {

        String storedPass = row.getString("password");
        if (storedPass.compareTo(encodedPassword) == 0) {
          return true;
        }
      }
    }
    return false;
  }
  
  
  
  public static boolean userExists(String username)
    throws NullSessionException, UnavailableSessionException
  {
    ResultSet rs = Cassandra.query("SELECT login FROM user_profiles WHERE login = ?", username);
    if ( rs.isExhausted() ) {
      return false;
    } else {
      return true;
    }
  }
  
  
  
  public static void delete(String username, String password)
    throws WrongLoginDetailsException, NullSessionException, UnavailableSessionException
  {
    System.out.println("User.delete(…): called. username = " + username );
    if ( isValidUser( username, password ) ) {
      Cassandra.query("DELETE FROM user_pictures WHERE user = ?", username);
      Cassandra.query("DELETE FROM user_profiles WHERE login = ?", username);
      
      LinkedList<Pic> list = PicModel.getPicsForUser(username);
      try {
        int n = list.size();
        String picId;
          for ( int i = 0; i < n; i++ ) {
            picId = list.get(i).getStringUuid();
            Cassandra.query( "DELETE FROM pictures WHERE id = ?", picId );
            System.out.println( "User.delete(…): Tried to delete " + picId );
          }
      } catch ( NullPointerException e ) {
        System.out.println( "User.delete(…): No pics found for " + username + ".");
      }
    } else {
      throw new WrongLoginDetailsException();
    }
  }
}