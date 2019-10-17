package com.example.exercise.rest.controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.exercise.DataUtil;
import com.example.exercise.model.Contact;
import com.example.exercise.rest.exception.ResourceModificationErrorException;
import com.example.exercise.rest.exception.ResourceNotFoundException;
import com.example.exercise.service.ContactsService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ContactsController.class)
public class ContactsControllerTest {
  
  private final String NEW_CONTACT_JSON = "{\"name\":{\"first\":\"Harold\",\"middle\":\"Francis\",\"last\":\"Gilkey\"},\"address\":{\"street\":\"8360 High Autumn Row\",\"city\":\"Cannon\",\"state\":\"Delaware\",\"zip\":\"19797\"},\"phone\":[{\"number\":\"302-611-9148\",\"type\":\"home\"},{\"number\":\"302-532-9427\",\"type\":\"mobile\"}],\"email\":\"harold.gilkey@yahoo.com\"}";
  private final String CONTACT_JSON = "{\"id\":2,\"name\":{\"first\":\"John\",\"middle\":\"\",\"last\":\"Adams\"},\"address\":{\"street\":\"1 1st street\",\"city\":\"Bologna\",\"state\":\"Nova Scotia\",\"zip\":\"12EJ345\"},\"phone\":[{\"number\":\"555-123-456\",\"type\":\"home\"},{\"number\":\"555-789-1012\",\"type\":\"mobile\"}],\"email\":\"test7@mailinator.com\"}";
  private final String ALL_CONTACTS_JSON = "[{\"id\":3,\"name\":{\"first\":\"John\",\"middle\":\"\",\"last\":\"Adams\"},\"address\":{\"street\":\"1 1st street\",\"city\":\"Bologna\",\"state\":\"Nova Scotia\",\"zip\":\"12EJ345\"},\"phone\":[{\"number\":\"555-123-456\",\"type\":\"home\"},{\"number\":\"555-789-1012\",\"type\":\"mobile\"}],\"email\":\"test7@mailinator.com\"},{\"id\":7,\"name\":{\"first\":\"Juan\",\"middle\":\"Luis\",\"last\":\"guerra\"},\"address\":{\"street\":\"365 Top Blvd\",\"city\":\"San Juan\",\"state\":\"Puerto Rico\",\"zip\":\"98765\"},\"phone\":[{\"number\":\"555-123-456\",\"type\":\"work\"}],\"email\":\"test15@mailinator.com\"}]";

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  ContactsService contactsService;

  @Before
  public void setUp() throws Exception {
    reset(contactsService);
  }

