package com.google.code.lightssh.project.party.web;

import org.apache.struts2.json.annotations.JSON;

import com.google.code.lightssh.common.model.page.ListPage;
import com.google.code.lightssh.common.util.StringUtil;
import com.google.code.lightssh.common.web.action.CrudAction;
import com.google.code.lightssh.project.log.entity.Access;
import com.google.code.lightssh.project.party.entity.Party;
import com.google.code.lightssh.project.party.service.PartyManager;

/**
 * Party Action
 * @author YangXiaojin
 *
 */
public class PartyAction extends CrudAction<Party>{

	private static final long serialVersionUID = 669140342947692813L;
	
	private static final String FAMILY = "family";
	private static final String CONTACT = "contact";
	
	private Party party;
	
	private boolean unique;
	
	public void setPartyManager( PartyManager manager ){
		super.manager = manager;
	}
	
	public PartyManager getManager( ){
		return (PartyManager)this.manager;
	}

	@JSON(name="unique")
	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public Party getParty() {
		this.party = super.model;
		return party;
	}

	public void setParty(Party party) {
		this.party = party;
		super.model = this.party;
	}
	
	public String edit( ){
		String profile = request.getParameter("profile");
		if( FAMILY.equalsIgnoreCase( profile ) )
			return FAMILY;
		else if(CONTACT.equalsIgnoreCase( profile ) )
			return CONTACT;
			
		return super.edit();
	}
	
    /**
    * list
    * @return
    */
    public String list( ){       
        if( page == null ){
            page = new ListPage<Party>(); //TODO
        }
        
        if( request.getRequestURI().indexOf("/person/list") != -1 ){
        	page = getManager().listPerson(page , party );
        }else if( request.getRequestURI().indexOf("/organization/list") != -1 ){
        	page = getManager().listOrganization( page , party );
        }else{
        	page = getManager().list( page , party );
        }
        request.setAttribute( "list", page );
        
        return SUCCESS;
    }
    
    /**
     * save
     * @return
     */
     public String save( ){    	
         if( model == null ){
             return INPUT;
         }
         
         boolean isInsert = StringUtil.clean( model.getIdentity())==null;
         
         Access access = new Access(  );
         access.init(request);
         //access.setOperator( SecurityUtil.getPrincipal() );
         
         try{
         	getManager().save(party,access);
         }catch( Exception e ){ //other exception
         	//GenerationType.SEQUENCE 未插入成功,Oracle 也会生成ID
         	if( isInsert )
         		party.setId( null);
         	
             addActionError( e.getMessage() );
             return INPUT;
         } 
         
         String hint =  "保存(id="+ model.getIdentity() +")成功！" ;
         saveSuccessMessage( hint );
         String saveAndNext = request.getParameter("saveAndNext");
         if( saveAndNext != null && !"".equals( saveAndNext.trim() ) ){
         	return NEXT;
         }else{        	
         	return SUCCESS;
         }
     }
     
     /**
      * delete
      * @return
      */
      public String delete( ){
          if( party == null || StringUtil.clean(party.getIdentity()) == null ){
              return INPUT;
          }
         
          Access access = new Access(  );
          access.init(request);
          
          try{
         	 getManager().remove( party,access );
         	 saveSuccessMessage( "成功删除数据(id=" + party.getIdentity() + ")！" );
          }catch( Exception e ){ //other exception
              saveErrorMessage( "删除发生异常：" + e.getMessage() );
              return INPUT;
          } 
         
          return SUCCESS;
     }
      
  	public String unique( ){
		this.unique = this.getManager().isUniqneName( party );
		return SUCCESS;
	}

}
