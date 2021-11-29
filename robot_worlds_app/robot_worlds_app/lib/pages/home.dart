import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:robot_worlds_app/model/player.dart';
import 'admin.dart';
import 'player.dart';
import 'package:robot_worlds_app/model/admin.dart';
import 'package:robot_worlds_app/controller/player_response.dart';
import 'package:robot_worlds_app/controller/admin_response.dart';



class HomePage extends StatefulWidget {
  const HomePage({Key? key, required this.title}) : super(key: key);

  final String title;

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text(widget.title),
        ),
        body: buttonLayout(context));
  }
}

final GlobalKey<FormState> _formkeyPlayer = GlobalKey<FormState>();
final GlobalKey<FormState> _formkey = GlobalKey<FormState>();
RegExp ipExp = RegExp(
    r"^(\d|[1-9]\d|1\d\d|2([0-4]\d|5[0-5]))\.(\d|[1-9]\d|1\d\d|2([0-4]\d|5[0-5]))\.(\d|[1-9]\d|1\d\d|2([0-4]\d|5[0-5]))\.(\d|[1-9]\d|1\d\d|2([0-4]\d|5[0-5]))$",
    caseSensitive: false,
    multiLine: false);
RegExp _portNumberRegex = RegExp("[0-9]+");
RegExp _pinNumberRegex = RegExp("[0-9]+");

final TextEditingController _ipAdminController = TextEditingController();
final TextEditingController _porAdminController = TextEditingController();
final TextEditingController _pinAdminController = TextEditingController();

Future<void> showLoginDialog(BuildContext context) async{
  AdminModel admin =
      AdminModel(adminPin: '', adminPorNumber: '', adminIpAddress: '');
  return showDialog(
      context: context,
      builder: (BuildContext context){
        return SingleChildScrollView(
          child: AlertDialog(
            content: Stack(
              clipBehavior: Clip.none,
              children: <Widget>[
                Form(
                    key: _formkey,
                    child: Column(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        Padding(
                          padding: const EdgeInsets.all(8.0),
                          child: TextFormField(
                            controller: _ipAdminController,
                            validator: (value) {
                              if (value == null || value.isEmpty || !ipExp.hasMatch(value)) {
                                return 'Please enter a valid IP address';
                              }
                              admin.adminIpAddress = value;
                              return null;
                            },
                            maxLines: 1,
                            maxLength: 20,
                            decoration: const InputDecoration(
                              hintText: 'IP ADDRESS',
                              border: OutlineInputBorder(),
                            ),
                          ),
                        ),
                        Padding(
                          padding: const EdgeInsets.all(8.0),
                          child: TextFormField(
                            controller: _porAdminController,
                            validator: (value) {
                              if (value == null || value.isEmpty || !_portNumberRegex.hasMatch(value)) {
                                return 'Please enter a valid Port Number';
                              }
                              admin.adminPorNumber = value;
                              return null;
                            },
                            maxLines: 1,
                            maxLength: 10,
                            decoration: const InputDecoration(
                              hintText: 'PORT NUMBER',
                              border: OutlineInputBorder(),
                            ),
                          ),
                        ),
                        Padding(
                          padding: const EdgeInsets.all(8.0),
                          child: TextFormField(
                            controller: _pinAdminController,
                            validator: (value) {
                              if (value == null || value.isEmpty || !_pinNumberRegex.hasMatch(value)) {
                                return 'Please enter a valid Pin Number';
                              }
                              admin.adminPin = value;
                              return null;
                            },
                            maxLines: 1,
                            maxLength: 5,
                            decoration: const InputDecoration(
                              hintText: 'PIN NUMBER',
                              border: OutlineInputBorder(),
                            ),
                          ),
                        ),
                        Padding(
                          padding: const EdgeInsets.all(8.0),
                          child: Row(
                            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                            children: [
                              ElevatedButton(
                                child: const Text('CANCEL'),
                                onPressed: () {
                                  Navigator.pop(context);
                                },
                              ),
                              ElevatedButton(
                                  child: const Text('LOGIN'),
                                  onPressed: () async{
                                    if (_formkey.currentState!.validate()) {
                                      await getAdminJsonData(
                                          admin.adminIpAddress, admin.adminPorNumber);
                                      Navigator.push(
                                          context,
                                          MaterialPageRoute(
                                              builder: (ctx) => const AdminScreen()));
                                    } else {
                                      Navigator.of(context).pop();
                                    }
                                  })
                            ],
                          ),
                        ),
                      ],
                    )),
              ],
            ),
          ),
        );
      },
  );
}



