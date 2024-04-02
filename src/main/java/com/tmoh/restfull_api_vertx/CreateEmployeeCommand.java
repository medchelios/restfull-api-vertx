package com.tmoh.restfull_api_vertx;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class CreateEmployeeCommand implements Serializable {

  String firstName;
  String lastName;
  String email;
  String department;
  String position;
  double salary;
  LocalDate hireDate;

  public static CreateEmployeeCommand of(String firstName, String lastName, String email, String department,
      String position, double salary, LocalDate hireDate) {
    CreateEmployeeCommand employeeCommand = new CreateEmployeeCommand();
    employeeCommand.setFirstName(firstName);
    employeeCommand.setLastName(lastName);
    employeeCommand.setEmail(email);
    employeeCommand.setDepartment(department);
    employeeCommand.setPosition(position);
    employeeCommand.setSalary(salary);
    employeeCommand.setHireDate(hireDate);

    return employeeCommand;
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

  @Override
  public String toString() {
    return "{" +
        " firstName='" + getFirstName() + "'" +
        ", lastName='" + getLastName() + "'" +
        ", email='" + getEmail() + "'" +
        ", department='" + getDepartment() + "'" +
        ", position='" + getPosition() + "'" +
        ", salary='" + getSalary() + "'" +
        ", hireDate='" + getHireDate() + "'" +
        "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this)
      return true;
    if (!(o instanceof CreateEmployeeCommand)) {
      return false;
    }
    CreateEmployeeCommand createEmployeeCommand = (CreateEmployeeCommand) o;
    return Objects.equals(firstName, createEmployeeCommand.firstName)
        && Objects.equals(lastName, createEmployeeCommand.lastName)
        && Objects.equals(email, createEmployeeCommand.email)
        && Objects.equals(department, createEmployeeCommand.department)
        && Objects.equals(position, createEmployeeCommand.position)
        && Objects.equals(salary, createEmployeeCommand.salary)
        && Objects.equals(hireDate, createEmployeeCommand.hireDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstName, lastName, email, department, position, salary, hireDate);
  }

}
