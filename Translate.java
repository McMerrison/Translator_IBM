import java.util.*;
import java.nio.charset.StandardCharsets;
import java.io.*;
import java.net.*;
import java.util.Base64.Encoder;
import java.nio.file.Files;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.Clip;



public class Translate {

  //CHANGE LANGAUGE HERE
  // Available:
  // en-es (Spanish)
  // en-fr (French)
  // en-de (German)
  // en-ko (Korean)
  // en-ar (Arabic)
  public static String tr = "en-es";
  public static long RECORD_TIME = 5000;  // 5 seconds
  public static long TIME_OUT = 30000; //30-second timeout

  public static final int BUFFER_SIZE = 4096;

  // path of the wav file
  static File wavFile = new File("sample.wav");

  // format of audio file
  static AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;

  // the line from which audio data is captured
  static TargetDataLine line;

  // creates a new thread that waits for
  // user input before stopping
  public static Thread readEnter = new Thread(new Runnable() {
      public void run() {
          try {

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            long startTime = System.currentTimeMillis();
            while ((System.currentTimeMillis() - startTime) < TIME_OUT && !in.ready()) {}
            timeout.interrupt();

          } catch (Exception e) {
              e.printStackTrace();
          }
          finish();
      }
  });
  // creates a new thread that waits for a specified
  // of time before stopping
  public static Thread timeout = new Thread(new Runnable() {
      public void run() {
          try {
              Thread.sleep(TIME_OUT);
          } catch (Exception e) {
          }
          finish();
      }
  });

  public static void main(String args[]) {

    if (args.length == 1) {
      tr = args[0];
    }



    System.out.println("Press Enter key to begin recording...");

    try {

    System.in.read();

    } catch (IOException e) {}

    readEnter.start();
    //timeout.start();

    /*
    Read in speech
    */
    record();
    Transcription script = new Transcription("script.txt", tr);
    sendPostSTT(script, script.outputFile);

    /*
    Translate
    */
    Language language = new Language(tr);
    String json = parseJSON("script.txt", "transcript");
    System.out.println("Original: " + json);

    translate(language, json, language.outputFile);
    String jsonTranslated = parseJSON("translated.txt", "translation\"");
    System.out.println("Translated: " + jsonTranslated);

    /*
    Convert translated text to Speech
    */
    Vocalizer vocals = new Vocalizer(jsonTranslated, "output.wav", tr);
    sendPostTTS(vocals, vocals.outputFile);

    playSound("output.wav");

}


  /*
    Play sound
  */
  public static void playSound(String filename) {

      try {
        File audioFile = new File(filename);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
        AudioFormat format = audioStream.getFormat();
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        SourceDataLine audioLine = (SourceDataLine) AudioSystem.getLine(info);

        audioLine.open(format);

        audioLine.start();

        System.out.println("Speaking Translation...");

        byte[] bytesBuffer = new byte[BUFFER_SIZE];
        int bytesRead = -1;

        while ((bytesRead = audioStream.read(bytesBuffer)) != -1) {
          audioLine.write(bytesBuffer, 0, bytesRead);
        }

        audioLine.drain();
        audioLine.close();
        audioStream.close();


      } catch (Exception e) {}
}

