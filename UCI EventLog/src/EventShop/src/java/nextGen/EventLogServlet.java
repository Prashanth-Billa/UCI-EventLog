package nextGen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


/**
 *
 * @author Prash
 */
class EventClass{
    double lat = 0.0;
    double lon = 0.0;
    String title = "";
    String location = "";
    String date = "";
    String emotion = "";
    String imageUrl = "";
    Double haversineDistance = 0.0;
    String audioUrl = "";
    String category = "";
}

@WebServlet(name = "EventLogServlet", urlPatterns = {"/EventLogServlet"})
public class EventLogServlet extends HttpServlet {
        ArrayList <EventClass> sportsEvents = new ArrayList <EventClass> ();
        ArrayList <EventClass> musicEvent = new ArrayList <EventClass> ();
        ArrayList <EventClass> technologyEvents = new ArrayList <EventClass> ();
        ArrayList <EventClass> artEvents = new ArrayList <EventClass> ();
        ArrayList <EventClass> freeGoodies = new ArrayList <EventClass> ();
        ArrayList <EventClass> gatheringEvents = new ArrayList <EventClass> ();
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        sportsEvents.clear();
        musicEvent.clear();
        technologyEvents.clear();
        artEvents.clear();
        freeGoodies.clear();
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet EventLogServlet</title>");  
            out.println("<script src='main.js'></script>");
            out.println("<script src='lightbox-plus-jquery.min.js'></script>");
            out.println("<link rel='stylesheet' type='text/css' href='style.css'>");
            out.println("<link rel='stylesheet' href='lightbox.min.css'>");
            out.println("</head>");
            out.println("<body>");
            
        String userLatitude = request.getParameter("latitude");
        String userLongitude = request.getParameter("longitude");
        Double latitudeuser = Double.valueOf(userLatitude);
        Double longitudeuser = Double.valueOf(userLongitude);
        String emotion = request.getParameter("emotion");
        String currentEmotion = emotion;
        if("all".equals(currentEmotion)){
            currentEmotion = "None";
        }
         
        String output = "";
    URL restServiceURL = null;
        try {
            restServiceURL = new URL("http://sln.ics.uci.edu:8085/eventshoplinux/rest/sttwebservice/search/181/box/null/null/null/null");
        } catch (MalformedURLException ex) {
        }

			HttpURLConnection httpConnection = null;
        try {
            httpConnection = (HttpURLConnection) restServiceURL.openConnection();
        } catch (IOException ex) {
        }
        try {
            httpConnection.setRequestMethod("GET");
        } catch (ProtocolException ex) {
        }
			httpConnection.setRequestProperty("Accept", "application/json");
        try {
            if (httpConnection.getResponseCode() != 200) {
                    throw new RuntimeException("HTTP GET Request Failed with Error code : " + httpConnection.getResponseCode());
            }
        } catch (IOException ex) {
        }

			BufferedReader responseBuffer = null;
        try {
            responseBuffer = new BufferedReader(new InputStreamReader((httpConnection.getInputStream())));
        } catch (IOException ex) {
        }

			
                        StringBuilder str = new StringBuilder();
                        str.append("{\"MAINCONTENT\": ");
        try {
            while ((output = responseBuffer.readLine()) != null) {
                    str.append(output);
            }
        } catch (IOException ex) {
        }
        str.append("\n}");
        
