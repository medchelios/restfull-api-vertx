package com.tmoh.restfull_api_vertx;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.jackson.DatabindCodec;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;

public class MainVerticle extends AbstractVerticle {
  private final static Logger LOGGER = Logger.getLogger(MainVerticle.class.getName());

  static {
    LOGGER.info("Customizing the built-in jackson ObjectMapper...");
    var objectMapper = DatabindCodec.mapper();
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    objectMapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
    objectMapper.disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);

    JavaTimeModule module = new JavaTimeModule();
    objectMapper.registerModule(module);
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOGGER.log(Level.INFO, "Starting HTTP server...");
    // setupLogging();

    // Create a PgPool instance
    var pgPool = pgPool();

    // Creating PostRepository
    var postRepository = EmployeeRepository.create(pgPool);

    // Creating PostHandler
    var postHandlers = EmployeesHandler.create(postRepository);

    // Initializing the sample data
    var initializer = DataInitializer.create(pgPool);
    initializer.run();

    // Configure routes
    var router = routes(postHandlers);

    // Create the HTTP server
    vertx.createHttpServer()
        // Handle every request using the router
        .requestHandler(router)
        // Start listening
        .listen(8888)
        // Print the port
        .onSuccess(server -> {
          startPromise.complete();
          System.out.println("HTTP server started on port " + server.actualPort());
        })
        .onFailure(event -> {
          startPromise.fail(event);
          System.out.println("Failed to start HTTP server:" + event.getMessage());
        });
  }

  private Pool pgPool() {
    PgConnectOptions connectOptions = new PgConnectOptions()
        .setPort(5432)
        .setHost("localhost")
        .setDatabase("vertx")
        .setUser("postgres")
        .setPassword("postgres");

    PoolOptions poolOptions = new PoolOptions().setMaxSize(5);

    Pool pool = Pool.pool(vertx, connectOptions, poolOptions);

    return pool;
  }

  // create routes
  private Router routes(EmployeesHandler handlers) {

    // Create a Router
    Router router = Router.router(vertx);
    // register BodyHandler globally.
    // router.route().handler(BodyHandler.create());
    router.get("/employees").produces("application/json").handler(handlers::all);
    router.post("/employees").consumes("application/json").handler(BodyHandler.create()).handler(handlers::save)
        .failureHandler(frc -> {
          Throwable failure = frc.failure();
          frc.response().setStatusCode(500).setStatusMessage("Server internal error:" + failure.getMessage()).end();
        });
    router.get("/employees/:id").produces("application/json").handler(handlers::get)
        .failureHandler(frc -> {
          Throwable failure = frc.failure();
          if (failure instanceof PostNotFoundException) {
            frc.response().setStatusCode(404).end();
          }
          frc.response().setStatusCode(500).setStatusMessage("Server internal error:" + failure.getMessage()).end();
        });
    router.put("/employees/:id").consumes("application/json").handler(BodyHandler.create()).handler(handlers::update);
    router.delete("/posts/:id").handler(handlers::delete);

    router.get("/hello").handler(rc -> rc.response().end("Hello from my route"));

    return router;
  }
}
