var client; // WebSockets client
var socket; // socket itself

/**
 * Processes HTML for the Active Users list 
 * @param data AJAX response containing JSON list of users
 */
function processActiveUsers(data) {
	$(data).each(function(idx) {
		//console.log(data[idx].userName + ":" + data[idx].messageCount);
		var newString = '<div id="userMsgCount-' + data[idx].userName + '"class="user"><p><span id="username">'+data[idx].userName+'</span>'+
		': <span id="messageCount">'+data[idx].messageCount+
		'</span></p></div>'
		$("#mostactive").append(newString);
	});
}

/**
 * Processes HTML for the Latest Messages list
 * @param data AJAX response containing JSON list of users
 */
function processLatestMessages(data) {
	$(data).each(function(idx) {
		//console.log(data[idx].timestamp + "| " + data[idx].username + data[idx].message);
		var newString = '<div class="message'+data[idx].id+'"><p><span id="username">'+data[idx].username+'</span>'+
		':'+stripHTML(data[idx].message)+
		'<br/><span id="timestamp">'+data[idx].timestamp+'</span></p></div>'
		$("#latestmessages").append(newString);
	});
}

function stripHTML(msg) {
	msg = msg.replace(/</g, "&lt;").replace(/>/g, "&gt;");
	return msg;
}

/**
 * Processes messages from WebSocket Topic
 * @param msg JSONified JabberMessage
 */
function processNewMessage(msg) {
	
	// update message counts
	msgObj = JSON.parse(msg.body);
	var msgCountSelect = "#userMsgCount-"+msgObj.username+" > p > #messageCount"
	var msgCount = $(msgCountSelect).text()
	if (msgCount != "") {
		$(msgCountSelect).text(++msgCount);
	}
	
	// prepend message to latest msgs
	var newMessage = '<div class="message'+msgObj.id+'"><p><span id="username">'+msgObj.username+'</span>'+
	':'+stripHTML(msgObj.message)+
	'<br/><span id="timestamp">'+msgObj.timestamp+'</span></p></div>'
	$("#latestmessages").prepend(newMessage);
}

/**
 * Callback for Socket connection - confirms connectivity
 * @param clientvar Frame from WebSocket
 */
function connectCallback(clientvar) {
	console.log("Connected:");
	console.log(clientvar);
	console.log(client);
	client.subscribe("/topic/jabbermessages", processNewMessage);
  };
  
  /**
   * Callback for Socket connection - handles errors
   * @param error
   */
function errorCallback(error) {
	    console.log("ERROR");
	    console.log(error);
 };  
 
 /**
  * lets get ready to rumbleeeeeeeeeeeee
  */
$(document).ready(function() {
	
	// AJAX Load what's already there
	$.ajax("getUsers").done(processActiveUsers);
	$.ajax("getMessages").done(processLatestMessages);
	
	// Socket load all the new shit!
	socket = new SockJS('http://localhost:8080/jabbercamel/jabbermessages');
	client = Stomp.over(socket);
	client.connect("user", "pass", connectCallback, errorCallback());
	
})