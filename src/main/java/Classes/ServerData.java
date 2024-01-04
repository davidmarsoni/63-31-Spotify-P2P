package Classes;

import java.net.*;
import java.util.*;

public class ServerData {
    public InetAddress address;
    public int port = 45000;
    public LinkedList<Entry> entries = new LinkedList<Entry>();
    public LinkedList<Client> clients = new LinkedList<Client>();
}
