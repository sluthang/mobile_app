import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:robot_worlds_app/model/player.dart';
import 'admin.dart';
import 'player.dart';
//import 'package:flutter_pin_code_fields/flutter_pin_code_fields.dart';

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

Future<void> showLoginDialog(BuildContext context) async {
  return await showDialog(
      context: context,
      builder: (context) {
        final TextEditingController _textController = TextEditingController();
        return AlertDialog(
          content: Form(
            autovalidateMode: AutovalidateMode.disabled,
            key: _formkey,
            child: SingleChildScrollView(
                child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                TextFormField(
                  validator: (value) {
                    if (value!.isEmpty || !ipExp.hasMatch(value)) {
                      return "Please enter IP address";
                    }
                    return null;
                  },
                  decoration: const InputDecoration(
                    hintText: 'Enter IP Address',
                    border: OutlineInputBorder(),
                  ),
                ),
                TextFormField(
                  validator: (value) {
                    if (value!.isEmpty || !_portNumberRegex.hasMatch(value)) {
                      return "Please enter Port Number";
                    }
                    return null;
                  },
                  decoration: const InputDecoration(
                    hintText: 'Enter Port Number',
                    border: OutlineInputBorder(),
                  ),
                ),
                TextFormField(
                  controller: _textController,
                  validator: (value) {
                    if (value!.isEmpty || !_pinNumberRegex.hasMatch(value)) {
                      return 'Please enter a correct pin';
                    }
                    return null;
                  },
                  decoration: const InputDecoration(
                    hintText: 'Enter Admin Pin',
                    border: OutlineInputBorder(),
                  ),
                ),
              ],
            )),
          ),
          actions: <Widget>[
            TextButton(
                child: const Text('Login'),
                onPressed: () {
                  if (_formkey.currentState!.validate()) {
                    Navigator.push(
                        context,
                        MaterialPageRoute(
                            builder: (ctx) => const AdminScreen()));
                  } else {
                    return;
                  }
                })
          ],
        );
      });
}

Widget buttonLayout(BuildContext context) {
  return Center(
    child: Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: <Widget>[
        ElevatedButton(
            child: const Text(
              "Player",
              style: TextStyle(fontSize: 20.0, color: Colors.white),
            ),
            onPressed: () async {
              await playerForm(context);
            }),
        ElevatedButton(
          child: const Text(
            "Admin",
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
  PlayerModel player = PlayerModel();

  return showDialog(
      context: context,
      builder: (BuildContext context) {
        return SingleChildScrollView(
          child: AlertDialog(
            content: Stack(
              clipBehavior: Clip.none,
              children: <Widget>[
                Positioned(
                  right: -40.0,
                  top: -40.0,
                  child: InkResponse(
                    onTap: () {
                      Navigator.of(context).pop();
                    },
                    child: const CircleAvatar(
                      child: Icon(Icons.close),
                      backgroundColor: Colors.red,
                    ),
                  ),
                ),
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
                        child: ElevatedButton(
                          child: const Text("Connect"),
                          onPressed: () {
                            if (_formkeyPlayer.currentState!.validate()) {
                              _formkeyPlayer.currentState!.save();
                              Navigator.push(
                                  context,
                                  MaterialPageRoute(
                                  builder: (ctx) => const PlayerScreen()));
                            }
                          },
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
