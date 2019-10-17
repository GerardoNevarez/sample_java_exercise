package com.example.exercise.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.exercise.DataUtil;
import com.example.exercise.model.Contact;
import com.example.exercise.orm.ContactRepository;

@RunWith(SpringRunner.class)
public class ContactsServiceImplTest {

  @TestConfiguration
  static class Configuration {

    @Bean
    public ContactsService contactsService() {
      return new ContactsServiceImpl();
    }
  }

  @Autowired
  ContactsService contactsService;

  @MockBean
  ContactRepository contactRepository;

  @Before
  public void setUp() throws Exception {
    reset(contactRepository);
  }

  @Test
  public void testGetAllContacts() {

    when(contactRepository.findAll()).thenReturn(DataUtil.makeContactList());

    List<Contact> contacts = contactsService.getAllContacts();

    assertThat(contacts.size(),equalTo(2));
  }

  @Test
  public void testAddContact() {

    Contact contact = DataUtil.makeContact1();

    contactsService.addContact(contact);

    verify(contactRepository).saveAndFlush(contact);
  }

  @Test(expected = RuntimeException.class)
  public void testAddContact_error_fail() {

    Contact contact = DataUtil.makeContact1();
    
    doThrow(RuntimeException.class)
    .when(contactRepository)
    .saveAndFlush(any(Contact.class));

    contactsService.addContact(contact);
  }

  @Test
  public void testUpdateContact() {

    Contact contact = DataUtil.makeContact1();
    when(contactRepository.findById(1)).thenReturn(Optional.ofNullable(contact));

    contactsService.updateContact(1, contact);

    contact.setId(1);
    verify(contactRepository).save(contact);
  }

  @Test(expected = RuntimeException.class)
  public void testUpdateContact_error_fail() {

    Contact contact = DataUtil.makeContact1();
    contact.setId(1);
    when(contactRepository.findById(1)).thenReturn(Optional.ofNullable(contact));

    doThrow(RuntimeException.class)
    .when(contactRepository)
    .save(contact);

    contactsService.updateContact(1, contact);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpdateContact_notFound_fail() {

    Contact contact = DataUtil.makeContact1();
    contact.setId(1);
    when(contactRepository.findById(1)).thenReturn(Optional.empty());

    contactsService.updateContact(1, contact);
  }

  @Test
  public void testGetContact() {

    Contact contact = DataUtil.makeContact1();
    contact.setId(1);
    when(contactRepository.findById(1)).thenReturn(Optional.ofNullable(contact));

    Contact retrievedContact = contactsService.getContact(1);

    assertThat(retrievedContact.getEmail(),equalTo("test7@mailinator.com"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetContact_notFound_fail() {

    when(contactRepository.findById(1)).thenReturn(Optional.empty());

    contactsService.getContact(1);

  }



  @Test
  public void testDeleteContact() {

    when(contactRepository.existsById(7)).thenReturn(true);

    contactsService.deleteContact(7);

    verify(contactRepository).deleteById(7);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeleteContact_notFound_fail() {

    when(contactRepository.existsById(7)).thenReturn(false);

    contactsService.deleteContact(7);
  }

  @Test(expected = RuntimeException.class)
  public void testDeleteContact_error_fail() {

    when(contactRepository.existsById(7)).thenReturn(true);

    doThrow(RuntimeException.class).
    when(contactRepository)
    .deleteById(7);

    contactsService.deleteContact(7);
  }


}
