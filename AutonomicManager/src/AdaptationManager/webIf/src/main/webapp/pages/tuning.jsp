<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title><s:text name="MyCloud.message" /></title>
<link
	href="${pageContext.request.contextPath}/assets/css/per_page/tuning.css"
	type="text/css" media="screen" rel="stylesheet" />

<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/fancybox/jquery.fancybox.css" type="text/css" media="screen" />
<script type="text/javascript">
	var REST_HOST = '<s:property value="getRestHost()" />';
	var REST_PORT = '<s:property value="getRestPort()" />';
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/tuning.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/fancybox/jquery.fancybox.pack.js"></script>
<script src="http://malsup.github.com/jquery.form.js"></script>
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

			<h2>Tuning</h2>
			<!-- <h2 id="slogan"><span><s:property value="message"/></span></h2> -->

			<!-- FORECASTER FORM -->
			<form id="forecaster" action="">

				<div class="resource">
					<fieldset class="resource">
						<legend>Forecaster:</legend>
						
						<!-- CURRENT -->
						<div class="current">
							<ol>
								<li>
									<p class="label">Current configuration:</p>
									<p class="config"><span id="current_forecaster">ANALYTICAL</span></p>								
								</li>
							</ol>
						</div>						
						
						<!-- TUNING -->
						<div class="tuning">
							<ol>							
								<li>
									<div class="conf_tuning">
										<fieldset name="forecaster_tuning_self">
											<!-- <label for="wr_tx_local_exec_time">Forecasting method:</label> -->
											<select name="forecaster" class="forecasting">
												<option selected value="ANALYTICAL">Analytical</option>
												<option value="SIMULATOR">Simulator</option>
												<option value="MACHINE_LEARNING">Machine Learning</option>
												<option value="COMMITTEE">Committee</option>
											</select>
										</fieldset>										
									</div>
								</li>
							</ol>
						</div>
						
						<!-- CONTROL -->
						<div class="control">
							<input class="submit" type="submit" value="Set" />
						</div>
					</fieldset>
				</div>
			</form>
				
				
			<!-- SCALE FORM -->
			<form id="scale">
				<!-- RESOURCE -->
				<div class="resource">
					<fieldset class="resource">
						<legend>Scale:</legend>
						
						<!-- CURRENT -->
						<div class="current">
							<ol>
								<li>
									<p class="label">Current configuration:</p>
									<p class="config"><span id="current_scale">5</span>instances</p>								
								</li>
								<li>
									<p class="label">Current optimal configuration:</p>
									<p class="config"><span id="current_opt_scale">7</span>instances</p>								
								</li>
							</ol>
						</div>						
						
						<!-- TUNING -->
						<div class="tuning">
							<ol>
								<li>
									<div class="radio_tuning">
										<input type="radio" name="scale_tuning" value="FALSE" />
										Manual tuning
									</div>
									<div class="conf_tuning">
										<fieldset name="scale_tuning_manual">
											<input type="text" name="scale_size" id=scale_size size="3" />
											<select name="instance_type">
												<option value="SMALL">Small</option>
												<option selected value="MEDIUM">Medium</option>
												<option value="LARGE">Large</option>
											</select>
										</fieldset>
									</div>		

								</li>
								<li>
									<div class="radio_tuning">
										<input type="radio" name="scale_tuning" value="TRUE" />Self-tuning
									</div>																	
								</li>
							</ol>
						</div>
						
						<!-- CONTROL -->
						<div class="control">
							<input class="submit" type="submit" value="Set" />
						</div>
					</fieldset>
				</div>
			</form>

			<!-- REPLICATION DEGREE FORM -->
			<form id="degree" action="" >
			
				<!-- RESOURCE -->
				<div class="resource">
					<fieldset class="resource">
						<legend>Replication Degree:</legend>
						
						<!-- CURRENT -->
						<div class="current">
							<ol>
								<li>
									<p class="label">Current configuration:</p>
									<p class="config"><span id="current_rep_degree">5</span>replicas per object</p>								
								</li>
								<li>
									<p class="label">Current optimal configuration:</p>
									<p class="config"><span id="current_opt_rep_degree">7</span>replicas per object</p>
								</li>
							</ol>
						</div>									
						
						<!-- TUNING -->
						<div class="tuning">
							<ol>
								<li>
									<div class="radio_tuning">
										<input type="radio" name="rep_degree_tuning" value="FALSE" />
										Manual tuning
									</div>
									<div class="conf_tuning">
										<fieldset name="rep_degree_tuning_manual">
											<input type="text" name="rep_degree_size" id=degree_conf size="3" />											
										</fieldset>
									</div>	
									

								</li>
								<li>
									<div class="radio_tuning">
										<input type="radio" name="rep_degree_tuning" value="TRUE" />Self-tuning
									</div>
								</li>
							</ol>
						</div>
						
						<!-- CONTROL -->
						<div class="control">
							<input class="submit" type="submit" value="Set" />
						</div>
					</fieldset>
				</div>
			</form>

			<!-- PROTOCOL SWITCHING FORM -->
			<form id="protocol" action="" >
			
				<!-- RESOURCE -->
				<div class="resource">
					<fieldset class="resource">
						<legend>Protocol Switching:</legend>
						
						<!-- CURRENT -->
						<div class="current">
							<ol>
								<li>
									<p class="label">Current configuration:</p>
									<p class="config"><span id="current_rep_protocol">TO</span></p>								
								</li>
								<li>
									<p class="label">Current optimal configuration:</p>
									<p class="config"><span id="current_opt_rep_protocol">2PC</span></p>
								</li>
							</ol>
						</div>												
						
						<!-- TUNING -->
						<div class="tuning">
							<ol>
								<li>
									<div class="radio_tuning">										
										<input type="radio" id="rep_protocol_tuning_manual" name="rep_protocol_tuning" value="FALSE" />
										<label for="rep_protocol_tuning_manual">Manual tuning</label>										
									</div>
									<div class="conf_tuning">
										<fieldset name="rep_protocol_tuning_manual">
											<select name="rep_protocol">
												<option selected value="2PC">2PC</option>
												<option value="TO">TO</option>
												<option value="PB">PB</option>
											</select>											
										</fieldset>
									</div>	
									

								</li>
								<li>
									<div class="radio_tuning">
										<input type="radio" id="rep_protocol_tuning_self" name="rep_protocol_tuning"  value="TRUE" />
										<label for="rep_protocol_tuning_self">Self-tuning</label>
									</div>
								</li>
							</ol>
						</div>
						
						<!-- CONTROL -->
						<div class="control">
							<input class="submit" type="submit" value="Set" />
						</div>
					</fieldset>
				</div>
			</form>

			<!-- DATA PLACEMENT FORM -->
			<form>
			
				<!-- RESOURCE -->
				<div class="resource">
					<fieldset class="resource">
						<legend>Data Placement:</legend>
												
						<!-- CURRENT -->
						<div class="current">
							<ol>
								<li>
									<p class="label">AVG Remote OPS/TX [%]:</p>
									<p class="config"><span id="current_data_placement">20</span></p>								
								</li>
								<li>
									<p class="label">AVG Nodes involved in TX:</p>
									<p class="config"><span id="current_opt_data_placement">30</span></p>
								</li>
							</ol>
						</div>						
						
						<!-- TUNING -->
						<div class="tuning">
							<ol>
								<li>
									<div class="radio_tuning">
										
									</div>
									<div class="conf_tuning">
										<input type="checkbox" name="data_placement_tuning" value="Bike" />Self-tuning										
									</div>									
								</li>
							</ol>
						</div>
						
						<!-- CONTROL -->
						<div class="control">
							<input class="submit" type="submit" value="Set" />
						</div>
					</fieldset>
				</div>
			</form>

		</div>
		<!-- /col-text -->

	</div>
	<!-- /col -->
	<div id="col-bottom"></div>

	<hr class="noscreen" />
	<hr class="noscreen" />
</body>
</html>



