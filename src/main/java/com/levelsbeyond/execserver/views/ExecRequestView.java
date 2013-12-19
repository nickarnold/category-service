package com.levelsbeyond.execserver.views;

import java.util.List;

import lombok.Getter;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.levelsbeyond.execserver.core.Command;
import com.levelsbeyond.execserver.core.ExecRequest;
import com.yammer.dropwizard.views.View;

@JsonAutoDetect
public class ExecRequestView extends View {
	@Getter
	private ExecRequest execRequest;
	
	@Getter
	@JsonIgnore
	private List<Command> commands;
	
	public ExecRequestView(ExecRequest execRequest, List<Command> commands) {
		super("exec_request.mustache");
		this.execRequest = execRequest;
		this.commands = commands;
	}
}
