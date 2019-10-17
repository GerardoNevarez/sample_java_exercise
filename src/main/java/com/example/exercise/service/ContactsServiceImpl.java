package com.example.exercise.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.exercise.model.Contact;
import com.example.exercise.orm.ContactRepository;

@Component
public class ContactsServiceImpl implements ContactsService {

  @Autowired
  ContactRepository contactRepository;

  @Override
  public List<Contact> getAllContacts() {
    return contactRepository.findAll();
  }

  @Override
  public Contact addContact(Contact newContact) {
    try {
      
      Contact savedContact = contactRepository.saveAndFlush(newContact);
      return savedContact;
      
    } catch(Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void updateContact(int id, Contact existingContact) {
    Optional<Contact> result = contactRepository.findById(id);

    if (result.isPresent()) {
      //TODO merge content
      try {
        existingContact.setId(id);
        contactRepository.save(existingContact);
      } catch(Exception e) {
        throw new RuntimeException(e);
      }
    } else {
      throw new IllegalArgumentException();
    }

  }

  @Override
  public Contact getContact(int id) {
    
    Optional<Contact> result = contactRepository.findById(id);

    if (!result.isPresent()) {
      throw new IllegalArgumentException();
    }
    
    return result.get();
  }

  @Override
  public void deleteContact(int id) {

    if (contactRepository.existsById(id)) {
      try {
        contactRepository.deleteById(id);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    } else {
      throw new IllegalArgumentException();
    }
  }

}
