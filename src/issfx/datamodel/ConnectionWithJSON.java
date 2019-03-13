package issfx.datamodel;

import java.io.*;
import java.net.URL;

public class ConnectionWithJSON {
    private final URL url = new URL("http://api.open-notify.org/iss-now.json");
    private BufferedReader in;


    public ConnectionWithJSON() throws IOException {
        this.in = new BufferedReader(new InputStreamReader(url.openStream()));
    }

    public BufferedReader getIn() {
        return in;
    }

}

