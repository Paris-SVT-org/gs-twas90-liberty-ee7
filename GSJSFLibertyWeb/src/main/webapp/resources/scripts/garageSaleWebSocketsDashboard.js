		var httpProtocol= window.document.location.protocol;
		var wsProtocol = getWSProtocol(httpProtocol);
		var gsWebSocketsDashboard = new WebSocket(wsProtocol+'://'  + window.document.location.host
				+ '/GSjsf20LibertyWeb/garageSaleDashboardEndPoint/webSocketsMetrics');
				
		gsWebSocketsDashboard.onerror = function(event) {
			onWebSocketsDashboardError(event);
		};

		gsWebSocketsDashboard.onopen = function(event) {
			onWebSocketsDashboardOpen(event);
		};

		gsWebSocketsDashboard.onmessage = function(event) {
			onWebSocketsDashboardMessage(event);
		};

		console.log = function() {}
		
		function onWebSocketsDashboardMessage(event) {
			console.log('Inside onMessage');
			var array=event.data;
			jsonArray=JSON.parse(array);
			if(jsonArray!=null){
				console.log(jsonArray);
				initializeWebSocketsDashboardMetrics(jsonArray);
				
			}
		}
		
		function initializeWebSocketsDashboardMetrics(jsonArray) {
			
			//loop through data and get dashboardNames first.
			console.log('Inside initializeDatabaseDashboardMetrics');
			var dashboardNames=[];
			for(var i = 0; i < jsonArray.length; i++) {
				//console.log(jsonArray.type);
			    var obj = jsonArray[i].webSocketEndpointName;
			    console.log("Second debug:  " +obj);
			    
		    	if(obj !=null && dashboardNames.indexOf(obj)==-1){
		    		dashboardNames.push(obj);
			    	console.log("Dashboard names lengh "+dashboardNames.length);
			    	
			    }//end-if
			    
			}//end-for-loop1
			console.log("The dashboardNames are : " +dashboardNames);
			    
			require([
					 // Require the basic chart class
					"dojox/charting/Chart",

					// Require the theme of our choosing
					"dojox/charting/themes/Claro",
					
					// Charting plugins: 

					// 	We want to plot a Pie chart
					"dojox/charting/plot2d/Pie",

					// Retrieve the Legend, Tooltip, and MoveSlice classes
					"dojox/charting/action2d/Tooltip",
					"dojox/charting/action2d/MoveSlice",

			     
			        // Require the theme of our choosing
			        "dojox/charting/themes/MiamiNice",
			     
			        //  We want to plot Columns
			        "dojox/charting/plot2d/Columns",
			     
			        // Require the highlighter
			        "dojox/charting/action2d/Highlight",
					
					//	We want to use Markers
					"dojox/charting/plot2d/Markers",

					//	We'll use default x/y axes
					"dojox/charting/axis2d/Default",

					// Wait until the DOM is ready
					"dojo/domReady!"
				], function(Chart, theme, Pie, Tooltip, MoveSlice,chartTheme,ColumnsPlot,Highlight) {

				
				tableElement=document.getElementById("WebSocketsMetricsTable");
				if(tableElement !=null){
					tableElement.parentNode.removeChild(tableElement);
				}
	    		var myTable= document.createElement("TABLE");
	    		myTable.setAttribute("id", "WebSocketsMetricsTable");
					
					
				for(var i = 0; i < dashboardNames.length; i++) {
			    	//create DIV  for Charts.
			    	parentDiv=document.getElementById("webSocketsMetricsDiv");
			    	var dashboardName=dashboardNames[i];
			    	console.log("The dashboard name is in forloop 2:  " +dashboardName);			    	
			    	if(parentDiv!=null){
			    		//chileNodesList = parentDiv.childNodes;
			    		childElement = document.getElementById("id", dashboardName+"Chart");
			    		newChildElement = document.getElementById("id", dashboardName+"PieChart");
			    		if(childElement==null && newChildElement==null){
				    		var h2=document.createElement("H2");
				    		var t=document.createTextNode(dashboardName);
				    		h2.setAttribute("style", "color:orange");
				    		h2.setAttribute("align", "center");
				    		h2.appendChild(t);
				    		myTable.appendChild(h2);
				    		var myRow = document.createElement("TR");
				    		var myTD1 = document.createElement("TD");
				    		var myTD2 = document.createElement("TD");
				    		
				    		var newChild = document.createElement('div');
				    		newChild.setAttribute("id", dashboardName+"Chart");
				    		newChild.setAttribute("style", "width:800px;height:400px;");
				    		
				    		var newChildPie = document.createElement('div');
				    		newChildPie.setAttribute("id", dashboardName+"PieChart");
				    		newChildPie.setAttribute("style", "width:800px;height:400px;");

				    		myTD1.appendChild(newChild);
				    		myTD2.appendChild(newChildPie);
				    		myRow.appendChild(myTD1);
				    		myRow.appendChild(myTD2);
				    		
				    		myTable.appendChild(myRow);
				    		var breakElement1=document.createElement("br");
				    		var breakElement2=document.createElement("br");
				    		myTable.appendChild(breakElement1);
				    		myTable.appendChild(breakElement2);
				    		
				    		parentDiv.appendChild(myTable);
				    		

							// When the DOM is ready and resources are loaded...
								// Create the chart within it's "holding" node
								var chart = new Chart(dashboardName+"Chart");

								// Set the theme
						        chart.setTheme(chartTheme);
						        
						        
						        // Add the only/default plot
						        chart.addPlot("default", {
						            type: ColumnsPlot,
						            markers: true,
						            gap: 5, labels: true, labelStyle: "outside", labelOffset: 15
						        });
						        

								// Create the chart within it's "holding" node
								var pieChart = new Chart(dashboardName+"PieChart");

								// Set the theme
								pieChart.setTheme(theme);

								// Add the only/default plot 
								pieChart.addPlot("default", {
									type: Pie,
									markers: true,
									radius:170
								});
								
								// Add axes
								pieChart.addAxis("x");
								pieChart.addAxis("y", { min: 5000, max: 30000, vertical: true, fixLower: "major", fixUpper: "major" });

						        var jsonLabels={};
								
								var labelsArray=[];
								var yAxisChartData=[];
								jsonLabels.labelsArray=labelsArray;
								var index=1;
								
								for(var j = 0; j < jsonArray.length; j++) {
									//console.log(jsonArray.type);
									var jsonObject = jsonArray[j];
								    var obj = jsonArray[j].webSocketEndpointName;
								    if(obj !=null){
										var jsonObjectKeys= getJsonKeys(jsonObject);
										var jsonObjectValues = getJsonValues(jsonObject);
									    console.log("Json object keys are :  " +jsonObjectKeys);
									    console.log("Json object values are :  " +jsonObjectValues);
									    console.log("Json object WebSocket Name is :  " +obj);
									    console.log("dashboardName Name is :  " +dashboardName);
									    if(dashboardName==obj){
									    	for(var k=0; k<jsonObjectKeys.length;k++){
									    		console.log("jsonObjectKey is :  " +jsonObjectKeys[k]);
									    		jsonObjectKeys[k]
									    		if((dashboardName !=jsonObjectValues[k]) && (jsonObjectKeys[k]!='webSocketEndpointName')){
											    	var xAxisLabel={"value":index, "text":jsonObjectKeys[k]};
											    	printJsonObject(xAxisLabel);
											    	jsonLabels.labelsArray.push(xAxisLabel);
											    	yAxisChartData.push(jsonObjectValues[k]);
											    	index=index+1;
									    		}
									    	}
									    }
								    	
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
								
								var length=yAxisChartData.length;
								
								//Need this for Dojo charting who has less columns to present. Adding dummy columns here.
								if(length !=0 && length <5){
									for(var k=length;k<5;k++){
										yAxisChartData.push(0);
									}
								}
								// Add the series of data
								chart.addSeries(dashboardName,yAxisChartData);
								
								// Highlight!
						        new Highlight(chart,"default");
						     
						        // Render the chart!
						        chart.render();

								// Add the series of data
								pieChart.addSeries(dashboardName,yAxisChartData);
								
								// Create the tooltip
								var tip = new Tooltip(pieChart,"default");
								
								// Create the slice mover
								var mag = new MoveSlice(pieChart,"default");
								
								// Render the chart!
								pieChart.render();						        
						        
						        
			    		}
			    	}
					
				}//end-for-loop2

			    }); //end-dojofunction

			
			
		}

		
		function onWebSocketsDashboardOpen(event) {
			console.log('Inside onDashboardOpen'); 
		}

		function onWebSocketsDashboardError(event) {
			console.log('Inside onDashboardError'); 
			alert(event.data);
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
		
		/**
		 * 
		 * @param jsonObject
		 */
		function printJsonObject(jsonObject){
			for(var key in jsonObject) {
			      console.log("Key: " + key + " value: " + jsonObject[key]);
			}		
		}
		
