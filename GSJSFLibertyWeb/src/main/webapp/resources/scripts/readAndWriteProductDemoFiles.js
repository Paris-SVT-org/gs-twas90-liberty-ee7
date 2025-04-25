		var httpProtocol= window.document.location.protocol;
		var wsProtocol = getWSProtocol(httpProtocol);
		var videoFileName;
		//var paramFileName=document.getElementById("playProductDemoVideoForm:fileName").value;
		var webSocket = new WebSocket(wsProtocol+'://' + window.document.location.host
				+ '/GSjsf20LibertyWeb/garageSaleProductDemosWebSocketEndpoint/demoRequest');
		
		console.log = function() {}
				
		webSocket.onerror = function(event) {
			onError(event);
		};

		webSocket.onopen = function(event) {
			onOpen(event);
		};

		webSocket.onmessage = function(event) {
			console.log('Calling onVideoFileMessage');				
			if(event.data instanceof Blob){
				console.log('Calling onVideoFileMessage');				
				onVideoFileMessage(event);
			}
			else{
				onMessage(event);
			}
		};

		function onMessage(event) {
			//console.log('Inside onMessage');
			var array= event.data;
		}
		
		function onVideoFileMessage(event) {
			//console.log('Inside onVideoFileMessage');
			var videoDivID = document.getElementById("playProductDemoVideo");
			if(videoDivID !=null){
				var videoTag = document.getElementById("productDemoVideoID");
				if(videoTag!=null){
					videoTag.parentNode.removeChild(videoTag);
				}
				videoTag = document.createElement("video");
				videoTag.setAttribute("id", "productDemoVideoID");
				window.URL = window.URL || window.webkitURL;
				//console.log(event.data.size())
				var url=window.URL.createObjectURL(event.data);
				videoTag.setAttribute("width","600");
				videoTag.setAttribute("height","500");
				videoTag.setAttribute("controls","controls");
				var sourceTag = document.createElement("source");
				sourceTag.setAttribute("src", url);
				sourceTag.setAttribute("type","video/mp4");
				videoTag.appendChild(sourceTag);
				videoDivID.appendChild(videoTag);
				videoTag.load();
			}
		}

		function onOpen(event) {
			//console.log('Inside onOpen'); 
			send();
		}

		function onError(event) {
			console.log('Inside onError'); 
			alert(event.data);
		}

		function send() {
			console.log('Inside send()'); 
			//alert(fileName);
			videoFileName = getVideoFileName();
			webSocket.send(videoFileName);
			return false;
		}

		
		function getWSProtocol(httpProtocol){
			//alert(httpProtocol);
			if(httpProtocol == 'http:'){
				wsProtocol="ws";
			}
			else{
				wsProtocol="wss";
			}
			//alert(wsProtocol);
			return wsProtocol;
		}
		
		function getVideoFileName(){
			var paramFileName=document.getElementById("playProductDemoVideoForm:fileName").value;
			console.log("The paramFileName is :"+paramFileName);
			return paramFileName;
		}
		
		