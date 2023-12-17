package Classes;

import java.net.*;
import java.util.*;

public class ClientData {
    public InetAddress serverAddress;
    public int serverPort = 45000;
    public InetAddress clientAddress;
    public int clientPort = 40000;
    public LinkedList<Entry> entries = new LinkedList<Entry>();
}
