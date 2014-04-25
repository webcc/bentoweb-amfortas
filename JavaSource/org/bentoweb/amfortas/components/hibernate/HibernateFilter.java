package org.bentoweb.amfortas.components.hibernate;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.hibernate.HibernateException;
import org.hibernate.Session;

public class HibernateFilter implements Filter {

  public void init(FilterConfig config) throws ServletException {
  }

  public void destroy() {
  }

  public void doFilter(ServletRequest request, ServletResponse response,
                     FilterChain chain) throws IOException, ServletException {
    // Pass the request on to cocoon
    chain.doFilter(request, response);

    // After cocoon has finished processing, close the
    // corresponding Hibernate session if it has been opened
    if( request.getAttribute("DisposeHibernateSession") != null )
    {
     Session hs = (Session) request.getAttribute("DisposeHibernateSession");
     try{
      hs.flush();
      hs.connection().close();
      hs.close();
     }
     catch( HibernateException e ){
      System.out.println(e.getMessage());
     }
     catch( SQLException e ){
      System.out.println(e.getMessage());
        }
        request.setAttribute("DisposeHibernateSession",null);
    }
  }
}