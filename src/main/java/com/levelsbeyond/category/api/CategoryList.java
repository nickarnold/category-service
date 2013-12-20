package com.levelsbeyond.category.api;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.levelsbeyond.category.core.Category;

import lombok.Data;

@JsonAutoDetect
@Data
public class CategoryList {
	private Collection<CategoryDTO> categories;

	public CategoryList() {
	}

	public CategoryList(Collection<Category> categories) {
		this.categories = ImmutableList.copyOf(
				Iterables.transform(categories, new Function<Category, CategoryDTO>() {
					@Override
					public CategoryDTO apply(Category input) {
						return new CategoryDTO(input);
					}
				})
				);
	}

}
