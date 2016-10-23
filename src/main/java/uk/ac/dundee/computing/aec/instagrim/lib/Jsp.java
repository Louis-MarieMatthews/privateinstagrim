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

import javax.servlet.http.HttpServletRequest;

/**
 * Contains utility functions for Jsp pages.
 * 
 * @author Louis-Marie Matthews
 * @version 1.0
 */
public class Jsp
{
  /**
   * Retrieves an attribute from the specified request's session, surrounded by
   * <p> tags, and delete the attribute from the session.
   * @param attribute
   * @param request
   * @return 
   */
  public static String getAttribute( String attribute, HttpServletRequest request ) {
    String value = (String) request.getSession().getAttribute( attribute );
    request.getSession().setAttribute( attribute, null );
    if ( value != null ) {
      return "<p>" + value + "</p>";
    } else {
      return "";
    }
  }
}