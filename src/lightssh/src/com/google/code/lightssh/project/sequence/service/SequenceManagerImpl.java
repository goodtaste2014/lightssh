package com.google.code.lightssh.project.sequence.service;

import com.google.code.lightssh.common.ApplicationException;
import com.google.code.lightssh.common.dao.Dao;
import com.google.code.lightssh.common.model.Sequenceable;
import com.google.code.lightssh.project.sequence.entity.Sequence;

/**
 * Sequence Manager implements
 * @author YangXiaojin
 *
 */
public class SequenceManagerImpl implements SequenceManager{
	
	private Dao<Sequence> dao;

	public void setDao(Dao<Sequence> dao) {
		this.dao = dao;
	}
	
	public Sequence nextSequence(String key,int step ){
		if( key == null )
			throw new ApplicationException("获取序列的参数KEY为空！");
		
		boolean inserted = false;
		Sequence seq = dao.read( key );
		if( seq == null ){
			seq = new Sequence( key );
			inserted =true;
		}
		seq.incLastNumber( step );
		
		if( inserted )
			dao.create( seq );
		else
			dao.update( seq );
		
		return seq;
	}

	@Override
	public Sequence nextSequence(Sequenceable sa) {
		if( sa == null )
			throw new ApplicationException("获取序列的参数为空！");
		
		return nextSequence( sa.getSequenceKey(),sa.getSequenceStep() );
	}

	@Override
	public Sequence nextSequence(String key) {
		return nextSequence(key,1);
	}

	@Override
	public String nextSequenceNumber(Sequenceable sa) {
		return sa.getSequenceKey() + nextSerialNumber( sa );
	}
	
	public String nextSerialNumber(Sequenceable sa) {
		Sequence seq = nextSequence( sa );
		if( seq == null )
			throw new ApplicationException("获取得的序列为空！");
		
		return seq.getFormatLastNumber( sa.getSequenceLength() );
	}
	
}
