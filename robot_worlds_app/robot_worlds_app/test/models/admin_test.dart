import 'package:test/test.dart';
import'package:robot_worlds_app/model/admin.dart';

void main(){
  group('Testing Admin model', (){
    var admin = AdminModel(adminIpAddress: '', adminPorNumber: '', adminPin: '');

    test('admin IP Address', (){
      var ipAddress = '172.17.0.1';

      expect(admin.adminIpAddress, '');

      admin.adminIpAddress = ipAddress;
      expect(ipAddress,admin.adminIpAddress);
    });

    test('admin PORT number', (){
      var portNumber = '5000';

      expect(admin.adminPorNumber,'');

      admin.adminPorNumber = portNumber;
      expect(portNumber,admin.adminPorNumber);
    });

    test('admin Pin Number', (){
      var pinNumber = '1234';

      expect(admin.adminPin,'');

      admin.adminPin = pinNumber;
      expect(pinNumber,admin.adminPin);
    });
  });

}