        JSONParser jsonParser = new JSONParser();
	JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) jsonParser.parse(str.toString());
        } catch (org.json.simple.parser.ParseException ex) {
        }
        JSONArray lang= (JSONArray) jsonObject.get("MAINCONTENT");
        JSONArray content = null;
        Iterator c = null;
        Iterator i = lang.iterator();
        String coords = "";
        Double latitude = 0.0;
        Double longitude = 0.0;
        String caption = "";
        
        while (i.hasNext()) {
		JSONObject innerObj = (JSONObject) i.next();
                coords = innerObj.get("stt_where").toString();
                latitude = Double.valueOf(coords.substring(coords.indexOf("[") + 1, coords.indexOf("]")).split(",")[0]);
                longitude = Double.valueOf(coords.substring(coords.indexOf("[") + 1, coords.indexOf("]")).split(",")[1]);
            try {
                caption = getCaption(innerObj.get("stt_what").toString(), "caption");
            } catch (ParseException ex) {
            } catch (org.json.simple.parser.ParseException ex) {
            }
                caption = caption.substring(caption.indexOf("\"")+1, caption.lastIndexOf(","));
                caption.replaceAll("(\\r|\\n)", "");
                EventClass e = new EventClass();
            e.lat = latitude;
            e.lon = longitude;
            e.haversineDistance = getHaversineDistance(latitudeuser, longitudeuser, latitude, longitude);
            e.title = caption;
            try {
                e.emotion = getEmotionCaption(innerObj.get("stt_what").toString(), "intent_used_synonym");
            } catch (ParseException ex) {
            } catch (org.json.simple.parser.ParseException ex) {
                out.println(ex.getMessage());
            }
            
            if(!e.emotion.toLowerCase().contains(emotion) && !"all".equals(emotion)){
                continue;
            }
            
            
            try {
                e.imageUrl = getImageUrl(innerObj.get("stt_what").toString(), "media_source_photo");
            } catch (ParseException ex) {
            } catch (org.json.simple.parser.ParseException ex) {
            }
               e.audioUrl = "";
        
            
            
            String cc = caption.toLowerCase();
            if(cc == "" || cc == null){
                continue;
            }
            
            if(cc.contains("freefood") || cc.contains("drink")){
                freeGoodies.add(e);
            }else if(cc.contains("sport") || (cc.contains("cool") && !cc.contains("technology"))){
                sportsEvents.add(e);
            }else if(cc.contains("art")){
                artEvents.add(e);
            }else if(cc.contains("seminar") || cc.contains("note")){
                technologyEvents.add(e);
            }else if(cc.contains("friends") || cc.contains("healthy")){
                gatheringEvents.add(e);
            }else if(cc.contains("celebrate")){
                musicEvent.add(e);
            }
        }
        Collections.sort(artEvents, new haversineComparator());
        Collections.sort(freeGoodies, new haversineComparator());
        Collections.sort(sportsEvents, new haversineComparator());
        Collections.sort(technologyEvents, new haversineComparator());
        Collections.sort(gatheringEvents, new haversineComparator());
        Collections.sort(musicEvent, new haversineComparator());
        DecimalFormat df = new DecimalFormat("#.####");
            String trimtitle = "";
            String imgu = "";
        String showSortText = "<br/><div align='center'>Currently sorted by : " + "distance" + "<br/><br/><b>Filter By Emotion</b><span>  </span><select id='emotionid' onchange='changeEmotion()'><option value=''></option><option value='all'>All</option><option value='junk'>Junk Food</option><option value='celebrate'>Celebrate</option><option value='funny'>Funny</option><option value='yawn'>Yawn</option><option value='notes'>Notes</option></select><br/><br/>Currently Sorted By Emotion : <span> </span> <b> " + currentEmotion + "</b></div>";
        String html = "<div align='center'><a href='#' id='buttonc' onClick='window.location.reload()' class='myButton'>Refresh my EventShop Events!</a><br/><br/><br/> " + showSortText + "<br/><br/></div><article class='accordion'>";
        String href = "";
        out.println("<div align='center'><img src='uci.png' width='60px'/><br/><img src='uc.jpg' width='100px'/></div><br/>");
            out.println(html);
            out.println("<section id='acc1'><h2><span style='height:10px'><a href='#acc1'>Free Goodies <img src='food.png' width='50px'/> (" + freeGoodies.size() + ")</a></span></h2>");
            for(int j = 0; j < freeGoodies.size(); j++){
                imgu = "<a class='example-image-link1' href='" + freeGoodies.get(j).imageUrl + "' data-lightbox='example-1'><img width='150px' height='150px' class='example-image' src='" + freeGoodies.get(j).imageUrl + "' alt='UCI Eventlog' /></a>";
                trimtitle = freeGoodies.get(j).title;
            trimtitle = trimtitle.substring(trimtitle.indexOf(",") + 1);
            trimtitle = trimtitle.replace("\\n", "");
                href = freeGoodies.get(j).imageUrl;
             
                out.println("<p><b><a href='" + href + "'> <span style='font-size:20px'>" + trimtitle + "</span><br/>Reported Intent : <b>" + freeGoodies.get(j).emotion + "</b><img src='food1.png' width='50px'/></a></b><br/>" + freeGoodies.get(j).location + ", " + freeGoodies.get(j).date);
                out.println("<br/><a target='_blank' href='route.html?tolatitude=" + freeGoodies.get(j).lat + "&tolongitude=" + freeGoodies.get(j).lon + "&latitude=" + userLatitude + "&longitude=" + userLongitude + "'><b>Get Directions</b></a>, " + df.format(freeGoodies.get(j).haversineDistance) + " miles<br/><br/>" + imgu + "<br/><br/><input name='toemail' placeholder='let your friend know' id='sendnow1' type='text'/><input type='button' onclick='sendemail1()' value='Send'/><hr/></p>");
            }
            out.println("</section>"); 
            
             out.println("<section id='acc2'><h2><span style='height:10px'><a href='#acc2'>Art <img src='art.png' width='50px'/> (" + artEvents.size() + ")</a></span></h2>");
            for(int j = 0; j < artEvents.size(); j++){
                imgu = "<a class='example-image-link1' href='" + artEvents.get(j).imageUrl + "' data-lightbox='example-1'><img width='150px' height='150px' class='example-image' src='" + artEvents.get(j).imageUrl + "' alt='UCI Eventlog' /></a>";
                trimtitle = artEvents.get(j).title;
            trimtitle = trimtitle.substring(trimtitle.indexOf(",") + 1);
            trimtitle = trimtitle.replace("\\n", "");
                href = artEvents.get(j).imageUrl;
                
                out.println("<p><b><a href='" + href + "'> <span style='font-size:20px'>" + trimtitle + "</span><br/>Reported Intent : <b>" + artEvents.get(j).emotion + "</b><img src='splash.png' width='50px'/></a></b><br/>" + artEvents.get(j).location + ", " + artEvents.get(j).date);
                out.println("<br/><a target='_blank' href='route.html?tolatitude=" + artEvents.get(j).lat + "&tolongitude=" + artEvents.get(j).lon + "&latitude=" + userLatitude + "&longitude=" + userLongitude + "'><b>Get Directions</b></a>, " + df.format(artEvents.get(j).haversineDistance) + " miles<br/><br/>" + imgu + "<br/><br/><input name='toemail' placeholder='let your friend know' id='sendnow2' type='text'/><input type='button' onclick='sendemail2()' value='Send'/><hr/></p>");
            }
            out.println("</section>");
            
             out.println("<section id='acc3'><h2><span style='height:10px'><a href='#acc3'>Sports <img src='sport.png' width='80px'/> (" + sportsEvents.size() + ")</a></span></h2>");
            for(int j = 0; j < sportsEvents.size(); j++){
                imgu = "<a class='example-image-link1' href='" + sportsEvents.get(j).imageUrl + "' data-lightbox='example-1'><img width='150px' height='150px' class='example-image' src='" + sportsEvents.get(j).imageUrl + "' alt='UCI Eventlog' /></a>";
                trimtitle = sportsEvents.get(j).title;
            trimtitle = trimtitle.substring(trimtitle.indexOf(",") + 1);
            trimtitle = trimtitle.replace("\\n", "");
                href = sportsEvents.get(j).imageUrl;
                
                out.println("<p><b><a href='" + href + "'> <span style='font-size:20px'>" + trimtitle + "</span><br/>Reported Intent : <b>" + sportsEvents.get(j).emotion + "</b><img src='ball.png' width='50px'/></a></b><br/>" + sportsEvents.get(j).location + ", " + sportsEvents.get(j).date);
                out.println("<br/><a target='_blank' href='route.html?tolatitude=" + sportsEvents.get(j).lat + "&tolongitude=" + sportsEvents.get(j).lon + "&latitude=" + userLatitude + "&longitude=" + userLongitude + "'><b>Get Directions</b></a>, " + df.format(sportsEvents.get(j).haversineDistance) + " miles<br/><br/>" + imgu + "<br/><br/><input name='toemail' placeholder='let your friend know' id='sendnow3' type='text'/><input type='button' onclick='sendemail3()' value='Send'/><hr/></p>");
            }
            out.println("</section>");
            
             out.println("<section id='acc4'><h2><span style='height:10px'><a href='#acc4'>Technology <img src='technology.png' width='90px'/> (" + technologyEvents.size() + ")</a></span></h2>");
            for(int j = 0; j < technologyEvents.size(); j++){
                imgu = "<a class='example-image-link1' href='" + technologyEvents.get(j).imageUrl + "' data-lightbox='example-1'><img width='150px' height='150px' class='example-image' src='" + technologyEvents.get(j).imageUrl + "' alt='UCI Eventlog' /></a>";
                trimtitle = technologyEvents.get(j).title;
            trimtitle = trimtitle.substring(trimtitle.indexOf(",") + 1);
            trimtitle = trimtitle.replace("\\n", "");
                href = technologyEvents.get(j).imageUrl;
                
                out.println("<p><b><a href='" + href + "'> <span style='font-size:20px'>" + trimtitle + "</span><br/>Reported Intent : <b>" + technologyEvents.get(j).emotion + "</b><img src='tech.png' width='50px'/></a></b><br/>" + technologyEvents.get(j).location + ", " + technologyEvents.get(j).date);
                out.println("<br/><a target='_blank' href='route.html?tolatitude=" + technologyEvents.get(j).lat + "&tolongitude=" + technologyEvents.get(j).lon + "&latitude=" + userLatitude + "&longitude=" + userLongitude + "'><b>Get Directions</b></a>, " + df.format(technologyEvents.get(j).haversineDistance) + " miles<br/><br/>" + imgu + "<br/><br/><input name='toemail' placeholder='let your friend know' id='sendnow4' type='text'/><input type='button' onclick='sendemail4()' value='Send'/><hr/></p>");
            }
            out.println("</section>");
            
             out.println("<section id='acc5'><h2><span style='height:10px'><a href='#acc5'>Gathering <img src='gather.png' width='50px'/> (" + gatheringEvents.size() + ")</a></span></h2>");
            for(int j = 0; j < gatheringEvents.size(); j++){
                imgu = "<a class='example-image-link1' href='" + gatheringEvents.get(j).imageUrl + "' data-lightbox='example-1'><img width='150px' height='150px' class='example-image' src='" + gatheringEvents.get(j).imageUrl + "' alt='UCI Eventlog' /></a>";
                trimtitle = gatheringEvents.get(j).title;
            trimtitle = trimtitle.substring(trimtitle.indexOf(",") + 1);
            trimtitle = trimtitle.replace("\\n", "");
                href = gatheringEvents.get(j).imageUrl;
                
                out.println("<p><b><a href='" + href + "'> <span style='font-size:20px'>" + trimtitle + "</span><br/>Reported Intent : <b>" + gatheringEvents.get(j).emotion + "</b><img src='party.png' width='50px'/></a></b><br/>" + gatheringEvents.get(j).location + ", " + gatheringEvents.get(j).date);
                out.println("<br/><a target='_blank' href='route.html?tolatitude=" + gatheringEvents.get(j).lat + "&tolongitude=" + gatheringEvents.get(j).lon + "&latitude=" + userLatitude + "&longitude=" + userLongitude + "'><b>Get Directions</b></a>, " + df.format(gatheringEvents.get(j).haversineDistance) + " miles<br/><br/>" + imgu + "<br/><br/><input name='toemail' placeholder='let your friend know' id='sendnow5' type='text'/><input type='button' onclick='sendemail5()' value='Send'/><hr/></p>");
            }
            out.println("</section>");
            
            
            
            out.println("</article>");
            out.println("</body>");
            out.println("</html>");
    }
    
    private static String getCaption(String content, String title) throws ParseException, org.json.simple.parser.ParseException{
        JSONParser jsonParser = new JSONParser();
	JSONObject jsonObject = (JSONObject) jsonParser.parse(content);
        return jsonObject.get(title).toString().split(":")[1];
    }
    
    private String getImageUrl(String content, String title) throws ParseException, org.json.simple.parser.ParseException{
        JSONParser jsonParser = new JSONParser();
	JSONObject jsonObject = (JSONObject) jsonParser.parse(content);
        String temp = jsonObject.get(title).toString();
        temp = temp.substring(temp.indexOf("{") + 1, temp.lastIndexOf("}"));
        temp = temp.substring(temp.indexOf(":") + 1);
        temp = temp.replace("\\", "");
        return temp.substring(temp.indexOf("\"") + 1, temp.lastIndexOf("\""));
    }
    
    private String getEmotionCaption(String content, String title) throws ParseException, org.json.simple.parser.ParseException{
        
        JSONParser jsonParser = new JSONParser();
	JSONObject jsonObject = (JSONObject) jsonParser.parse(content);
        
        String temp = jsonObject.get(title).toString().split(":")[1];
        temp = temp.substring(temp.indexOf("\"") + 1, temp.lastIndexOf("\""));
        
        return temp;
    }
    
    public Double getHaversineDistance(Double lat1, Double lon1, Double lat2, Double lon2){
        final int R = 6371;
        Double latDistance = toRad(lat2-lat1);
        Double lonDistance = toRad(lon2-lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + 
                   Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) * 
                   Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
     }
     
     public static Double toRad(Double value) {
        return value * Math.PI / 180;
    }
     
    static class haversineComparator implements Comparator<EventClass> {
    @Override
    public int compare(EventClass o1, EventClass o2) {
         return o1.haversineDistance.compareTo(o2.haversineDistance);
    }
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
