package com.hackudc.poustfit_server.exceptions;

public class OperationNotAllowed extends ModelException {

  public OperationNotAllowed(String msg) {
    super("Operation not allowed: " + msg);
  }
}
