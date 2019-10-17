package com.example.exercise.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.CascadeType;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.RequiredArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "CONTACTS")
public class Contact {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonSetter(nulls = Nulls.SKIP)
  @JsonProperty(required = false)
  private Integer id;

  @OneToOne(fetch = FetchType.EAGER, optional = true, orphanRemoval = true, cascade = CascadeType.ALL)
  @JoinColumn(name = "NAME_ID", nullable = true)
  @NonNull
  private Name name;

  @OneToOne(fetch = FetchType.EAGER, optional = true, orphanRemoval = true, cascade = CascadeType.ALL)
  @JoinColumn(name = "ADDRESS_ID", nullable = true)
  @NonNull
  private Address address;

  @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
  @JoinTable(name = "CONTACT_PHONES",
              joinColumns = @JoinColumn(name = "CONTACT_ID", referencedColumnName = "ID"),
              inverseJoinColumns = @JoinColumn(name = "PHONE_ID", referencedColumnName = "ID"))
  @NonNull
  private List<Phone> phone;

  @Size(max = 255)
  @NonNull
  private String email;
}
