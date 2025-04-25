		var httpProtocol= window.document.location.protocol;
		var wsProtocol = getWSProtocol(httpProtocol);
		var personaWebServicesPMI = new WebSocket(wsProtocol+'://'  + window.document.location.host
				+ '/WASPersonaWebServicesPMIWeb/personaWebServicesPMIWebSocketEndpoint');


		// Require the basic 2d chart resource: Chart2D
		dojo.require("dojox.charting.Chart2D");
			
			// Require the highlighter
		dojo.require("dojox.charting.action2d.Highlight");

			// Require the theme of our choosing
			//"Claro", new in Dojo 1.6, will be used
		dojo.require("dojox.charting.themes.MiamiNice");
			
			// Define the data
		var serviceNames=[];
		var jsonArray;
				
		personaWebServicesPMI.onerror = function(event) {
			onPersonaPMIError(event);
		};

		personaWebServicesPMI.onopen = function(event) {
			onPersonaPMIOpen(event);
		};

		personaWebServicesPMI.onmessage = function(event) {
			onPersonaPMIMessage(event);
		};
		
		/**
		 * 
		 * @param event
		 */
		function onPersonaPMIMessage(event) {
			console.log('Inside onPersonaPMIMessage');
			var array=event.data;
			jsonArray=JSON.parse(array);
			if(jsonArray!=null){
				console.log(jsonArray);
				initializeDataForCharting(jsonArray);
				
			}
		}
		
		/**
		 * 
		 */
		function initializeDataForCharting(jsonArray){
			//loop through data and get serviceNames first.
			console.log('Inside initializeDataForCharting');
			serviceNames=[];
			for(var i = 0; i < jsonArray.length; i++) {
				//console.log(jsonArray.type);
			    var obj = jsonArray[i].serviceName;
			    console.log("Second debug:  " +obj);
			    
		    	if(serviceNames.indexOf(obj)==-1){
			    	serviceNames.push(obj);
			    	console.log("Service names lengh "+serviceNames.length);
			    	
			    }//end-if
			    
			}//end-for-loop1
			
			
			for(var i = 0; i < serviceNames.length; i++) {
		    	//create DIV  for Charts.
		    	parentDiv=document.getElementById("parentDiv");
		    	var serviceName=serviceNames[i];
		    	if(parentDiv!=null){
		    		//chileNodesList = parentDiv.childNodes;
		    		childElement = document.getElementById("id", serviceName+"Chart");
		    		if(childElement==null){
			    		var newChild = document.createElement('div');
			    		newChild.setAttribute("id", serviceName+"Chart");
			    		newChild.setAttribute("style", "width:800px;height:400px;");
			    		parentDiv.appendChild(newChild);
			    		
			    		
						// When the DOM is ready and resources are loaded...
						dojo.ready(function() {
							
							// Create the chart within it's "holding" node
							var chart = new dojox.charting.Chart2D(serviceName+"Chart");

							// Set the theme
							chart.setTheme(dojox.charting.themes.MiamiNice);

							// Add the only/default plot 
							chart.addPlot("default", {
								type: "Columns",
								markers: true,
								gap: 7, labels: true, labelStyle: "outside", labelOffset: 15
							});
							
							var jsonLabels={};
							
							var labelsArray=[];
							var yAxisChartData=[];
							jsonLabels.labelsArray=labelsArray;
							var index=1;
							for(var j = 0; j < jsonArray.length; j++) {
								//console.log(jsonArray.type);
								
							    var obj = jsonArray[j].serviceName;
							    console.log("Second debug:  " +obj);
							    if(serviceName==obj){
							    	
							    	var xAxisLabel={"value":index, "text":jsonArray[j].operationName};
							    	printJsonObject(xAxisLabel);
							    	jsonLabels.labelsArray.push(xAxisLabel);
							    	yAxisChartData.push(jsonArray[j].currentCount);
							    	index=index+1;
							    }
							}
							
							// Add axes
							chart.addAxis("x",{
								    labels: labelsArray,				            
								             majorTicks:true,
								             majorTickStep:1,
								             minorTicks:false,
								             dropLabels: false,
								             labelSizeChange: true,
								             includeZero: true,
								             natural: true,
								             majorLabels:true,
								             minorLabeles:true,
								             rotation:-25,
								             font: "normal normal 13pt Arial",
								             fontColor : "black",
								             fixLower: "major", 
								             fixUpper: "major"
								       			});
							console.log("The yAxisDataIs :" +yAxisChartData);
								//chart.addAxis("x", { labelFunc: myLabelFunc });
							chart.addAxis("y", { vertical: true, fixLower: "major", fixUpper: "major" });

							// Add the series of data
							chart.addSeries(serviceName,yAxisChartData);
							
							// Highlight!
							new dojox.charting.action2d.Highlight(chart,"default");

							// Render the chart!
							chart.render();
							
						});
		    		}
		    	}
				
			}//end-for-loop2
		}
		
		/**
		 * 
		 * @param jsonObject
		 */
		function printJsonObject(jsonObject){
			for(var key in jsonObject) {
			      console.log("Key: " + key + " value: " + jsonObject[key]);
			}		
		}
		
		/**
		 * 
		 * @param jsonObject
		 * @returns
		 */
		function getJsonKeys(jsonObject){
			if(jsonObject!=null){
				return Object.keys(jsonObject);
			}
		}
		
		/**
		 * 
		 * @param jsonObject
		 * @returns {Array}
		 */
		function getJsonValues(jsonObject){
			var jsonValues=[];
			for(var key in jsonObject) {
				jsonValues.push(jsonObject[key]);
			}	
			return jsonValues;
			
		}
		
		
		function onPersonaPMIOpen(event) {
			console.log('Inside onPersonaPMIOpen'); 
		}

		function onPersonaPMIError(event) {
			console.log('Inside onPersonaPMIError'); 
			alert(event.data);
		}

		function sendForDashboard() {
			console.log('Inside sendForDashboard()'); 
			webSocket.send("getData");
			return false;
			
		}
		function getWSProtocol(httpProtocol){
			if(httpProtocol == 'http:'){
				wsProtocol="ws";
			}
			else{
				wsProtocol="wss";
			}
			//alert(wsProtocol);
			return wsProtocol;
		}
