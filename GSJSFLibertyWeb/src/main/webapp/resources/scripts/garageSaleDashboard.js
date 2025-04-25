		var httpProtocol= window.document.location.protocol;
		var wsProtocol = getWSProtocol(httpProtocol);
		var gsDashboardSocket = new WebSocket(wsProtocol+'://'  + window.document.location.host
				+ '/GSjsf20LibertyWeb/garageSaleDashboardEndPoint/databaseMetrics');
				
		gsDashboardSocket.onerror = function(event) {
			onDashboardError(event);
		};

		gsDashboardSocket.onopen = function(event) {
			onDashboardOpen(event);
		};

		gsDashboardSocket.onmessage = function(event) {
			onDashboardMessage(event);
		};
		console.log = function() {}
		

		function onDashboardMessage(event) {
			console.log('Inside onMessage');
			//var jsonObject= event.data;
			var jsonObject=JSON.parse(event.data);
			initializeDatabaseDashboardMetrics(jsonObject);
		}
		
		function initializeDatabaseDashboardMetrics(jsonObject) {
			
			//loop through data and get dashboardNames first.
			console.log('Inside initializeDatabaseDashboardMetrics');
			var dashboardNames=[];
			dashboardNames.push('DatabaseMetrics');
	    	console.log("dashboardNames  lengh "+dashboardNames.length);
			    
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

				
				tableElement=document.getElementById("DatabaseMetricsTable");
				if(tableElement !=null){
					tableElement.parentNode.removeChild(tableElement);
				}
	    		var myTable= document.createElement("TABLE");
	    		myTable.setAttribute("id", "DatabaseMetricsTable");
					
					
				for(var i = 0; i < dashboardNames.length; i++) {
			    	//create DIV  for Charts.
			    	parentDiv=document.getElementById("databaseMetricsDiv");
			    	var dashboardName=dashboardNames[i];
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
								
								var jsonObjectKeys= getJsonKeys(jsonObject);
								var jsonObjectValues = getJsonValues(jsonObject);
								for(var j = 0; j < jsonObjectKeys.length; j++) {
									//console.log(jsonArray.type);
								    	var xAxisLabel={"value":index, "text":jsonObjectKeys[j]};
								    	printJsonObject(xAxisLabel);
								    	jsonLabels.labelsArray.push(xAxisLabel);
								    	yAxisChartData.push(jsonObjectValues[j]);
								    	index=index+1;
								    
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

		
		function onDashboardOpen(event) {
			console.log('Inside onDashboardOpen'); 
		}

		function onDashboardError(event) {
			console.log('Inside onDashboardError'); 
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
