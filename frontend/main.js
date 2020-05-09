$(document).ready(function() {
            
    var client, destination;
    
    var url = 'http://localhost:8080/socket';
    //var login = $("#connect_login").val();
    //var passcode = $("#connect_passcode").val();
    destination = '/app/send/message';

    ws = new SockJS(url);
    client = Stomp.over(ws);

    client.debug = function(str) {
        $("#debug").append(str + "\n");
    };
    client.msgs = function(str) {
      $("#messages").append("<p>" + str + "</p>\n");
    };
    
    client.connect({}, function(frame) {
      client.debug("connected to Stomp");
      $('#connected').fadeIn();
      client.subscribe('/message', function(message) {
          if (message.body) {
            console.log(message.body);
            client.msgs(message.body);
          }
      });
    });

    $('#send_form').submit(function() {
    var text = $('#send_form_input').val();
    if (text) {
        client.send(destination, {}, text);
        $('#send_form_input').val("");
    }
    return false;
    });

  });