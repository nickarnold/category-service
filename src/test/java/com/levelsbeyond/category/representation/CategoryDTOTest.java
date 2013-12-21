package com.levelsbeyond.category.representation;

import static com.yammer.dropwizard.testing.JsonHelpers.*;
import static org.fest.assertions.api.Assertions.*;

import com.levelsbeyond.category.api.CategoryDTO;

import org.junit.Test;

public class CategoryDTOTest {
	final CategoryDTO category = new CategoryDTO(1, "Fixture Category");

	@Test
	public void serializesToJSON() throws Exception {
		assertThat(asJson(category)).isEqualTo(jsonFixture("fixtures/category.json"));
	}

	@Test
	public void deserializesFromJSON() throws Exception {
		assertThat(fromJson(jsonFixture("fixtures/category.json"), CategoryDTO.class)).isEqualTo(category);
	}

}
