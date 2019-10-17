package com.example.exercise.service;

import java.util.List;

import com.example.exercise.model.Contact;

public interface ContactsService {

  public List<Contact> getAllContacts();

  public Contact addContact(Contact newContact);

  public void updateContact(int id, Contact existingContact);
   
  public Contact getContact(int id);
  
  public void deleteContact(int id);
}
