package sample.Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataRetrieval {
    private static String ParksCanadaWeatherStationURL = "https://avalanche.pc.gc.ca/station-eng.aspx?d=";

    // create the links required for getting the date. Returns th list of links
    public static List<String> GenerateLink(){
        List<String> links =new ArrayList<String>();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();

        // creating the links for the available data. Data Started becoming readily available in 2016.
        // Starting in 2016 and adding days until the todays current date is reached
        for (LocalDate date = LocalDate.of(2016,1,1); today.compareTo(date) != 0 ; date = date.plusDays(1)) {
            links.add(ParksCanadaWeatherStationURL + dateFormat.format(date).toString());
        }

        return links;
    }

    // obtains the html of the url
    public static String ObtainHtml(String urlAddress){
        return "";
    }
}
