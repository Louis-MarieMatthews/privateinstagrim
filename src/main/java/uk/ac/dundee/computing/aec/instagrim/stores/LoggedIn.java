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
package uk.ac.dundee.computing.aec.instagrim.stores;

/**
 *
 * @author Administrator
 */
public class LoggedIn
{
  private boolean logedin = false;
  private String Username = null;
  
  
  
  public void LogedIn()
  {
  }
  
  
  
  public void setUsername(String name)
  {
    this.Username = name;
  }
  
  
  
  public String getUsername()
  {
    return Username;
  }
  
  
  
  public void setLogedin()
  {
    logedin = true;
  }
  
  
  
  public void setLogedout()
  {
    logedin = false;
  }
  
  
  
  public void setLoginState(boolean logedin)
  {
    this.logedin = logedin;
  }
  
  
  
  public boolean getlogedin()
  {
    return logedin;
  }
}