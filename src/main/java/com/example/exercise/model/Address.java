package com.example.exercise.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "addresses")
public class Address {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  private Integer id;
  
  @Size(max = 255)
  @NonNull
  private String street;
  
  @Size(max = 255)
  @NonNull
  private String city;
  
  @Size(max = 100)
  @NonNull
  private String state;
  
  @Size(max = 50)
  @NonNull
  private String zip;
}
