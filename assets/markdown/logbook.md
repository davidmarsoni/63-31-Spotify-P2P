# Logbook

If you need more information about the project you can look about the commites message i have made for each update of the project.

## 5 december 2023

### work done

Download sample files from the course and browse these files

- Understand who to send a information to the client / server
- Understand who to send a information to the client / server
- create a basic command system and a ASSCI logo to determine with code is the client and with is the server on the command line

## 09 december 2023

### Work done

Do some test and research about the socket

- Try to create a log class
- Try do create a command system

## 12 december 2023

### Work done

- Create a command patern for the client commands
- Update the storage of the client
- Add help, listMusics, exist, connect and init
- Add comentary to the code

## 13 december 2023

### Work done

- Do some refactoring on the classes
- Update Playlist class to store only the name of the playlist
- Add a commandPattern for the server

## 14 december 2023

- Do some minor correction on the code

## 16 december 2023

### Work done

- Add music file sharing and start playlist development
- Combine properties in musicsFile and playlist classes
- Improve music display and fix server disconnect bugs
- Optimize help command and update utils with dark purple
- Refactor classes using Utils.colorize and add commentary
- Introduce ShareUnshare command, implement HandelUnShareCommand
- Enhance music file and playlist sharing, correct bugs
- Update .gitignore and prepare for client listening
- Introduce HandeShareUnshare on the server for code reuse
- Implement playlist sharing, correct Test command issues

### problems

I had difficulties at first to optimize the code of sharing and unsharing a musics or a playlist because there was a lot of code duplication. I have solved this problem by creating a new class with a bolean to determine if the command is a share or a unshare and a method to handle the command.

## 17 december 2023 Part 1

### Work done

- Add classes to allow to save information about the client and the server on a json file
- Add the gson dependency to the project
- Use System.out.println insted of Utils.p because it add just more complexity
- Add the disconnect command

### problems

- Find a proper way to save the information about the Entry list i have on the client because i was using a Abstract class and GSON can't serialize abstract class. I have solved this problem by adding a type field to each subclass of Entry and by creating a custom serializer for the Entry class.

## 17 december 2023 Part 2

### Work done

- Add a new class to handle thread data on the server

## problems

- I had a problem with the thread data class because i was not using a list of thread data but a single variable to store the client socket or it listening port and when i connect a second client the first one was disconnected. I have solved this problem by creating a list of a new class that store the client socket and it listening port and it ip address.

## 20 december 2023

### Work done

- Create a class Colors and move every colors in this class
- Update init command to add listening port
- Add Ping command client / server

## Special information

Because of big functionnalities and the fact that i have to do a lot of refactoring i have decided to not had a lookbook of each day beceause it will be only thing like "do some refactoring" or "add a new class" and it will be not very interesting to read. I rather prefer to do a lookbook of each big functionnalities when they are finished and with that i can explain how i have done it and what problem i have encountered.

## 21-24 december 2023


### Work done

- Create a Storage class to merge similar code beetween the 2 StorageClient and StorageServer class
- Create a ServerManagerClass to handle the managmenent  more easily on the client and the init for the server
- Create the command play

Start the implementation for playing a music on the client

- Create the Command HandleGetInfo to be use wth the command play on the client
- Prepare the class StreamEntry on the Server
- Rename a lot of variables to have a better concistency (ServerThreadData -> ThreadData, CommandClient to Commmand,ClientAdress -> local adrress, ...) and add commentary

### problems

I begin to have to much code on the project and when i want to do something i pass the a lot of time doing something else because there is a bug here or i have to refactor this class. I have solved this problem by creating  a lot of TODO in the code and i will do them when i finish the implementation of my current functionnality.

## 24-25 december 2023

I just have read the code and do some minor correction and it was also christmas🎁

## 26-27 december 2023

### Work done

- Add the JavaZoom library to the project
- Update the init to be at the start of the program to be able to update the listening port
- Add 2 new classes to handle new logSystem for the server
- Add logs to the server classes
- Correct the share file command to be fully functionnal

### problems

- had some problems with the client server beceause the second client was not able to create his listening server. I have solved this problem by adding a verification at the start of the client in the init command to check if the port is already used and if it is used i will ask the user to enter a new port even if he choos the config file option.

## 27-03 december 2023

First I hadn't work on the project 30,31 december and 1 january.

### Work done

- Update poml.xml to add the main class
- Add explanation to the readme to build the project and use it
- Add basic readme for the project to build it and use it and add requirement
- Add Example folder to help the user to use the project
- Allow the player to stream playlist
- Correct a lot of bug and add missing features
- Correct display of logs for the server part
- Test for building the project
- Do a lot of rename, refactoring and optization

### problems

I have encounter my biggest problem on the project because i was not able to stream a playlist and i have spend a lot of time to find the problem research and so one. I have solved this problem by chance with the help of AI witch was able to gave me a basic part that help me to undrestand better the problem and i have solved it

## 04 december 2023

### Work done

- do some minor correction on the code
- write the lookbook of the project
- add the init command to the server
- add json storage for the server