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
        LocalDate today = LocalDate.now().plusDays(-1);

        // creating the links for the available data. Data Started becoming readily available in 2011.
        // Starting in 2016 and adding days until the todays current date is reached
        for (LocalDate date = LocalDate.of(2019,8,21); today.compareTo(date) != 0 ; date = date.plusDays(1)) {
            String link = ParksCanadaWeatherStationURL + dateFormat.format(date).toString();
            links.add(link);
            ObtainHtml(link);

        }

        return links;
    }

    // obtains the html of the url
    public static String ObtainHtml(String urlAddress){
        try {
            // create doc object and use JSoup to fetch the website
            Document doc = Jsoup.connect(urlAddress + "&r=1").get();
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
            String description;
            Double latitude;
            Double longitude;

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
                    stationNameStr.add(str[0].trim());
                    String[] str2  = str[1].split("(?<=\\d)(?=[m])"); // remove the meters unit from end of elevation, or if it say no data
                    String ele = str2[0].trim();
                    elevation.add(Integer.parseInt(ele)); // convert elevation string to Integer
                }else {
                    stationNameStr.add(str[0].trim());
                    elevation.add(0);
                }
            }

            stationNum = 0;
            // obtain  all the stations data boxes
            Elements stationsDataBox = region.getElementsByClass("stationDataBox");

            // go through stations data boxes to get the latitude, longitude, and descriptions
            for (Element stationDataBox : stationsDataBox) {
                Elements rows = stationDataBox.getElementsByClass("row");

                // if there is data then retrieve it. If not increase station number and continue
                if(rows.size()>0){
                    // obtain the stations description
                    description = rows.select("div.col-sm-10").text();

                    // begin looking for the longitude and latitude of stations
                    Elements latLong = rows.select("span");
                    if (latLong.size() == 2) {
                        // obtain the stations longitude and latitude
                        String lat = latLong.get(0).select("span").text();
                        String lon = latLong.get(1).select("span").text();

                        // convert the longitude and latitude to double
                        latitude = Double.parseDouble(lat);
                        longitude = Double.parseDouble(lon);
                    }
                    else {
                        latitude = 0.0;
                        longitude = 0.0;
                    }

                    if(!StationInfoDB.SetStationInfo(new StationInfo(stationNum+1, regionNum+1,
                            stationNameStr.get(stationNum),elevation.get(stationNum),latitude,longitude,description))){
                        //TODO: print there was an error inserting
                    }

                    // Obtain the station weather
                    StationWeather(stationDataBox,stationNum,regionNum);
                }

                stationNum++;
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    // obtains the stations weather
    private  static void StationWeather(Element stationDataBox, Integer stationNum, Integer regionNum){
        try{
            List<Weather> weather = new ArrayList<Weather>();
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("EEE MMM d, yyyy");

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

                entry.setStationID(stationNum+1);
                entry.setRegionID(regionNum+1);
                entry.setDate(date); // only for current test case

                Integer hour = Integer.parseInt(columns.get(0).text().substring(0,2)); // the hour column is always the first column, remove the h at the end of string
                entry.setHour(hour);

                Double temp = columns.get(1).text().isBlank() ? null : Double.parseDouble(columns.get(1).text()); // the temp column is always the second column
                entry.setTemp(temp);

                Integer relHumNum = columnHeaders.get("RH") != null ? columnHeaders.get("RH") : null; // check to see if there is a relative humidity column
                Double relHum = relHumNum == null ? null : // if there is no column for RH then integer will be null
                        columns.get(relHumNum).text().isBlank() ? null : Double.parseDouble(columns.get(relHumNum).text()); // get the relative humidity column value. If it is blank it is null
                entry.setRel_Hum(relHum);

                Integer snowPackNum = columnHeaders.get("Snow Pack (cm)") != null ? columnHeaders.get("Snow Pack (cm)") : null; // check to see if there is a snow pack column
                Double snowPack = snowPackNum == null ? null : // if there is no column for snow pack then integer will be null
                        columns.get(snowPackNum).text().isBlank() ? null : Double.parseDouble(columns.get(snowPackNum).text()); // get the snow pack column value. If it is blank it is null
                entry.setSnow_Pack(snowPack);

                Integer snowNewNum = columnHeaders.get("Snow New (cm)") != null ? columnHeaders.get("Snow New (cm)") : null; // check to see if there is a snow new column
                Double snowNew = snowNewNum == null ? null : // if there is no column for snow new then integer will be null
                        columns.get(snowNewNum).text().isBlank() ? null : Double.parseDouble(columns.get(snowNewNum).text()); // get the snow new column value. If it is blank it is null
                entry.setSnow_New(snowNew);

                Integer precipNewNum = columnHeaders.get("Precip. New (mm)") != null ? columnHeaders.get("Precip. New (mm)") : null; // check to see if there is a precip new column
                Double precipNew = precipNewNum == null ? null : // if there is no column for precip new then integer will be null
                        columns.get(precipNewNum).text().isBlank() ? null : Double.parseDouble(columns.get(precipNewNum).text()); // get the precip new column value. If it is blank it is null
                entry.setPrecip_New(precipNew);

                Integer hrSnowNum = columnHeaders.get("24Hr Snow (cm)") != null ? columnHeaders.get("24Hr Snow (cm)") : null; // check to see if there is a 24 snow column
                Double hrSnow = hrSnowNum == null ? null : // if there is no column for 24hr snow then integer will be null
                        columns.get(hrSnowNum).text().isBlank() ? null : Double.parseDouble(columns.get(hrSnowNum).text()); // get the 24 snow column value. If it is blank it is null
                entry.setHr_Snow(hrSnow);

                Integer windSpeedNum = columnHeaders.get("Wind Speed (km/h)") != null ? columnHeaders.get("Wind Speed (km/h)") : null; // check to see if there is a wind speed column
                Double windSpeed = windSpeedNum == null ? null : // if there is no column for wind speed then integer will be null
                        columns.get(windSpeedNum).text().isBlank() ? null : Double.parseDouble(columns.get(windSpeedNum).text()); // get the wind speed column value. If it is blank it is null
                entry.setWind_Speed(windSpeed);

                Integer maxWindSpeedNum = columnHeaders.get("Max Wind Speed (km/h)") != null ? columnHeaders.get("Max Wind Speed (km/h)") : null; // check to see if there is a max wind speed column
                Double maxWindSpeed = maxWindSpeedNum == null ? null : // if there is no column for max wind speed then integer will be null
                        columns.get(maxWindSpeedNum).text().isBlank() ? null : Double.parseDouble(columns.get(maxWindSpeedNum).text()); // get the max wind speed column value. If it is blank it is null
                entry.setMax_Wind_Speed(maxWindSpeed);

                Integer windDirNum = columnHeaders.get("Wind Dir.") != null ? columnHeaders.get("Wind Dir.") : null; // check to see if there is a wind dir column
                String windDir = windDirNum == null ? null : "'" + columns.get(windDirNum).text() + "'"; // get the wind dir column value
                entry.setWind_Dir(windDir);

                //WeatherDB.InsertWeather(entry);

                weather.add(entry);
            }
            WeatherDB.InsertWeather(weather);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
