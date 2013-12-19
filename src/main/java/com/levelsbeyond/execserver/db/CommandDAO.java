package com.levelsbeyond.execserver.db;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.google.common.base.Optional;
import com.levelsbeyond.execserver.core.Command;
import com.yammer.dropwizard.hibernate.AbstractDAO;

public class CommandDAO extends AbstractDAO<Command> {
    public CommandDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Command> findById(int id) {
        return Optional.fromNullable(get(id));
    }

    public Optional<Command> findByName(String name) {
    	Command cmd = uniqueResult(currentSession()    						
    						.createCriteria(Command.class)
    							.add(Restrictions.eq("name", name))
    	    			);
    	
    	return Optional.fromNullable(cmd);
    }

    public List<Command> findAll() {
        return list(namedQuery("com.levelsbeyond.execserver.core.Command.findAll"));
    }
    
    public Command save(Command cmd) {
    	return persist(cmd);
    }
}