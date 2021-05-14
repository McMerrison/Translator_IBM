# Translator_IBM
A vocal translator allowing user to speak a phrase in one language which is then translated into another and spoken aloud using IBM synthesized speech.

# Setup
1. Pull files into any directory (5 java files: Transcription, Language, Vocalizer, Translator, and API_KEYS). No external dependenices
2. Compile with 'javac'
3. Execute 'java Translator [translation]
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
  
Any of the above can also be reversed to translate from that language to English (translation from non-English to non-English not yet supported)

Example commands: 

java Translation en-ko (English to Korean)

java Translation ar-en (Arabic to English)

java Translation it-en (Italian)

4. After execution, press Enter to begin recording a phrase. Press Enter again to stop recording, and translation will begin. Speech will be synthesized and played back after a few moments.

