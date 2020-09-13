<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="com.generate.jsp.*" import="java.io.*"%>
<!DOCTYPE html>
<html xmlns = "http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<!-- shim -->
		<script src="./MIDI.js/inc/shim/Base64.js" type="text/javascript"></script>
		<script src="./MIDI.js/inc/shim/Base64binary.js" type="text/javascript"></script>
		<script src="./MIDI.js/inc/shim/WebAudioAPI.js" type="text/javascript"></script>
		<script src="./MIDI.js/inc/shim/WebMIDIAPI.js" type="text/javascript"></script>
		<!-- jasmid package -->
		<script src="./MIDI.js/inc/jasmid/stream.js"></script>
		<script src="./MIDI.js/inc/jasmid/midifile.js"></script>
		<script src="./MIDI.js/inc/jasmid/replayer.js"></script>
		<!-- midi.js package -->
		<script src="./MIDI.js/js/midi/audioDetect.js" type="text/javascript"></script>
		<script src="./MIDI.js/js/midi/gm.js" type="text/javascript"></script>
		<script src="./MIDI.js/js/midi/loader.js" type="text/javascript"></script>
		<script src="./MIDI.js/js/midi/plugin.audiotag.js" type="text/javascript"></script>
		<script src="./MIDI.js/js/midi/plugin.webaudio.js" type="text/javascript"></script>
		<script src="./MIDI.js/js/midi/plugin.webmidi.js" type="text/javascript"></script>
		<script src="./MIDI.js/js/midi/player_1.1.js" type="text/javascript"></script>
		<!-- utils -->
		<script src="./MIDI.js/js/util/dom_request_xhr.js" type="text/javascript"></script>
		<script src="./MIDI.js/js/util/dom_request_script.js" type="text/javascript"></script>
		<!-- includes -->
		<script src="./MIDI.js/examples/inc/timer.js" type="text/javascript"></script>
		<script src="./MIDI.js/examples/inc/colorspace.js" type="text/javascript"></script>
		<script src="./MIDI.js/examples/inc/event.js" type="text/javascript"></script>
	  	<script src="https://code.jquery.com/jquery-3.5.0.js"></script>
	  	<!-- css -->
	  	<link rel="stylesheet" href="/MelodyGeneratorWeb/css/main.css" type="text/css">
	  	<!-- Populate options -->
	  	<script type = "text/javascript">
		function populate() {
			var major = ["C", "D flat", "D", "E flat", "E", "F", "G flat", "G", "A flat", "A", "B flat", "B"];
			var minor = ["a", "b flat", "b", "c", "c sharp", "d", "e flat", "e", "f", "f sharp", "g", "g sharp"];
	
	
			document.getElementById("emotion").addEventListener("change", function(e){
				if (this.value != ""){
					document.getElementById('divscale').style.visibility = "visible";
					var select2 = document.getElementById("scale");
				    select2.innerHTML = "";
				    var aItems = [];
				    if(this.value == "calm" || this.value == "joyful"){
				        aItems = major;
				    } else if (this.value == "sad" || this.value == "fearful") {
				        aItems = minor;
				    }
				    var blank = document.createElement("option");
				    blank.value= "";
			        var blankNode = document.createTextNode("");
			        blank.appendChild(blankNode);
			        select2.appendChild(blank);
				    for(var i=0,len=aItems.length; i<len;i++) {
				        var option = document.createElement("option");
				        option.value= (i+1);
				        var textNode = document.createTextNode(aItems[i]);
				        option.appendChild(textNode);
				        select2.appendChild(option);
				    }
				} else {
					document.getElementById('divscale').style.visibility = "hidden";
					document.getElementById('divinstrument').style.visibility = "hidden";
					document.getElementById('divgenerate').style.visibility = "hidden";
				}
			}, false);
		}
		function instrumentchoice() {
			
			document.getElementById("scale").addEventListener("change", function(e){
				if (document.getElementById("scale").value != ""){
					document.getElementById('divinstrument').style.visibility = "visible";
				} else {
					document.getElementById('divinstrument').style.visibility = "hidden";
					document.getElementById('divgenerate').style.visibility = "hidden";
				}
			}, false);
		}
		function generatebutton() {
	
			document.getElementById("instrument").addEventListener("change", function(e){
				if (this.value != ""){
					document.getElementById('divgenerate').style.visibility = "visible";
				} else {
					document.getElementById('divgenerate').style.visibility = "hidden";
				}
			}, false);
		}
		function onload() {
			populate();
			instrumentchoice();
			generatebutton();
		}
		</script>
	</head>
	<body onLoad="javascript:onload()">
		<header id="header" class="alt">
			<div class="inner">
				<h1>Epilogue</h1>
				<p>A free responsive site template by <a href="https://templated.co">TEMPLATED</a></p>
			</div>
		</header>
		<form>
			<div>
				<label for="emotion" class="font-weight-bold">Select emotion:</label>
				<select class="form-control" id = "emotion" name = "emotion" required>
					<option selected = "selected"></option>
					<option value = "sad"> Sad, depressing </option>
					<option value = "joyful"> Joyful, cheerful </option>
					<option value = "fearful"> Scary, fearful </option>
					<option value = "calm"> Calm, relaxing, serene </option>	
				</select>
			</div>
			<br>
			<div id="divscale" style="visibility:hidden;">
				<label for="emotion" class="font-weight-bold">Select scale:</label>
				<select class="form-control" id = "scale" name = "scale" required>
					<option selected = "selected"></option>
				</select>
			</div>
			<br>
			<div id="divinstrument" style="visibility:hidden;">
				<label for="instrument" class="font-weight-bold">Select instrument:</label>
				<select class="form-control" id = "instrument" name = "instrument" required>
					<option selected = "selected"></option>
					<option value = "default"> Default instrument for your emotion </option>
					<option value = "acoustic_grand_piano"> Acoustic Grand Piano </option>
					<option value = "music_box"> Music Box </option>
					<option value = "cello"> Cello </option>
					<option value = "trombone"> Trombone </option>	
					<option value = "marimba"> Marimba </option>	
				</select>
			</div>
			<br>
			<br>
			<div id="divgenerate" style="visibility:hidden;">
				<input type = "submit" value = "Generate!" name = "submit" formmethod="POST">
			</div>
		</form>
		<%
		if (request.getParameter("submit") != null) {
			String[] major = {"C", "D flat", "D", "E flat", "E", "F", "G flat", "G", "A flat", "A", "B flat", "B"};
			String[] minor = {"a", "b flat", "b", "c", "c sharp", "d", "e flat", "e", "f", "f sharp", "g", "g sharp"};
			String emotion = request.getParameter("emotion");
			int index = Integer.parseInt(request.getParameter("scale"))-1;
			int BPM = 60;
			String choseninstrument = request.getParameter("instrument");
			String instrument = "acoustic_grand_piano";
			String scale = "C";
			switch(emotion) {
			case "calm":
				BPM = 85;
				instrument = "music_box";
				scale = major[index];
				break;
			case "sad":
				BPM = 60;
				instrument = "cello";
				scale = minor[index];
				break;
			case "joyful":
				BPM = 120;
				instrument = "acoustic_grand_piano";
				scale = major[index];
				break;
			case "fearful":
				BPM = 80;
				instrument = "trombone";
				scale = minor[index];
				break;
			}
			if (!choseninstrument.equals("default")) instrument = choseninstrument; 
			String[] result = melodyGenerator.webplay(emotion, scale);
			String melody = result[0];
			String midi = "data:audio/midi;base64," + result[1];
		%>				
		<p class="font-weight-bold"> Turn your audio on to hear it!</p>
		<br>
		<%= melody %>
		<br>
		<div id="title"></div>
		<script type="text/javascript">
			var inst = '<%= instrument %>';
			$(function() {
				MIDI.loadPlugin({
					soundfontUrl: "https://gleitz.github.io/midi-js-soundfonts/MusyngKite/",
					instrument: inst,
					onprogress: function(state, progress) {
						console.log(state, progress);
					},
					onsuccess: function() {
						MIDI.programChange(0, MIDI.GM.byName[inst].number);
						var title = document.getElementById("title");
						var base64midi = '<%= midi %>';
						var bpm = '<%= BPM %>';
						/// this sets up the MIDI.Player and gets things going...
						player = MIDI.Player;
						//player.timeWarp = 1; // speed the song is played back
						player.BPM = bpm;
						// Trying out
						//player.loadFile('data:audio/midi;base64,TVRoZAAAAAYAAAABAIBNVHJrAAAAggCQP0CBAIA/QACQRECBAIBEQACQP0CBAIA/QACQRECBAIBEQACQQECBAIBAQACQQECCAIBAQACQRECBAIBEQACQUkCBAIBSQACQUECCAIBQQACQT0CBAIBPQACQTECBAIBMQACQS0CBAIBLQACQSUCBAIBJQACQT0CBAIBPQAD/LwA=', player.start);
						//player.loadFile('data:audio/midi;base64,TVRoZAAAAAYAAAABAIBNVHJrAAAAkACQSECBAIBIQACQTEBAgExAAJBIQECASEAAkE9AgQCAT0AAkEhAgQCASEAAkExAggCATEAAkExAgQCATEAAkE1AgQCATUAAkEdAgQCAR0AAkEpAQIBKQACQR0BAgEdAAJBKQIEAgEpAAJBHQIEAgEdAAJBNQIEAgE1AAJBNQIEAgE1AAJBMQIIAgExAAP8vAA==', player.start);
						player.loadFile(base64midi, player.start);
					}
				});
			});
		</script>
		<form action="midiplayer.jsp" method="GET">
			<input type = "submit" value = "Save!" name = "save">
			<input type="text" id="midi" name="midi" value=<%=midi%> style="visibility:hidden;">
		</form>
		<br>
		<%
		}
		%>
		<br>
	</body>
</html>