		var serviceNames=[];
		var jsonArray;
				
		console.log = function() {}

		function printHidden(){
			var hiddenValue=document.getElementById('pmiDashboardForm:pmiDashboardJsonDataId').value;
			console.log(hiddenValue);
			//alert(hiddenValue);
			onPersonaPMIMessage(hiddenValue);
		}
		
		/**
		 * 
		 * @param event
		 */
		function onPersonaPMIMessage(data) {
			console.log('Inside onPersonaPMIMessage');
			var array=data;
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

				
				tableElement=document.getElementById("myTable");
				if(tableElement !=null){
					tableElement.parentNode.removeChild(tableElement);
				}
	    		var myTable= document.createElement("TABLE");
	    		myTable.setAttribute("id", "myTable");
					
					
				for(var i = 0; i < serviceNames.length; i++) {
			    	//create DIV  for Charts.
			    	parentDiv=document.getElementById("parentDiv");
			    	var serviceName=serviceNames[i];
			    	if(parentDiv!=null){
			    		//chileNodesList = parentDiv.childNodes;
			    		childElement = document.getElementById("id", serviceName+"Chart");
			    		newChildElement = document.getElementById("id", serviceName+"PieChart");
			    		if(childElement==null && newChildElement==null){
				    		var h2=document.createElement("H2");
				    		var t=document.createTextNode(serviceName);
				    		h2.setAttribute("style", "color:orange");
				    		h2.setAttribute("align", "center");
				    		h2.appendChild(t);
				    		myTable.appendChild(h2);
				    		var myRow = document.createElement("TR");
				    		var myTD1 = document.createElement("TD");
				    		var myTD2 = document.createElement("TD");
				    		
				    		var newChild = document.createElement('div');
				    		newChild.setAttribute("id", serviceName+"Chart");
				    		newChild.setAttribute("style", "width:800px;height:400px;");
				    		
				    		var newChildPie = document.createElement('div');
				    		newChildPie.setAttribute("id", serviceName+"PieChart");
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
								var chart = new Chart(serviceName+"Chart");

								// Set the theme
						        chart.setTheme(chartTheme);
						        
						        
						        // Add the only/default plot
						        chart.addPlot("default", {
						            type: ColumnsPlot,
						            markers: true,
						            gap: 5, labels: true, labelStyle: "outside", labelOffset: 15
						        });
						        

								// Create the chart within it's "holding" node
								var pieChart = new Chart(serviceName+"PieChart");

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
								
								var length=yAxisChartData.length;
								
								//Need this for Dojo charting who has less columns to present. Adding dummy columns here.
								if(length !=0 && length <5){
									for(var k=length;k<5;k++){
										yAxisChartData.push(0);
									}
								}
								// Add the series of data
								chart.addSeries(serviceName,yAxisChartData);
								
								// Highlight!
						        new Highlight(chart,"default");
						     
						        // Render the chart!
						        chart.render();

								// Add the series of data
								pieChart.addSeries(serviceName,yAxisChartData);
								
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
		
        window.onload= function() { 
	        document.getElementById('pmiDashboardForm:personaPMIDashboardButton').click();
	    }
				
		