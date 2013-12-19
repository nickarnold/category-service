package com.levelsbeyond.execserver.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.common.base.Optional;
import com.levelsbeyond.execserver.core.Authentication;
import com.levelsbeyond.execserver.core.Command;
import com.levelsbeyond.execserver.core.ExecRequest;
import com.levelsbeyond.execserver.db.CommandDAO;
import com.levelsbeyond.execserver.db.ExecRequestDAO;
import com.levelsbeyond.execserver.views.ExecRequestListView;
import com.levelsbeyond.execserver.views.ExecRequestView;
import com.sun.jersey.api.NotFoundException;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import com.yammer.dropwizard.jersey.params.IntParam;

@Path("/exec_requests")
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
public class ExecRequestResource {

    private final ExecRequestDAO execRequestDAO;
    private final CommandDAO commandDAO;

    public ExecRequestResource(ExecRequestDAO execRequestDAO, CommandDAO commandDAO) {
        this.execRequestDAO = execRequestDAO;
        this.commandDAO = commandDAO;
    }

    @POST
    @UnitOfWork
    public ExecRequestView createExecRequest(@Auth Authentication user, ExecRequest execRequest) {
        return new ExecRequestView(execRequestDAO.save(execRequest), commandDAO.findAll());
    }

    @GET
    @UnitOfWork
    public ExecRequestListView listExecRequests(@Auth Authentication user, 
    											@QueryParam("fetchIndex") @DefaultValue("0") IntParam fetchIndex,
    											@QueryParam("fetchLimit") @DefaultValue("25") IntParam fetchLimit) {
        return new ExecRequestListView(execRequestDAO.findAll(fetchIndex.get(), fetchLimit.get()), fetchIndex.get(), fetchLimit.get());
    }
    
    @GET
    @UnitOfWork
    @Path("/{id}")
    public ExecRequestView viewExecRequest(@Auth Authentication user, @PathParam("id") Integer id) {
    	Optional<ExecRequest> cmd = execRequestDAO.findById(id);
    	
    	if (cmd.isPresent()) {
    		return new ExecRequestView(cmd.get(), commandDAO.findAll());
    	}
    	else {
    		throw new NotFoundException();
    	}
    }

    @GET
    @UnitOfWork
    @Path("/new")
    public ExecRequestView newExecRequest(@Auth Authentication user) {
   		return new ExecRequestView(new ExecRequest(), commandDAO.findAll());
    }
    
    @POST
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public ExecRequestListView createExecRequest(@Auth Authentication user, 
    										 @FormParam("commandId") IntParam commandId,
    										 @FormParam("command") String command,
    										 @FormParam("params") String params) {
    	
    	Optional<Command> cmd;
    	if (commandId != null) {
    		 cmd = commandDAO.findById(commandId.get());
    	}
    	else if (command != null) {
    		cmd = commandDAO.findByName(command);
    	}
    	else {
    		cmd = Optional.absent();
    	}
    	
    	if (cmd.isPresent()) {
    		Command c = cmd.get();
    		ExecRequest request = new ExecRequest();
    		request.setCommand(c.getName());
    		request.setExecPath(c.getExecPath());
    		request.setRequestor("web");
    		request.setParams(params);
    		
    		request = execRequestDAO.save(request);
    		
    		return listExecRequests(user, null, null);
    	}
    	else {
    		throw new NotFoundException("Invalid command.");
    	}
    }

}
