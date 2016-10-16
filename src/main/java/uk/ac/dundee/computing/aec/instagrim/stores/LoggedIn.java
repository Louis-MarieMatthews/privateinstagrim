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
  private boolean logedIn = false;
  private String username = null;
  
  
  
  public void LogedIn()
  {
  }
  
  
  
  public void setUsername(String name)
  {
    this.username = name;
  }
  
  
  
  public String getUsername()
  {
    return username;
  }
  
  
  
  public void setLogedIn()
  {
    logedIn = true;
  }
  
  
  
  public void setLogedOut()
  {
    logedIn = false;
  }
  
  
  
  public void setLoginState(boolean logedIn)
  {
    this.logedIn = logedIn;
  }
  
  
  
  public boolean getLogedIn()
  {
    return logedIn;
  }
}