import 'dart:convert';
import 'package:robot_worlds_app/model/admin.dart';
import 'package:http/http.dart' as http;

Future<AdminModel>getAdminJsonData(String adminIpAddress, String adminPortNumber) async{
  var _auth = '';
  var response = await http
      .post(Uri.parse('http://'+ adminIpAddress + ':'+ adminPortNumber + '/robot/{Hal}'),
    headers: <String, String>{
      'Content-type' : 'application/json; charset=UTF-8',
      //'Authorization' : 'Bear $_auth',
    },
    body: jsonEncode(<String, dynamic>{
      'robot': 'Hal',
      'command': 'launch',
      'arguments': ['sniper',5,5],
    }),
  );
  if(response.statusCode == 201){
    //print('The connection status code for Admin is : {$response.statusCode}');
    return AdminModel.fromJson(jsonDecode(response.body));
  }else{
    throw Exception('Failed to create connection');
  }

}