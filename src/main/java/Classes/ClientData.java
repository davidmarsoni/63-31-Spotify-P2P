package Classes;

import java.net.*;
import java.util.*;

/**
 * This class is use to store information about a client for json serialization
 */
public class ClientData {
    public InetAddress serverAddress;
    public int serverPort = 45000;
    public InetAddress clientAddress;
    public int listeningPort = 40000;
    public LinkedList<Entry> entries = new LinkedList<Entry>();
}
