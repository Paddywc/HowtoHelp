package com.howtohelp.howtohelp.model.Data;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

/**
 * Handles opening, closing, and reading from an input stream in the JSON format
 */
abstract class RemoteDatabaseReader {
  /**
   * Adds additional functionality to InputStreamReader to read and navigate an input stream in the
   * JSON format
   */
  private JsonReader reader;
  /**
   * Handles the input stream from the HttpsURLConnection
   */
  private InputStream response;
  /**
   * Reads from the InputStream
   */
  private InputStreamReader responseReader;

  /**
   * Initialize reader, response, and responseReader
   *
   * @param dbConnection connection to the database
   * @throws IOException io exception
   */
  void startReading(HttpsURLConnection dbConnection) throws IOException {
    response = dbConnection.getInputStream();
    responseReader = new InputStreamReader(response, StandardCharsets.UTF_8);
    reader = new JsonReader(responseReader);
  }

  /**
   * Read the key in a JSON key/value pair
   *
   * @return the key as a string
   * @throws IOException io exception
   */
  String readJsonKey() throws IOException {
    return reader.nextName();
  }

  /**
   * Read the value in a JSON key/value pair
   *
   * @return the value as a string
   * @throws IOException io exception
   */
  String readJsonValue() throws IOException {
    String value;
    // return null if next value is null, rather than reading value from next key/value
    if (reader.peek() == JsonToken.NULL) {
      value = null;
      reader.skipValue();
    } else value = reader.nextString();

    return value;
  }

  /**
   * Start reading json object.
   *
   * @throws IOException io exception
   */
  void startReadingJsonObject() throws IOException {
    reader.beginObject();
  }

  /**
   * Stop reading json object.
   *
   * @throws IOException
   */
  void stopReadingJsonObject() throws IOException {
    reader.endObject();
  }

  /**
   * Start reading JSON array .
   *
   * @throws IOException
   */
  void startReadingArray() throws IOException {
    reader.beginArray();
  }

  /**
   * Stop reading JSON array.
   *
   * @throws IOException
   */
  void stopReadingArray() throws IOException {
    reader.endArray();
  }

  /**
   * Returns false if JSON object has another key/value pair, true otherwise
   *
   * @throws IOException
   */
  boolean finishedReadingObject() throws IOException {
    return !reader.hasNext();
  }

  /**
   * Returns false if JSON array has another JSON object, true otherwise
   *
   * @throws IOException the io exception
   */
  boolean finishedReadingArray() throws IOException {
    return !reader.hasNext();
  }

  /**
   * Close reader, responseReader, and response
   *
   * @throws IOException
   */
  void stopAllReading() throws IOException {
    reader.close();
    responseReader.close();
    response.close();
  }
}
