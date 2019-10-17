package com.example.exercise.model;

public enum PhoneType {
  
  home("home"),
  work("work"),
  mobile("mobile")
  ;
  
  private final String phoneType;

  PhoneType(String phoneType) {
      this.phoneType = phoneType;
  }
  
  public String getPhoneType() {
      return this.phoneType;
  }
  
  @Override
  public String toString() {
    return this.phoneType;
  }
}
