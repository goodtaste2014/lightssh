package com.google.code.lightssh.project.party.web;

import com.google.code.lightssh.common.web.action.CrudAction;
import com.google.code.lightssh.project.log.entity.Access;
import com.google.code.lightssh.project.party.entity.Person;
import com.google.code.lightssh.project.party.service.PersonManager;

/**
 * person action
 * @author YangXiaojin
 *
 */
public class PersonAction extends CrudAction<Person>{

	private static final long serialVersionUID = 1L;
	
	private Person person;
	
	private PersonManager personManager;
	
	public Person getPerson() {
		person = super.model;
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
		super.model = this.person;
	}

	public void setPersonManager( PersonManager manager ){
		super.manager = manager;
		this.personManager = manager;
	}
	
    /**
    * save
    * @return
    */
    public String save( ){    	
        if( model == null ){
            return INPUT;
        }
        
        boolean isInsert = model.getIdentity()==null;
        
        Access access = new Access(  );
        access.init(request);
        //access.setOperator( SecurityUtil.getPrincipal() );
        
        try{
        	personManager.save(person,access);
        }catch( Exception e ){ //other exception
        	//GenerationType.SEQUENCE 未插入成功,Oracle 也会生成ID
        	if( isInsert )
        		person.setId(null);
        	
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
         if( person == null || person.getIdentity() == null ){
             return INPUT;
         }
        
         Access access = new Access(  );
         access.init(request);
         
         try{
        	 personManager.remove( person.getId(),access );
         	saveSuccessMessage( "成功删除数据(id=" + person.getIdentity() + ")！" );
         }catch( Exception e ){ //other exception
             saveErrorMessage( "删除发生异常：" + e.getMessage() );
             return INPUT;
         } 
        
         return SUCCESS;
     }
	
}
