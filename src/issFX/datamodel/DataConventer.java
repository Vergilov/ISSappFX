package issFX.datamodel;

import java.util.Date;

public interface DataConventer {
    static Date epochConventer(Long date) {
        Date expiry = new Date(date * 1000);
        return expiry;
    }

    static double differenceTime(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return (double) diff / (60 * 60 * 1000); //in hours because of KM/H final output
    }
}
