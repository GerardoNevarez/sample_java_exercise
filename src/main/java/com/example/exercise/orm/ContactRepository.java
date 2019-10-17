package com.example.exercise.orm;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.exercise.model.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

}
