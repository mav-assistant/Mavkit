Mav
===
**Modular Assistant (Voice)**

Mav is a personal assistant, similar to Google Now, Microsoft Cortana, Apple's Siri, and ILA.
It is written in Java 7, and uses MARY and Sphinx-4 by default, but can be configured to use Google's cloud APIs.

It is heavily inspired by ILA, but is designed to be fully modular and customizable, and open-source from the start.

**Mav is not at all ready for use yet.** You have been warned.

Goals
=====
1. **Don't make it obvious Mav is written in Java.** I feel this was one of the biggest reasons I didn't like ILA. It integrates poorly with the OS, and looks generally ugly as it uses default Swing widgets, and replaces the window manager's decorations. Mav already does this by setting the LaF to the system LaF on start, therefore making the error dialogs look native, even early on in the startup.
2. **Look simple, be delightful.** Even the very first commit to Mav fits this; the triangle is very simplistic, but yet looks very slick. The thin font I think also adds to this a lot.
3. **Work well.** This is a rather vague and hard-to-obtain goal, as everyone has a different definition for what this means. My definition is probably to handle errors well, and come with useful functionality out of the box.
4. **Be fully modular.** If the user wishes, they should be able to swap out the complete appearance of Mav, the hotword, the TTS engine, the ASR engine, the fonts, sounds, etc. If they want their digital assistant to look like a character from their favorite anime, that's their choice, and if they have the commitment to develop a module to do it, they should be allowed to.
5. **Support plugins.** A number of things cannot be done well using a simple command markup language, and need to be done in code. For this, we should offer the ability to just add plugins instead of requiring a change to the core code.
6. **Don't complicate the interface with irrelevant technical details.** This would include the internal format commands are stored in, what is and isn't a module, etc.

Screenshots
===========
![Waiting](http://i.imgur.com/PSYqrmt.png)
![Responding](http://i.imgur.com/r3fC8AF.png)

Compiling
=========
Linux
-----
Run `./gradlew`. A zip will be created in build/distributions that contains all files needed to run Mav.

Windows
-------
Run `gradlew`. A zip will be created in build/distributions that contains all files needed to run Mav.