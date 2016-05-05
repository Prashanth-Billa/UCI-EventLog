package nextGen;

import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.evdb.javaapi.data.Event;
import com.evdb.javaapi.data.SearchResult;
import com.evdb.javaapi.data.request.EventSearchRequest;
import com.evdb.javaapi.operations.EventOperations;
import com.evdb.javaapi.APIConfiguration;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

public class EventLogServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         String userLatitude = request.getParameter("latitude");
         String userLongitude = request.getParameter("longitude");
         Double latitude = Double.valueOf(userLatitude);
         Double longitude = Double.valueOf(userLongitude);
         String sortType = request.getParameter("sort").toString();
         String stype = "";
         if("distance".equals(sortType)){
             stype = "time";
         }else{
             stype = "distance";
         }
         
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet EventLogServlet</title>");  
            out.println("<script src='main.js'></script>");
            out.println("<link rel='stylesheet' type='text/css' href='style.css'>");
            out.println("</head>");
            out.println("<body>");
            String showSortText = "<br/><div align='center'>Currently sorted by : " + sortType + "</div>";
            String html = "<div align='center'><a href='#' id='buttonc' onClick='window.location.reload()' class='myButton'>Refresh my EventLog!</a><br/><br/><input type='button' onClick='sortFunction(this.value)' value='Sort by " + stype + "' id='sortby'/><br/> " + showSortText + "<br/><br/></div><article class='accordion'>";
        
        String href = "";
        try {
            
            
            out.println("<div align='center'><img src='uci.png' width='60px'/><br/><img src='uc.jpg' width='100px'/></div><br/>");
            out.println(html);
            ArrayList<EventClass> e = populateSportsEvents(latitude, longitude, sortType);
            out.println("<section id='acc1'><h2><a href='#acc1'>Sports (" + e.size() + ")</a></h2>");
            
            for(int i = 0; i < e.size(); i++){
                href = e.get(i).event.getURL();
                out.println("");
                out.println("<p><b><a href='" + href + "'> " + e.get(i).event.getTitle() + "</a></b><br/>" + e.get(i).event.getStartTime() + ", " + e.get(i).event.getVenueAddress() + " " + e.get(i).event.getVenuePostalCode());
                out.println("<br/><a target='_blank' href='route.html?tolatitude=" + e.get(i).event.getVenueLatitude() + "&tolongitude=" + e.get(i).event.getVenueLongitude() + "&latitude=" + userLatitude + "&longitude=" + userLongitude + "'>Get Directions</a></p>");
            }
            out.println("</section>");
            e = null;
            e = populateConcertEvents(latitude, longitude, sortType);
            out.println("<section id='acc2'><h2><a href='#acc2'>Concerts (" + e.size() + ")</a></h2>");
            
            for(int i = 0; i < e.size(); i++){
                href = e.get(i).event.getURL();
                out.println("");
                out.println("<p><b><a href='" + href + "'> " + e.get(i).event.getTitle() + "</a></b><br/>" + e.get(i).event.getStartTime() + ", " + e.get(i).event.getVenueAddress() + " " + e.get(i).event.getVenuePostalCode());
                out.println("<br/><a target='_blank' href='route.html?tolatitude=" + e.get(i).event.getVenueLatitude() + "&tolongitude=" + e.get(i).event.getVenueLongitude() + "&latitude=" + userLatitude + "&longitude=" + userLongitude + "'>Get Directions</a></p>");
            }
            out.println("</section>");
            e = null;
            e = populateMusicEvents(latitude, longitude, sortType);
            out.println("<section id='acc3'><h2><a href='#acc3'>Music (" + e.size() + ")</a></h2>");
            
            for(int i = 0; i < e.size(); i++){
                href = e.get(i).event.getURL();
                out.println("");
                out.println("<p><b><a href='" + href + "'> " + e.get(i).event.getTitle() + "</a></b><br/>" + e.get(i).event.getStartTime() + ", " + e.get(i).event.getVenueAddress() + " " + e.get(i).event.getVenuePostalCode());
                out.println("<br/><a target='_blank' href='route.html?tolatitude=" + e.get(i).event.getVenueLatitude() + "&tolongitude=" + e.get(i).event.getVenueLongitude() + "&latitude=" + userLatitude + "&longitude=" + userLongitude + "'>Get Directions</a></p>");
            }
            out.println("</section>");
            e = null;
            e = populateTechnologyEvents(latitude, longitude, sortType);
            out.println("<section id='acc4'><h2><a href='#acc4'>Technology (" + e.size() + ")</a></h2>");
            
            for(int i = 0; i < e.size(); i++){
                href = e.get(i).event.getURL();
                out.println("");
                out.println("<p><b><a href='" + href + "'> " + e.get(i).event.getTitle() + "</a></b><br/>" + e.get(i).event.getStartTime() + ", " + e.get(i).event.getVenueAddress() + " " + e.get(i).event.getVenuePostalCode());
                out.println("<br/><a target='_blank' href='route.html?tolatitude=" + e.get(i).event.getVenueLatitude() + "&tolongitude=" + e.get(i).event.getVenueLongitude() + "&latitude=" + userLatitude + "&longitude=" + userLongitude + "'>Get Directions</a></p>");
            }
            out.println("</section>");
            e = null;
            e = populateBusinessEvents(latitude, longitude, sortType);
            out.println("<section id='acc5'><h2><a href='#acc5'>Business (" + e.size() + ")</a></h2>");
            for(int i = 0; i < e.size(); i++){
                href = e.get(i).event.getURL();
                out.println("");
                out.println("<p><b><a href='" + href + "'> " + e.get(i).event.getTitle() + "</a></b><br/>" + e.get(i).event.getStartTime() + ", " + e.get(i).event.getVenueAddress() + " " + e.get(i).event.getVenuePostalCode());
                out.println("<br/><a target='_blank' href='route.html?tolatitude=" + e.get(i).event.getVenueLatitude() + "&tolongitude=" + e.get(i).event.getVenueLongitude() + "&latitude=" + userLatitude + "&longitude=" + userLongitude + "'>Get Directions</a></p>");
            }
            out.println("</section>");
            out.println("</article>");
            out.println("</body>");
            out.println("</html>");
        } finally {            
            out.close();
        }
    }

     class EventClass{
         Double haversineDistance;
         Event event;
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
     
    class haversineComparator implements Comparator<EventClass> {
    @Override
    public int compare(EventClass o1, EventClass o2) {
        if(o1.haversineDistance.compareTo(o2.haversineDistance) == 0){
            return o1.event.getStartTime().compareTo(o2.event.getStartTime());
        }else{
            return o1.haversineDistance.compareTo(o2.haversineDistance);
        }
    }
    }
    
    class dateComparator implements Comparator<EventClass> {
    @Override
    public int compare(EventClass o1, EventClass o2) {
        if(o1.event.getStartTime().compareTo(o2.event.getStartTime()) == 0){
            return o1.haversineDistance.compareTo(o2.haversineDistance);
        }else{
            return o1.event.getStartTime().compareTo(o2.event.getStartTime());
        }
    }
    }
     
     public ArrayList<EventClass> populateSportsEvents(Double latitude, Double longitude, String sortType) {
         ArrayList<EventClass> sportsEvents = new ArrayList<EventClass>();
         APIConfiguration.setApiKey("KTZMkj3gHwCpn5Fm");
         APIConfiguration.setEvdbUser("prashanthreddybillae1062");
         APIConfiguration.setEvdbPassword("dart916!");
         APIConfiguration.setBaseURL("http://api.eventful.com/rest/");EventOperations eo = new EventOperations();
         EventSearchRequest esr = new EventSearchRequest();
         esr.setLocation("Irvine");
         esr.setKeywords("sports");
         esr.setPageSize(20);
         SearchResult sr = null;
        try {
            sr = eo.search(esr);
            List<Event> ee = sr.getEvents();
            double hv = 0;
            for (Event e : ee) {
                hv = getHaversineDistance(latitude, longitude, e.getVenueLatitude(), e.getVenueLongitude());
                EventClass ec = new EventClass();
                ec.event = e;
                ec.haversineDistance = hv;
                sportsEvents.add(ec);
            }
        } catch (Exception var) {
            return sportsEvents;
        }
        if("distance".equals(sortType)){
            Collections.sort(sportsEvents, new haversineComparator());
        }else{
            Collections.sort(sportsEvents, new dateComparator());
        }
        
        return sportsEvents;
    }
     public ArrayList<EventClass> populateConcertEvents(Double latitude, Double longitude, String sortType) {
         ArrayList<EventClass> concertEvents = new ArrayList<EventClass>();
         APIConfiguration.setApiKey("KTZMkj3gHwCpn5Fm");
         APIConfiguration.setEvdbUser("prashanthreddybillae1062");
         APIConfiguration.setEvdbPassword("dart916!");
         APIConfiguration.setBaseURL("http://api.eventful.com/rest/");EventOperations eo = new EventOperations();
         EventSearchRequest esr = new EventSearchRequest();
         esr.setLocation("Irvine");
         esr.setKeywords("concert");
         esr.setPageSize(20);
         SearchResult sr = null;
        try {
            sr = eo.search(esr);
            List<Event> ee = sr.getEvents();
            double hv = 0;
            for (Event e : ee) {
                hv = getHaversineDistance(latitude, longitude, e.getVenueLatitude(), e.getVenueLongitude());
                EventClass ec = new EventClass();
                ec.event = e;
                ec.haversineDistance = hv;
                concertEvents.add(ec);
            }
        } catch (Exception var) {
            return concertEvents;
        }
        if("distance".equals(sortType)){
            Collections.sort(concertEvents, new haversineComparator());
        }else{
            Collections.sort(concertEvents, new dateComparator());
        }
        return concertEvents;
    }

   public ArrayList<EventClass> populateMusicEvents(Double latitude, Double longitude, String sortType) {
         ArrayList<EventClass> musicEvents = new ArrayList<EventClass>();
         APIConfiguration.setApiKey("KTZMkj3gHwCpn5Fm");
         APIConfiguration.setEvdbUser("prashanthreddybillae1062");
         APIConfiguration.setEvdbPassword("dart916!");
         APIConfiguration.setBaseURL("http://api.eventful.com/rest/");EventOperations eo = new EventOperations();
         EventSearchRequest esr = new EventSearchRequest();
         esr.setLocation("Irvine");
         esr.setKeywords("music");
         esr.setPageSize(20);
         SearchResult sr = null;
        try {
            sr = eo.search(esr);
            List<Event> ee = sr.getEvents();
            double hv = 0;
            for (Event e : ee) {
                hv = getHaversineDistance(latitude, longitude, e.getVenueLatitude(), e.getVenueLongitude());
                EventClass ec = new EventClass();
                ec.event = e;
                ec.haversineDistance = hv;
                musicEvents.add(ec);
            }
        } catch (Exception var) {
            return musicEvents;
        }
        if("distance".equals(sortType)){
            Collections.sort(musicEvents, new haversineComparator());
        }else{
            Collections.sort(musicEvents, new dateComparator());
        }
        return musicEvents;
    }
   
   public ArrayList<EventClass> getSportsEventsFromEventShop(){
       
       return null;
   }
   
   
   public ArrayList<EventClass> populateTechnologyEvents(Double latitude, Double longitude, String sortType) {
         ArrayList<EventClass> technologyEvents = new ArrayList<EventClass>();
         APIConfiguration.setApiKey("KTZMkj3gHwCpn5Fm");
         APIConfiguration.setEvdbUser("prashanthreddybillae1062");
         APIConfiguration.setEvdbPassword("dart916!");
         APIConfiguration.setBaseURL("http://api.eventful.com/rest/");EventOperations eo = new EventOperations();
         EventSearchRequest esr = new EventSearchRequest();
         esr.setLocation("Irvine");
         esr.setKeywords("technology");
         esr.setPageSize(20);
         SearchResult sr = null;
        try {
            sr = eo.search(esr);
            List<Event> ee = sr.getEvents();
            double hv = 0;
            for (Event e : ee) {
                hv = getHaversineDistance(latitude, longitude, e.getVenueLatitude(), e.getVenueLongitude());
                EventClass ec = new EventClass();
                ec.event = e;
                ec.haversineDistance = hv;
                technologyEvents.add(ec);
            }
        } catch (Exception var) {
            return technologyEvents;
        }
        if("distance".equals(sortType)){
            Collections.sort(technologyEvents, new haversineComparator());
        }else{
            Collections.sort(technologyEvents, new dateComparator());
        }
        return technologyEvents;
    }
   
   public ArrayList<EventClass> populateBusinessEvents(Double latitude, Double longitude, String sortType) {
         ArrayList<EventClass> businessEvents = new ArrayList<EventClass>();
         APIConfiguration.setApiKey("KTZMkj3gHwCpn5Fm");
         APIConfiguration.setEvdbUser("prashanthreddybillae1062");
         APIConfiguration.setEvdbPassword("dart916!");
         APIConfiguration.setBaseURL("http://api.eventful.com/rest/");EventOperations eo = new EventOperations();
         EventSearchRequest esr = new EventSearchRequest();
         esr.setLocation("Irvine");
         esr.setKeywords("business");
         esr.setPageSize(20);
         SearchResult sr = null;
        try {
            sr = eo.search(esr);
            List<Event> ee = sr.getEvents();
            double hv = 0;
            for (Event e : ee) {
                hv = getHaversineDistance(latitude, longitude, e.getVenueLatitude(), e.getVenueLongitude());
                EventClass ec = new EventClass();
                ec.event = e;
                ec.haversineDistance = hv;
                businessEvents.add(ec);
            }
        } catch (Exception var) {
            return businessEvents;
        }
        if("distance".equals(sortType)){
            Collections.sort(businessEvents, new haversineComparator());
        }else{
            Collections.sort(businessEvents, new dateComparator());
        }
        return businessEvents;
    }
}
