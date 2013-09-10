<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title><s:text name="MyCloud.message" /></title>
<link
	href="${pageContext.request.contextPath}/assets/css/per_page/whatif.css"
	type="text/css" media="screen" rel="stylesheet" />
	
<script type="text/javascript">
	var REST_HOST = '<s:property value="getRestHost()" />';
	var REST_PORT = '<s:property value="getRestPort()" />';
</script>
	
	<!-- PACKERY + DRAGGABILLY -->
	<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/packery.pkgd.min.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/draggabilly.js" ></script>
	
	<!-- FLOT + SMOOTH -->
	<script type="text/javascript" src="${pageContext.request.contextPath}/assets/flot/jquery.flot.min.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/assets/flot/curvedLines.js" ></script>    
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/whatif.js"></script>
	<script type="text/javascript" src="http://malsup.github.com/jquery.form.js"></script>
</head>

<body>
	<!-- Promo -->
	<div id="col-top"></div>
	<div class="box" id="col">

		<div id="ribbon"></div>
		<!-- /ribbon (design/ribbon.gif) -->

		<!-- Screenshot in browser (replace tmp/browser.gif) -->
		<div id="col-browser"></div>

		<div id="col-text">

			<h2>What-If Analysis</h2>

			<!-- <h2 id="slogan"><span><s:property value="message"/></span></h2> -->

			<!--  <a href="${pageContext.request.contextPath}/registration.jsp">Register</a>  -->


			<form id="whatif" action="">
			
				<div class="table">
					<div class="row">
						<div class="column first">
							<fieldset>
								<legend>Data Access Pattern:</legend>
									<div class="param">
										<label for="acf">Application Contention Factor:</label>
										<input type="text" id="acf" name="acf" size="5" />
									</div>
									<div class="param">
										<label for="percentageSuccessWriteTransactions">Write Transactions Percentage:</label>
										<input type="text" name="percentageSuccessWriteTransactions" id="percentageSuccessWriteTransactions" size="5" />
									</div>
									<div class="param">
										<label for="avgNumPutsBySuccessfulLocalTx"># PUT per write transaction:</label>
										<input type="text" name="avgNumPutsBySuccessfulLocalTx" id="avgNumPutsBySuccessfulLocalTx" size="5" />
									</div>
									<div class="param">
										<label for="avgGetsPerWrTransaction"># GET per write transaction:</label>
										<input type="text" name="avgGetsPerWrTransaction" id="avgGetsPerWrTransaction" size="5" />
									</div>
									<div class="param">
										<label for="avgGetsPerROTransaction"># GET per read only transaction:</label>
										<input type="text" name="avgGetsPerROTransaction" id="avgGetsPerROTransaction" size="5" />
									</div>
									<div class="param">
									</div>							
							</fieldset>											
						</div>
					</div>
					<div class="row">
						<div class="column">
							<fieldset>
								<legend>CPU Costs:</legend>
									<div class="param">
										<label for="localUpdateTxLocalServiceTime">Write transaction demand:</label>
										<input type="text" name="localUpdateTxLocalServiceTime" id="localUpdateTxLocalServiceTime" size="5" />
									</div>
									<div class="param">
										<label for="localReadOnlyTxLocalServiceTime">Read Only transaction demand:</label>
										<input type="text" name="localReadOnlyTxLocalServiceTime" id="localReadOnlyTxLocalServiceTime" size="5" />																	
									</div>
									<div class="param">
									</div>
					
							</fieldset>											
						</div>
					</div>
					<div class="row">
						<div class="column">
							<fieldset>
								<legend>NET Costs:</legend>
									<div class="param">
										<div class="line"></div>
										<label for="avgPrepareCommandSize">Size of prepare message:</label>										
										<input type="text" name="avgPrepareCommandSize" id="avgPrepareCommandSize" size="5" />
									</div>
									<div class="param">
										<label for="avgPrepareAsync">Prepare latency:</label>
										<input type="text" name="avgPrepareAsync" id="avgPrepareAsync" size="5" />
									</div>
									<div class="param">
										<label for="avgCommitAsync">Commit latency:</label>
										<input type="text" name="avgCommitAsync" id="avgCommitAsync" size="5" />
									</div>
									<div class="param">
										<label for="avgRemoteGetRtt">Remote get latency:</label>
										<input type="text" name="avgRemoteGetRtt" id="avgRemoteGetRtt" size="5" />
									</div>
									<div class="param">
									</div>
									<div class="param">
									</div>
								</fieldset>											
						</div>
					</div>
					<div class="row">
						<div class="column">
							<fieldset>
								<legend>Forecasting:</legend>
								
									<div class="param">
										<fieldset>
										<legend>X-axis:</legend>
											<div class="line"></div>
											<label class="close" for="xaxisNodes">Nodes</label>																						
											<input id="xaxisNodes" type="radio" name="xaxis" value="NODES" />
																						
											<input id="fixedNODES" type="text" name="fixed_nodes" size="2" value="2" />
											<br />
											
											<label class="close" for="xaxisDegree">Rep. Degree</label>
											<input id="xaxisDegree" type="radio" name="xaxis" value="DEGREE" />
											
											<input id="fixedDEGREE" type="text" name="fixed_degree" size="2" value="2" />
											<br />											
											
											<label class="close" for="xaxisProtocol">Rep. Protocol</label>																															
											<input id="xaxisProtocol" type="radio" name="xaxis" value="PROTOCOL" />
											
											<select id="fixedPROTOCOL" name="fixed_protocol">
												<option value="TWOPC">2PC</option>
												<option value="TO">TO</option>
												<option value="PB">PB</option>
											</select>
											<br />
												 
										</fieldset>										
									</div>
									
									<div class="param">
										<div class="line"></div>
										<fieldset>
										<legend>Predictors:</legend>
											<div class="line"></div>
											<input type="checkbox" name="oracoles" value="ANALYTICAL" />Analytical<br />
											<input type="checkbox" name="oracoles" value="SIMULATOR" />Simulator<br />
											<input type="checkbox" name="oracoles" value="MACHINE_LEARNING" />Machine Learning<br />	
										</fieldset>										
									</div>
									
									<div class="param">
									</div>
