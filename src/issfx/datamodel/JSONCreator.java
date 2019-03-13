package issfx.datamodel;

import issfx.Controller;
import org.json.JSONObject;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public interface JSONCreator {
    Logger LOGGER = Logger.getLogger(Controller.class.getName());

    static String jsonAsString() throws Exception {
        ConnectionWithJSON json = new ConnectionWithJSON();
        String string = "";
        try (Scanner scan = new Scanner(json.getIn())) {
            while (scan.hasNext()) {
                string += scan.nextLine();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.FINE, "Message: ", ex);
        }
        return string;
    }

    static JSONObject buildJSON() throws Exception {
        String str = jsonAsString();
        JSONObject obj = new JSONObject(str);
        if (!isOnline(obj)) {
            return null;
        } else {
            return obj;
        }
    }

    static boolean isOnline(JSONObject obj) {
        boolean isOnline = true;
        try {
            if (!obj.getString("message").equals("success")) {
                isOnline= false;
            }
        } catch (Exception ex){
            LOGGER.log( Level.FINE, "Message: ", ex );
        }
        return isOnline;
    }
}
