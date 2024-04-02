package com.tmoh.restfull_api_vertx;

public class PostNotFoundException extends RuntimeException {
  public PostNotFoundException(Integer id) {
    super("Post id: " + id + " was not found. ");
  }
}
