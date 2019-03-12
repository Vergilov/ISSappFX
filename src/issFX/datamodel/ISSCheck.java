package issFX.datamodel;

import org.json.JSONObject;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ISSCheck {
    private List<String> arrayList;

    public ISSCheck() {
        this.arrayList = new ArrayList<>();
    }

    public String calculateSpeedFromLastTwoPoints() {
        JSONObject oneBeforeLastJSONObject = createJSONOBject(arrayList.get(arrayList.size() - 2));
        JSONObject lastJSONObject = createJSONOBject(this.arrayList.get(arrayList.size() - 1));

        double oneBeforeLastLatidue = JSONDataOutput.getLatitude(oneBeforeLastJSONObject);
        double oneBeforeLastLongtitude = JSONDataOutput.getLongitude(oneBeforeLastJSONObject);
        double lastLatitude = JSONDataOutput.getLatitude(lastJSONObject);
        double lastLongtitude = JSONDataOutput.getLongitude(lastJSONObject);
        Date oneBeforeLastDate = DataConventer.epochConventer(JSONDataOutput.getTimestamp(oneBeforeLastJSONObject));
        Date lastDate = DataConventer.epochConventer(JSONDataOutput.getTimestamp(lastJSONObject));
        int result = (int) ((distance(lastLatitude, oneBeforeLastLatidue, lastLongtitude, oneBeforeLastLongtitude)) / (DataConventer.differenceTime(oneBeforeLastDate, lastDate)));

        return "" + result; // distance/ diffrenceTime
    }


    private double calculateDistanceBetweenTwoPoints(String one, String two) {
        JSONObject first = createJSONOBject(one);
        double firstLatitude = JSONDataOutput.getLatitude(first);
        double firstLongitude = JSONDataOutput.getLongitude(first);

        JSONObject second = createJSONOBject(two);
        double secondLatitude = JSONDataOutput.getLatitude(second);
        double secondLongtidute = JSONDataOutput.getLongitude(second);
        return distance(secondLatitude, firstLatitude, secondLongtidute, firstLongitude);
    }

    public String calculateOverallDistance() {
        double result = 0;
        for (int i = 0; i <= arrayList.size() - 2; i++) {
            double current = calculateDistanceBetweenTwoPoints(arrayList.get(i), arrayList.get(i + 1));
            result += current;
        }
        return "" + (int) result + " KM";
    }

    private JSONObject createJSONOBject(String obj) {
        return new JSONObject(obj);
    }


    private double distance(double lat1, double lat2, double lon1,
                            double lon2) {
        //Haversine_formula
        final int R = 6371; // Radius of the earth
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        distance = Math.pow(distance, 2) + Math.pow(0.0, 2);

        return (Math.sqrt(distance)) / 1000; //return distance in KM
    }

    public void addJSONtoArray() throws Exception {
        arrayList.add(JSONCreator.buildJSON().toString());
    }


    public void backupToFile() throws Exception {
        ConnectionWithJSON connectionWithJSON = new ConnectionWithJSON();
        File desktop = new File(System.getProperty("user.home"), "/Desktop/ISS/Backup");
        if (!desktop.exists()) {
            desktop.mkdirs();
        }
        File output = new File(desktop.getAbsolutePath(), "ISSBackup.json");
        PrintWriter pw = new PrintWriter(new FileWriter(output, true));

        try {
            LineNumberReader reader = new LineNumberReader(connectionWithJSON.getIn());
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                pw.println(inputLine);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            pw.close();
            connectionWithJSON.getIn().close();
        }
    }


    public void saveToFile() throws Exception {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        File desktop = new File(System.getProperty("user.home"), "/Desktop/ISS/SavedLog");
        if (!desktop.exists()) {
            desktop.mkdir();
        }
        String time = (timeStamp + "ISS.txt");
        File output = new File(desktop, time);
        PrintWriter pw = new PrintWriter(output);
        try {
            for (String inputLine : arrayList) {
                pw.println(inputLine);
            }
            System.out.println("Created file: " + output.getAbsolutePath());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            pw.close();
        }
    }

    public List<String> getArrayList() {
        return arrayList;
    }

}
