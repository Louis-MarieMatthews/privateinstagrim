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
package uk.ac.dundee.computing.aec.instagrim.lib;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.exceptions.NoHostAvailableException;
import uk.ac.dundee.computing.aec.instagrim.exception.NoDatabaseConnectionException;

/**
 * Singleton class allowing other classes to acess a cassandra cluster.
 * @author Andy Cobley, Louis-Marie Matthews
 * @version 1.0.1
 */
public final class CassandraHosts
{
  private static Cluster cluster;
  private final static String HOST = "127.0.0.1";  //at least one starting point to talk to
  
  
  
  public static Cluster getCluster()
    throws NoDatabaseConnectionException
  {
    if (cluster == null) {
      System.out.println("getCluster");
      cluster = Cluster.builder().addContactPoint(HOST).build();
      Keyspaces.setUpKeyspaces(cluster);
    }
    if ( isClusterWorking() ) {
      return cluster;
    } else {
      throw new NoDatabaseConnectionException();
    }
  }
  
  
  
  private static boolean isClusterWorking() {
    if ( cluster != null & ! cluster.isClosed() ) {
      return true;
    } else {
      return false;
    }
  }
  
  
  
  public static void close()
  {
    cluster.close();
  }
}