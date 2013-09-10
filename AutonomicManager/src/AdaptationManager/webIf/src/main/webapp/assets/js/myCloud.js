var REST_MONITOR = 'http://' + REST_HOST + ':' + REST_PORT + '/monitor/';

var resources = [ 'throughput', 'nodes', 'writePercentage', 'abortRate' ];

var containers = {
	'throughput' : 'placeholderThroughput',
	'nodes' : 'placeholderNodes',
	'writePercentage' : 'placeholderWritePercentage',
	'abortRate' : 'placeholderAbortRate'
}

var plots = {
	'throughput' : null,
	'nodes' : null,
	'writePercentage' : null,
	'abortRate' : null
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
	"nodes" : [ {
		data : [],
		lines : {
			fill : true
		}
	} ],
	"writePercentage" : [ {
		data : [],
		lines : {
			fill : true
		},
		curvedLines: {
			apply : true
		}
	} ],
	"abortRate" : [ {
		data : [],
		lines : {
			fill : true
		},
		curvedLines: {
			apply : true
		}
	} ]
}

var lastIDs = {}

//	series = [ {
//		data : getNumNodes(),
//		lines : {
//			fill : true
//		}
//	} ];

var size = 300;

$(function() {

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

	// DENTRO

	function update(param) {
		var url = REST_MONITOR + param + '/last';

		$.getJSON(url, function(json) {

			//console.log("LASTID_OLD:" + lastIDs[param]);
			//console.log("LASTID_NEW:" + json.data[0][0]);

			if (lastIDs[param] == json.data[0][0]) {
				//console.log('skipping');
				return;
			}
			//console.log('not skipped');
			lastIDs[param] = json.data[0][0];

			var currSeries = series[param];
			var nextData = currSeries[0].data;

			console.log(nextData[19]);
			nextData = nextData.slice(1);
			nextData.push([ size - 1, json.data[0][1] ]);
			//console.log(nextData);

			var res = [];
			for ( var i = 0; i < nextData.length; ++i) {
				res.push([ i, nextData[i][1] ]);
			}

			currSeries[0].data = res;

			var currPlot = plots[param];
			currPlot.setData(currSeries);

			console.log(currPlot.getData());

			currPlot.setupGrid();
			currPlot.draw();

		});
	}

	function getAllAndUpdate(param) {

		var url = REST_MONITOR + param;

		$.getJSON(url, function(json) {

			dataSize = json.data.length;
			lastIDs[param] = json.data[dataSize - 1][0];
			console.log("LASTID di " + param + "=" + lastIDs[param]);

			var nextData = [];
			for ( var i = 0; i < size; i++)
				nextData[i] = 0;

			for ( var i = 0; i < json.data.length; i++) {
				nextData = nextData.slice(1);
				nextData.push(json.data[i][1]);
			}

			var res = [];
			for ( var i = 0; i < nextData.length; ++i) {
				res.push([ i, nextData[i] ]);
			}

			var currSeries = series[param];
			currSeries[0].data = res;

			var currPlot = plots[param];
			currPlot.setData(currSeries);

			currPlot.setupGrid();
			currPlot.draw();

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
										for ( var x = Math.floor(xaxis.min); x < xaxis.max; x += xaxis.tickSize * 2) {
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
										return markings;
									}
								},
								xaxis : {
									tickSize: 60,
									tickFormatter : function(val, axis) {
										var totSec = ((size*5) - (val*5));
										var label;
										var seconds;
										var minutes;
										
										minutes = Math.floor(totSec/60);
										seconds = totSec % 60;
										
										if(minutes>0)
											label = minutes + "m "+ ((seconds>0)? (seconds+"s") : "");
										else
											label = seconds +"s";
										
										return label;								    									
									}
								},
								yaxis : {
									min : 0,
									autoscaleMargin : 0.1
								//max : 10
								},
								legend : {
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

	drawPlots();
	
	getAllAndUpdate('throughput');
	getAllAndUpdate('nodes');
	getAllAndUpdate('writePercentage');
	getAllAndUpdate('abortRate');

	
	setInterval(function() {
		update('throughput');
		update('nodes');
		update('writePercentage');
		update('abortRate');

		//getAllAndUpdate('throughput');
		//getAllAndUpdate('node');
	}, 2000);

	//	setInterval(function updateRandom() {
	//		seriesThroughput[0].data = getAll(throughput);
	//		plotThroughput.setData(seriesThroughput);
	//		plotThroughput.draw();
	//	}, 40);

	//	function getLast(param){
	//		var url = REST_MONITOR + param + '/last'
	//		$.getJSON( url, function(data){
	//								
	//		});	
	//	}

	//	var dataNodes = [], dataResponseTime, totalPoints = 300;

	//	function getNumNodes(){
	//
	//		if (dataNodes.length > 0)
	//			dataNodes = dataNodes.slice(1);
	//
	//		// Do a random walk
	//
	//		while (dataNodes.length < totalPoints) {
	//
	//			var prev = dataNodes.length > 0 ? dataNodes[dataNodes.length - 1] : 50, y = prev
	//					+ Math.random() * 10 - 5;
	//
	//			if (y < 0) {
	//				y = 0;
	//			} else if (y > 100) {
	//				y = 100;
	//			}
	//
	//			dataNodes.push(y);
	//		}
	//
	//		// Zip the generated y values with the x values
	//
	//		var res = [];
	//		for ( var i = 0; i < dataNodes.length; ++i) {
	//			res.push([ i, dataNodes[i] ])
	//		}
	//
	//		return res;	
	//	}	

	//	// Set up the control widget
	//
	//	var updateInterval = 300;
	//	$("#updateInterval").val(updateInterval).change(function() {
	//		var v = $(this).val();
	//		if (v && !isNaN(+v)) {
	//			updateInterval = +v;
	//			if (updateInterval < 1) {
	//				updateInterval = 1;
	//			} else if (updateInterval > 2000) {
	//				updateInterval = 2000;
	//			}
	//			$(this).val("" + updateInterval);
	//		}
	//	});

});
