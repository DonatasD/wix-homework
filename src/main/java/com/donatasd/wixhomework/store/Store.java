package com.donatasd.wixhomework.store;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Store {

    @Id
    private String id;

    private String title;
    private String content;
    private Integer views;
    private Integer timestamp;
}
