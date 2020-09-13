<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
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
	  	<!-- Populate options -->
	  	<!-- CSS styling for body -->
    	<style>
    		body{
    			background: #333;
    			color:#fff;
    		}
    	</style>
	</head>
	<body>
		<form id="instrumentplay" name="instrumentplay">
			<div id="divinstrument">
				<label for="instrument" class="font-weight-bold">Select instrument:</label>
				<select class="form-control" id = "instrument" name = "instrument" required>
					<option selected = "selected"></option>
					<option value = "acoustic_grand_piano"> Acoustic Grand Piano </option>
					<option value = "music_box"> Music Box </option>
					<option value = "cello"> Cello </option>
					<option value = "trombone"> Trombone </option>	
					<option value = "marimba"> Marimba </option>	
				</select>
			</div>
			<br>
			<% String midi = request.getParameter("midi"); %>
			<%= midi %>
			<input type = "submit" value = "Play!" name = "submit" formmethod="POST">
			
		</form>
		<%
		if (request.getParameter("submit") != null) {
			String instrument = request.getParameter("instrument");
		%>
		<p class="font-weight-bold"> Turn your audio on to hear it!</p>
		<%= midi %>
		
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
						/// this sets up the MIDI.Player and gets things going...
						player = MIDI.Player;
						//player.timeWarp = 1; // speed the song is played back
						// Trying out
						//player.loadFile('data:audio/midi;base64,TVRoZAAAAAYAAAABAIBNVHJrAAAAggCQP0CBAIA/QACQRECBAIBEQACQP0CBAIA/QACQRECBAIBEQACQQECBAIBAQACQQECCAIBAQACQRECBAIBEQACQUkCBAIBSQACQUECCAIBQQACQT0CBAIBPQACQTECBAIBMQACQS0CBAIBLQACQSUCBAIBJQACQT0CBAIBPQAD/LwA=', player.start);
						//player.loadFile('data:audio/midi;base64,TVRoZAAAAAYAAAABAIBNVHJrAAAAkACQSECBAIBIQACQTEBAgExAAJBIQECASEAAkE9AgQCAT0AAkEhAgQCASEAAkExAggCATEAAkExAgQCATEAAkE1AgQCATUAAkEdAgQCAR0AAkEpAQIBKQACQR0BAgEdAAJBKQIEAgEpAAJBHQIEAgEdAAJBNQIEAgE1AAJBNQIEAgE1AAJBMQIIAgExAAP8vAA==', player.start);
						player.loadFile(midi, player.start);
					}
				});
			});
		</script>
		<br>
		<%
		}
		%>
		<br>
	</body>
</html>