<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html class="no-js" lang="en">
<head>
<meta charset="utf-8">
<!-- If you delete this meta tag World War Z will become a reality -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Jabber Analytics</title>
<link rel="stylesheet" href="resources/css/jabber-analytics.css">
<link rel="stylesheet" href="resources/css/bootstrap.css">
<link rel="stylesheet" href="resources/css/grayscale.css">
</head>

<body id="page-top" data-spy="scroll" data-target=".navbar-fixed-top">

    <!-- Navigation -->
    <nav class="navbar navbar-custom navbar-fixed-top" role="navigation">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-main-collapse">
                    <i class="fa fa-bars"></i>
                </button>
                <a class="navbar-brand page-scroll" href="#page-top">
                    <i class="fa fa-play-circle"></i>  <span class="light">JABBER</span>CAMEL
                </a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse navbar-right navbar-main-collapse">
                <ul class="nav navbar-nav">
                    <!-- Hidden li included to remove active class from about link when scrolled up past about section -->
                    <li class="hidden">
                        <a href="#page-top"></a>
                    </li>
                    <li>
                        <a class="page-scroll" href="#about">Most Active</a>
                    </li>
                    <li>
                        <a class="page-scroll" href="#download">Links</a>
                    </li>
                    <li>
                        <a class="page-scroll" href="#contact">Pics</a>
                    </li>
                </ul>
            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container -->
    </nav>

    <!-- Intro Header -->
    <header class="intro">
        <div class="intro-body">
            <div class="container">
                <div class="row">
                    <div class="col-md-8 col-md-offset-2">
                        <h1 class="brand-heading">JabberCamel</h1>
                        <p class="intro-text">Some shit made by Benco that sucks.</p>
                       	<p class="intro-text">Scroll down to get analyzin'.</p>
                    </div>
                </div>
            </div>
        </div>
    </header>

    <!-- About Section -->
    <section id="about" class="container content-section text-center">
        <div class="row">
            <div class="col-lg-5 col-lg-offset-1">
                <h2>Cunts that need to shut up</h2>
                <table class="table" id="mostactive">
                <tr>
                <th>Person</th>
                <th>Message Count</th>
                </tr>
                </table>
            </div>
            <div class="col-lg-5 col-lg-offset-1">
                    <h2>Graphs</h2>
                    <p>hell yeah, graphs.</p>
                    
                </div>
        </div>
    </section>

    <!-- Download Section -->
    <section id="download" class="content-section text-center">
        <div class="download-section">
            <div class="container">
                <div class="col-lg-5 col-lg-offset-1">
                    <h2>Links</h2>
                    <p>Heres some nifty links.</p>
                    <table class="table" id="popularlinks">
	                <tr>
	                <th>URL</th>
	                <th>OP</th>
	                <th>Latest</th>
	                <th>Count</th>
	                </tr>
	                </table>
                </div>
                <div class="col-lg-5 col-lg-offset-1">
                    <h2>Graphs</h2>
                    <p>hell yeah, graphs.</p>
                    
                </div>
            </div>
        </div>
    </section>

    <!-- Contact Section -->
    <section id="contact" class="container content-section text-center">
        <div class="row">
            <div class="col-lg-8 col-lg-offset-2">
                <h2>Pics</h2>
                <p>For when you really want to see some Hammyhamm pics.</p>
                
            </div>
        </div>
    </section>

    <section id="contact" class="container content-section text-center">
        <div class="row">
            <div id="searchbox" class="col-lg-8 col-lg-offset-2">
                <h2>Search</h2>
                <p>Want to know how much shit you post? Find out here.</p>
    <input type="text" class="form-control" id="searchname" placeholder="Enter your dumb name here">
  	<button id="searchbutton" class="btn btn-default">Search</button>
            </div>
            <div id="searchresults" class="col-lg-0 hidden">
            	<h2>Results</h2>
                <p>Congratulations, you're retarded!</p>
            </div>
        </div>
    </section>

    <!-- Footer -->
    <footer>
        <div class="container text-center">
            <p>made by benco. pretty neat huh</p>
            <br />
            <ul class="list-inline banner-social-buttons">
                    <li>
                        <a href="https://twitter.com/SBootstrap" class="btn btn-default btn-lg"><i class="fa fa-twitter fa-fw"></i> <span class="network-name">Twitter</span></a>
                    </li>
                    <li>
                        <a href="https://github.com/IronSummitMedia/startbootstrap" class="btn btn-default btn-lg"><i class="fa fa-github fa-fw"></i> <span class="network-name">Github</span></a>
                    </li>
                    <li>
                        <a href="https://plus.google.com/+Startbootstrap/posts" class="btn btn-default btn-lg"><i class="fa fa-google-plus fa-fw"></i> <span class="network-name">Google+</span></a>
                    </li>
                </ul>
        </div>
    </footer>

	
	<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/0.3.4/sockjs.min.js"></script>
	<script src="resources/js/jquery.js"></script>
	<script src="resources/js/jquery.easing.min.js"></script>
	<script src="resources/js/grayscale.js"></script>
	<script src="resources/js/stomp.js"></script>
	<script src="resources/js/bootstrap.js"></script>
	<script src="resources/js/jabber-analytics.js"></script>

</body>
</html>
