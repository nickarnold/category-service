package com.levelsbeyond.execserver;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.db.DatabaseConfiguration;

public class ExecServerConfiguration extends Configuration {
    @NotEmpty
    @Getter
    @Setter
    private String reachEngineHost;
    
    @NotEmpty
    @Getter
    @Setter
    private String adminPassword;
    
    @Valid
    @NotNull
    @JsonProperty("database")
    private DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration();

    public DatabaseConfiguration getDatabaseConfiguration() {
        return databaseConfiguration;
    }

    public void setDatabaseConfiguration(DatabaseConfiguration databaseConfiguration) {
        this.databaseConfiguration = databaseConfiguration;
    }
}
