package com.levelsbeyond.execserver.views;

import lombok.Getter;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.levelsbeyond.execserver.core.Command;
import com.yammer.dropwizard.views.View;

@JsonAutoDetect
public class CommandView extends View {
	@Getter
	private Command command;
	
	public CommandView(Command command) {
		super("command.mustache");
		this.command = command;
	}
}
