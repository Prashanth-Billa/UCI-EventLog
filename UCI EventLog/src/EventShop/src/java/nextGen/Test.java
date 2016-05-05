//package nextGen;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.ProtocolException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Iterator;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
//
//class EventClass{
//    double lat;
//    double lon;
//    String title;
//    String location;
//    String date;
//    String emotion;
//    String imageUrl;
//    Double haversineDistance;
//}
//
//
//public class Test {
//    
//    static ArrayList <EventClass> sportsEvents = new ArrayList <EventClass> ();
//    static ArrayList <EventClass> musicEvent = new ArrayList <EventClass> ();
//    static ArrayList <EventClass> technologyEvents = new ArrayList <EventClass> ();
//    static ArrayList <EventClass> artEvents = new ArrayList <EventClass> ();
//    static ArrayList <EventClass> freeGoodies = new ArrayList <EventClass> ();
//    static String userLatitude = "0.0";//request.getParameter("latitude"); - will be getting in servlet
//    static String userLongitude = "0.0";//request.getParameter("longitude");
//    static Double latitudeuser = Double.valueOf(userLatitude);
//    static Double longitudeuser = Double.valueOf(userLongitude);
//    
//    public static Double getHaversineDistance(Double lat1, Double lon1, Double lat2, Double lon2){
//        final int R = 6371;
//        Double latDistance = toRad(lat2-lat1);
//        Double lonDistance = toRad(lon2-lon1);
//        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + 
//                   Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) * 
//                   Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
//        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
//        return R * c;
//     }
//     
//     public static Double toRad(Double value) {
//        return value * Math.PI / 180;
//    }
//     
//    static class haversineComparator implements Comparator<EventClass> {
//    @Override
//    public int compare(EventClass o1, EventClass o2) {
//         return o1.haversineDistance.compareTo(o2.haversineDistance);
//    }
//    }
//    
//    public static void main(String[] args){
//        
//     
//        
//         
//    String output = "";
//                    try{
//    URL restServiceURL = null;
//        try {
//            restServiceURL = new URL("http://sln.ics.uci.edu:8085/eventshoplinux/rest/sttwebservice/search/181/box/null/null/null/null");
//        } catch (MalformedURLException ex) {
//            Logger.getLogger(EventLogServlet.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//			HttpURLConnection httpConnection = null;
//        try {
//            httpConnection = (HttpURLConnection) restServiceURL.openConnection();
//        } catch (IOException ex) {
//            Logger.getLogger(EventLogServlet.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        try {
//            httpConnection.setRequestMethod("GET");
//        } catch (ProtocolException ex) {
//            Logger.getLogger(EventLogServlet.class.getName()).log(Level.SEVERE, null, ex);
//        }
//			httpConnection.setRequestProperty("Accept", "application/json");
//        try {
//            if (httpConnection.getResponseCode() != 200) {
//                    throw new RuntimeException("HTTP GET Request Failed with Error code : " + httpConnection.getResponseCode());
//            }
//        } catch (IOException ex) {
//            Logger.getLogger(EventLogServlet.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//			BufferedReader responseBuffer = null;
//        try {
//            responseBuffer = new BufferedReader(new InputStreamReader((httpConnection.getInputStream())));
//        } catch (IOException ex) {
//            Logger.getLogger(EventLogServlet.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//			
//                        StringBuilder str = new StringBuilder();
//                        str.append("{\"MAINCONTENT\": ");
//        try {
//            while ((output = responseBuffer.readLine()) != null) {
//                    str.append(output);
//            }
//        } catch (IOException ex) {
//            Logger.getLogger(EventLogServlet.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        str.append("\n}");
//        parseJson(str.toString());
//
//			httpConnection.disconnect();
//}catch(Exception e){
//    
//}
//    }
//
//    private static void parseJson(String json) throws ParseException {
//        JSONParser jsonParser = new JSONParser();
//	JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
//        JSONArray lang= (JSONArray) jsonObject.get("MAINCONTENT");
//        JSONArray content = null;
//        Iterator c = null;
//        Iterator i = lang.iterator();
//        String coords = "";
//        Double latitude = 0.0;
//        Double longitude = 0.0;
//        String caption = "";
//        while (i.hasNext()) {
//		JSONObject innerObj = (JSONObject) i.next();
//                coords = innerObj.get("stt_where").toString();
//                latitude = Double.valueOf(coords.substring(coords.indexOf("[") + 1, coords.indexOf("]")).split(",")[0]);
//                longitude = Double.valueOf(coords.substring(coords.indexOf("[") + 1, coords.indexOf("]")).split(",")[1]);
//                caption = getCaption(innerObj.get("stt_what").toString(), "caption");
//                caption = caption.substring(caption.indexOf("\"")+1, caption.lastIndexOf(","));
//                caption.replaceAll("(\\r|\\n)", "");
//                EventClass e = new EventClass();
//            e.lat = latitude;
//            e.lon = longitude;
//            e.haversineDistance = getHaversineDistance(latitudeuser, longitudeuser, latitude, longitude);
//            e.title = caption;
//            e.emotion = getEmotion(innerObj.get("stt_what").toString(), "intent_name");
//            e.imageUrl = getImageUrl(innerObj.get("stt_what").toString(), "media_source_photo");
//            
//            if(caption.contains("#art")){
//                artEvents.add(e);
//            }else if(caption.contains("#freefood") || caption.contains("drink")){
//                freeGoodies.add(e);
//            }else if(caption.contains("#sport")){
//                sportsEvents.add(e);
//            }else if(caption.contains("#technology")){
//                technologyEvents.add(e);
//            }
//            
//            Collections.sort(artEvents, new haversineComparator());
//            Collections.sort(freeGoodies, new haversineComparator());
//            Collections.sort(sportsEvents, new haversineComparator());
//            Collections.sort(technologyEvents, new haversineComparator());
//            
//            for(int m = 0; m < freeGoodies.size(); m++){
//                System.out.println(freeGoodies.get(m).imageUrl);
//            }
//                
//        }
//    }
//    
//    private static String getCaption(String content, String title) throws ParseException{
//        JSONParser jsonParser = new JSONParser();
//	JSONObject jsonObject = (JSONObject) jsonParser.parse(content);
//        return jsonObject.get(title).toString().split(":")[1];
//    }
//    
//    private static String getImageUrl(String content, String title) throws ParseException{
//        JSONParser jsonParser = new JSONParser();
//	JSONObject jsonObject = (JSONObject) jsonParser.parse(content);
//        String temp = jsonObject.get(title).toString();
//        temp = temp.substring(temp.indexOf("{") + 1, temp.lastIndexOf("}"));
//        temp = temp.substring(temp.indexOf(":") + 1);
//        temp = temp.replace("\\", "");
//        return temp.substring(temp.indexOf("\"") + 1, temp.lastIndexOf("\""));
//    }
//    
//    private static String getEmotion(String content, String title) throws ParseException{
//        JSONParser jsonParser = new JSONParser();
//	JSONObject jsonObject = (JSONObject) jsonParser.parse(content);
//        String temp = jsonObject.get(title).toString().split(":")[1];
//        temp = temp.substring(temp.indexOf("\"") + 1, temp.lastIndexOf("\""));
//        
//        return temp;
//    }
//}
