import java.util.*;
import java.nio.charset.StandardCharsets;
import java.io.*;
import java.net.*;
import java.util.Base64.Encoder;


public class Transcription {

  String api = API_KEYS.getT_KEY();
  String url = "https://api.us-east.speech-to-text.watson.cloud.ibm.com/instances/2b76c515-715e-4c1b-b4d8-26a1f255e0ed";

  String model = "en-US_ShortForm_NarrowbandModel";
  String audioType = "audio/wav";
  String language;
  String outputFile;


  public Transcription(String outputFile, String language) {
    this.outputFile = outputFile;
    this.language = language;

    if (language.equals("es-en")) {
      this.model = "es-ES_BroadbandModel";
    } else if (language.equals("fr-en")) {
      this.model = "fr-FR_BroadbandModel";
    } else if (language.equals("de-en")) {
      this.model = "de-DE_BroadbandModel";
    } else if (language.equals("ko-en")) {
      this.model = "ko-KR_BroadbandModel";
    } else if (language.equals("ar-en")) {
      this.model = "ar-MS_BroadbandModel";
    } else if (language.equals("it-en")) {
      this.model = "it-IT_BroadbandModel";
    } else if (language.equals("pt-en")) {
      this.model = "pt-BR_BroadbandModel";
    } else if (language.equals("zh-en")) {
      this.model = "zh-CN_BroadbandModel";
    } else if (language.equals("nl-en")) {
      this.model = "nl-NL_BroadbandModel";
    } else if (language.equals("ja-en")) {
      this.model = "ja-JP_BroadbandModel";
    }
  }

  /*

  curl -X POST -u "apikey:enfr6McE3JeAhenaS6ckd3R39lkBkCe5iXmENE5WW1yT" \ --header "Content-Type: audio/flac" --data-binary @test.wav "https://api.us-east.speech-to-text.watson.cloud.ibm.com/instances/2b76c515-715e-4c1b-b4d8-26a1f255e0ed/v1/recognize"


  */



}
