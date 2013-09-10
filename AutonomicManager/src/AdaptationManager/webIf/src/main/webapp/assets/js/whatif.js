var REST_GET_VALUES = 'http://' + REST_HOST + ':' + REST_PORT +'/whatif/values';
var REST_GET_WHATIF = 'http://' + REST_HOST + ':' + REST_PORT +'/whatif';

var resources = [ 'abortRate', 'throughput', 'readResponseTime', 'writeResponseTime' ];

var containers = {
		'throughput' : 'placeholderThroughput',
		'abortRate' : 'placeholderAbortRate',
		'readResponseTime' : 'placeholderReadResponseTime',
		'writeResponseTime' : 'placeholderWriteResponseTime',
}

var plots = {
		'throughput' : null,
		'abortRate' : null,
		'readResponseTime' : null,
		'writeResponseTime' : null,
}

var series = {
		"throughput" : [ {			
			data : [],
			lines : {
				fill : true,
			},
			curvedLines: {
				apply : true
			}
		} ],
		"abortRate" : [ {
			data : [],
			lines : {
				fill : true
			}
		} ],
		"readResponseTime" : [ {
			data : [],
			lines : {
				fill : true
			},
			curvedLines: {
				apply : true
			}
		} ],

		"writeResponseTime" : [ {
			data : [],
			lines : {
				fill : true
			},
			curvedLines: {
				apply : true
			}
		} ]
}

$(document).ready( 

		function(){
			
			init_radioBtn();

			$("body").on({
				// When ajaxStart is fired, add 'loading' to body class
				ajaxStart: function() { 			    	
					$(this).addClass("loading"); 
				},
				// When ajaxStop is fired, rmeove 'loading' from body class
				ajaxStop: function() {
					$(this).removeClass("loading"); 
				}    
			});

			$('#updateValues').click(function() { 
				retrieveValues(); 
				return false;
			});

			$('#forecastAction').click(function() { 
				getAllAndUpdate(); 
				return false;
			});

			$(":input").focus(function(){
				console.log();
				var label_for = $(this).attr('id');
				$("label[for=" + label_for + "]").css("border-bottom", "1px dashed");
			});

			$(":input").focusout(function(){
				console.log();
				var label_for = $(this).attr('id');
				$("label[for=" + label_for + "]").css("border-bottom", "0px");
			});

			/**
			 * CONTAINER PKRY
			 */
			var container = document.querySelector('#container');

			var pckry = new Packery(container, {
				columnWidth : 430,
				rowHeight : 330
			});

			var itemElems = pckry.getItemElements();
			// for each item...
			for ( var i = 0, len = itemElems.length; i < len; i++) {
				var elem = itemElems[i];
				// make element draggable with Draggabilly
				var draggie = new Draggabilly(elem);
				// bind Draggabilly events to Packery
				pckry.bindDraggabillyEvents(draggie);
			}

			$('#container').bind('dblclick', function(eventObject) {
				$target = $(event.target);
				console.log($target.parent().parent());
				if ($target.hasClass('flot-overlay')) {
					pckry.remove($target.parent().parent());
				}				
			});



			drawPlots();
			//getAllAndUpdate();
			// getAllAndUpdate('nodes');
			// getAllAndUpdate('writePercentage');


		}
);

/* *********** */
/* Out of main */
/* *********** */

function init_radioBtn(){	
	var $radio = $('input:radio[name=xaxis]');

	// INIT		
	if($radio.is(':checked') === false) {
		$radio.filter('[value=NODES]').prop('checked', true);
		disableFieldset($radio);
	}

	// LISTENER
	$radio.change(function(){
		disableFieldset($(this));
	});	      

}

function disableFieldset($radio){
	
	$("[id^=fixed]").attr('readonly', false);
	
	console.log('A ' + $radio.val()); // nodes	
	console.log('B ' + $radio.attr("name")); // xaxis
	// seleziono tutte le fieldset che non si chiamano in quel modo e le disattivo
	
	// name + '_' + value
	
	var fieldsetName = 'fixed' + $radio.attr("value");
	console.log('C ' + fieldsetName);
	var enabled = disabled = fieldsetName;
	
	$("#" + fieldsetName ).attr('readonly', true);	
	/*
	if($radio.attr("value")=="TRUE"){
		//enabled += 'self';
		disabled += 'manual';
		console.log("disabled: " + disabled);
		$('fieldset[name="'+ disabled +'"]').children().attr("disabled", "disabled");
	} else {
		enabled += 'manual';
		console.log("enabled: " + enabled);
		$('fieldset[name="'+ enabled +'"]').children().removeAttr("disabled");
		//disabled += 'self';
	}	
	*/		
}








