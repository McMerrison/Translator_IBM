# Translator_IBM
A vocal translator allowing user to speak a phrase in one language which is then translated into another and spoken aloud using IBM synthesized speech.

# Setup
1. Pull files into any directory (5 java files: Transcription, Language, Vocalizer, Translator, and API_KEYS). No external dependenices needed.
2. Compile with 'javac *.java'
3. Execute command 'java Translator [translation]'
  
  Possible translations:
  
  English to...
  
  Spanish 'en-es'
  
  French 'en-fr'
  
  German 'en-de'
  
  Korean 'en-ko'
  
  Arabic 'en-ar'
  
  Italian 'en-it'
  
  Portuguese 'en-pt'
  
  Chinese (Mandarin) 'en-zh'
  
  Dutch 'en-nl'
  
  Japanese 'en-ja'
  
Any of the above can also be reversed (i.e. 'es-en' or 'ko-en' to translate from another to English (translation from non-English to non-English not yet supported)

Example commands: 

java Translation en-ko

java Translation ar-en

java Translation en-es 

4. After execution, press Enter to begin recording a phrase. Press Enter again to stop recording, and translation will begin. Speech will be synthesized and played back after a few moments.

# Notes
- It is possible to input any language pair (not listed here) from IBM documentation for translation. However, certain voices are not supported. The program will outut a text translation but speech playback will likely be incorrect, as it is given in normal English pronunciation.
- Speech recording time will automatically timeout after 30 seconds if user does not press Enter again.
- API keys are guest keys with limited requests. *THIS APP IS A DEMO ONLY*

