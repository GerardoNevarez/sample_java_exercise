package com.example.exercise.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "phones")
public class Phone {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  private Integer id;
  
  @Size(max = 50)
  @NonNull
  private String number;
  
  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  @NonNull
  private PhoneType type;
 
  }
