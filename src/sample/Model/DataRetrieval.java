package sample.Model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
            Elements regionsElements = doc.getElementsByClass("tabpanels").select("details[id]");
            //Elements regions = tabs.select("details[id]");

            /**
             * For each region, extract the following:
             * 1. Region name
             * 2. station name
             * 3. station elevation
             * 4. station description
             * 5. station latitude
             * 6. station longitude
             * 7.
             */
            Integer regionNum = 0; // regions have an index starting with 0

            // go through all of the regions to get the required data
            for (Element region: regionsElements){
                // Extract the region name
                String regionName = region.getElementsByAttributeValue("id", "pageContent_rptRegions_regionTitle_"+ regionNum).text();

                RegionDB.SetRegions(new Region(regionNum+1,regionName));

                // print the region name
                System.out.println(regionName);


                // get the list of stations for the given region
                Element stations = regionsElements.get(regionNum)/**/;

                StationData(stations, regionNum);

                // print new line
                System.out.println();
                regionNum++;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    // will receive the region details, go through get the stations, ang get the stations info including
    // name, description, lat, long, elevation, and weather
    private static void StationData(Element region,Integer regionNum){

        StationInfo(region,regionNum);



        /*// go through the stations for the given region to obtain the required data
//        for (Element station:stations){
            // get the station name based for the given region with the station id


            Elements stationDataBox = regions.get(i).getElementsByClass("stationDataBox");

            String description = stationDataBox.get(j)*//*.select("div[row]")*//*.select("div.col-sm-10").text();

            // print the station name
            System.out.println("\t" + stationName + " - " + description.substring(0,10) + "...");
            j++;
//        }*/
    }

    // finds the stations information
    private static void StationInfo(Element region,Integer regionNum){
        StationInfoDB.GetStationInfo();

        try {
            // prepare lists for the stations
            List<Station> stations = new ArrayList<Station>();
            List<StationInfo> stationInfos = new ArrayList<StationInfo>();

            // initialize variables
            Integer stationNum = 0;
            List<String> stationNameStr = new ArrayList<String>();
            List<Integer> elevation = new ArrayList<Integer>();
            List<String> description = new ArrayList<String>();
            List<Double> latitude = new ArrayList<Double>();
            List<Double> longitude = new ArrayList<Double>();

            // Find all the stations details
            Elements stationNames = region.getElementsByClass("toggleStationDetails");

            // go through all the station details to get a stations name and elevation
            for (Element stationName : stationNames) {
                // obtain the stations name and elevation
                String name = stationName.getElementsByAttributeValue("id", "pageContent_rptRegions_rptStations_" + regionNum + "_lblStationName_" + stationNum).text();
                stationNum++;

                // split the station name into separate name and elevation
                String[] str = name.split("(?<=\\s)(?=\\d)");
                if (str.length == 2) {
                    stationNameStr.add(str[0]);
                    String ele = str[1].substring(0,str[1].length()-1); // remove the meters unit from end of elevation
                    elevation.add(Integer.parseInt(ele)); // convert elevation string to Integer
                }
            }

            stationNum = 0;
            // obtain  all the stations data boxes
            Elements stationsDataBox = region.getElementsByClass("stationDataBox");

            // go through stations data boxes to get the latitude, longitude, and descriptions
            for (Element stationDataBox : stationsDataBox) {
                Elements rows = stationDataBox.getElementsByClass("row");

                // obtain the stations description
                description.add(rows.select("div.col-sm-10").text());

                // begin looking for the longitude and latitude of stations
                Elements latLong = rows.select("span");
                if (latLong.size() == 2) {
                    // obtain the stations longitude and latitude
                    String lat = latLong.get(0).select("span").text();
                    String lon = latLong.get(1).select("span").text();

                    // convert the longitude and latitude to double
                    latitude.add(Double.parseDouble(lat));
                    longitude.add(Double.parseDouble(lon));
                }
            }

            // add the information to the lists
            for (stationNum = 0; stationNum < stationNameStr.size(); stationNum++) {
                // as the stationID and regionID indexes start at 1 for the sql database we need to add one to the indexes
                stations.add(new Station(stationNum + 1,stationNameStr.get(stationNum)));
                stationInfos.add(new StationInfo(stationNum+1, regionNum+1,elevation.get(stationNum),
                        latitude.get(stationNum),longitude.get(stationNum),description.get(stationNum)));
            }

            // set the stations in the SQL database
            StationDB.SetStations(stations);

            // set the station info in the sql database
            StationInfoDB.SetStationInfo(stationInfos);

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    // obtains the stations weather
    private  static void StationWeather(){

    }
}
