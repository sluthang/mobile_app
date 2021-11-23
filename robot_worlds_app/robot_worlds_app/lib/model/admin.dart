class AdminModel{
  late  String adminIpAddress = '';
  late  String adminPorNumber = '';
  late  String adminPin = '';

  AdminModel({
    required this.adminIpAddress,
    required this.adminPorNumber,
    required this.adminPin,
});
  AdminModel.fromJson(Map<String, dynamic> json)
         : adminIpAddress = json['adminIpAddress'] ?? '',
           adminPorNumber = json['adminPortNumber'] ?? '',
           adminPin = json['adminPin'] ?? '';
}