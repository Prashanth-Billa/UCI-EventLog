var button = document.getElementById("sortby");

function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi,    
    function(m,key,value) {
      vars[key] = value;
    });
    return vars;
  }
  
  function onloadBody(){
		alert("w");
  }
  
  function sendemail1(){
	  var e = document.getElementById("sendnow1").value;
	  document.location.href = "SendEmailServlet?email="+e;
  }
  
  function sendemail2(){
	  var e = document.getElementById("sendnow2").value;
	  document.location.href = "SendEmailServlet?email="+e;
  }
  
  function sendemail3(){
	  var e = document.getElementById("sendnow3").value;
	  document.location.href = "SendEmailServlet?email="+e;
  }
  
  function sendemail4(){
	  var e = document.getElementById("sendnow4").value;
	  document.location.href = "SendEmailServlet?email="+e;
  }
  
  function sendemail5(){
	  var e = document.getElementById("sendnow5").value;
	  document.location.href = "SendEmailServlet?email="+e;
  }

  function sortFunction(textvalue){
	    var lat = getUrlVars()["latitude"];
  var lon = getUrlVars()["longitude"];
if(textvalue == "Sort by time"){
	document.location.href = "EventLogServlet?sort=time&emotion=" + emotionMain + "&latitude=" + lat + "&longitude=" + lon;
	button.value = "Sort by Location Proximity";
	window.location.reload();
}else{
	document.location.href = "EventLogServlet?sort=distance&emotion=" + emotionMain + "&latitude=" + lat + "&longitude= " + lon;
	button.value = "Sort by time";
	window.location.reload();
}
  }

function changeEmotion(){
	var lat = getUrlVars()["latitude"];
  var lon = getUrlVars()["longitude"];
	var e = document.getElementById("emotionid")[document.getElementById("emotionid").selectedIndex].value;
	document.location.href = "EventLogServlet?sort=distance&emotion="+e+"&sort=distance&latitude="+lat+"&longitude="+lon;
}
