package com.levelsbeyond.category.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.levelsbeyond.category.core.Category;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

@JsonAutoDetect
@Data
public class CategoryDTO {
	private Integer id;
	@NotEmpty
	private String name;

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
