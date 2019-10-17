package com.example.exercise.rest.controller;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.exercise.model.Contact;
import com.example.exercise.rest.exception.ResourceModificationErrorException;
import com.example.exercise.rest.exception.ResourceNotFoundException;
import com.example.exercise.service.ContactsService;

@RestController
@RequestMapping("contacts")
public class ContactsController {
  
  final static Logger logger = LoggerFactory.getLogger(ContactsController.class);

  @Autowired
  ContactsService contactsService;

  @GetMapping(path = "", produces = "application/json")
  public List<Contact> getAllContacts() {
    List<Contact> result = contactsService.getAllContacts();
    return result;
  }

  @PostMapping(path = "", produces = "application/json")
  public ResponseEntity<Void>  addContact(@RequestBody Contact newContact) {
    
    try {
      
      Contact savedContact = contactsService.addContact(newContact);
      
      if (savedContact == null) {
        return ResponseEntity.badRequest().build();
      }
      
      URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
          "/{id}").buildAndExpand(savedContact.getId()).toUri();

      return ResponseEntity.created(location).build();
  
    } catch(Exception e){
      logger.error("Unable to save new contact",e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @RequestMapping(method = RequestMethod.PUT, path = "/{id}", produces = "application/json")
  public void updateContact(@PathVariable("id") int id, @RequestBody Contact existingContact) {
    
    try {
      
      contactsService.updateContact(id, existingContact);
      
    } catch (IllegalArgumentException e) {
      notFound(id, e);
      
    } catch (RuntimeException e ) {
      logger.error("Unable to update contact");
      throw new ResourceModificationErrorException("Unable to update contact");
    }

  }

  @GetMapping(path = "/{id}", produces = "application/json")
  public Contact getContact(@PathVariable("id") int id) {
    Contact contact = null;
    
    try {
      contact = contactsService.getContact(id);
      
    }catch (IllegalArgumentException e) {
      notFound(id, e);
      
    } catch (RuntimeException e ) {
      throw new ResourceModificationErrorException();
    }
    
    return contact;
  }

  @DeleteMapping(path = "/{id}", produces = "application/json")
  public void deleteContact(@PathVariable("id") int id) {
    try {
      
      contactsService.deleteContact(id);
      
    } catch (IllegalArgumentException e) {
      notFound(id, e);
      
    } catch (Exception e ) {
      throw new ResourceModificationErrorException();
    }
  }

  private void notFound(int id, Exception e) {
    String message = String.format("Contact ID {%s} not found", id);
    logger.error(message, e);
    throw new ResourceNotFoundException(message);
  }

}