Widget buttonLayout(BuildContext context) {
  return Center(
    child: Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: <Widget>[
        ElevatedButton(
            child: const Text(
              "PLAYER",
              style: TextStyle(fontSize: 20.0, color: Colors.white),
            ),
            onPressed: () async {
              await playerForm(context);
            }),
        ElevatedButton(
          child: const Text(
            "ADMIN",
            style: TextStyle(fontSize: 20.0, color: Colors.white),
          ),
          onPressed: () {
            showLoginDialog(context);
          },
        ),
      ],
    ),
  );
}

final TextEditingController _ipController = TextEditingController();
final TextEditingController _portController = TextEditingController();
final TextEditingController _robotNameController = TextEditingController();
final TextEditingController _robotTypeController = TextEditingController();

Future<void> playerForm(BuildContext context) async {
  PlayerModel player =
      PlayerModel(robotName: '', ipAddress: '', robotType: '', portNumber: '');

  return showDialog(
      context: context,
      builder: (BuildContext context) {
        return SingleChildScrollView(
          child: AlertDialog(
            content: Stack(
              clipBehavior: Clip.none,
              children: <Widget>[
                Form(
                  key: _formkeyPlayer,
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: <Widget>[
                      Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: TextFormField(
                          controller: _ipController,
                          validator: (value) {
                            if (value == null || value.isEmpty) {
                              return 'Please enter a valid IP address';
                            }
                            player.ipAddress = value;
                            return null;
                          },
                          maxLines: 1,
                          maxLength: 20,
                          decoration: const InputDecoration(
                            hintText: 'IP ADDRESS',
                            border: OutlineInputBorder(),
                          ),
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: TextFormField(
                          controller: _portController,
                          validator: (value) {
                            if (value == null || value.isEmpty) {
                              return 'Please enter a valid PORT';
                            }
                            player.portNumber = value;
                            return null;
                          },
                          maxLines: 1,
                          maxLength: 20,
                          decoration: const InputDecoration(
                            hintText: 'PORT NUMBER',
                            border: OutlineInputBorder(),
                          ),
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: TextFormField(
                          controller: _robotNameController,
                          validator: (value) {
                            if (value == null || value.isEmpty) {
                              return 'Please enter a name for your robot';
                            }
                            player.robotName = value;
                            return null;
                          },
                          maxLines: 1,
                          maxLength: 20,
                          decoration: const InputDecoration(
                            hintText: 'ROBOT NAME',
                            border: OutlineInputBorder(),
                          ),
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: TextFormField(
                          controller: _robotTypeController,
                          validator: (value) {
                            if (value == null || value.isEmpty) {
                              return 'Please enter a valid robot type';
                            }
                            player.robotType = value;
                            return null;
                          },
                          maxLines: 1,
                          maxLength: 20,
                          decoration: const InputDecoration(
                            hintText: 'ROBOT TYPE',
                            border: OutlineInputBorder(),
                          ),
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                          children: [
                            ElevatedButton(
                              child: const Text('CANCEL'),
                              onPressed: (){
                                Navigator.pop(context);
                              },
                            ),
                            ElevatedButton(
                              child: const Text("LAUNCH"),
                              onPressed:  () async {
                                if (_formkeyPlayer.currentState!.validate()) {
                                  await getPlayerJsonData(
                                      player.ipAddress,
                                      player.portNumber,
                                      player.robotName,
                                      player.robotType);
                                  Navigator.push(
                                      context,
                                      MaterialPageRoute(
                                          builder: (ctx) => PlayerScreen(
                                            player: player,
                                          )));
                                }
                              },
                            ),
                          ],
                        ),
                      )
                    ],
                  ),
                ),
              ],
            ),
          ),
        );
      });
}
