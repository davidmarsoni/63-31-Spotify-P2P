# User Manual

## Introduction

Welcome to SpotifyP2P, a peer-to-peer music streaming application. This application allows you to stream music from one device to another. It is based on a client-server architecture, where the server is used to synchronize the music files between the clients. The server is also used to keep track of the clients and their music files.

## How to use the application

To use the application, you need to start the server and the clients.

You can found more information about how to build and start the application jar here :

- [Build the project](Build.md)
- [Quick Start Guide](QuickStart.md)

## Commands

### Client

#### Config

This command allows you to print the current configuration of the client.

```bash
config
```

#### Connect

This command allows you to connect to the server. You need to be connected to the server to use a lot of the command of the client.

```bash
connect
```

n.b If you need to change the server address you can look for the command init.

#### Disconnect

This command allows you to disconnect from the server.

```bash
disconnect
```

#### Exit

This command allows you to exit the program. and disconnect safely from the server if you still connected.

```bash
exit
```

#### Help

This command allows you to print the list of all the commands available.

Usage:

- `help` print the list of all the commands available
- `help <command>` print the description of a specific command

#### Init

This command allows you the configuration of the client.
At the start of the program you can change this configuration on the client :

- The server address
- The server port
- The client listening port

After the client start you cannot change anymore the client listening port.

```bash
init
```

#### List

List all the musics available on the server

Usage:

- `list` get all the musics/playlist available on the server
- `list <musicName>` get details about a specific music/playlist (it get the first music/playlist that match the name)
- `list <musicName> <host:port>` get details about a specific music/playlist on a specific host

#### Ping

This command is used to ping the server

Usage:

- `ping` ping the server
- `ping <nubmer>` ping the server nubmer times (Between 1 and 10)

#### Play

This command is used to play in streaming a file from the server

Usage:

- `play <file name>` to play a file from the server (the first file name like this will be Streamed)
- `play <file name> <ip and port of the host>` to play a file from a specific person

Example:

- `play test.mp3` to play the file test.mp3 from the host that have the file
- `play "music for test.mp3 192.169.1.10:12345` to play the file test.mp3 from a specific person

#### Share

This command is used to share a file with other users

Usage:

- `share <file name>` to share a music file with other users
- `share <playlist path> <playlist name>` to share a playlist with other users

n.b: you must put absolute path to the file
n.b2: if you have a space in your file name you must put it between double quote or simple quote.

#### Unshare

This command is used to unshare a file with other users

Usage:

- `unshare <file name>` to unshare a music file with other users
- `unshare <playlist name>` to unshare a playlist with other users

n.b: you must put absolute path to the file
n.b2: if you have a space in your file name you must put it between double quote or simple quote.
