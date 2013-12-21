package com.levelsbeyond.category.resource;

import static org.fest.assertions.api.Assertions.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import com.google.common.base.Optional;
import com.levelsbeyond.category.core.Category;
import com.levelsbeyond.category.db.CategoryDAO;
import com.levelsbeyond.category.resources.CategoryResource;
import com.yammer.dropwizard.testing.ResourceTest;

import org.junit.Test;

public class CategoryResourceTest extends ResourceTest {
	private Category category = new Category(1, "Fixture Name");
	private final CategoryDAO dao = mock(CategoryDAO.class);

	@Override
	protected void setUpResources() {
		when(dao.findById(anyInt())).thenReturn(Optional.of(category));
		addResource(new CategoryResource(dao));
	}

	@Test
	public void simpleResourceTest() throws Exception {
		assertThat(client().resource("/category/1").get(Category.class))
				.isEqualTo(category);

		verify(dao).findById(1);
	}

}
