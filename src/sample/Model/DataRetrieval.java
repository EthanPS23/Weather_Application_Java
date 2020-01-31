package sample.Model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
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
        try {
            // create doc object and use JSoup to fetch the website
            Document doc = Jsoup.connect("https://avalanche.pc.gc.ca/station-eng.aspx?d=TODAY&r=1").get();
            // with doc fetched, we use Jsoups title() method to fetch the title
            System.out.printf("Title: %s\n", doc.title());

            // print new line
            System.out.println();

            //get the list of regions
            Elements regions = doc.getElementsByClass("tabpanels").select("details[id]");
            //Elements regions = tabs.select("details[id]");

            /**
             * For each region, extract the following:
             * 1. Name
             *
             */
            Integer i = 0; // regions have an index starting with 0

            // go through all of the regions to get the required data
            for (Element region: regions){
                // Extract the region name
                String regionName = region.getElementsByAttributeValue("id", "pageContent_rptRegions_regionTitle_"+i).text();

                // print the region name
                System.out.println(regionName);

                Integer j=0; // each station is indexed starting at 0 for the id value

                // get the list of stations for the given region
                Elements stations = regions.get(i).getElementsByClass("toggleStationDetails");

                // go through the stations for the given region to obtain the required data
                for (Element station:stations){
                    // get the station name based for the given region with the station id
                    String stationName = station.getElementsByAttributeValue("id","pageContent_rptRegions_rptStations_" + i + "_lblStationName_" + j).text();

                    // print the station name
                    System.out.println("\t" + stationName);
                    j++;
                }
                // print new line
                System.out.println();
                i++;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
