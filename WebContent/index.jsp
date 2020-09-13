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
				} 
			}, false);
		}
		function onload() {
			populate();
		}
		</script>
	</head>
	<body onLoad="javascript:onload()">
		<header id="header" class="alt">
				<div class="inner">
					<h1>Melodify</h1>
					<p>An algorithmic app to unlock the music creativity in you</p>
				</div>
		</header>
		<section id="intro" class="main">
				<span class="icon fa-diamond major"></span>
				<h2>Customize your melody</h2>
				<p>See what melody the algorithm will generate for you!</p>
		</section>
		<form>
			<section class="main items">
					<article class="item" id = "emotions">
							<header>
								<a><img src="${pageContext.request.contextPath}/images/pic05.jpg" style='height: 100%; width: 100%;'/></a>
								<h3>Step I. Select an emotion</h3>
							</header>
							<p>What emotions do you want your melody to evoke? Sad? Fearful? Joyful? Or relaxing?</p>
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
					</article>
			
			<br>
			
			<br>
					<article class="item">
						<header>
							<a><img src="${pageContext.request.contextPath}/images/pic06.jpg" style='height: 100%; width: 100%;'/> </a>
							<h3>Step II. Select a scale</h3>
						</header>
						<p>Scale is an essential part of composition! Select a scale that fit your emotion!</p>
						<div id="divscale">
							<label for="emotion" class="font-weight-bold">Select scale:</label>
							<select class="form-control" id = "scale" name = "scale" required>
								<option selected = "selected"></option>
							</select>
						</div>	
						</article>
			<br>
			
					<article class="item">
						<header>
							<a><img src= "${pageContext.request.contextPath}/images/pic07.jpg" style='height: 100%; width: 100%;'/></a>
							<h3>Step III. Select an instrument</h3>
						</header>
						<p>Choose an instrument or select default to let the algorithm will choose for you!</p>
						<div id="divinstrument">
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
					</article>
			<br>
			<br>
					<article class="item">
						<header>
							<a><img src= "${pageContext.request.contextPath}/images/pic08.jpg" style='height: 100%; width: 100%;'/></a>
							<h3>Step IV. Generate</h3>
						</header>
						<p>Hit generate to get your melody!</p>
						<div id="divgenerate">
							<input type = "submit" value = "Generate!" name = "submit" formmethod="POST">
						</div>
					</article>
			</section>
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
		<br>
		<form id="save_melody" name="save_melody" action="https://melodify.perfectnite.com.my">
			<input type="text" placeholder="<%= midi %>" name="encoding" style="visibility:hidden;">
			<input type="text" placeholder="Name of melody" name="name">
			<input type="text" placeholder="Username" name="username">
			<input type="text" placeholder="Create a caption!" name="caption">
			<br>
			<button value="submit" type="submit">Save!</button>
		</form>
		<%
		}
		%>
		<br>
		<section id="cta" class="main special">
			<h3>Many thanks to</h3>
            <ul>
            	<li><a href="https://www.ocf.berkeley.edu/~acowen/music.html#modal">Berkeley Database</a></li>
            	<li><a href="http://www.jfugue.org/">JFugue</a></li>
                <li><a href="https://github.com/mudcube/MIDI.js">mudcube/MIDI.js</a></li>
                <li><a href="https://f4.bcbits.com/img/a0890685999_10.jpg">Background image source </a></li>
                <li><a href="https://www.google.com/url?sa=i&url=https%3A%2F%2Fkaleela.com%2Fcommon-arabic-words-for-feelings-and-emotions%2F&psig=AOvVaw0NbtoazbsJPYfFNJblTxwa&ust=1600070473123000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCJCF7JXV5esCFQAAAAAdAAAAABAD">Emotion image source </a></li>
                <li><a href="https://study.com/academy/lesson/what-is-impressionism-in-music-definition-characteristics-timeline.html">Scale image source</a></li>
                <li><a href="https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.pngegg.com%2Fen%2Fpng-bipjg&psig=AOvVaw3kf2tWWCVOKWzVw_bTkyX5&ust=1600070647582000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCJCvyZnW5esCFQAAAAAdAAAAABAD">Instruments image source</a></li>
                <li><a href="https://www.google.com/url?sa=i&url=https%3A%2F%2Failabs.tw%2Fhuman-interaction%2Fai-music-composition%2F&psig=AOvVaw1ruwFjaVfbfq52GLsHhuF4&ust=1600071241047000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCKil04LY5esCFQAAAAAdAAAAABAD">Generate image source</a></li>
			</ul>
	
		</section>
		<footer id="footer">
			<ul class="icons">
				<li><a href="#" class="icon fa-twitter"><span class="label">Twitter</span></a></li>
				<li><a href="#" class="icon fa-facebook"><span class="label">Facebook</span></a></li>
				<li><a href="#" class="icon fa-instagram"><span class="label">Instagram</span></a></li>
				<li><a href="#" class="icon fa-linkedin"><span class="label">LinkedIn</span></a></li>
				<li><a href="#" class="icon fa-envelope"><span class="label">Email</span></a></li>
			</ul>
			<p class="copyright">&copy; Untitled. Design: <a href="https://templated.co">TEMPLATED</a>. Images: <a href="https://unsplash.com">Unsplash</a>.</p>
		</footer>
		
	</body>
</html>