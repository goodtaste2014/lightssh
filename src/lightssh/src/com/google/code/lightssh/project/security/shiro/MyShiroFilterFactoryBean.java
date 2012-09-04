package com.google.code.lightssh.project.security.shiro;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;

public class MyShiroFilterFactoryBean extends ShiroFilterFactoryBean{
	
	private static transient final Logger log = LoggerFactory.getLogger(MyShiroFilterFactoryBean.class);
	
	private boolean regexPathMatcher = false;
	
    public boolean isRegexPathMatcher() {
		return regexPathMatcher;
	}

	public void setRegexPathMatcher(boolean regexPathMatcher) {
		this.regexPathMatcher = regexPathMatcher;
	}

	protected AbstractShiroFilter createInstance() throws Exception {

        log.debug("Creating Shiro Filter instance.");

        SecurityManager securityManager = getSecurityManager();
        if (securityManager == null) {
            String msg = "SecurityManager property must be set.";
            throw new BeanInitializationException(msg);
        }

        if (!(securityManager instanceof WebSecurityManager)) {
            String msg = "The security manager does not implement the WebSecurityManager interface.";
            throw new BeanInitializationException(msg);
        }

        FilterChainManager manager = createFilterChainManager();

        //路径匹配
        MyPathMatchingFilterChainResolver chainResolver = new MyPathMatchingFilterChainResolver(regexPathMatcher);
        chainResolver.setFilterChainManager(manager);

        return new SpringShiroFilter((WebSecurityManager) securityManager, chainResolver);
    }
    
    private static final class SpringShiroFilter extends AbstractShiroFilter {

        protected SpringShiroFilter(WebSecurityManager webSecurityManager, FilterChainResolver resolver) {
            super();
            if (webSecurityManager == null) {
                throw new IllegalArgumentException("WebSecurityManager property cannot be null.");
            }
            setSecurityManager(webSecurityManager);
            if (resolver != null) {
                setFilterChainResolver(resolver);
            }
        }
    }

}
