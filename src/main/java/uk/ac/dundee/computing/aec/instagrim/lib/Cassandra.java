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

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SimpleStatement;
import uk.ac.dundee.computing.aec.instagrim.exception.NullSessionException;
import uk.ac.dundee.computing.aec.instagrim.exception.UnavailableSessionException;

/**
 * Singleton class allowing other classes to acess a the Cassandra session for
 * Instagrim. The initialisation and closure of the cluster should be 
 * respectively done during the deployment of the context and before its 
 * undeployment.
 * It is OK to have one static variable representing the cluster and the session
 * because those objects are thread-safe.
 * 
 * @author Andy Cobley, Louis-Marie Matthews
 * @version 1.0.1
 */
public final class Cassandra
{
  private static Cluster cluster;
  private static Session session;
  private final static String HOST = "127.0.0.1";  //at least one starting point to talk to
  
  
  /**
   * Initialise the cluster, connect it to a node, sets up the keyspace 
   * instagrim if it doesn't exist yet, and create and save a session using this
   * keyspace.
   */
  public static void init()
  {
    cluster = Cluster.builder().addContactPoint(HOST).build();
    setUpKeyspaces(cluster);
    session = cluster.connect("instagrim");
  }
  
  
  /**
   * Returns a session of the cluster using the instagrim keyspace. This 
   * session should not be closed, as it is shared by all the threads. Instead,
   * this task should be delegated to a servlet event listener called before 
   * the undeployment of the context.
   * Closing it in a different way may cause UnavailableSessionException.
   * In such an event, it is still possible to reopen the session simply by 
   * calling the init() method again.
   * 
   * @return the session of the Cassandra cluster using the instagrim keyspace
   * @throws NullSessionException if the cluster is null
   * @throws UnavailableSessionException if the cluster is closed / closing
   */
  public static Session getSession()
    throws NullSessionException, UnavailableSessionException
  {
    if ( session == null) {
      throw new NullSessionException();
    } else if ( session.isClosed() ) {
      throw new UnavailableSessionException();
    } else {
      return session;
    }
  }
  
  /**
   * Useful when a prepared statement is only used once. Otherwise, it might be 
   * better to reuse it. Since prepared statements in the context will 
   * necessarily be used multiple times, it might be worth it saving them in 
   * some variables. To confirm though.
   * 
   * @param query the prepared query as a string
   * @param bound what to bind the prepared statement with
   * @return result set from the query
   */
  public static ResultSet query(String query, Object... bound)
    throws NullSessionException, UnavailableSessionException
  {
    PreparedStatement ps = getSession().prepare( query );
    BoundStatement boundStatement = new BoundStatement(ps);
    return getSession().execute(boundStatement.bind( bound ));
  }
  
  
  /**
   * Close the cluster (and the session in the process).
   */
  public static void close()
  {
    cluster.close();
  }
  
  
  
  /**
   * Sets up the Cassandra schema needed by the context. This method can not
   * use the getSession() of CassandraHosts directly because it can be called 
   * while the keyspace instagrim does not exist yet.
   * 
   * @param c the cluster on which to define the schema
   */
  public static void setUpKeyspaces(Cluster c)
  {
    try {
      //Add some keyspaces here
      String createKeyspace = "CREATE KEYSPACE IF NOT EXISTS instagrim  WITH replication = {'class':'SimpleStrategy', 'replication_factor':1}";
      String createImgTable = "CREATE TABLE IF NOT EXISTS instagrim.images ("
          + " user VARCHAR,"
          + " id UUID, "
          + " interaction_time TIMESTAMP,"
          + " title VARCHAR,"
          + " image BLOB,"
          + " thumbnail BLOB,"
          + " processed BLOB,"
          + " image_length INT,"
          + " thumbnail_length INT,"
          + " processed_length INT,"
          + " type VARCHAR,"
          + " name VARCHAR,"
          + " PRIMARY KEY (id)"
          + ")";
      String createUserImgList = "CREATE TABLE IF NOT EXISTS instagrim.user_images (\n"
          + "image_id UUID,\n"
          + "user VARCHAR,\n"
          + "image_added TIMESTAMP,\n"
          + "PRIMARY KEY (user, image_added)\n"
          + ") WITH CLUSTERING ORDER BY (image_added desc);";
      String createAddressType = "CREATE TYPE IF NOT EXISTS instagrim.address (\n"
          + "street TEXT,\n"
          + "city TEXT,\n"
          + "zip INT\n"
          + ");";
      String createUserProfile = "CREATE TABLE IF NOT EXISTS instagrim.user_profiles (\n"
          + "login TEXT PRIMARY KEY,\n"
          + "password TEXT,\n"
          + "first_name TEXT,\n"
          + "last_name TEXT,\n"
          + "email SET<TEXT>,\n"
          + "addresses  MAP<TEXT, FROZEN <address>>\n"
          + ");";
      Session session = c.connect();
      try {
        PreparedStatement statement = session
            .prepare(createKeyspace);
        BoundStatement boundStatement = new BoundStatement(
            statement);
        ResultSet rs = session
            .execute(boundStatement);
        System.out.println("created instagrim ");
      } catch (Exception et) {
        System.out.println("Can't create instagrim " + et);
      }

      //now add some column families 
      System.out.println("" + createImgTable);

      try {
        SimpleStatement cqlQuery = new SimpleStatement(createImgTable);
        session.execute(cqlQuery);
      } catch (Exception et) {
        System.out.println("Can't create tweet table " + et);
      }
      System.out.println("" + createUserImgList);

      try {
        SimpleStatement cqlQuery = new SimpleStatement(createUserImgList);
        session.execute(cqlQuery);
      } catch (Exception et) {
        System.out.println("Can't create user img list table " + et);
      }
      System.out.println("" + createAddressType);
      try {
        SimpleStatement cqlQuery = new SimpleStatement(createAddressType);
        session.execute(cqlQuery);
      } catch (Exception et) {
        System.out.println("Can't create Address type " + et);
      }
      System.out.println("" + createUserProfile);
      try {
        SimpleStatement cqlQuery = new SimpleStatement(createUserProfile);
        session.execute(cqlQuery);
      } catch (Exception et) {
        System.out.println("Can't create Address Profile " + et);
      }
      session.close();

    } catch (Exception et) {
      System.out.println("Other keyspace or could definition error" + et);
    }
  }
}