  @Test
  public void testGetAllContacts() throws Exception {
    Contact contact = DataUtil.makeContact1();
    contact.setId(2);
    List<Contact> contacts = DataUtil.makeContactList();
    contacts.get(0).setId(3);
    contacts.get(1).setId(7);

    when(contactsService.getAllContacts()).thenReturn(contacts);

    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get("/contacts").accept(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    
    verify(contactsService).getAllContacts();
    assertThat(result.getResponse().getContentAsString(), containsString(ALL_CONTACTS_JSON));
    assertThat(result.getResponse().getStatus(), equalTo(200));
  }
  
  @Test
  public void testGetAllContacts_empty() throws Exception {
    when(contactsService.getAllContacts()).thenReturn(new ArrayList<>());

    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get("/contacts").accept(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    
    verify(contactsService).getAllContacts();
    assertThat(result.getResponse().getContentAsString(), containsString("[]"));
    assertThat(result.getResponse().getStatus(), equalTo(200));
  }

  @Test
  public void testAddContact() throws Exception {
    
    Contact postContact = DataUtil.makeContact1();
    postContact.setId(999);
    
    when(contactsService.addContact(any(Contact.class))).thenReturn(postContact);
    
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .post("/contacts").accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(NEW_CONTACT_JSON);
    
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    
    verify(contactsService).addContact(any(Contact.class));
    assertThat(result.getResponse().getStatus(), equalTo(201));
    assertTrue(result.getResponse().getHeader("Location").contains("/contacts/999"));
  }
  
  @Test
  public void testAddContact_null_fail() throws Exception {
    
    when(contactsService.addContact(any(Contact.class))).thenReturn(null);
    
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .post("/contacts").accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(NEW_CONTACT_JSON);
    
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    
    verify(contactsService).addContact(any(Contact.class));
    assertThat(result.getResponse().getStatus(), equalTo(400));
  }
  
  @Test
  public void testAddContact_error_fail() throws Exception {
    
    doThrow(RuntimeException.class)
    .when(contactsService)
    .addContact(any(Contact.class));
    
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .post("/contacts").accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(NEW_CONTACT_JSON);
    
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    
    verify(contactsService).addContact(any(Contact.class));
    assertThat(result.getResponse().getStatus(), equalTo(500));
  }
  
  
  @Test
  public void testUpdateContact() throws Exception {
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .put("/contacts/2").accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(CONTACT_JSON);
    
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    
    assertThat(result.getResponse().getStatus(), equalTo(200));
    verify(contactsService).updateContact(eq(2), any(Contact.class));
  }
  
  @Test
  public void testUpdateContact_notfound_fail() throws Exception {
    
    doThrow(IllegalArgumentException.class)
    .when(contactsService)
    .updateContact(eq(2), any(Contact.class));
    
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .put("/contacts/2").accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(CONTACT_JSON);
    
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    
    assertThat(result.getResponse().getStatus(), equalTo(404));
    verify(contactsService).updateContact(eq(2), any(Contact.class));
  }
  
  @Test
  public void testUpdateContact_error_fail() throws Exception {
    
    doThrow(RuntimeException.class)
    .when(contactsService)
    .updateContact(eq(2), any(Contact.class));
    
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .put("/contacts/2").accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(CONTACT_JSON);
    
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    
    assertThat(result.getResponse().getStatus(), equalTo(500));
    verify(contactsService).updateContact(eq(2), any(Contact.class));
  }

  @Test
  public void testGetContact() throws Exception {
    Contact contact = DataUtil.makeContact1();
    contact.setId(2);

    when(contactsService.getContact(2)).thenReturn(contact);

    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get("/contacts/2").accept(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    
    verify(contactsService).getContact(2);
    assertThat(result.getResponse().getContentAsString(), containsString(CONTACT_JSON));
    assertThat(result.getResponse().getStatus(), equalTo(200));
  }
  
  @Test
  public void testGetContact_notfound_fail() throws Exception {
    
    doThrow(IllegalArgumentException.class)
    .when(contactsService)
    .getContact(2);
    
        RequestBuilder requestBuilder = MockMvcRequestBuilders
        .get("/contacts/2").accept(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    
    verify(contactsService).getContact(2);
    assertThat(result.getResponse().getStatus(), equalTo(404));
  }

  @Test
  public void testDeleteContact() throws Exception {
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .delete("/contacts/5").accept(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    
    verify(contactsService).deleteContact(5);
    assertThat(result.getResponse().getStatus(), equalTo(200));
  }
  
  @Test
  public void testDeleteContact_notfound_fail() throws Exception {
    
    doThrow(IllegalArgumentException.class)
      .when(contactsService)
      .deleteContact(9);
      
    
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .delete("/contacts/9").accept(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    
    verify(contactsService).deleteContact(9);
    assertThat(result.getResponse().getStatus(), equalTo(404));
  }
  
  @Test
  public void testDeleteContact_error_fail() throws Exception {
    
    doThrow(RuntimeException.class)
      .when(contactsService)
      .deleteContact(9);
      
    
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .delete("/contacts/9").accept(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    
    verify(contactsService).deleteContact(9);
    assertThat(result.getResponse().getStatus(), equalTo(500));
  }

}
