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

			// Define the data
			var pieChartData = [10000,9200,11811,12000,7662,13887,14200,12222,12000,10009,11288,12099];
			
			// Create the chart within it's "holding" node
			var pieChart = new Chart("pieChartNode");

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

			// Add the series of data
			pieChart.addSeries("Monthly Sales - 2010",pieChartData);
			
			// Create the tooltip
			var tip = new Tooltip(pieChart,"default");
			
			// Create the slice mover
			var mag = new MoveSlice(pieChart,"default");
			
			// Render the chart!
			pieChart.render();
			
	        // Define the data
	        var chartData = [10000,9200,11811,12000,7662,13887,14200,12222,12000,10009,11288,12099];
	     
	        // Create the chart within it's "holding" node
	        var chart = new Chart("chartNode");
	     
	        // Set the theme
	        chart.setTheme(theme);
	     
	        // Add the only/default plot
	        chart.addPlot("default", {
	            type: ColumnsPlot,
	            markers: true,
	            gap: 5
	        });
	     
	        // Add axes
	        chart.addAxis("x");
	        chart.addAxis("y", { vertical: true, fixLower: "major", fixUpper: "major" });
	     
	        // Add the series of data
	        chart.addSeries("Monthly Sales",chartData);
	     
	        // Highlight!
	        new Highlight(chart,"default");
	     
	        // Render the chart!
	        chart.render();


		});




