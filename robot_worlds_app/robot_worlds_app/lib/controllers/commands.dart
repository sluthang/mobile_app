import 'dart:convert';
import 'package:http/http.dart' show Client;
import 'package:robot_worlds_app/model/command.dart';

class Commands {
  Client client = Client();

  Future<Command> issueCommand(String robotName, String command,
      List<String> arguments, String ipAddress, String portNumber) async {
    var body = jsonEncode(<String, dynamic>{
      'robot': robotName,
      'command': command,
      'arguments': arguments,
    });
    final response = await client.post(
      Uri.parse(
          'http://' + ipAddress + ':' + portNumber + '/robot/$robotName'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: body,
    );

    if (response.statusCode == 201) {
      return Command.fromJson(jsonDecode(response.body));
    }
    throw Exception('Failed to run command.');
  }
}
