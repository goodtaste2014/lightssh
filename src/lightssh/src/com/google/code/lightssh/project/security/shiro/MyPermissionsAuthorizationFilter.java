package com.google.code.lightssh.project.security.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.RegExPatternMatcher;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 支持 URL使用正则表达式
 * @author YangXiaojin
 *
 */
public class MyPermissionsAuthorizationFilter extends PermissionsAuthorizationFilter{
	
    private static final Logger log = LoggerFactory.getLogger(MyPermissionsAuthorizationFilter.class);
    
    /** 正式表达式匹配 */
    protected boolean regexExpMatcher = false;
	
	public MyPermissionsAuthorizationFilter( ){
		this( true );
	}
	
	public MyPermissionsAuthorizationFilter( boolean regexExp ){
		super();
		
		this.regexExpMatcher = regexExp;
		if( regexExp ){
			pathMatcher = new RegExPatternMatcher();
		}
	}
	
    protected boolean pathsMatch(String pattern, ServletRequest request) {
        String requestURI = getPathWithinApplication(request);
        if( request instanceof HttpServletRequest ){
        	String queryString = ((HttpServletRequest)request).getQueryString();
        	if( regexExpMatcher && !StringUtils.isEmpty(queryString) )
        		requestURI += ("?"+queryString);
        }
        
        String regex = pattern;
        if( regexExpMatcher )
        	regex = MyPathMatchingFilterChainResolver.replacePattern(pattern);
        
        if (log.isTraceEnabled()) {
            log.trace((regexExpMatcher?"正则":"ANT")+"规则[" + regex 
            		+ "]与当前请求地址[" + requestURI + "]...");
        }
        
        return pathsMatch(regex, requestURI);
    }

}
