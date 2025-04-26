<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Chat</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/chat.css">
</head>
<body>
<h2>Chat</h2>
<div>Logged in as: <b>${username}</b></div>

<input type="hidden" id="username" value="${username}" />

<input type="text" id="message" placeholder="Type your message..." />
<button onClick="sendMessage()">Send</button>


<div id="chat" style="margin-top:20px">
    <c:forEach var="msg" items="${messages}">
        <div><b>${msg.user}</b>: ${msg.content}</div>
    </c:forEach>
</div>

<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
<script>
    let stompClient = null;

    function connect() {
        const socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, () => {
            stompClient.subscribe('/topic/public', function (message) {
                const msg = JSON.parse(message.body);
                document.getElementById('chat').innerHTML +=
                    `<div><b>${msg.user}</b>: ${msg.content}</div>`;
            });
        });
    }

    function sendMessage() {
        const user = document.getElementById("username").value;
        const content = document.getElementById("message").value;
        if (!content.trim()) return;
        stompClient.send("/app/chat.send", {}, JSON.stringify({ user, content }));
        document.getElementById("message").value = "";
    }

    window.onload = connect;
</script>
</body>
</html>
