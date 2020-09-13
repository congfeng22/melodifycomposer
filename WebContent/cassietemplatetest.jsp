<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="com.generate.jsp.*" import="java.io.*"%>
<!DOCTYPE html>
<!-- THIS IS THE ORIGINAL ALGORHYTHM (not quite actually). DO NOT TOUCH -->
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
					document.getElementById('divgenerate').style.visibility = "hidden";
				}
			}, false);
		}
		function generatebutton() {
	
			document.getElementById("scale").addEventListener("change", function(e){
				if (this.value != ""){
					document.getElementById('divgenerate').style.visibility = "visible";
				} else {
					document.getElementById('divgenerate').style.visibility = "hidden";
				}
			}, false);
		}
		function onload() {
			populate();
			generatebutton();
		}
		</script>
		<!-- CSS styling for body -->
    	<style>
    		body{
    			background: #333;
    			color:#fff;
    		}
			footer {
			    padding-top: 50px;
			    color: #fff;
			}
			footer ul {
			    padding: 0px;
			}
			footer ul li {
			    list-style: none;
			}
			footer ul li a {
			    padding: 5px 0px;
			    color: #efefef;
			    display: block;
			    border-bottom: 1px solid #ddd;
			}
			footer ul li a:hover{
			    color: #c3e0ff;
			    text-decoration: none;
			}
			footer h3 {
			    margin-bottom: 20px;
			    font-size: 20px;
			}
    	</style>
	  	<title> Algorhythm </title>
	</head>
	<body onLoad="javascript:onload()">
		<img src="https://i.imgur.com/Ms4GgZ3.png" class="img-fluid" alt = "title"></img>
		<div class = "container-fluid">
			<br>
			<br>
			<div class = "row">	
				<div class = "col-1"></div>	
				<div class = "col-10">	
					<div class = "card text-white bg-secondary mb-3">
						<div class = "card-header text-center">
							<h5> See what customized melody the algorithm will generate for you! </h5>
						</div>						
					</div>
				</div>
				<div class = "col-1"></div>	
			</div>
			<br>
			<br>
			<div class = "row">
				<div class = "col-1"></div>
				<div class = "col-4">
					<form>
					<div class = "row">
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
					<div class = "row" id="divscale" style="visibility:hidden;">
						<label for="emotion" class="font-weight-bold">Select scale:</label>
						<select class="form-control" id = "scale" name = "scale" required>
							<option selected = "selected"></option>
						</select>
					</div>
					<br>
					<br>
					<div class = "row" id="divgenerate" style="visibility:hidden;">
						<input class="btn btn-primary" type = "submit" value = "Generate!" name = "submit" formmethod="POST">
					</div>
					</form>
				</div>
				<div class = "col-2"></div>
				<div class = "col-4">
					<%
					if (request.getParameter("submit") != null) {
						String[] major = {"C", "D flat", "D", "E flat", "E", "F", "G flat", "G", "A flat", "A", "B flat", "B"};
						String[] minor = {"a", "b flat", "b", "c", "c sharp", "d", "e flat", "e", "f", "f sharp", "g", "g sharp"};
						String emotion = request.getParameter("emotion");
						int index = Integer.parseInt(request.getParameter("scale"))-1;
						int BPM = 60;
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
					<%
					}
					%>
					
				</div>
				<div class = "col-1"></div>
		    </div>
			<br>
			<br>
			<br>
			<br>
			<br>
			<div class = "row">
				<div class = "col-4"> </div>
				<div class = "col-4">
				<div class = "card text-left text-white bg-secondary mb-3" style = "max-width: 25rem;">
						<div class = "card-header text-center" >
							<h5> How does this work? </h5>
						</div>						
				</div>
				</div>
				<div class = "col-4"> </div>
			</div>
			<br>
			<br>
			<div class = "row">
				<div class = "col-2"></div>
				<div class = "col-12">
					<div class = "card">
						<div class="card-body bg-warning">
    						<p class="card-text text-dark text-justify"> Algorhythm is an attempt to algorithmically generate four bars of melody 
    						with the goal to facilitate the brainstorming process for composers. The underlying mechanism of this 
    						algorithm is a Markov Model for determining subsequent notes and beats. We built the model by analyzing 
    						numerous music sheets to strengthen the resemblance between melodies algorithmically generated and those 
    						traditionally composed. In addition, we manipulated the Markov Model to convey specific emotions through 
    						analyzing chord progressions, beats, and tempo of existing compositions that have been scientifically 
    						proven to convey that emotion. Limitations are set based on basic music theory concepts.  </p>	
    						<a href="https://www.ocf.berkeley.edu/~acowen/music.html#" class="btn btn-info"> See Database Reference </a>
    						<br>
    					</div>						
					</div>	
				</div>
				<div class = "col-2"></div>
					      			
			</div>
			<br>
			<br>
			<br>
			
			<footer class="bg-dark">
               	<h3>Many thanks to</h3>
                   <ul>
                       <li><a href="http://www.jfugue.org/">JFugue</a></li>
                       <li><a href="https://github.com/mudcube/MIDI.js">mudcube/MIDI.js</a></li>
                       <li><a href="https://f4.bcbits.com/img/a0890685999_10.jpg">Piano image source </a></li>
                   </ul>
               	<h3>Contact us</h3>
                   <ul>
                       <li><a>pl164@duke.edu</a></li>
                       <li><a>fc84@duke.edu</a></li>
                   </ul>
		    </footer>
					
		</div>
	</body>
</html>