$(document).ready(function() {
  
  var client, destination;
  var protHost = 'http://127.0.0.1'
  var port = ':8080'
  var socketUrl = protHost + port + '/socket';
  //var login = $("#connect_login").val();
  //var passcode = $("#connect_passcode").val();
  destination = '/app/send/message';
  $('#welcome').fadeIn();

  function welcomeApi(data){// pass your data in method
    var ajaxUrl = protHost + port + '/welcome'
    console.log('ajax to ' + ajaxUrl);
    $.ajax({
      
      type: "POST",
      url: ajaxUrl,
      data: JSON.stringify(data),// now data come in this function
      contentType: "application/json; charset=utf-8",
      crossDomain: true,
      async: true,
      dataType: "json",
      success: function (data, status, jqXHR) {
        //alert("success" + data);
        console.log(data);
        $('#welcome').fadeOut();
        ws = new SockJS(socketUrl);
        client = Stomp.over(ws);
        setClient();
      },
      error: function (jqXHR, status) {
          // error handler
          console.log(jqXHR);
          //alert('fail' + status.code);
      }
    });
  }

  $('#send_name_form').submit(function(e) {
    e.preventDefault();
    console.log('send_name_form');
    var name = $('#send_name_form_input').val();
      if (name) {
        var data = {};
        data['name'] = name;
        welcomeApi(data);
      }
  });

  //if(client){
  function setClient(){
    console.log('setClient')
    client.debug = function(str) {
        $("#debug").append(str + "\n");
    };
    client.msgs = function(str) {
      $("#messages").append("<p>" + str + "</p>\n");
    };
    
    client.connect({}, function(frame) {
      client.debug("connected to Stomp");
      $('#connected').fadeIn();
      client.subscribe('/topic/public', function(message) {
          if (message.body) {
            console.log(message.body);
            client.msgs(message.body);
          }
      });
    });
  }

    $('#send_form').submit(function(e) {
      e.preventDefault();
        var text = $('#send_form_input').val();
        if (text) {
          client.send(destination, {}, text);
          $('#send_form_input').val("");
        }
      
      return false;
    });

  

});