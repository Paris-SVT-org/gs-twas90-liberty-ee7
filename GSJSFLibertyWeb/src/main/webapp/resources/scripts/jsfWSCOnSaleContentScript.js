		console.log = function() {}

		function printHidden(){
			var hiddenValue=document.getElementById('onSaleContentForm:jsfWSInventoryOnSaleID').value;
			console.log(hiddenValue);
			//alert(hiddenValue);
			onMessage(hiddenValue);
		}

		
		function onMessage(data) {
			//console.log('Inside onMessage');
			var jsonArray=JSON.parse(data);
			printTable(jsonArray);
		}
		
		function printTable(jsonArray) {
			var protocolVar=window.document.location.protocol;
			var videoRequestURL=protocolVar + '//' + window.document.location.host	+ '/GSjsf20LibertyWeb/facelets/jsfWSCPlayGarageSaleDemoVideoFile.jsf';
			if(jsonArray.length > 0){
				var htmlTable = '<table border="1" cellpadding="2" cellspacing="0" style="text-align: center" class="dataTable">';
				htmlTable = htmlTable + '<tr class="rowClass1"><th class="headerClass" scope="col"><span id="name" class="outputText">Name</span></th> \
										<th class="headerClass" scope="col"><span id="unitPrice" class="outputText">Unit pice</span></th> \
										<th class="headerClass" scope="col"><span id="salePrice" style="text-decoration: underline" class="outputText">Sale price</span></th></tr>';
				var index=1;
				for(var i=0;i<jsonArray.length;i++){
					var queryString ='?'+'uploadMe=true&amp;' +'fileName=ProductDemo' + index+'.mp4'+'&amp;unitPrice='+jsonArray[i].unitPrice+'&amp;salePrice='+jsonArray[i].salePrice+'&amp;itemID='+jsonArray[i].itemID;
						htmlTable += '<tr class="rowClass1">' + '<td class="columnClass1">'+'<a href="'+ videoRequestURL + queryString+'">' + jsonArray[i].itemID +	'</a></td>' + 
																'<td class="columnClass1">' + '<span id="unitPriceSpan" style="text-decoration: line-through">' + jsonArray[i].unitPrice + '</span>' + '</td>'+
																'<td class="columnClass1">' + '<span id="salePriceSpan" style="color: red">' + jsonArray[i].salePrice + '</span>' + '</td></tr>';
						index=index+1;
				}
											
				htmlTable += "</table>";
				document.getElementById('inventoryOnSaleTable').innerHTML = '<br />' + htmlTable;
			}
		}
		
        window.onload= function() { 
	        document.getElementById('onSaleContentForm:inventoryOnSaleButtonID').click();
	    }
				

		
