package com.levelsbeyond.execserver.resources;

import java.net.URI;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.BooleanUtils;

import com.google.common.base.Optional;
import com.levelsbeyond.execserver.core.Authentication;
import com.levelsbeyond.execserver.core.Command;
import com.levelsbeyond.execserver.db.CommandDAO;
import com.levelsbeyond.execserver.views.CommandListView;
import com.levelsbeyond.execserver.views.CommandView;
import com.sun.jersey.api.NotFoundException;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.dropwizard.hibernate.UnitOfWork;

@Path("/commands")
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
public class CommandResource {

    private final CommandDAO commandDAO;

    public CommandResource(CommandDAO commandDAO) {
        this.commandDAO = commandDAO;
    }

    @POST
    @UnitOfWork
    public CommandView createCommand(@Auth Authentication user, Command command) {
        return new CommandView(commandDAO.save(command));
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @UnitOfWork
    public Response save(@Auth Authentication user, 
									 @FormParam("id") Integer id,
									 @Valid @FormParam("name") String name,
									 @FormParam("execPath") String execPath,
									 @FormParam("enabled") Boolean enabled) {
    	
    	Optional<Command> cmd;
    	if (id != null) {
    		cmd = commandDAO.findById(id);
    	}
    	else {
    		cmd = Optional.of(new Command());
    	}
    	
    	if (cmd.isPresent()) {
    		Command c = cmd.get();
    		c.setName(name);
    		c.setExecPath(execPath);
    		c.setEnabled(BooleanUtils.isNotFalse(enabled));
    		
    		c = commandDAO.save(c);
    		
    		return Response.seeOther(URI.create("/commands")).build(); 

    	}
    	else {
    		throw new NotFoundException();
    	}
    }

    @GET
    @UnitOfWork
    public CommandListView listCommands(@Auth Authentication user) {
        return new CommandListView(commandDAO.findAll());
    }
    
    @GET
    @UnitOfWork
    @Path("/{id}")
    public CommandView viewCommand(@Auth Authentication user, @PathParam("id") Integer id) {
    	Optional<Command> cmd = commandDAO.findById(id);
    	
    	if (cmd.isPresent()) {
    		return new CommandView(cmd.get());
    	}
    	else {
    		throw new NotFoundException();
    	}
    }

    @GET
    @UnitOfWork
    @Path("/new")
    public CommandView newCommand(@Auth Authentication user) {
   		return new CommandView(new Command());
    }

}