  /*
    Translate
  */
  public static void translate(Language lang, String text, String fileName) {
    try {
      try {


        URL url = new URL(lang.url + "/v3/translate?version=2018-05-01");
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "application/json");

        String cred = "apikey:" + lang.api;
        byte[] encodedAuth = Base64.getEncoder().encode(cred.getBytes(StandardCharsets.UTF_8));
        String authHeaderValue = "Basic " + new String(encodedAuth);


        http.setRequestProperty("Authorization", authHeaderValue);

        String data = "{\"text\": [\"" + text + "\"], \"model_id\":\"" + lang.language + "\"}";

        byte[] out = data.getBytes(StandardCharsets.UTF_8);

        OutputStream stream = http.getOutputStream();
        stream.write(out);

        System.out.println(http.getResponseCode() + " " + http.getResponseMessage());


        // opens input stream from the HTTP connection
        InputStream inputStream = http.getInputStream();
        String saveFilePath = fileName;

        // opens an output stream to save into file
        FileOutputStream outputStream = new FileOutputStream(saveFilePath);

        int bytesRead = -1;
        byte[] buffer = new byte[BUFFER_SIZE];
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.close();
        inputStream.close();

        System.out.println("File downloaded");
        http.disconnect();

    } catch (ProtocolException e) { e.printStackTrace(); }
    } catch (IOException e) { e.printStackTrace(); }
  }

  /*
    Get JSON Text
  */
  public static String parseJSON(String filename, String label) {
    String transcript = "";

    try {
      File file = new File(filename);
      Scanner scanner = new Scanner(file);

      while (scanner.hasNext()) {
        String line = scanner.nextLine();
        if (line.contains(label)) {
          line = line.trim();
          line = line.substring(label.length()+4);
          line = line.replaceAll("\"", "");
          line = line.replaceAll(",", "");
          line = line.replaceAll("%HESITATION", "uh");
          line = line.substring(0, line.length()-1);
          transcript += line + ". ";
        }
      }

      if (!transcript.equals("")) {
        return transcript;
      }

    } catch (FileNotFoundException e) {}

      return null;

  }

  /*
    Speech to Text
  */
  public static void sendPostSTT(Transcription script, String fileName) {

  try {
    try {


      URL url = new URL(script.url + "/v1/recognize" + "?model=" + script.model);
      HttpURLConnection http = (HttpURLConnection)url.openConnection();
      http.setRequestMethod("POST");
      http.setDoOutput(true);
      http.setRequestProperty("Content-Type", script.audioType);

      String cred = "apikey:" + script.api;
      byte[] encodedAuth = Base64.getEncoder().encode(cred.getBytes(StandardCharsets.UTF_8));
      String authHeaderValue = "Basic " + new String(encodedAuth);


      http.setRequestProperty("Authorization", authHeaderValue);

      File audio = wavFile;
      byte[] out = Files.readAllBytes(audio.toPath());

      //byte[] out = data;

      OutputStream stream = http.getOutputStream();
      stream.write(out);

      System.out.println(http.getResponseCode() + " " + http.getResponseMessage());


      // opens input stream from the HTTP connection
      InputStream inputStream = http.getInputStream();
      String saveFilePath = fileName;

      // opens an output stream to save into file
      FileOutputStream outputStream = new FileOutputStream(saveFilePath);

      int bytesRead = -1;
      byte[] buffer = new byte[BUFFER_SIZE];
      while ((bytesRead = inputStream.read(buffer)) != -1) {
          outputStream.write(buffer, 0, bytesRead);
      }

      outputStream.close();
      inputStream.close();

      http.disconnect();

  } catch (ProtocolException e) { e.printStackTrace(); }
  } catch (IOException e) { e.printStackTrace(); }

}

  /*
    Text to Speech
  */
  public static void sendPostTTS(Vocalizer vocals, String fileName) {

    try {
      try {


        URL url = new URL(vocals.url + "/v1/synthesize?voice=" + vocals.voice);
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "application/json");
        http.setRequestProperty("Accept", "audio/wav");

        String cred = "apikey:" + vocals.api;
        byte[] encodedAuth = Base64.getEncoder().encode(cred.getBytes(StandardCharsets.UTF_8));
        String authHeaderValue = "Basic " + new String(encodedAuth);

        //System.out.println(authHeaderValue);


        http.setRequestProperty("Authorization", authHeaderValue);

        //String data = "{\"text\": \"" + vocals.text + "\"}";

        String data = "{\"text\": \"" + vocals.text + "\", \"voice\":\"es-ES_EnriqueV3Voice\"}";

        byte[] out = data.getBytes(StandardCharsets.UTF_8);

        OutputStream stream = http.getOutputStream();
        stream.write(out);

        System.out.println(http.getResponseCode() + " " + http.getResponseMessage());


        // opens input stream from the HTTP connection
        InputStream inputStream = http.getInputStream();
        String saveFilePath = fileName;

        // opens an output stream to save into file
        FileOutputStream outputStream = new FileOutputStream(saveFilePath);

        int bytesRead = -1;
        byte[] buffer = new byte[BUFFER_SIZE];
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.close();
        inputStream.close();

        http.disconnect();

    } catch (ProtocolException e) { e.printStackTrace(); }
    } catch (IOException e) { e.printStackTrace(); }

}

  /*
  Borrowed from:
  https://www.codejava.net/coding/capture-and-record-sound-into-wav-file-with-java-sound-api
  */
  public static File record() {


    try {
            AudioFormat format = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            // checks if system supports the data line
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Line not supported");
                System.exit(0);
            }
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();   // start capturing

            System.out.println("Recording...");

            AudioInputStream ais = new AudioInputStream(line);

            System.out.println("Please say a pharse to be translated. Press Enter key to end recording.");
            System.out.println();


            // start recording
            AudioSystem.write(ais, fileType, wavFile);



        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }



    return null;
  }

  /**
     * Defines an audio format
  */
  public static AudioFormat getAudioFormat() {
        float sampleRate = 44000;
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                                             channels, signed, bigEndian);
        return format;
    }

  public static void finish() {
    line.stop();
    line.close();
    System.out.println("Finished. Analyzing...");
  }
}
