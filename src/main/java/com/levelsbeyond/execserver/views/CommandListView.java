package com.levelsbeyond.execserver.views;

import java.util.List;

import lombok.Getter;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.levelsbeyond.execserver.core.Command;
import com.yammer.dropwizard.views.View;

@JsonAutoDetect
public class CommandListView extends View {
	@Getter
	private List<Command> commands;
	
	public CommandListView(List<Command> commands) {
		super("command_list.mustache");
		this.commands = commands;
	}
}
