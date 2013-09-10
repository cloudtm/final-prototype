<?xml version="1.0" encoding="UTF-8" ?>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Cloud-TM - Autonomic Manager</title>
    
    <link href="${pageContext.request.contextPath}/assets/css/reset.css" type="text/css" media="screen,projection" rel="stylesheet" />
    <link href="${pageContext.request.contextPath}/assets/css/boxy.css" type="text/css" media="screen,projection" rel="stylesheet" />
    <link href="${pageContext.request.contextPath}/assets/css/main.css" type="text/css" media="screen,projection" rel="stylesheet" />
    <!--[if lte IE 6]><link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/main-msie.css" /><![endif]-->
    <link href="${pageContext.request.contextPath}/assets/css/style.css" type="text/css" media="screen,projection" rel="stylesheet" />
    <link href="${pageContext.request.contextPath}/assets/css/print.css" type="text/css" media="print" rel="stylesheet" />
    
    <sj:head/>
    
    <!-- PER PAGE HEADER -->
    <decorator:head />
    <!-- END PER PAGE HEADER -->
    
    
</head>
<body>


<div id="main">

    <!-- Header -->
    <div id="header">

        <h1 id="logo"><a title="[Go to homepage]" href="./"><img width="160" src="http://www.cloudtm.eu/_/rsrc/1288609844892/home/spotlight_logo.png" alt="" /></a></h1>
        <hr class="noscreen" />

        <!-- Navigation -->
        <div id="nav">
            <a href="#">Homepage</a> <span>|</span>
            <a href="#">About us</a> <span>|</span>
            <a href="#">Support</a> <span>|</span>
            <a href="#">Contact</a>
        </div> <!-- /nav -->

    </div> <!-- /header -->
    
    <!-- Tray -->
    <div id="tray">

        <ul>
        	<li>
        		<a href="javascript:void(0)" onclick="window.open('http://html.it/articoli/esempi/articoli_pro/esempio_11_a.htm', 'Console', 'resizable=0,top=10,left=10,width=600,height=200')">Console</a>
        	</li>
            <li><a href="myCloud">MyCloud</a></li> <!-- Active page -->
            <li><a href="tuning">Tuning</a></li>
            <li><a href="whatIf">What-if Analysis</a></li>            
            <li><a href="workloadAnalysis">Workload Analysis</a></li>
            <!-- 
            <li><a href="#"></a></li>
            <li><a href="#"></a></li>
             -->
        </ul>
        
        <!-- Search -->
        <div class="box" id="search">
            <form method="get" action="#">
                <div class="box">
                    
                    
                </div>
            </form>
        </div> <!-- /search -->

    <hr class="noscreen" />
    </div> <!-- /tray -->    
    
    
    <decorator:body />
    
    
    <!-- Footer -->
    <div id="footer">
	
	<h2>Cloud-TM EU Project, 2010-2013</h2>
       
    </div> <!-- /footer -->

</div> <!-- /main -->


</body>
</html>
