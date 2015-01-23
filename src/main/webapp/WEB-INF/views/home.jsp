<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html class="no-js" lang="en">
<head>
<meta charset="utf-8">
<!-- If you delete this meta tag World War Z will become a reality -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Jabber Analytics</title>
<script src="https://code.jquery.com/jquery-2.1.3.min.js"></script>
<link rel="stylesheet" href="resources/css/jabber-analytics.css">
<link rel="stylesheet" href="resources/css/foundation.css">
<link rel="stylesheet" href="resources/css/normalize.css">
</head>

<body class="index">
	<div class="row">
	<h1>Jabber Analytics</h1>
	</div>
	<div class="row">
		<div id="currentTime" class="small-3 large-4 column">
		<h4>Current Time</h4>
			<P>The time on the server is ${serverTime}.</P>
		</div>
		<div id="latestJabberLog" class="small-6 large-4 column">
		<h4>Latest Messages</h4>
			<div id="latestmessages">
			</div>
		</div>
		<div id="mosttalked" class="small-3 large-4 column">
			<h4>Most Active</h4>
			<div id="mostactive">
			</div>
		</div>
	</div>
	
	<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/0.3.4/sockjs.min.js"></script>
	<script src="resources/js/vendor/modernizr.js"></script>
	<script src="resources/js/foundation.min.js"></script>
	<script src="resources/js/stomp.js"></script>
	<script src="resources/js/jabber-analytics.js"></script>
	<script>
		Foundation.global.namespace = '';
		$(document).foundation();
	</script>
</body>
</html>
