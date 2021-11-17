import 'package:flutter_test/flutter_test.dart';

import 'package:robot_worlds_app/main.dart';

void main() {
  group('Home Page Widget Tests', () {
    testWidgets('HomePage has a title', (WidgetTester tester) async {
      await tester.pumpWidget(const RobotWorldsApp());

      final titleFinder = find.text('Robot Worlds');

      expect(titleFinder, findsOneWidget);
    });
  });
}
