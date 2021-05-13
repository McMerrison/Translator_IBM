import java.util.*;
import java.nio.charset.StandardCharsets;
import java.io.*;
import java.net.*;
import java.util.Base64.Encoder;

public class Vocalizer {

  String api = API_KEYS.getV_KEY();
  String url = "https://api.us-south.text-to-speech.watson.cloud.ibm.com/instances/4ef23041-b7a3-488b-a5a6-387e651fe040";
  String outputFile = "test.wav";
  String text = "Hello how are you?";
  String voice = "en-US_MichaelV3Voice";
  String language;

  public Vocalizer(String text, String file, String language) {
    this.text = text;
    this.outputFile = file;
    this.language = language;

    if (language.equals("en-es")) {
      this.voice = "es-ES_EnriqueV3Voice";
    } else if (language.equals("en-fr")) {
      this.voice = "fr-FR_ReneeV3Voice";
    } else if (language.equals("en-de")) {
      this.voice = "de-DE_ErikaV3Voice";
    } else if (language.equals("en-ko")) {
      this.voice = "ko-KR_YunaVoice";
    } else if (language.equals("en-ar")) {
      this.voice = "ar-MS_OmarVoice";
    } else if (language.equals("en-it")) {
      this.voice = "it-IT_FrancescaV3Voice";
    } else if (language.equals("en-pt")) {
      this.voice = "pt-BR_IsabelaV3Voice";
    } else if (language.equals("en-zh")) {
      this.voice = "zh-CN_WangWeiVoice";
    } else if (language.equals("en-nl")) {
      this.voice = "nl-NL_EmmaVoice";
    } else if (language.equals("en-ja")) {
      this.voice = "ja-JP_EmiV3Voice";
    } else if (language.equals("en-ur")) {
      this.voice = "ar-MS_OmarVoice";
    }


  }




/*
curl -X POST -u "apikey:Wuy9VXoGhSUQEsKpxJdxagngiWQZIODokE5E89ts1QdB" \
--header "Content-Type: application/json" \
--header "Accept: audio/flac" \
--data "{\"text\": \"Hi my name is Talha\"}" \
--output output.flac \
"https://api.us-south.text-to-speech.watson.cloud.ibm.com/instances/4ef23041-b7a3-488b-a5a6-387e651fe040/v1/synthesize"
*/

/*

curl -X GET -u "apikey:Wuy9VXoGhSUQEsKpxJdxagngiWQZIODokE5E89ts1QdB" --header "Accept: audio/wav" --output hello_world.wav "https://api.us-south.text-to-speech.watson.cloud.ibm.com/instances/4ef23041-b7a3-488b-a5a6-387e651fe040/v1/synthesize?text=Goodbye%20world"



*/





}
