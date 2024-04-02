package com.tmoh.restfull_api_vertx;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import io.vertx.core.Future;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlResult;
import io.vertx.sqlclient.Tuple;

public class EmployeeRepository {
  private static Function<Row, Employee> MAPPER = (row) -> Employee.of(
      row.getInteger("id"),
      row.getString("first_name"),
      row.getString("last_name"),
      row.getString("email"),
      row.getString("department"),
      row.getString("position"),
      row.getDouble("salary"),
      row.getLocalDate("hire_date"));

  private final Pool client;

  private EmployeeRepository(Pool _client) {
    this.client = _client;
  }

  // factory method
  public static EmployeeRepository create(Pool client) {
    return new EmployeeRepository(client);
  }

  public Future<List<Employee>> findAll() {
    return client.query("SELECT * FROM employee ORDER BY id ASC")
        .execute()
        .map(rs -> StreamSupport.stream(rs.spliterator(), false)
            .map(MAPPER)
            .collect(Collectors.toList()));
  }

  public Future<Employee> findById(Integer id) {
    Objects.requireNonNull(id, "id can not be null");
    return client.preparedQuery("SELECT * FROM employee WHERE id=$1").execute(Tuple.of(id))
        .map(RowSet::iterator)
        .map(iterator -> {
          if (iterator.hasNext()) {
            return MAPPER.apply(iterator.next());
          }
          throw new PostNotFoundException(id);
        });
  }

  public Future<Integer> save(Employee data) {
    return client.preparedQuery(
        "INSERT INTO employee (first_name, last_name, email, department, position, salary, hire_date) VALUES ($1, $2, $3, $4, $5, $6, $7) RETURNING (id)")
        .execute(Tuple.of(data.getFirstName(), data.getLastName(), data.getEmail(), data.getDepartment(),
            data.getPosition(), data.getSalary(), data.getHireDate()))
        .map(rs -> rs.iterator().next().getInteger("id"));
  }

  public Future<Integer> saveAll(List<Employee> data) {
    var tuples = data.stream()
        .map(
            d -> Tuple.of(d.getFirstName(), d.getLastName(), d.getEmail(), d.getDepartment(),
                d.getPosition(), d.getSalary(), d.getHireDate()))
        .collect(Collectors.toList());

    return client.preparedQuery(
        "INSERT INTO employee (first_name, last_name, email, department, position, salary, hire_date) VALUES ($1, $2, $3, $4, $5, $6, $7)")
        .executeBatch(tuples)
        .map(SqlResult::rowCount);
  }

  public Future<Integer> update(Employee data) {
    return client.preparedQuery("UPDATE employee SET first_name=$1, last_name=$2 WHERE id=$3")
        .execute(Tuple.of(data.getFirstName(), data.getLastName(), data.getId()))
        .map(SqlResult::rowCount);
  }

  public Future<Integer> deleteAll() {
    return client.query("DELETE FROM employee").execute()
        .map(SqlResult::rowCount);
  }

  public Future<Integer> deleteById(Integer id) {
    Objects.requireNonNull(id, "id can not be null");
    return client.preparedQuery("DELETE FROM employee WHERE id=$1").execute(Tuple.of(id))
        .map(SqlResult::rowCount);
  }
}
