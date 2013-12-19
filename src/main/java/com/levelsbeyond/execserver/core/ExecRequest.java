package com.levelsbeyond.execserver.core;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.NonNull;

@Entity
@Table(name="exec_request")
@Data
public class ExecRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="command", nullable=false)
    @NonNull 
    private String command;

    @Column(name="exec_path", nullable=false)
    @NonNull 
    private String execPath;

    @Column(name="requestor")
    private String requestor;
        
    @Column(name="params")
    private String params;
    
    @Column(name="result_code")
    private Integer resultCode;
    
    @Column(name="std_out")
    private String stdOut;

    @Column(name="std_err")
    private String stdErr;

    @Column(name="date_created", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created = new Date();
    
    @Column(name="start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Column(name="end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    
    // for unmarshalling
    public ExecRequest() {}
}
