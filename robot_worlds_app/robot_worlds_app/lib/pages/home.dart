import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'admin.dart';
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
final GlobalKey<FormState> _formkey = GlobalKey<FormState>();

Future<void> showLoginDialog(BuildContext context) async {
  return await showDialog(context: context,
      builder: (context){
      final TextEditingController _textController = TextEditingController();
        return AlertDialog(
          content: Form(
              key: _formkey,
              child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              TextFormField(
                controller: _textController,
                validator: (value){
                  if(value == null || value.isEmpty){
                    return 'Please enter a correct pin';
                  }
                  return null;
                },
                decoration: const InputDecoration(hintText: 'Enter Admin Pin'),
              )
            ],
          )),
          actions:<Widget> [
            TextButton(
              child: const Text('Login'),
              onPressed: (){
                if(_formkey.currentState!.validate()){
                  ScaffoldMessenger.of(context).showSnackBar(
                    const SnackBar(content: Text('Processing Pin')),
                  );
                  Navigator.of(context).pop();
                }
              },
            )
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
          onPressed: () {},
        ),
        ElevatedButton(
          child: const Text(
            "Admin",
            style: TextStyle(fontSize: 20.0, color: Colors.white),
          ),
          onPressed: () async {
            await showLoginDialog(context);
            Navigator.push(
                context,
                MaterialPageRoute(builder: (ctx) =>
                const AdminScreen())
            );
          },
        ),
      ],
    ),
  );
}
