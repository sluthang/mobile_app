import 'dart:convert';
import 'package:http/http.dart' show Client;
import 'package:robot_worlds_app/model/command.dart';
class Commands {

  Client client = Client();

  Future<Command> issueCommand(String robotName, String command, List<String> arguments) async {
    final response = await client.post(
      Uri.parse("http://127.0.0.1:5000/robot/$robotName"),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
    },
      body: jsonEncode(<String, dynamic>{
        'robot': robotName,
        'command': command,
        'arguments': arguments,
      }),
    );

    if(response.statusCode == 200){
      return Command.fromJson(jsonDecode(response.body));
    }
    throw Exception('Failed to run command.');
  }
}