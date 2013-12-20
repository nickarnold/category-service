package com.levelsbeyond.category.resources;

import static com.jayway.jaxrs.hateoas.support.FieldPath.*;

import java.net.URI;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.common.base.Optional;
import com.jayway.jaxrs.hateoas.Linkable;
import com.jayway.jaxrs.hateoas.core.HateoasResponse;
import com.levelsbeyond.category.api.CategoryDTO;
import com.levelsbeyond.category.api.CategoryList;
import com.levelsbeyond.category.core.Authentication;
import com.levelsbeyond.category.core.Category;
import com.levelsbeyond.category.db.CategoryDAO;
import com.levelsbeyond.category.views.CategoryListView;
import com.levelsbeyond.category.views.CategoryView;
import com.sun.jersey.api.NotFoundException;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.dropwizard.hibernate.UnitOfWork;

@Path("/categories")
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
public class CategoryResource {

	private final CategoryDAO categoryDAO;

	public CategoryResource(CategoryDAO categoryDAO) {
		this.categoryDAO = categoryDAO;
    }

	/*
	 * ACCESSORS
	 */

	@GET
	@Produces(MediaType.TEXT_HTML)
	@UnitOfWork
	public Response listCategories(@Auth Authentication user) {
		return Response.ok(new CategoryListView(categoryDAO.findAll())).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Linkable(CategoryLinks.CATEGORIES)
	@UnitOfWork
	public Response listCategoriesJson(@Auth Authentication user) {
		return HateoasResponse.ok(new CategoryList(categoryDAO.findAll()))
				.link(path("categories"), CategoryLinks.CATEGORY, "self", "id")
				.link(CategoryLinks.ADD_CATEGORY, "create")
				.selfLink(CategoryLinks.CATEGORIES)
				.build();
	}

	@GET
	@Path("/{id}")
	@UnitOfWork
	@Produces(MediaType.TEXT_HTML)
	public CategoryView viewCategory(@Auth Authentication user, @PathParam("id") Integer id) {
		Optional<Category> cmd = categoryDAO.findById(id);

		if (cmd.isPresent()) {
			return new CategoryView(new CategoryDTO(cmd.get()));
		}
		else {
			throw new NotFoundException();
		}
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Linkable(CategoryLinks.CATEGORY)
	@UnitOfWork
	public Response viewCategoryJson(@Auth Authentication user, @PathParam("id") Integer id) {
		Optional<Category> cmd = categoryDAO.findById(id);

		if (cmd.isPresent()) {
			return HateoasResponse.ok(new CategoryDTO(cmd.get()))
					.selfLink(CategoryLinks.CATEGORY, cmd.get().getId())
					.build();
		}
		else {
			return HateoasResponse.status(Status.NOT_FOUND).build();
		}
	}

	/*
	 * CREATORS
	 */

    @POST
	@Produces(MediaType.TEXT_HTML)
	@UnitOfWork
	public CategoryView createCategory(@Auth Authentication user, @Valid CategoryDTO category) {
		return new CategoryView(
				new CategoryDTO(
						categoryDAO.save(category.toCategory()
								)
				));
	}

	@POST
	@Linkable(CategoryLinks.ADD_CATEGORY)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
	public Response createCategoryJson(@Auth Authentication user, @Valid CategoryDTO category) {
		try {
			CategoryDTO dto = new CategoryDTO(categoryDAO.save(category.toCategory()));
			return HateoasResponse
					.created(CategoryLinks.CATEGORY, dto.getId())
					.build();
		}
		catch (Exception ex) {
			return HateoasResponse.serverError().entity(ex.getMessage()).build();
		}
    }

	@GET
	@UnitOfWork
	@Path("/new")
	public CategoryView newCategory(@Auth Authentication user) {
		return new CategoryView(new CategoryDTO());
	}

	/*
	 * UPDATERS
	 */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @UnitOfWork
    public Response save(@Auth Authentication user, 
									 @FormParam("id") Integer id,
			@Valid @FormParam("name") String name) {
    	
    	Optional<Category> cmd;
    	if (id != null) {
			cmd = categoryDAO.findById(id);
    	}
    	else {
    		cmd = Optional.of(new Category());
    	}
    	
    	if (cmd.isPresent()) {
    		Category c = cmd.get();
    		c.setName(name);
    		
			c = categoryDAO.save(c);
    		
			return Response.seeOther(URI.create("/categories")).build();

    	}
    	else {
    		throw new NotFoundException();
    	}
    }

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@UnitOfWork
	public Response saveJson(@Auth Authentication user, @PathParam("id") Integer id, CategoryDTO categoryDto) {

		Category category = categoryDto.toCategory();
		category = categoryDAO.save(category);

		return Response.ok().build();
	}




}
