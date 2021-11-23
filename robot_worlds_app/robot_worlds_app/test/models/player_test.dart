import 'package:test/test.dart';
import 'package:robot_worlds_app/model/player.dart';

void main() {
  group('Testing Player', () {
    var player = PlayerModel(robotName: '', ipAddress: '', robotType: '', portNumber: '');

    test('IP Address getter and setter', () {
      var ipAddress = "20.10.11.47";

      expect(player.ipAddress, "");

      player.ipAddress = ipAddress;
      expect(player.ipAddress, ipAddress);
    });

    test('Port number getter and setter', () {
      var port = "5000";

      expect(player.portNumber, "");

      player.portNumber = port;
      expect(player.portNumber, port);
    });

    test('Robot name getter and setter', () {
      var name = "HAL";

      expect(player.robotName, "");

      player.robotName = name;
      expect(player.robotName, name);
    });

    test('Robot type getter and setter', () {
      var type = "Sniper";

      expect(player.robotType, "");

      player.robotType = type;
      expect(player.robotType, type);
    });
  });
}
