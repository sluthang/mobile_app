import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'dart:convert';
import 'package:http/http.dart';
import 'package:flutter/cupertino.dart';
import 'package:joystick/joystick.dart';
import 'package:robot_worlds_app/controllers/commands.dart';
import 'package:robot_worlds_app/model/command.dart';
import 'package:robot_worlds_app/model/player.dart';

PlayerModel playerInfo =
    PlayerModel(ipAddress: '', portNumber: '', robotName: '', robotType: '');

Commands commands = Commands();

class PlayerScreen extends StatefulWidget {
  PlayerScreen({Key? key, required PlayerModel player}) : super(key: key) {
    playerInfo = player;
  }

  @override
  _PlayerScreenState createState() => _PlayerScreenState();
}

class _PlayerScreenState extends State<PlayerScreen> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Player Page'),
        backgroundColor: Colors.blue,
      ),
      body: buttonLayout(context),
    );
  }
}

Widget buttonLayout(BuildContext context) {
  return Stack(children: [commandButtons(context), joystick(context)]);
}

Widget commandButtons(BuildContext context) {
  return Container(
      height: 420,
      padding: const EdgeInsets.all(10.0),
      alignment: Alignment.bottomLeft,
      child: Row(children: [
        ElevatedButton(
            child: const Text(
              "Look",
              style: TextStyle(fontSize: 20.0, color: Colors.white),
            ),
            onPressed: () async {
              Command response = await commands.issueCommand(
                  playerInfo.robotName,
                  "look",
                  [],
                  playerInfo.ipAddress,
                  playerInfo.portNumber);
            }),
      ]));
}

Widget joystick(BuildContext context) {
  return Container(
    padding: const EdgeInsets.all(10.0),
    child: Joystick(
        size: 130,
        isDraggable: false,
        iconColor: Colors.blue,
        backgroundColor: Colors.black,
        opacity: 0.1,
        joystickMode: JoystickModes.all,
        onUpPressed: () async {
          Command response = await commands.issueCommand(playerInfo.robotName,
              "forward", ["1"], playerInfo.ipAddress, playerInfo.portNumber);
          print(response.data);
        },
        onLeftPressed: () async {
          Command response = await commands.issueCommand(playerInfo.robotName,
              "turn", ["left"], playerInfo.ipAddress, playerInfo.portNumber);
          print(response.data);
        },
        onRightPressed: () async {
          Command response = await commands.issueCommand(playerInfo.robotName,
              "turn", ["right"], playerInfo.ipAddress, playerInfo.portNumber);
          print(response.data);
        },
        onDownPressed: () async {
          Command response = await commands.issueCommand(playerInfo.robotName,
              "back", ["1"], playerInfo.ipAddress, playerInfo.portNumber);
          print(response.data);
        },
        onPressed: (_direction) {
          // print("pressed $_direction");
          // print(playerInfo.robotName);
        }),
  );
}
