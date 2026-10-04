# Dook project

This is a greenfield Java project developed from the Duke project template.

This project is a bare-bones CLI chatbot for task management.

## Quick start

1. Ensure that Java `25` or later is installed on your computer.

1. Locate the `src/main/java/dook/Dook.java` file, compile the project, and run it from the project root.

1. The program will greet you with a banner and a short message. The output should start with:
   ```
      ▓█████▄  ▒█████   ▒█████   ██ ▄█▀
      ▒██▀ ██▌▒██▒  ██▒▒██▒  ██▒ ██▄█▒
      ░██   █▌▒██░  ██▒▒██░  ██▒▓███▄░
      ░▓█▄   ▌▒██   ██░▒██   ██░▓██ █▄
      ░▒████▓ ░ ████▓▒░░ ████▓▒░▒██▒ █▄
       ▒▒▓  ▒ ░ ▒░▒░▒░ ░ ▒░▒░▒░ ▒ ▒▒ ▓▒
       ░ ▒  ▒   ░ ▒ ▒░   ░ ▒ ▒░ ░ ░▒ ▒░
       ░ ░  ░ ░ ░ ░ ▒  ░ ░ ░ ▒  ░ ░░ ░
         ░        ░ ░      ░ ░  ░  ░
       ░
   ```

## Acknowledgments

- The Dook banner was created with [ASCII Art Archive](https://www.asciiart.eu/text-to-ascii-art), using the Bloody font (Figlet conversion by patorjk, April 17, 2008).
- The fuzzy search feature uses an implementation of the Wagner–Fischer algorithm for Damerau–Levenshtein distance, adapted from the [Damerau–Levenshtein distance Wikipedia article](https://en.wikipedia.org/wiki/Damerau%E2%80%93Levenshtein_distance). The original pseudocode is licensed under [CC BY-SA 4.0](https://creativecommons.org/licenses/by-sa/4.0/).
