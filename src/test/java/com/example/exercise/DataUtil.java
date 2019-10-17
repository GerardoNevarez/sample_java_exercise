package com.example.exercise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.exercise.model.Address;
import com.example.exercise.model.Contact;
import com.example.exercise.model.Name;
import com.example.exercise.model.Phone;
import com.example.exercise.model.PhoneType;

public class DataUtil {

  public static Contact makeContact1() {
    
    Name name = new Name("John", "", "Adams");
    Address address = new Address("1 1st street", "Bologna", "Nova Scotia", "12EJ345");
    
    Phone phone1 = new Phone("555-123-456", PhoneType.home);
    Phone phone2 = new Phone("555-789-1012", PhoneType.mobile);
    List<Phone> phones = new ArrayList<>();
    Collections.addAll(phones, phone1, phone2);
    
    
    Contact contact = new Contact(name, address, phones, "test7@mailinator.com");
    return contact;
  }
  
  private static Contact makeContact2() {
    
    Name name = new Name("Juan", "Luis", "guerra");
    Address address = new Address("365 Top Blvd", "San Juan", "Puerto Rico", "98765");
    
    Phone phone1 = new Phone("555-123-456", PhoneType.work);
    List<Phone> phones = Collections.singletonList(phone1);
        
    Contact contact = new Contact(name, address, phones, "test15@mailinator.com");
    return contact;
  }
  
  public static List<Contact> makeContactList(){
    List<Contact> contacts = new ArrayList<>();
    Collections.addAll(contacts, makeContact1(), makeContact2());
    
    return contacts;
  }
  
}
