import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'dart:convert';
import 'package:http/http.dart';
import 'package:flutter/cupertino.dart';
import 'package:joystick/joystick.dart';
import 'package:robot_worlds_app/model/player.dart';

PlayerModel playerInfo =
    PlayerModel(ipAddress: '', portNumber: '', robotName: '', robotType: '');

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
            onPressed: () async {}),
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
        onUpPressed: () {
          // if (selectedGallery - 1 > 0)
          //   setState(() {
          //     selectedGallery -= 1;
          //   });
        },
        onLeftPressed: () {
          // if (selectedImage - 1 > 0)
          //   setState(() {
          //     selectedImage -= 1;
          //   });
        },
        onRightPressed: () {
          // if (selectedImage + 1 < galleryList[selectedGallery].length)
          //   setState(() {
          //     selectedImage += 1;
          // });
        },
        onDownPressed: () {
          // if (selectedGallery + 1 < galleryList.length)
          //   setState(() {
          //     selectedGallery += 1;
          //   });
        },
        onPressed: (_direction) {
          // print("pressed $_direction");
          // print(playerInfo.robotName);
        }),
  );
}
