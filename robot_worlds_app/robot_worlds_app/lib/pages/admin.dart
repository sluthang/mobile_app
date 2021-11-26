import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'dart:convert';
import 'package:http/http.dart';
import 'package:flutter/cupertino.dart';
import 'package:robot_worlds_app/controllers/admin_commands.dart';



class AdminScreen extends StatefulWidget {
  const AdminScreen({Key? key}) : super(key: key);  
  @override
  _AdminScreenState createState() => _AdminScreenState();
}

class _AdminScreenState extends State<AdminScreen> {
  late TextEditingController text;
  late final List <String>_commandList = <String>[];

  AdminController adminController = AdminController();

  @override
  void initState(){
    super.initState();
    text = TextEditingController();
  }

  @override
  void dispose(){
    text.dispose();
    super.dispose();
  }

  void addCommand() async {
    String commandOut = '';

    List<String> arguments = text.text.split(" ");
    switch(arguments[0]){
      case 'robots': {
          commandOut = await adminController.getRobots();
      } break;
      case 'save': {
        commandOut = await adminController.saveWorldMap(arguments[1]);
      } break;
      case 'load': {
        commandOut = await adminController.loadWorldMap(arguments[1]);
      } break;
      case 'delete': {
        commandOut = await adminController.killRobot(arguments[1]);
      } break;
    }
    if(text.text == ' '){
      _showTextBox(context);
    }else{
      setState(() {
        _commandList.add(commandOut);
      });
      print(_commandList);
    }
  }

 void _showTextBox(BuildContext ctx){
    //var text;
    showModalBottomSheet(
        isScrollControlled: true,
        elevation: 5,
        context: ctx,
        builder: (ctx) => Padding(
          padding: MediaQuery.of(ctx).viewInsets,
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              TextField(
                keyboardType: TextInputType.text,
                controller: text,
                decoration: const InputDecoration(
                  hintText: 'Type Command here!',
                  border: OutlineInputBorder(),
                ),
              ),
              const SizedBox(
                height: 15,
              ),
              ElevatedButton(onPressed: (){
                Navigator.pop(ctx);
                addCommand();
                text.clear();
              }, child: const Text('Execute!'))
            ],
          ),
        ));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Admin Page'),
        backgroundColor: Colors.blue,
      ),
      body: ListView.builder(
          itemCount: _commandList.length,
          itemBuilder: (BuildContext context, int index) => ListTile(
            title: Text(_commandList[index],
              style: const TextStyle(fontSize: 20, color: Colors.black),
              textAlign: TextAlign.start,
              softWrap: false,
              overflow: TextOverflow.ellipsis,
            ),
          )),

      floatingActionButton: FloatingActionButton(
          child: const Icon(Icons.edit),
          onPressed: () => _showTextBox(context)),
    );
  }
 
}




