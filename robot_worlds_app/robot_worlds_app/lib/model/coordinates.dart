class CoordinatesModel{
  String xCoordinate = '';
  String yCoordinate = '';


  CoordinatesModel({
    required this.xCoordinate,
    required this.yCoordinate,
  });
  CoordinatesModel.fromJson(Map<String, dynamic> json)
      : xCoordinate = json['adminIpAddress'] ?? '',
        yCoordinate = json['adminPortNumber'] ?? '';
}