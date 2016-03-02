# Mav
Mav is a personal assistant, focused on modularity. It's similar in concept to
Google Now, Cortana, and Siri.

The plan is for the majority of functionality to be implemented via plugins, which
includes the appearance of the assistant, the text-to-speech engine, the speech
recognition engine, etc.

**Mav is not yet ready for use.**

## Todo

 - [ ] Basic audio engine
 - [ ] Basic rendering engine
 - [ ] Data/config directory (conforming to XDG Base Directory spec)
 - [ ] Plugin system
 - [ ] ASR
  - [ ] Google speech recognition (via Chromium)
  - [ ] PocketSphinx
 - [ ] TTS
  - [ ] MARY
  - [ ] Google TTS
 - [ ] GUI system (JavaFX? Custom?)
 - [ ] Setup wizard
  - [ ] Name entry
  - [ ] Personalization
  - [ ] ASR configuration
  - [ ] TTS configuration
  - [ ] etc.
 - [ ] Main interface
  - [ ] Listen for 'Hey, Mav' or custom hotword
  - [ ] Send general queries to...
    - [ ] DuckDuckGo (once this todo is checked off, Mav should be usable)
    - [ ] Google
    - [ ] Yahoo
    - [ ] Bing
    - [ ] etc.
  - [ ] Send special queries to...
    - [ ] Local (easter eggs, math, date, etc)
    - [ ] Wolfram Alpha
    - [ ] Wordnik
    - [ ] Wikipedia
    - [ ] etc.
 - [ ] Configuration interface
   - [ ] Plugin manager
   - [ ] Configuration options for everything set during setup
 - [ ] etc?