<!-- 
									<div class="param">
										<input type="text" id="repDegree" name="repDegree" value="2">
										
									</div>
									<div class="param">
										<select name="repProtocol">
											<option value="TWOPC">2PC</option>
											<option value="TO">TO</option>
											<option value="PB">PB</option>
										</select>
									</div>
									<div class="param">
									</div>
 -->									
								</fieldset>											
						</div>
					</div>					
					
					<div class="action">
									
				    
				    
					
					
					
					
					
								<input id="updateValues" type="submit" class="submit" value="Update values from system" size="40" />
								<input id="forecastAction" type="button" class="submit" value="Forecast" size="40" /> 																
					</div>
					
				</div>				
			</form>
			
			<div id="container">
				<div class="item">
					<div class="plotTitle">Throughput (tx/sec)</div>
					<div id="placeholderThroughput" class="plot"></div>
				</div>
				<div class="item">
					<div class="plotTitle">Read Response Time (msec)</div>
					<div id="placeholderReadResponseTime" class="plot"></div>
				</div>
				<div class="item">
					<div class="plotTitle">Write Response Time (msec)</div>
					<div id="placeholderWriteResponseTime" class="plot"></div>
				</div>
				<div class="item">
					<div class="plotTitle">AbortRate (%)</div>
					<div id="placeholderAbortRate" class="plot"></div>
				</div>
			</div>	   				
			


		</div>
		<!-- /col-text -->

	</div>
	<!-- /col -->
	<div id="col-bottom"></div>

	<hr class="noscreen" />
	<hr class="noscreen" />
	<div class="modal"></div>
</body>
</html>



