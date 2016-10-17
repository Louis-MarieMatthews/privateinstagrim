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

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import uk.ac.dundee.computing.aec.instagrim.lib.AeSimpleSHA1;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;

/**
 *
 * @author Andy Cobley, Louis-Marie Matthews
 * @version 1.0.1
 */
public class User
{
  
  
  
  public User()
  {
  }
  
  
  
  public boolean registerUser(String username, String password)
  {
    String encodedPassword = null;
    try {
      encodedPassword = AeSimpleSHA1.SHA1(password);
    } catch (UnsupportedEncodingException | NoSuchAlgorithmException et) {
      System.out.println("Can't check your password");
      return false;
    }
    Session session = CassandraHosts.getCluster().connect("instagrim");
    PreparedStatement ps = session.prepare("insert into userprofiles (login,password) Values(?,?)");

    BoundStatement boundStatement = new BoundStatement(ps);
    session.execute( // this is where the query is executed
        boundStatement.bind( // here you are binding the 'boundStatement'
            username, encodedPassword));
    //We are assuming this always works.  Also a transaction would be good here !

    return true;
  }
  
  
  
  public boolean isValidUser(String username, String password)
  {
    String encodedPassword = null;
    try {
      encodedPassword = AeSimpleSHA1.SHA1(password);
    } catch (UnsupportedEncodingException | NoSuchAlgorithmException et) {
      System.out.println("Can't check your password");
      return false;
    }
    Session session = CassandraHosts.getCluster().connect("instagrim");
    PreparedStatement ps = session.prepare("select password from userprofiles where login = ?");
    ResultSet rs = null;
    BoundStatement boundStatement = new BoundStatement(ps);
    rs = session.execute( // this is where the query is executed
        boundStatement.bind( // here you are binding the 'boundStatement'
            username));
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
}