package com.levelsbeyond.execserver.db;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.google.common.base.Optional;
import com.levelsbeyond.execserver.core.ExecRequest;
import com.yammer.dropwizard.hibernate.AbstractDAO;

public class ExecRequestDAO extends AbstractDAO<ExecRequest> {
    public ExecRequestDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<ExecRequest> findById(int id) {
        return Optional.fromNullable(get(id));
    }

    public List<ExecRequest> findByCommandName(String commandName, int fetchIndex, int fetchLimit) {
    	return list(currentSession()    						
    				.createCriteria(ExecRequest.class)
    				.add(Restrictions.eq("command", commandName))
    				.setFirstResult(fetchIndex)
    				.setFetchSize(fetchLimit)
    				.addOrder(Order.desc("created"))
    			);
    }

    public List<ExecRequest> findAll(int fetchIndex, int fetchLimit) {
    	return list(currentSession()    						
				.createCriteria(ExecRequest.class)
				.setFirstResult(fetchIndex)
				.setFetchSize(fetchLimit)
				.addOrder(Order.desc("created"))
			);
    }
    
    public ExecRequest save(ExecRequest request) {
    	return persist(request);
    }
}