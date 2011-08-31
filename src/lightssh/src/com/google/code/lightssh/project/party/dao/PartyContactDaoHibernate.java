package com.google.code.lightssh.project.party.dao;

import java.util.Collection;

import com.google.code.lightssh.common.dao.hibernate.HibernateDao;
import com.google.code.lightssh.project.contact.entity.ContactMechanism;
import com.google.code.lightssh.project.contact.entity.ContactMechanism.ContactMechanismType;
import com.google.code.lightssh.project.party.entity.Party;
import com.google.code.lightssh.project.party.entity.PartyContact;

/**
 * 
 * @author YangXiaojin
 *
 */
public class PartyContactDaoHibernate extends HibernateDao<PartyContact> implements PartyContactDao{

	@Override
	public void remove(ContactMechanism contact) {
		String hql = " DELETE FROM " + entityClass.getName() 
			+ " AS m WHERE m.contact = ? "; 
		
		getHibernateTemplate().delete(hql,contact );
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<ContactMechanism> list(Party party,
			ContactMechanismType... types ) {
		StringBuffer hql = new StringBuffer( 
				" SELECT m.contact FROM " + entityClass.getName()
			+ " AS m WHERE m.party = ? " );
		
		if( types != null ){
			if( types.length == 1 ){
				hql.append(" AND m.contact.type = '"+types[0].name()+"' ");
			}else
				for( int i=0;i<types.length;i++ ){
					if( i== 0 )
						hql.append(" AND m.contact.type in ( ");
					hql.append( ((i==0)?"":",") + "'"+ types[i].name() +"'" );
					if( i==types.length-1 )
						hql.append(" )");
				}//end for
		}
		
		return getHibernateTemplate().find(hql.toString(),party);
	}

}
