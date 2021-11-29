import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'dart:convert';
import 'package:http/http.dart';
import 'package:flutter/cupertino.dart';
import 'package:robot_worlds_app/controllers/admin_commands.dart';
import 'package:robot_worlds_app/model/coordinates.dart';



class AdminScreen extends StatefulWidget {
  const AdminScreen({Key? key}) : super(key: key);  
  @override
  _AdminScreenState createState() => _AdminScreenState();
}

class _AdminScreenState extends State<AdminScreen> {
  late TextEditingController text;
  late TextEditingController X;
  late TextEditingController Y;
  late final List <String>_commandList = <String>[];
  //final GlobalKey<FormState> _formkey = GlobalKey<FormState>();
 final RegExp _coordinateXRegex = RegExp("[0-9]+");
 final RegExp _coordinateYRegex = RegExp("[0-9]+");

  AdminController adminController = AdminController();

  @override
  void initState(){
    super.initState();
    X = TextEditingController();
    Y = TextEditingController();
    text = TextEditingController();
  }

  @override
  void dispose(){
    X.dispose();
    Y.dispose();
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
      //adminBodyLayout(context);
    }
    //adminBodyLayout(context);
  }


Future<void> coordinates(BuildContext context){
    CoordinatesModel _coordinates = CoordinatesModel(xCoordinate: '', yCoordinate: '');
    return showDialog<void>(
      context: context,
      // false = user must tap button, true = tap outside dialog
      builder: (BuildContext dialogContext) {
        return AlertDialog(
          title: const Text('Coordinates'),
          content: Container(
              padding: const EdgeInsets.all(8.0),
              child: Column(
                  children:[
                    Row(
                      children: [
                        Expanded(
                          child:Card(
                            child: TextField(
                              onChanged: (value) {
                                if (value.isEmpty || _coordinateXRegex.hasMatch(value)) {
                                  print('Please enter a valid Number') ;
                                }
                                _coordinates.xCoordinate = value;
                              },
                                    keyboardType: TextInputType.text,
                                    controller: X,
                                    decoration: const InputDecoration(hintText: "X"),
                            ),
                          ),
                        ),
                        Expanded(
                          child:Card(
                            child: TextField(
                              onChanged: (value) {
                                if (value.isEmpty || _coordinateYRegex.hasMatch(value)) {
                                  print('Please enter a valid Pin Number') ;
                                }
                                _coordinates.yCoordinate = value;
                              },
                                    keyboardType: TextInputType.text,
                                    controller: Y,
                                    decoration: const InputDecoration(hintText: "Y"),
                            ),
                          ),
                        )
                      ],
                    )]
              )
          ),
          actions: <Widget>[
            Padding(
                padding: const EdgeInsets.all(8.0),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  children: [
                    ElevatedButton(
                        onPressed: (){
                          Navigator.pop(context);
                        },
                        child: const Text('CANCEL')
                    ),
                    ElevatedButton(
                      child: const Text('ADD'),
                      onPressed: () {
                        Navigator.of(dialogContext).pop(); // Dismiss alert dialog
                      },
                    ),
                  ],
                ),),
          ],
        );
      },
    );
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
              TextFormField(
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
              Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                    children: [
                      ElevatedButton(onPressed: (){
                        Navigator.pop(ctx);
                      }, child: const Text('CANCEL')
                      ),
                      ElevatedButton(onPressed: (){
                        Navigator.pop(ctx);
                        addCommand();
                        text.clear();
                      }, child: const Text('EXECUTE!'))
                    ],
                  ),),

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
      body: Padding(
          padding: const EdgeInsets.all(10),
          child:adminButtonLayout(context),
      ),
    );
  }

  Widget adminButtonLayout(BuildContext context) {
    return Stack(children: [adminBodyLayout(context),adminButtons(context)]);
  }

  Widget adminButtons(BuildContext context){
    return Container(
      height: 500,
      //padding: const EdgeInsets.all(8.0),
      alignment: Alignment.bottomLeft,
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          ElevatedButton(
              child: const Text(
                "ADD COORDINATES",
                style: TextStyle(fontSize: 7.0, color: Colors.white),
              ),
              onPressed:(){
                coordinates(context);
              }),
          ElevatedButton(
              child: const Text(
                "DELETE COORDINATES",
                style: TextStyle(fontSize: 7.0, color: Colors.white),
              ),
              onPressed:(){
              }),
          ElevatedButton(
            onPressed: () {
              _showTextBox(context);
            },
            child: const Text(
              'LIST ROBOTS',
              style: TextStyle(fontSize: 7.0, color: Colors.white),
            ),
          ),
        ],
      ),
    );
  }

  Widget adminBodyLayout(BuildContext context){
    return Padding(
       padding: const EdgeInsets.only(bottom: 200.0),
       child: ListView.builder(
                  itemCount: _commandList.length,
                  itemBuilder: (BuildContext context, int index) => ListTile(
                    title: Text(_commandList[index],
                      style: const TextStyle(fontSize: 20, color: Colors.black),
                      textAlign: TextAlign.start,
                      softWrap: false,
                      overflow: TextOverflow.ellipsis,
                    ),
                  )
              ),
          );
  }
}


