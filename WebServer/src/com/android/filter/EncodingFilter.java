package com.android.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * ×Ö·û¹ýÂËÆ÷
 * 
 * @author 
 * 
 */
public class EncodingFilter implements Filter {
  private FilterConfig filterconfig;
  private String encoding;

  public void destroy() {
    filterconfig = null;
  }

  public void doFilter(ServletRequest servletrequest,
      ServletResponse servletresponse, FilterChain filterchain)
      throws IOException, ServletException {

    if (servletrequest.getCharacterEncoding() == null) {
      HttpServletRequest request = (HttpServletRequest) servletrequest;
      request.setCharacterEncoding(this.encoding);
    }
    filterchain.doFilter(servletrequest, servletresponse);
  }

  public void init(FilterConfig filterconfig) throws ServletException {
    this.filterconfig = filterconfig;
    this.encoding = this.filterconfig.getInitParameter("encoding");
  }
}
