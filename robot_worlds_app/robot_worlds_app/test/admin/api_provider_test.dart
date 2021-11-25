import 'package:test/test.dart';
import 'package:http/http.dart';
import 'package:http/testing.dart';
import 'dart:convert';
import 'package:robot_worlds_app/controllers/commands.dart';

void main(){
  test("Launch command test", () async {
    //setup the test
    final apiProvider = Commands();
    apiProvider.client = MockClient((request) async {
      final mapJson = {'result' : "OK",
                       'state': {"shields":5,"position":[0,0],"shots":1,"direction":"NORTH","status":"NORMAL"},
                       'data' :{"mine":10,"repair":10,"shields":5,"reload":10,"visibility":10,"position":[0,0]}
      };
      return Response(json.encode(mapJson), 200);
    });

    final item = await apiProvider.issueCommand("hal", "launch", ["sniper","999","1"], "", "");
    expect(item.result, "OK");
    expect(item.shields, 5);
  });
}