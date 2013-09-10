var resources = [
                 "scale",
                 "rep_degree",
                 "rep_protocol",
                 "data_placement"
                 ];

var resourceToFeature = { "scale":"scale", 
						"replicationDegree":"rep_degree", 
						"replicationProtocol":"rep_protocol",
						"dataPlacement":"data_placement"
					  };
var featureToField = { 
		"scale":"size", 
		"rep_protocol":"protocol", 
		"rep_degree":"degree",
		//"dataPlacement":"data_placement"
	  };

var REST_STATUS = 'http://' + REST_HOST + ':' + REST_PORT +'/status';
var REST_SET_SCALE = 'http://' + REST_HOST + ':' + REST_PORT +'/tuning/scale';
var REST_SET_DEGREE = 'http://' + REST_HOST + ':' + REST_PORT +'/tuning/degree';
var REST_SET_PROTOCOL = 'http://' + REST_HOST + ':' + REST_PORT +'/tuning/protocol';

var REST_SET_FORECASTER = 'http://' + REST_HOST + ':' + REST_PORT +'/tuning/forecaster';

$(document).ready( 
    		
    		function(){
    			console.log("REST_STATUS: " + REST_STATUS);
    			init(); 
    			$('.fancybox').fancybox();

    			$('#forecaster').submit(function() {
    				alert("forecaster");
                    sendForecaster(); 
                    return false;
                });
    			
    			$('#scale').submit(function() { 
                    sendScale(); 
                    return false;
                });
    			
    			$('#degree').submit(function() { 
                    sendDegree(); 
                    return false;
                });
    			
    			$('#protocol').submit(function() { 
                    sendProtocol(); 
                    return false;
                });


    			
    			
    		}    
    		    		
);

function init(){
	init_radioBtn();
	//initCurrentConfig();
}

/*
{
	"status":"WORKING",
	"scale":{"size":0,"tuning":"AUTO","forecaster":"ANALYTICAL"},
	"replicationProtocol":{"protocol":"TWOPC","tuning":"AUTO","forecaster":"ANALYTICAL"},
	"replicationDegree":{"degree":2,"tuning":"AUTO","forecaster":"ANALYTICAL"},
	"dataPlacement":{"tuning":"AUTO","forecaster":"ANALYTICAL"}
}
*/

function initCurrentConfig(){
	var current = 'current_';
	var currentOpt = 'current_opt_';
	
	$.getJSON( REST_STATUS, function(data){
				
		var items = [];
		$.each(data, function(key, val) {
			var htmlField = resourceToFeature[key];
			
			if(htmlField){	
				console.log("htmlField: " + htmlField);
				console.log("val:" + val);
				var field = featureToField[htmlField];
				console.log("field:" + field);
				console.log("valore:" + val[field]);
				$('span#' + current + htmlField ).text(val[field]);
				$('span#' + currentOpt + htmlField ).text("OPT");				
			}						
		});
		//alert(items);
	});
	
}

function init_radioBtn(){	
	
	var element = null;
	
	for (var i = 0; i < resources.length; i++) {		
		console.log(resources.length);
		element = resources[i];
		if(element == 'data_placement')
			break;
		
		var $radios = $('input:radio[name='+element+'_tuning]');
				
		// INIT		
	    if($radios.is(':checked') === false) {
	        $radios.filter('[value=FALSE]').prop('checked', true);
	        disableFieldset($radios);
	    }
	    
	    // LISTENER
	    $radios.change(function(){
	    	disableFieldset($(this));
	    });	      
	}	
}

function disableFieldset($radio){

	
	console.log($radio.val()); // manual o self	
	console.log($radio.attr("name"));
	// seleziono tutte le fieldset che non si chiamano in quel modo e le disattivo
	
	// name + '_' + value
	
	var fieldsetName = $radio.attr("name") + '_';
	var enabled = disabled = fieldsetName;
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
}

function sendForecaster() {
	var dataToBeSent = $("form#forecaster").serialize();
	console.log("forecaster - dataToBeSent: " + dataToBeSent);
	
	//$('#form#forecaster').ajaxForm();
	
	$.ajax({
		dataType: "json",
		contentType: "application/x-www-form-urlencoded",		
		url: REST_SET_FORECASTER,
		type: "POST",
		crossDomain: true,
		data: dataToBeSent,	

		success: function(data) {	        
	        alert('ok!');
	    },
		error: function(xhr, status) {
			console.log(xhr);
			alert(status);
		}
	});
	
	
//	$.ajax({
//	    url: REST_SET_FORECASTER,
//	    type: 'PUT',
//	    crossDomain: true,
//	    data: dataToBeSent,
//	    dataType: 'json',
//	    success: function(data) {	        
//	        alert('ok!');
//	    }
//	});
}

function sendScale() {
	var dataToBeSent = $("form#scale").serialize();
	$('#form#scale').ajaxForm();
	
	$.ajax({
		dataType: "json",
		contentType: "application/x-www-form-urlencoded",		
		url: REST_SET_SCALE,
		type: "POST",
		crossDomain: true,
		data: dataToBeSent,	

		success: function(data) {	        
	        alert('ok! ' + data);
	    },
		error: function(xhr, status) {
			console.log(xhr);
			alert(status);
		}
	});
	
}

function sendDegree() {
	var dataToBeSent = $("form#degree").serialize();
	$('#form#degree').ajaxForm();
	
	$.ajax({
		dataType: "json",
		contentType: "application/x-www-form-urlencoded",		
		url: REST_SET_DEGREE,
		type: "POST",
		crossDomain: true,
		data: dataToBeSent,	

		success: function(data) {	        
	        alert('ok! ' + data);
	    },
		error: function(xhr, status) {
			console.log(xhr);
			alert(status);
		}
	});
	
}


function sendProtocol() {
	var dataToBeSent = $("form#protocol").serialize();
	$('#form#protocol').ajaxForm();
	
	$.ajax({
		dataType: "json",
		contentType: "application/x-www-form-urlencoded",		
		url: REST_SET_PROTOCOL,
		type: "POST",
		crossDomain: true,
		data: dataToBeSent,	

		success: function(data) {	        
	        alert('ok! ' + data);
	    },
		error: function(xhr, status) {
			console.log(xhr);
			alert(status);
		}
	});
	
}

//$.post(REST_SET_SCALE, dataToBeSent);
/*
console.log("DATA_TO_SENT" + dataToBeSent);
$.ajax({
    type: 'PUT',
    crossDomain: true,
    data: {}
    //dataType: 'json',
    url: REST_SET_SCALE,
    success: function (data) {
    	alert("Thank you for your comment!");
    },
    error: function (request, status, error) {
//    	setTimeout(function(){
//    		document.location.reload(true);	
//    	}, 5000);
    console.log(status);
    console.log(error);            	
    }
});	
*/
	