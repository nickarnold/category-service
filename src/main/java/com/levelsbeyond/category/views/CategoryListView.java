package com.levelsbeyond.category.views;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.levelsbeyond.category.core.Category;
import com.yammer.dropwizard.views.View;

import lombok.Getter;

@JsonAutoDetect
public class CategoryListView extends View {
	@Getter
	private List<Category> commands;
	
	public CategoryListView(List<Category> commands) {
		super("category_list.mustache");
		this.commands = commands;
	}
}
