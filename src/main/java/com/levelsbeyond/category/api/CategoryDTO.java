package com.levelsbeyond.category.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.levelsbeyond.category.core.Category;

import lombok.AllArgsConstructor;
import lombok.Data;

@JsonAutoDetect
@Data
@AllArgsConstructor
public class CategoryDTO {
	private Integer id;
	public String name;

	public CategoryDTO() {
	}

	public CategoryDTO(Category category) {
		this.id = category.getId();
		this.name = category.getName();
	}

	public Category toCategory() {
		Category c = new Category();
		c.setId(id);
		c.setName(name);

		return c;
	}

}
