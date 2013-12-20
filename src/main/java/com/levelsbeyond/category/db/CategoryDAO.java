package com.levelsbeyond.category.db;

import java.util.List;

import com.google.common.base.Optional;
import com.levelsbeyond.category.core.Category;
import com.yammer.dropwizard.hibernate.AbstractDAO;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class CategoryDAO extends AbstractDAO<Category> {
    public CategoryDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Category> findById(int id) {
        return Optional.fromNullable(get(id));
    }

    public Optional<Category> findByName(String name) {
    	Category cmd = uniqueResult(currentSession()    						
    						.createCriteria(Category.class)
    							.add(Restrictions.eq("name", name))
    	    			);
    	
    	return Optional.fromNullable(cmd);
    }

    public List<Category> findAll() {
		return list(namedQuery("com.levelsbeyond.category.core.Category.findAll"));
    }
    
    public Category save(Category cmd) {
    	return persist(cmd);
    }
}