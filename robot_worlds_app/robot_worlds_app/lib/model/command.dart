class Command {

  String? robot;
  String? command;
  List<String>? arguments;
  String? result;
  Map<String, dynamic> data;
  Map<String, dynamic> state;

  Command({this.robot, this.arguments, this.command, this.result, required this.data, required this.state});

  factory Command.fromJson(Map<String, dynamic> json) {
    return Command(
      result: json["result"],
      data: json["data"],
      state: json["state"],
    );
  }

  Map<String, dynamic> commandToJson() =>
      {
        'robot': robot,
        'command': command,
        'arguments': arguments,
      };


  int get shields => state['shields'];
}