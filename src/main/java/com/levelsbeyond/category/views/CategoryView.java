package com.levelsbeyond.category.views;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.levelsbeyond.category.api.CategoryDTO;
import com.yammer.dropwizard.views.View;

import lombok.Getter;

@JsonAutoDetect
public class CategoryView extends View {
	@Getter
	private CategoryDTO category;
	
	public CategoryView(CategoryDTO category) {
		super("category.mustache");
		this.category = category;
	}
}
