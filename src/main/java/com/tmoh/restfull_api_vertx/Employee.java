package com.tmoh.restfull_api_vertx;

import java.time.LocalDate;

public class Employee {
  Integer id;
  String firstName;
  String lastName;
  String email;
  String department;
  String position;
  double salary;
  LocalDate hireDate;

  public static Employee of(String firstName, String lastName, String email, String department, String position,
      double salary, LocalDate hireDate) {
    return of(null, firstName, lastName, email, department, position, salary, hireDate);
  }

  public static Employee of(Integer id, String firstName, String lastName, String email, String department,
      String position, double salary, LocalDate hireDate) {
    Employee data = new Employee();
    data.setId(id);
    data.setFirstName(firstName);
    data.setLastName(lastName);
    data.setEmail(email);
    data.setDepartment(department);
    data.setPosition(position);
    data.setSalary(salary);
    data.setHireDate(hireDate);
    return data;
  }

  public Integer getId() {
    return this.id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return this.lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getDepartment() {
    return this.department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getPosition() {
    return this.position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public double getSalary() {
    return this.salary;
  }

  public void setSalary(double salary) {
    this.salary = salary;
  }

  public LocalDate getHireDate() {
    return this.hireDate;
  }

  public void setHireDate(LocalDate hireDate) {
    this.hireDate = hireDate;
  }

}
