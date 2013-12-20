package com.levelsbeyond.category.core;

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
@Table(name = "category")
@Data
@NamedQueries({
    @NamedQuery(
				name = "com.levelsbeyond.category.core.Category.findAll",
				query = "FROM Category c"
    ),
})
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name", nullable=false)
    @NonNull 
    private String name;

    // for unmarshalling
    public Category() {}
}
