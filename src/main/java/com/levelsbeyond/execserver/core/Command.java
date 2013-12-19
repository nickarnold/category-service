package com.levelsbeyond.execserver.core;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;
import lombok.NonNull;

@Entity
@Table(name="command")
@Data
@NamedQueries({
    @NamedQuery(
        name = "com.levelsbeyond.execserver.core.Command.findAll",
        query = "FROM Command c"
    ),
})
public class Command {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name", nullable=false)
    @NonNull 
    private String name;
    
    @Column(name="exec_path", nullable=false)
    @NonNull private String execPath;

    @Column(name="date_created", nullable=false)
    private Date created = new Date();
    
    @Column(name="enabled")
    private Boolean enabled = true;
    
    // for unmarshalling
    public Command() {}
}
