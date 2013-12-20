package com.levelsbeyond.category;

import com.levelsbeyond.category.application.CategoryServiceApplication;
import com.levelsbeyond.category.auth.SimpleAuthenticator;
import com.levelsbeyond.category.core.Authentication;
import com.levelsbeyond.category.core.Category;
import com.levelsbeyond.category.db.CategoryDAO;
import com.levelsbeyond.category.resources.CategoryResource;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.auth.basic.BasicAuthProvider;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.db.DatabaseConfiguration;
import com.yammer.dropwizard.hibernate.HibernateBundle;
import com.yammer.dropwizard.migrations.MigrationsBundle;
import com.yammer.dropwizard.views.ViewBundle;

public class CategoryService extends Service<CategoryServiceConfiguration> {
    public static void main(String[] args) throws Exception {
        new CategoryService().run(args);
    }

    private final HibernateBundle<CategoryServiceConfiguration> hibernateBundle =
            new HibernateBundle<CategoryServiceConfiguration>(
					Category.class) {
    	
                @Override
                public DatabaseConfiguration getDatabaseConfiguration(CategoryServiceConfiguration configuration) {
                    return configuration.getDatabaseConfiguration();
                }
            };

    @Override
    public void initialize(Bootstrap<CategoryServiceConfiguration> bootstrap) {
		bootstrap.setName("category-service");
		bootstrap.addBundle(new AssetsBundle());
        bootstrap.addBundle(new MigrationsBundle<CategoryServiceConfiguration>() {
            @Override
            public DatabaseConfiguration getDatabaseConfiguration(CategoryServiceConfiguration configuration) {
                return configuration.getDatabaseConfiguration();
            }
        });
        bootstrap.addBundle(hibernateBundle);
        bootstrap.addBundle(new ViewBundle());
    }

//    void initializeAtmosphere(ExecServerConfiguration configuration, Environment environment) {
//        FilterBuilder fconfig = environment.addFilter(CrossOriginFilter.class, "/chat");
//        fconfig.setInitParam(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
//
//        AtmosphereServlet atmosphereServlet = new AtmosphereServlet();
//        atmosphereServlet.framework().addInitParameter("com.sun.jersey.config.property.packages", "com.example.helloworld.resources.atmosphere");
//        atmosphereServlet.framework().addInitParameter("org.atmosphere.websocket.messageContentType", "application/json");
//        environment.addServlet(atmosphereServlet, "/chat/*");
//    }

    @Override
    public void run(CategoryServiceConfiguration configuration,
                    Environment environment) throws ClassNotFoundException {

        final CategoryDAO commandDAO = new CategoryDAO(hibernateBundle.getSessionFactory());

		environment.addProvider(new BasicAuthProvider<Authentication>(new SimpleAuthenticator(configuration.getAdminPassword()), "Category Service"));

        //final Template template = configuration.buildTemplate();

        //environment.addHealthCheck(new TemplateHealthCheck(template));
        environment.addResource(new CategoryResource(commandDAO));

		environment.getJerseyResourceConfig().add(new CategoryServiceApplication());

        //initializeAtmosphere(configuration, environment);
    }
    
}
