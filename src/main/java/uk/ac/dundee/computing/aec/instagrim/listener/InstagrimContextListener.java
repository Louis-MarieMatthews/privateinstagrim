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
package uk.ac.dundee.computing.aec.instagrim.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;

/**
 * This class contains methods called during the deployment of the context and 
 * before its shutdown. Its only purpose so far is to initialize a Cassandra
 * session and to close it.
 * 
 * @author Louis-Marie Matthews
 * @version 1.0
 */
@WebListener
public class InstagrimContextListener
  implements ServletContextListener
{

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    CassandraHosts.init();
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    CassandraHosts.close();
  }
}
