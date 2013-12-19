package com.levelsbeyond.execserver;

import com.levelsbeyond.execserver.auth.SimpleAuthenticator;
import com.levelsbeyond.execserver.core.Authentication;
import com.levelsbeyond.execserver.core.Command;
import com.levelsbeyond.execserver.core.ExecRequest;
import com.levelsbeyond.execserver.db.CommandDAO;
import com.levelsbeyond.execserver.db.ExecRequestDAO;
import com.levelsbeyond.execserver.resources.CommandResource;
import com.levelsbeyond.execserver.resources.ExecRequestResource;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.auth.basic.BasicAuthProvider;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.db.DatabaseConfiguration;
import com.yammer.dropwizard.hibernate.HibernateBundle;
import com.yammer.dropwizard.migrations.MigrationsBundle;
import com.yammer.dropwizard.views.ViewBundle;

public class ExecServerService extends Service<ExecServerConfiguration> {
    public static void main(String[] args) throws Exception {
        new ExecServerService().run(args);
    }

    private final HibernateBundle<ExecServerConfiguration> hibernateBundle =
            new HibernateBundle<ExecServerConfiguration>(
            		Command.class,
            		ExecRequest.class) {
    	
                @Override
                public DatabaseConfiguration getDatabaseConfiguration(ExecServerConfiguration configuration) {
                    return configuration.getDatabaseConfiguration();
                }
            };

    @Override
    public void initialize(Bootstrap<ExecServerConfiguration> bootstrap) {
        bootstrap.setName("exec-server");
        bootstrap.addBundle(new AssetsBundle());
        bootstrap.addBundle(new MigrationsBundle<ExecServerConfiguration>() {
            @Override
            public DatabaseConfiguration getDatabaseConfiguration(ExecServerConfiguration configuration) {
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
    public void run(ExecServerConfiguration configuration,
                    Environment environment) throws ClassNotFoundException {

        final CommandDAO commandDAO = new CommandDAO(hibernateBundle.getSessionFactory());
        final ExecRequestDAO execRequestDAO = new ExecRequestDAO(hibernateBundle.getSessionFactory());

        environment.addProvider(new BasicAuthProvider<Authentication>(new SimpleAuthenticator(configuration.getAdminPassword()), "ExecRequest Server"));

        //final Template template = configuration.buildTemplate();

        //environment.addHealthCheck(new TemplateHealthCheck(template));
        environment.addResource(new CommandResource(commandDAO));
        environment.addResource(new ExecRequestResource(execRequestDAO, commandDAO));

        //initializeAtmosphere(configuration, environment);
    }
    
}
