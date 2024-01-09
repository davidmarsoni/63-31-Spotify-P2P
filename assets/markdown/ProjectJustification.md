# Project Justification

## Choice to have only one jar file

I have decided to have only one jar file because i have a lot of dependencies that are the same for the client and the server and with only one jar file i can avoid to have a lot of duplicate dependencies and code.

For example the storage of the client and the server a quite similar. Both of them use also the same Entry,MusicFile and Playlist class.

## Command Pattern

In this project i have used 2 commands pattern. The first pattern is use to handle client side command and the second one is use to handle server side command.

I have used this pattern because it allow me to add new command easily and to handle them in a simple way with a HashMap of command.
The command pattern also allow me to have a better separation of the code in general and to have a better readability of the code.

## Singleton Pattern

In this project i have used the singleton pattern for the server and the client storage. I have used this pattern because i need to have only one instance of the server and the client storage because in praticaly every command i need to access information about the server or the client storage.

The singleton pattern also me to avoid mistakes when i was needing to store information about the server or the client storage.It is also more efficient than writing all modification on a file and read it every time.

## LinkedList

I have used 2 linked list in this project. The first one is use to store the client information on the server and the second one is use to store the music information on the client and on the server.

I have use this data structure because the information i store in this list change a lot and i need to be able to add and remove element easily.

## HashMap and TreeMap

I have used 2 HashMap in this project. The first one is use to store the command on the client and the second one is use to store the command on the server.

I have use this data structure because they provide a good performance and simple way implémentation to a key value structure.
This also allow me to find rapidly a command and to execute it.

The TreeMap is aslo really useful because it allow me to sort the command by alphabetical order when i print the help command.
