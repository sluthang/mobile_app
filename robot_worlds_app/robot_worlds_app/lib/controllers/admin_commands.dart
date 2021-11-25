import 'dart:convert';
import 'package:http/http.dart' show Client;


class AdminController {

  Client client = Client();

  Future<String> addObstacles(List<String> obstacleList) async {
    final response = await client.post(
      Uri.parse("http://127.0.0.1:5000/admin/obstacles"),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(<String, dynamic>{
        'objects': obstacleList,
      }),
    );

    if(response.statusCode == 201){
      return "Success";
    }
    throw Exception('Failed to add obstacles.');
  }

  Future<String> deleteObstacles(List<String> arguments) async {
    final response = await client.delete(
      Uri.parse("http://127.0.0.1:5000/admin/obstacles"),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(<String, dynamic>{
        'objects': arguments,
      }),
    );

    if(response.statusCode == 200){
      return "Success";
    }
    throw Exception('Failed to delete obstacles.');
  }


  Future<String> killRobot(String robotName) async {
    final response = await client.delete(
      Uri.parse("http://127.0.0.1:5000/admin/robot/$robotName"),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
    );

    if(response.statusCode == 200){
      return "Success";
    }
    throw Exception('Failed to delete robot.');
  }

  Future<String> saveWorldMap(String worldName) async {
    final response = await client.post(
      Uri.parse("http://127.0.0.1:5000/admin/save/$worldName"),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
    );

    if(response.statusCode == 201){
      return "Success";
    }
    throw Exception('Failed to save world.');
  }

  Future<String> loadWorldMap(String worldName) async {
    final response = await client.get(
      Uri.parse("http://127.0.0.1:5000/admin/load/$worldName"),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
    );

    if(response.statusCode == 200){
      return "Success";
    }
    throw Exception('Failed to load world.');
  }
}