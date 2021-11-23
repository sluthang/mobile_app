import 'dart:convert';
import 'package:robot_worlds_app/model/player.dart';
import 'package:http/http.dart' as http;

Future<PlayerModel>getPlayerJsonData(String ipAddress, String portNumber, robotName,robotType) async{
  var response = await http
      .post(Uri.parse('http://'+ ipAddress + ':'+ portNumber + '/robot/{$robotName}'),
    headers: <String, String>{
      'Content-type' : 'application/json; charset=UTF-8',
    },
    body: jsonEncode(<String, dynamic>{
      'robot': robotName,
      'command': 'launch',
      'arguments': [robotType,5,5],
    }),
  );
  //var responseData = jsonDecode(response.body);
  return PlayerModel.fromJson(jsonDecode(response.body));
}