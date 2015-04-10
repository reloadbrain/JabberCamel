var client; // WebSockets client
var socket; // socket itself

/**
 * Processes HTML for the Active Users list
 * 
 * @param data
 *            AJAX response containing JSON list of users
 */
function processActiveUsers(data) {
	$(data).each(
			function(idx) {
				// console.log(data[idx].userName + ":" +
				// data[idx].messageCount);
				var newString = '<tr id="msgCount'+data[idx].userName+'">'
						+ '<td id="username">'+ data[idx].userName + '</td>'
						+ '<td id="messageCount">' + data[idx].messageCount + '</td>'
						+ '</tr>'
				$("#mostactive").append(newString);
			});
}

function incrementUserMessageCount(message) {
	$("tr#msgCount" + message.username).effect("highlight", {color: 'white'})
	var row = $("tr#msgCount" + message.username);
	
	// only bother doing the rest if it's a valid user with a count
	if (row.length != 0) {
	var rowMessageCount = $(row).children("#messageCount")
	var value = parseInt(rowMessageCount.text())
	$(rowMessageCount).text(value + 1);
	} else {
		var newString = '<tr id="msgCount'+message.username+'">'
		+ '<td id="username">'+ message.username + '</td>'
		+ '<td id="messageCount">1</td>'
		+ '</tr>'
		$("#mostactive").append(newString);
	}
}
/**
 * Processes HTML for the Latest Messages list
 * 
 * @param data
 *            AJAX response containing JSON list of users
 */
function processLatestMessages(data) {
	$(data)
			.each(
					function(idx) {
						var newString = '<div class="message" id="'	+ data[idx].id + '">'
							+ '<span data-tooltip aria-haspopup="true" class="has-tip tip-left" title="' + data[idx].timestamp + '"id="username">'
									+ data[idx].username 
							+ ': </span>'
							+ stripHTML(data[idx].message) 
							+ '</div>'
						$("#livechat").append(newString);
					});
}

function processPopularLinks(data) {
	$(data).each(
			function(idx) {
				var html = '<tr id="link">' 
						+ '<td id="url">'+ stripHTML(data[idx].url) + '</td>'
						+ '<td id="op">' + data[idx].op + '</td>'
						+ '<td id="latest">' + data[idx].lastPostedBy + '</td>' 
						+ '<td id="count">'	+ data[idx].count + '</td>'+
						'</tr>';
				$("#popularlinks").append(html);
			});
}

function stripHTML(msg) {
	msg = msg.replace(/</g, "&lt;").replace(/>/g, "&gt;");
	return msg;
}

/**
 * Processes messages from WebSocket Topic
 * 
 * @param msg
 *            JSONified JabberMessage
 */
function processNewMessage(msg) {

	// update message counts
	var msgObj = JSON.parse(msg.body);
	
	incrementUserMessageCount(msgObj); 

	// prepend message to latest msgs
	var newMessage = '<div class="message" id="'	+ msgObj.id + '">'
	+ '<span data-tooltip aria-haspopup="true" class="has-tip tip-left" title="' + msgObj.timestamp + '"id="username">'
			+ msgObj.username 
	+ ': </span>'
	+ stripHTML(msgObj.message) 
	+ '</div>'
	$("#livechat").prepend(newMessage);
}

function toggleConnectionStatus(toggle) {
	if (toggle) {
		$("#connectionStatus").removeClass("label-default");
		$("#connectionStatus").addClass("label-success");
		$("#connectionStatus").text("Connected");
	} else if (!toggle) {
		$("#connectionStatus").removeClass("label-success");
		$("#connectionStatus").addClass("label-default");
		$("#connectionStatus").text("Disconnected");
	}
}
/**
 * Callback for Socket connection - confirms connectivity
 * 
 * @param clientvar
 *            Frame from WebSocket
 */
function connectCallback(clientvar) {
	console.log("Connected:");
	console.log(clientvar);
	console.log(client);
	toggleConnectionStatus(true);
	client.subscribe("/topic/jabbermessages", processNewMessage);
};

/**
 * Callback for Socket connection - handles errors
 * 
 * @param error
 */
function errorCallback(error) {
	console.log("ERROR");
	console.log(error);
};

/*
 * Handler for search button click event
 */
function handleSearchButton(eventData) {
	$("#searchbox").toggleClass("col-lg-8 col-lg-4");
	$("#searchbox").toggleClass("col-lg-offset-2");
	$("#searchresults").toggleClass("col-lg-0 col-lg-8");

	if (($("#searchbutton").text()).indexOf("hide") != -1) {
		$("#searchbutton").text("search");
	} else {
		$("#searchbutton").text("hide ya dumb results");
	}

	// if searchresults showing, hide, otherwise wait for transitions before
	// showing
	if (!$("#searchresults").hasClass("hidden")) {
		$("#searchresults").addClass("hidden")
	} else {
		setTimeout(toggleSearchResults, 400);
	}

}
function toggleSearchResults() {
	$("#searchresults").toggleClass("hidden")
}
/**
 * lets get ready to rumbleeeeeeeeeeeee
 */
$(document).ready(function() {

	$("#searchbutton").on("click", handleSearchButton);

	// AJAX Load what's already there
	$.ajax("getUsers").done(processActiveUsers);
	$.ajax("getLinks").done(processPopularLinks);
	$.ajax("getMessages").done(processLatestMessages);

	// Socket load all the new shit!
	// http://localhost:8080/jabbercamel/jabbermessages
	socket = new SockJS('http://bencompany.net:8080/jabbercamel/jabbermessages');
	client = Stomp.over(socket);
	client.connect("user", "pass", connectCallback, errorCallback);

})