package sample.Model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.print.DocFlavor;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

                StationData(stations, regionNum); // a region contains many stations

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

    }

    // finds the stations information
    private static void StationInfo(Element region,Integer regionNum){

        try {
            // prepare lists for the stations
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
                //StationInfoDB.SetStationInfo(new StationInfo(stationNum+1,regionNum+1,))

                // Obtain the station weather
                StationWeather(stationDataBox,stationNum,regionNum);
                stationNum++;
            }
            stationNum = 0;
            // add the information to the lists
            for (stationNum = 0; stationNum < stationNameStr.size(); stationNum++) {
                // as the stationID and regionID indexes start at 1 for the sql database we need to add one to the indexes
//                stations.add(new Station(stationNum + 1,stationNameStr.get(stationNum)));
                stationInfos.add(new StationInfo(stationNum+1, regionNum+1, stationNameStr.get(stationNum),
                        elevation.get(stationNum),latitude.get(stationNum),longitude.get(stationNum),description.get(stationNum)));
            }

            // set the station info in the sql database
            StationInfoDB.SetStationInfo(stationInfos);

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    // obtains the stations weather
    private  static void StationWeather(Element stationDataBox, Integer stationNum, Integer regionNum){
        try{
            List<Weather> weather = new ArrayList<Weather>();
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");


            Element table = stationDataBox.select("table").first(); // currently just trying with the first table of data
            String dateStr = table.select("caption").first().text();
            LocalDate date = LocalDate.parse(dateStr,dateFormat);
            Elements rows = table.select("tr");

            /* As the tables for each station have different columns of data (ie wind, vs snow) I need to look at how to enter into the correct columns of the database
            * Could use a hashmap to associate a column header to a column index. With the dictionary, we then associate the column header to the column in our database,
            * using the key to get the column index from a table that is created containing the data of the station.
            * If a key doesn't exist and return a null value for that column instead of trying to obtain the value from the array/list of data
            *
            * */
            HashMap<String, Integer> columnHeaders = new HashMap<String, Integer>(); // to contain the column name with its column number
            Elements firstRow = rows.get(0).select("th"); // get the table headers

            
            // go through the table headers to put into the hashtable
            for (int i =0; i<firstRow.size(); i++){
                columnHeaders.put(firstRow.get(i).text(),i);
            }

            for (int i = 1; i < rows.size(); i++) {
                Elements columns =  rows.get(i).select("td"); // get the column values for row i
                Weather entry =  new Weather();

                entry.setStationID(stationNum);
                entry.setRegionID(regionNum);
                entry.setDate(date); // only for current test case

                Integer hour = Integer.parseInt(columns.get(0).text().substring(0,2)); // the hour column is always the first column, remove the h at the end of string
                entry.setHour(hour);

                Integer temp = Integer.parseInt(columns.get(1).text()); // the temp column is always the second column
                entry.setTemp(temp);

                Integer relHumNum = columnHeaders.get("RH") != null ? columnHeaders.get("RH") : null; // check to see if there is a relative humidity column
                Integer relHum = relHumNum == null ? null : Integer.parseInt(columns.get(relHumNum).text()); // get the relative humidity column value
                entry.setRel_Hum(relHum);

                Integer snowPackNum = columnHeaders.get("Snow Pack (cm)") != null ? columnHeaders.get("Snow Pack (cm)") : null; // check to see if there is a snow pack column
                Integer snowPack = snowPackNum == null ? null : Integer.parseInt(columns.get(snowPackNum).text()); // get the snow pack column value
                entry.setSnow_Pack(snowPack);

                Integer snowNewNum = columnHeaders.get("Snow New (cm)") != null ? columnHeaders.get("Snow New (cm)") : null; // check to see if there is a snow new column
                Integer snowNew = snowNewNum == null ? null : Integer.parseInt(columns.get(snowNewNum).text()); // get the snow new column value
                entry.setSnow_New(snowNew);

                Integer precipNewNum = columnHeaders.get("Precip. New (mm)") != null ? columnHeaders.get("Precip. New (mm)") : null; // check to see if there is a precip new column
                Integer precipNew = precipNewNum == null ? null : Integer.parseInt(columns.get(precipNewNum).text()); // get the precip new column value
                entry.setPrecip_New(precipNew);

                Integer hrSnowNum = columnHeaders.get("24Hr Snow (cm)") != null ? columnHeaders.get("24Hr Snow (cm)") : null; // check to see if there is a 24 snow column
                Integer hrSnow = hrSnowNum == null ? null : Integer.parseInt(columns.get(hrSnowNum).text()); // get the 24 snow column value
                entry.setHr_Snow(hrSnow);

                Integer windSpeedNum = columnHeaders.get("Wind Speed (km/h)") != null ? columnHeaders.get("Wind Speed (km/h)") : null; // check to see if there is a wind speed column
                Integer windSpeed = windSpeedNum == null ? null : Integer.parseInt(columns.get(windSpeedNum).text()); // get the wind speed column value
                entry.setWind_Speed(windSpeed);

                Integer maxWindSpeedNum = columnHeaders.get("Max Wind Speed (km/h)") != null ? columnHeaders.get("Max Wind Speed (km/h)") : null; // check to see if there is a max wind speed column
                Integer maxWindSpeed = maxWindSpeedNum == null ? null : Integer.parseInt(columns.get(maxWindSpeedNum).text()); // get the max wind speed column value
                entry.setWind_Speed(maxWindSpeed);

                Integer windDirNum = columnHeaders.get("Wind Dir.") != null ? columnHeaders.get("Wind Dir.") : null; // check to see if there is a wind dir column
                Integer windDir = windDirNum == null ? null : Integer.parseInt(columns.get(windDirNum).text()); // get the wind dir column value
                entry.setWind_Speed(windDir);

                weather.add(entry);
            }

            stationNum++;

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
