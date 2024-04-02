package com.tmoh.restfull_api_vertx;

import java.util.logging.Level;
import java.util.logging.Logger;

import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class EmployeesHandler {
  private static final Logger LOGGER = Logger.getLogger(EmployeesHandler.class.getSimpleName());

  EmployeeRepository employees;

  private EmployeesHandler(EmployeeRepository _employees) {
    this.employees = _employees;
  }

  // factory method
  public static EmployeesHandler create(EmployeeRepository posts) {
    return new EmployeesHandler(posts);
  }

  public void all(RoutingContext rc) {
    this.employees.findAll()
        .onSuccess(
            data -> rc.response().end(Json.encode(data)));
  }

  public void get(RoutingContext rc) {
    var params = rc.pathParams();
    var id = params.get("id");
    this.employees.findById(Integer.parseInt(id))
        .onSuccess(
            post -> rc.response().end(Json.encode(post)))
        .onFailure(
            rc::fail);

  }

  public void save(RoutingContext rc) {
    var body = rc.body().asJsonObject();
    LOGGER.log(Level.INFO, "request body: {0}", body);
    var form = body.mapTo(CreateEmployeeCommand.class);
    this.employees.save(Employee.of(form.getFirstName(), form.getLastName(), form.getEmail(), form.getDepartment(),
        form.getPosition(), form.getSalary(), form.getHireDate()))
        .onSuccess(
            savedId -> rc.response()
                .putHeader("Location", "/employees/" + savedId)
                .setStatusCode(201)
                .end()

        );
  }

  public void update(RoutingContext rc) {
    var params = rc.pathParams();
    var id = params.get("id");
    var body = rc.body().asJsonObject();
    LOGGER.log(Level.INFO, "\npath param id: {0}\nrequest body: {1}", new Object[] { id, body });
    var form = body.mapTo(CreateEmployeeCommand.class);
    this.employees.findById(Integer.parseInt(id))
        .compose(
            employees -> {
              employees.setFirstName(form.getFirstName());
              employees.setLastName(form.getLastName());
              employees.setEmail(form.getEmail());
              employees.setDepartment(form.getDepartment());
              employees.setPosition(form.getPosition());
              employees.setSalary(form.getSalary());
              employees.setHireDate(form.getHireDate());

              return this.employees.update(employees);
            })
        .onSuccess(
            data -> rc.response().setStatusCode(204).end())
        .onFailure(
            throwable -> rc.fail(404, throwable));

  }

  public void delete(RoutingContext rc) {
    var params = rc.pathParams();
    var idParams = params.get("id");

    var id = Integer.parseInt(idParams);
    this.employees.findById(id)
        .compose(
            post -> this.employees.deleteById(id))
        .onSuccess(
            data -> rc.response().setStatusCode(204).end())
        .onFailure(
            throwable -> rc.fail(404, throwable));

  }
}
