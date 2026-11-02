package com.kokoro.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "counsellors")
@Getter @Setter
public class Counsellor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String specialization;
    private String clinic;
    private String area;
    private String city;
    private String state;
    private double latitude;
    private double longitude;
    private double rating;
    private int experienceYears;
    private int fee;
    private String phone;
    private boolean online;
}
