package com.google.code.lightssh.project.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.Cache;

import org.springframework.web.filter.GenericFilterBean;

import com.google.code.lightssh.common.web.SessionKey;
import com.google.code.lightssh.project.security.service.SecuirtyFramework;
import com.google.code.lightssh.project.security.service.SecurityUtil;

public class SessionInvalidateFilter extends GenericFilterBean{
	
	/**
	 * 用于存放Session强制失效的用户名
	 */
	private Cache cache;
	
	private String logoutUrl = "/logout.do";

	public void setCache(Cache cache) {
		this.cache = cache;
	}

	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse rsp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)rsp;
		if( cache != null ){
			String username = SecurityUtil.getPrincipal( SecuirtyFramework.SHIRO.getValue() );
			if( cache.get( username ) != null ){
				cache.remove( username );
				request.getSession().setAttribute( SessionKey.ERROR_MESSAGES, "账号["+username+"]已被禁止使用！");
				response.sendRedirect( request.getContextPath() + logoutUrl);
			}
		}
		
		chain.doFilter(req, rsp);
	}

}
