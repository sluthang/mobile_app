

class AdminCommands {

  List<String>? robots;
  String? robotName;
  Map<String, dynamic>? objects;
  String? worldName;

  AdminCommands({objects});

factory AdminCommands.fromJson(Map<String, dynamic> json){
  return AdminCommands(
    objects: json['objects'],
  );
}

Map<String, dynamic> commandToJson() =>
    {
      'objects': objects,
      // 'objects': [{"position": [1,1], "type": "OBSTACLE"}], how it's going to look like
    };
}