package com.tmoh.restfull_api_vertx;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.StreamSupport;

import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.Tuple;

public class DataInitializer {
  private final static Logger LOGGER = Logger.getLogger(DataInitializer.class.getName());

  private Pool client;

  public DataInitializer(Pool client) {
    this.client = client;
  }

  public static DataInitializer create(Pool client) {
    return new DataInitializer(client);
  }

  public void run() {
    LOGGER.info("Data initialization is starting...");

    Tuple first = Tuple.of("Yve", "Sylvain", "yves@example.com", "Engineering", "Software Developer", 75000.00,
        LocalDate.now());
    Tuple second = Tuple.of("Daniel", "Haba", "daniel@example.com", "Marketing", "Marketing Manager", 65000.00,
        LocalDate.now());

    client
        .withTransaction(
            conn -> conn.query("DELETE FROM employee").execute()
                // .flatMap(r -> conn.preparedQuery("CREATE TABLE employee").execute())
                .flatMap(r -> conn.preparedQuery(
                    "INSERT INTO employee (first_name, last_name, email, department, position, salary, hire_date) VALUES ($1, $2, $3, $4, $5, $6, $7)")
                    .executeBatch(List.of(first, second)))
                .flatMap(r -> conn.query("SELECT * FROM employee").execute()))
        .onSuccess(data -> StreamSupport.stream(data.spliterator(), true)
            .forEach(row -> LOGGER.log(Level.INFO, "saved data:{0}", new Object[] { row.toJson() })))
        .onComplete(
            r -> {
              // client.close(); will block the application.
              LOGGER.info("Data initialization is done...");
            })
        .onFailure(
            throwable -> LOGGER.warning("Data initialization is failed:" + throwable.getMessage()));

  }
}
