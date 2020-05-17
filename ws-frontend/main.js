$(document).ready(function() {
  
  var client, destinationAll, username, users;
  var protHost = 'http://127.0.0.1'
  var port = ':8080'
  var socketUrl = protHost + port + '/socket';
  //var login = $("#connect_login").val();
  //var passcode = $("#connect_passcode").val();
  destinationAll = '/app/send/all';
  users = new Array();
  
  $('#welcome').fadeIn();

  $('#send_name_form').submit(function(e) {
    e.preventDefault();
    console.log('send_name_form');
    username = $('#send_name_form_input').val();
      if (username) {
        welcomeApi(username);
      }
  });

  $('#send_form').submit(function(e) {
    e.preventDefault();
      var text = $('#send_form_input').val();
      if (text) {
        var msg = JSON.stringify({
          'type': 'CHAT',
          'sender': username,
          'recipient': 'All',
          'content': text
        });
        client.send(destinationAll, {name: username}, msg);
        $('#send_form_input').val("");
      }
    return false;
  });

  $('#disconnect').click(function(){
    client.disconnect();
    $('#connected').fadeOut();
    $('#welcome').fadeIn();
    users.length = 0;
    updateUsersList(users);

  });

  function welcomeApi(username){
    var ajaxUrl = protHost + port + '/welcome'
    console.log('ajax to ' + ajaxUrl);
    var data = {};
    data['name'] = username;
    $.ajax({
      type: "POST",
      url: ajaxUrl,
      data: JSON.stringify(data),
      contentType: "application/json; charset=utf-8",
      crossDomain: true,
      async: true,
      dataType: "json",
      success: function (data, status, jqXHR) {
        //alert("success" + data);
        console.log("response data:" + data);
        $('#messageboard').html(data.message);
        console.log("response extra:" + data.extra);
        users = JSON.parse(data.extra);
        updateUsersList(users);
        $('#welcome').fadeOut();
        
        setClient(username);
      },
      error: function (jqXHR, status) {
          // error handler
          console.log(jqXHR);
          //alert('fail' + status.code);
      }
    });
  }

  function setClient(username){
    console.log('setClient')
    ws = new SockJS(socketUrl);
    client = Stomp.over(ws);
    client.debug = function(str) {
        //$("#debug").append(str + "\n");
        //console.log('[debug]\t'+str);
    };
    client.msgs = function(str) {
      $("#messages").append("<p>" + str + "</p>\n");
    };
    
    client.connect({name:username}, function(frame) {
      client.debug("connected to Stomp");
      var url = client.ws._transport.url;
      console.log("Your current raw session is: " + url);
      url = url.replace(
      "ws://localhost:8080/socket/app/",  "");
      url = url.replace("/websocket", "");
      url = url.replace(/^[0-9]+\//, "");
      console.log("Your current session is: " + url);
      sessionId = url;
      
      $('#connected').fadeIn();
      setClientSubscriptions(client);
    });
  }

  function setClientSubscriptions(client){
    subscribePublic(client);
    //subscribeUser(client);
    
  }

  function subscribePublic(client){
    client.subscribe('/topic/public', function(message) {
      if (message.body) {
        visible = buildVisibleMsg(message.body);
        console.log(message.body);
        if (visible){
          client.msgs(visible);
        }
      }
    });
  }

  function subscribeUser(client){
    client.subscribe('/topic/user/queue' + '-user' + sessionId, function (message) {
      if (message.body) {
        console.log(message.body);
        client.msgs(message.body);
      }
    });
  }
  
  function buildVisibleMsg(body){
    msg = JSON.parse(body);
    console.log('msg.type= ' + msg.type)
    if (msg.type == "JOIN"){
      console.log('msg.sender= ' + msg.sender)
      users.push(msg.sender);
      updateUsersList(users);
      //return;
    }else if (msg.type == "LEAVE"){
      var filtered = users.filter(
        function(value, index, arr){
           return value != msg.sender;
          });
      users = filtered;
      updateUsersList(users);
      //return;
    }
    visible = "from " + msg.sender + " to " + msg.recipient + ": " + msg.content;
    return visible;
  }

  function updateUsersList(users){
    console.log('updateUsersList');
    var arrayLength = users.length;
    console.log('arrayLength= ' + arrayLength);
    console.log('users= ' + users);
    var htmlList = document.createElement('ul');
    for (var i = 0; i < arrayLength; i++) {
      console.log('user= ' + arrayLength[i])
      var item = document.createElement('li');
      item.appendChild(document.createTextNode(users[i]));
      htmlList.appendChild(item);
    };
    console.log('htmlList= ' + htmlList)
    $('#users').html(htmlList);
  }

});