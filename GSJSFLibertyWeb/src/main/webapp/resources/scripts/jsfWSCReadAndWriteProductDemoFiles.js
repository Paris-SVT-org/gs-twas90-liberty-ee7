		var videoFileName;
		//var paramFileName=document.getElementById("playProductDemoVideoForm:fileName").value;
		
		console.log = function() {}
				

		
		function onVideoFileMessage(data) {
			//console.log('Inside onVideoFileMessage');
			var blob= '#{jsfWSCJsonBean.videoFile}';
			alert(blob);
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
				var url=window.URL.createObjectURL(data);
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

		function getVideoFileName(){
			var paramFileName=document.getElementById("playProductDemoVideoForm:fileName").value;
			console.log("The paramFileName is :"+paramFileName);
			return paramFileName;
		}
		
        window.onload= function() { 
	        document.getElementById('playProductDemoVideoForm:productDemoButtonID').click();
        }
