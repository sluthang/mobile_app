import 'package:flutter/material.dart';
import 'package:robot_worlds_app/pages/home.dart';

void main() {
  runApp(const RobotWorldsApp());
}

class RobotWorldsApp extends StatelessWidget {
  const RobotWorldsApp({Key? key}) : super(key: key);

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const HomePage(title: 'Robot Worlds'),
    );
  }
}
