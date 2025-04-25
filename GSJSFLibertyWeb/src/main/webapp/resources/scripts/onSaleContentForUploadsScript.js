		var httpProtocol= window.document.location.protocol;
		var wsProtocol = getWSProtocol(httpProtocol);
		var webSocket = new WebSocket(wsProtocol+'://' + window.document.location.host
				+ '/GSjsf20LibertyWeb/inventoryOnSaleProgEndpoint');
		
		console.log = function() {}

				
		webSocket.onerror = function(event) {
			onError(event);
		};

		webSocket.onopen = function(event) {
			onOpen(event);
		};

		webSocket.onmessage = function(event) {
			onMessage(event);
		};

		function onMessage(event) {
			//console.log('Inside onMessage');
			var array= event.data;
			var jsonArray=JSON.parse(array);
			printTable(jsonArray);
		}
		
		function printTable(jsonArray) {
			var protocolVar=window.document.location.protocol;
			var videoRequestURL=protocolVar + '//' + window.document.location.host	+ '/GSjsf20LibertyWeb/facelets/uploadGarageSaleDemoVideoFile.jsf';
			if(jsonArray.length > 0){
				var htmlTable = '<table border="1" cellpadding="2" cellspacing="0" style="text-align: center" class="dataTable">';
				htmlTable = htmlTable + '<tr class="rowClass1"><th class="headerClass" scope="col"><span id="name" class="outputText">Name</span></th> \
										<th class="headerClass" scope="col"><span id="unitPrice" class="outputText">Unit pice</span></th> \
										<th class="headerClass" scope="col"><span id="salePrice" style="text-decoration: underline" class="outputText">Sale price</span></th></tr>';
				var index=1;
				for(var i=0;i<jsonArray.length;i++){
					var queryString ='?'+'uploadMe=true&amp;' +'fileName=ProductDemo' + index+'.mp4'+'&amp;unitPrice='+jsonArray[i].unitPrice+'&amp;salePrice='+jsonArray[i].salePrice+'&amp;itemID='+jsonArray[i].itemID;
						htmlTable += '<tr class="rowClass1">' + '<td class="columnClass1">'+'<a href="'+ videoRequestURL +queryString+'">' + jsonArray[i].itemID +	'</a></td>' + 
																'<td class="columnClass1">' + '<span id="unitPriceSpan" style="text-decoration: line-through">' + jsonArray[i].unitPrice + '</span>' + '</td>'+
																'<td class="columnClass1">' + '<span id="salePriceSpan" style="color: red">' + jsonArray[i].salePrice + '</span>' + '</td></tr>';
						index=index+1;
				}
											
				htmlTable += "</table>";
				document.getElementById('inventoryOnSaleTable').innerHTML = '<br />' + htmlTable;
			}
		}

		
		function onOpen(event) {
			//console.log('Inside onOpen'); 
		}

		function onError(event) {
			//console.log('Inside onError'); 
			//alert(event.data);
		}

		function send() {
			//console.log('Inside send()'); 
			webSocket.send("refreshMe");
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
		
