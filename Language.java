import java.util.*;
import java.nio.charset.StandardCharsets;
import java.io.*;
import java.net.*;
import java.util.Base64.Encoder;

public class Language {

  String api = API_KEYS.getL_KEY();
  String url = "https://api.us-east.language-translator.watson.cloud.ibm.com/instances/bac412c7-2ff4-4bce-874d-f14658fa350a";
  String outputFile = "translated.txt";
  String language;

  public Language(String language) {
    this.language = language;
  }
}
