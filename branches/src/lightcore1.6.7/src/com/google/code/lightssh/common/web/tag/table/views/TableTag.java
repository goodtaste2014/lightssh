package com.google.code.lightssh.common.web.tag.table.views;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ui.AbstractUITag;

import com.google.code.lightssh.common.web.tag.table.components.Table;
import com.opensymphony.xwork2.util.ValueStack;

public class TableTag extends AbstractUITag{

	private static final long serialVersionUID = -3333068276669233395L;
	
	/**
	 * 状态
	 */
    protected String status;
    
    /**
     * 分页参数前缀
     */
    protected String pageParamPrefix;
    
    /**
     * 是否分页
     */
    protected String pageable;
    
	/**
	 * 是否动态表格
	 */
	protected String dynamic;
	
	/**
	 * 动态列名
	 */
	protected String dynamicCols;

	@Override
	public Component getBean(ValueStack stack, HttpServletRequest req,
			HttpServletResponse res) {
        Table table = new Table(stack, req, res);

        return table;
	}
	
    /**
     * @see org.apache.struts2.views.jsp.ComponentTagSupport#doEndTag()
     */
    public int doEndTag() throws JspException {
        component = null;
        return EVAL_PAGE;
    }

    /**
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doAfterBody()
     */
    public int doAfterBody() throws JspException {
        boolean again = component.end(pageContext.getOut(), getBody());

        if(again) {
            return EVAL_BODY_AGAIN;
        } else {
            if(bodyContent != null) {
                try {
                    bodyContent.writeOut(bodyContent.getEnclosingWriter());
                } catch(Exception e) {
                    throw new JspException(e.getMessage());
                }
            }
            return SKIP_BODY;
        }
    }
    
    /**
     * @see com.opensymphony.webwork.views.jsp.ui.AbstractUITag#populateParams()
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	protected void populateParams() {
        super.populateParams();

        Table table = ((Table) component);
        table.setValue(value);
        table.setStatus(status);
        table.setPageable("true".equals(pageable));
        table.setPageParamPrefix(pageParamPrefix);
        table.setDynamic("true".equals(dynamic));
        
        if( dynamicCols != null ){
        	Object obj = findValue(dynamicCols);
        	if( obj != null ){
        		if(obj instanceof Collection )
        			table.setDynamicCols((Collection)obj);
        		else if( obj instanceof String[] ){
        			Set<String> cols = new HashSet<String>();
                	for(String col:(String[])obj )
                		cols.add(col);
                	
                	table.setDynamicCols(cols);
        		}
        	}
        		
        	
        }
    }

	public void setStatus(String status) {
		this.status = status;
	}

	public void setPageable(String pageable) {
		this.pageable = pageable;
	}

	public void setPageParamPrefix(String pageParamPrefix) {
		this.pageParamPrefix = pageParamPrefix;
	}

	public void setDynamic(String dynamic) {
		this.dynamic = dynamic;
	}

	public void setDynamicCols(String dynamicCols) {
		this.dynamicCols = dynamicCols;
	}

}