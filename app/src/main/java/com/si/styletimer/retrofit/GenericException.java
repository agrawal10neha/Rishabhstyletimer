package com.si.styletimer.retrofit;

import android.content.Context;

import java.io.IOException;
import java.net.SocketException;

public class GenericException extends IOException {
  Context context;
  String message;

  public GenericException( Exception e) {
    this.context = context;
    message = "str_internet_connection_error";

    if (e instanceof SocketException) {
      message = "str_internet_connection_error";

    } else if (e instanceof InternalServerError) {
      message = "str_internal_server_error";
    }
  }

  @Override
  public String getMessage() {
    return message;
  }

}

class InternalServerError extends IOException {}
