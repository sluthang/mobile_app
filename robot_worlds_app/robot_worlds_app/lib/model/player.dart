class PlayerModel {
  String ipAddress = "";
  String portNumber = "";
  String robotName = "";
  String robotType = "";

  PlayerModel({
    required this.ipAddress,
    required this.portNumber,
    required this.robotName,
    required this.robotType,
  });
  PlayerModel.fromJson(Map<String, dynamic> json)
      : ipAddress = json['ipAddress'] ?? '',
        portNumber = json['portNumber'] ?? '',
        robotName = json['robotName'] ?? '',
        robotType = json['robotType'] ?? '';
}