function serie(label,data)
{
	this.label = label;
	this.data = data;
	this.lines = { fill : true, };
	this.curvedLines = { apply : true };
}

function getAllAndUpdate() {
	var dataToBeSent = $("form#whatif").serialize();
	console.log("dataToBeSent: " + dataToBeSent);

	var url = REST_GET_WHATIF;

	$.ajax({
		dataType: "json",
		contentType: "application/x-www-form-urlencoded",		
		url: url,
		type: "POST",
		crossDomain: true,
		data: dataToBeSent,	

		success: function(json) {	 

			console.log(json);
			console.log("risorse: " + resources.length);
			console.log("predizione fatta da: " + json.length + " oracoli");

			for ( var resLen = 0; resLen < resources.length; resLen++) {				
				var param = resources[resLen];
				var currPlot = plots[param];

				console.log("creando grafico per risorsa: " + param);

				var dataseries = [];

				for(var forecastIter=0; forecastIter < json.length; forecastIter++){
					var forecast = json[forecastIter];
					console.log("analizzando oracolo: " + forecast.forecaster);
					var currSerie = new serie(forecast.forecaster, forecast[param]);
					console.log("nuova serie: " + currSerie);
					dataseries.push(currSerie);
				}

				// updating the plot 
				currPlot.setData(dataseries);
				currPlot.setupGrid();
				currPlot.draw();
			}

		},
		error: function(xhr, status) {
			alert(status);
		}
	});
}


function drawPlots() {
	for ( var i = 0; i < resources.length; i++) {
		element = resources[i];

		var containerId = containers[element];
		var container = $("#" + containerId);
		var currSeries = series[element];

		var plot = $
		.plot(
				container,
				currSeries,
				{
					grid : {
						borderWidth : 1,
						minBorderMargin : 20,
						labelMargin : 10,
						backgroundColor : {
							colors : [ "#fff", "#e4f4f4" ]
						},
						margin : {
							top : 8,
							bottom : 20,
							left : 20
						},
						markings : function(axes) {
							var markings = [];
							var xaxis = axes.xaxis;
							for ( var x = Math.floor(xaxis.min); x < xaxis.max; x += xaxis.tickSize) {
								markings
								.push({
									xaxis : {
										from : x,
										to : x
										+ xaxis.tickSize
									},
									color : "rgba(232, 232, 255, 0.2)"
								});
							}
							console.log(markings);
							return markings;
						}
					},
					xaxis : {
						//min: 0,
						//max: 5,
						tickSize: 1,
						//tickFormatter : function(val, axis) {
						//return val+1;
						//}
					},
					yaxis : {
						min : 0,
						autoscaleMargin : 0.02
						// max : 10
					},
					legend : {
						position: "nw", // or "nw" or "se" or "sw"
						show : true
					},
					series: {
						curvedLines: {
							active: true
						}
					}
				});
		plots[element] = plot;
	}
}

function retrieveValues(){
	console.log(REST_GET_VALUES);
	$.getJSON( REST_GET_VALUES, function(data){

		$.each(data, function(key, val) {
			console.log("key: " + key);
			console.log("val: " + val);
			$('#' + key ).val(val);
		});



		console.log(data);
		// alert("ricevuto");
		/*
		 * var items = []; $.each(data, function(key, val) { var htmlField =
		 * resourceToFeature[key];
		 * 
		 * if(htmlField){ console.log("htmlField: " + htmlField);
		 * console.log("val:" + val); var field = featureToField[htmlField];
		 * console.log("field:" + field); console.log("valore:" + val[field]);
		 * $('span#' + current + htmlField ).text(val[field]); $('span#' +
		 * currentOpt + htmlField ).text("OPT"); } });
		 */
		// alert(items);
	});
}


