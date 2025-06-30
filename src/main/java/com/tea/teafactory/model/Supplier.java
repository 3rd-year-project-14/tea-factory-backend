package com.tea.teafactory.model;

import jakarta.persistence.*;

@Entity
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String no;
    private String name;
    private String weight;

    // getters + setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNo() { return no; }
    public void setNo(String no) { this.no = no; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getWeight() { return weight; }
    public void setWeight(String weight) { this.weight = weight; }
}
