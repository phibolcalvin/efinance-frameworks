package com.nokor.frmk.web.struts.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 
 * @author prasnar
 *
 */
public class AuthorizationActionFilter implements Filter {
//	  private String[] roleNames;
//	  private String onErrorUrl;
//	  
	
	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
//	    String roles = filterConfig.getInitParameter("roles");
//	    if (roles == null || "".equals(roles)) {
//	      roleNames = new String[0];
//	    }
//	    else {
//	      roles.trim();
//	      roleNames = roles.split(\\s*,\\s*);
//	    }
//	    onErrorUrl = filterConfig.getInitParameter("onError");
//	    if (onErrorUrl == null || "".equals(onErrorUrl)) {
//	      onErrorUrl = "/index.jsp";
//	    }
	  }
//	  
	/**
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request,  ServletResponse response, FilterChain chain) throws IOException, ServletException {
//	    HttpServletRequest req = (HttpServletRequest) request;
//	    HttpServletResponse res = (HttpServletResponse) response;
//	    HttpSession session = req.getSession();
//	    User user = (User) session.getAttribute("user");
//	    ActionErrors errors = new ActionErrors();
//	    if (user == null) {
//	      errors.add(ActionErrors.GLOBAL_ERROR,
//	        new ActionError("error.authentication.required"));
//	    }
//	    else {
//	      boolean hasRole = false;
//	      for (int i=0; i<roleNames.length; i++) {
//	        if (user.hasRole(roleNames[i])) {
//	          hasRole = true;
//	          break;
//	        }
//	      }
//	      if (!hasRole) {
//	        errors.add(ActionErrors.GLOBAL_ERROR,
//	          new ActionError("error.authorization.required"));
//	      }
//	    }
//	    if (errors.isEmpty()) {
//	      chain.doFilter(request, response);
//	    }
//	    else {
//	      req.setAttribute(Globals.ERROR_KEY, errors);
//	      req.getRequestDispatcher(onErrorUrl).forward(req, res);
//	    }
	  }
	

	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
	}

}