package com.levelsbeyond.category.application;

import static com.levelsbeyond.category.util.MapUtils.*;

import com.jayway.jaxrs.hateoas.HateoasOption;
import com.jayway.jaxrs.hateoas.HateoasVerbosity;
import com.jayway.jaxrs.hateoas.core.jersey.JerseyHateoasApplication;
import com.sun.jersey.api.core.PackagesResourceConfig;

import org.fest.util.Arrays;

public class CategoryServiceApplication extends JerseyHateoasApplication {
	public CategoryServiceApplication() {
		super(new HateoasVerbosity(
				HateoasOption.REL, HateoasOption.HREF, HateoasOption.TYPE,
				HateoasOption.METHOD, HateoasOption.CONSUMES),

				hashMap(String.class, Object.class)
						.put(PackagesResourceConfig.PROPERTY_PACKAGES, Arrays.array("com.levelsbeyond.category.resources"))
						.build());
		;

	}
}
