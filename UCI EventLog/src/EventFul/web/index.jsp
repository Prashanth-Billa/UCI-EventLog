<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>UCI Eventlog</title>
        <script>
            if(navigator.geolocation){
                navigator.geolocation.getCurrentPosition(callback);
            }else{
                document.location.href = "EventLogServlet?latitude=0&longitude=0&sort=distance";
            }
            function callback(position) {
                document.location.href = "EventLogServlet?sort=distance&latitude="+position.coords.latitude+"&longitude="+position.coords.longitude;
            }
        </script>
    </head>
    
    <body>
        <div align="center">
        <img src="uc.jpg" width="400px"/><br/>
        <img src="uci.png" width="200px"/><br/>
        <b><img src="load.gif"/></b>
        <br/><br/><b>UCI Eventlog ~ Under Guidance of Prof. Ramesh Jain</b>
        </div>
        
         
    </body>
</html>
