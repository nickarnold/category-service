package com.levelsbeyond.execserver.views;

import java.util.List;

import lombok.Getter;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.google.common.base.Function;
import com.levelsbeyond.execserver.core.ExecRequest;
import com.yammer.dropwizard.views.View;

@JsonAutoDetect
public class ExecRequestListView extends View {
	@Getter
	private List<ExecRequest> execRequests;
	
	@Getter
	private int fetchIndex;
	
	@Getter
	private int fetchLimit;
	
	public Function<String, String> formatDateTime() {
		return ViewUtils.formatDateTime();
	}
	
	public ExecRequestListView(List<ExecRequest> execRequests, int fetchIndex, int fetchLimit) {
		super("exec_request_list.mustache");
		this.execRequests = execRequests;
		this.fetchIndex = fetchIndex;
		this.fetchLimit = fetchLimit;
	}
}
