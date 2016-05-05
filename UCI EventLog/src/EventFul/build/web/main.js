var button = document.getElementById("sortby");
function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi,    
    function(m,key,value) {
      vars[key] = value;
    });
    return vars;
  }
  
  function sortFunction(textvalue){
	    var lat = getUrlVars()["latitude"];
  var lon = getUrlVars()["longitude"];
if(textvalue == "Sort by time"){
	document.location.href = "EventLogServlet?sort=time&latitude=" + lat + "&longitude=" + lon;
	button.value = "Sort by Location Proximity";
	window.location.reload();
}else{
	document.location.href = "EventLogServlet?sort=distance&latitude=" + lat + "&longitude= " + lon;
	button.value = "Sort by time";
	window.location.reload();
}
  }


