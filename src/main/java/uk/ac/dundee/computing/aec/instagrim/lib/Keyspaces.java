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

import com.datastax.driver.core.Session;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.SimpleStatement;

/**
 * Sets up the Cassandra keyspaces.
 * 
 * @author Andy Cobley, Louis-Marie Matthews
 * @version 1.0.1
 */
public final class Keyspaces
{
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
      String createKeyspace = "create keyspace if not exists instagrim  WITH replication = {'class':'SimpleStrategy', 'replication_factor':1}";
      String createPicTable = "CREATE TABLE if not exists instagrim.Pics ("
          + " user varchar,"
          + " picid uuid, "
          + " interaction_time timestamp,"
          + " title varchar,"
          + " image blob,"
          + " thumb blob,"
          + " processed blob,"
          + " imagelength int,"
          + " thumblength int,"
          + "  processedlength int,"
          + " type  varchar,"
          + " name  varchar,"
          + " PRIMARY KEY (picid)"
          + ")";
      String createUserPicList = "CREATE TABLE if not exists instagrim.userpiclist (\n"
          + "picid uuid,\n"
          + "user varchar,\n"
          + "pic_added timestamp,\n"
          + "PRIMARY KEY (user,pic_added)\n"
          + ") WITH CLUSTERING ORDER BY (pic_added desc);";
      String createAddressType = "CREATE TYPE if not exists instagrim.address (\n"
          + "    street text,\n"
          + "    city text,\n"
          + "    zip int\n"
          + "  );";
      String createUserProfile = "CREATE TABLE if not exists instagrim.userprofiles (\n"
          + "    login text PRIMARY KEY,\n"
          + "   password text,\n"
          + "    first_name text,\n"
          + "    last_name text,\n"
          + "    email set<text>,\n"
          + "    addresses  map<text, frozen <address>>\n"
          + "  );";
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
      System.out.println("" + createPicTable);

      try {
        SimpleStatement cqlQuery = new SimpleStatement(createPicTable);
        session.execute(cqlQuery);
      } catch (Exception et) {
        System.out.println("Can't create tweet table " + et);
      }
      System.out.println("" + createUserPicList);

      try {
        SimpleStatement cqlQuery = new SimpleStatement(createUserPicList);
        session.execute(cqlQuery);
      } catch (Exception et) {
        System.out.println("Can't create user pic list table " + et);
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