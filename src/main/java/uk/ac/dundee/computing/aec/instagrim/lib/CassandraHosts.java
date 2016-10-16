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
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Host;
import java.util.Iterator;
import java.util.Set;

/**
 * Singleton class allowing other classes to acess a cassandra cluster.
 */
public final class CassandraHosts
{
  private static Cluster cluster;
  private final static String HOST = "127.0.0.1";  //at least one starting point to talk to
  
  
  
  public CassandraHosts()
  { 
  }
  
  
  
  public static Cluster getCluster()
  {
    if ( cluster == null ) {
      System.out.println("getCluster");
      cluster = Cluster.builder().addContactPoint(HOST).build();
      Keyspaces.setUpKeyspaces(cluster);
    }
    return cluster;
  }
  
  
  
  public void close()
  {
    cluster.close();
  }
}