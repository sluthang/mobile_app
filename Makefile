.PHONY: release compile_jars run_reference_server run_uss_victory_server\
  		run_1x1_acceptance_tests verify_dependencies stop_reference_server\
  		stop_uss_victory_server run_uss_victory_server_main_2x2_with_obstacle\
  		run_uss_victory_server_main_2x2 run_all_non_server_unittests compile clean\

all: run_reference_server_jar run_1x1_acceptance_tests stop_reference_server

release: run_uss_victory_server_main_default run_server_tests\
		 stop_uss_victory_server test_reference_server_default release_build

#REFERENCE SERVER DEFAULT CONFIGURATION TEST
test_reference_server_default: run_reference_server_jar run_1x1_acceptance_tests stop_reference_server

#USS VICTORY DEFAULT CONFIGURATION TEST
test_uss_server_default: run_uss_victory_server_jar run_1x1_acceptance_tests stop_uss_victory_server

#VICTORY SERVER 2 X 2 ACCEPTANCE TESTS
run_victory_tests_2x2_world: run_uss_victory_server_main_2x2 run_2x2_acceptance_tests stop_uss_victory_server

#VICTORY SERVER 2 X 2 ACCEPTANCE TESTS WITH OBSTACLE
run_victory_tests_2x2_world_with_obstacle: run_uss_victory_server_main_2x2_with_obstacle run_2x2_obstacle_acceptance_tests stop_uss_victory_server

#REFERENCE SERVER 2 X 2 ACCEPTANCE TESTS
run_reference_server_2x2_world_tests: run_reference_server_2x2_world run_2x2_acceptance_tests stop_reference_server

#REFERENCE SERVER 2 X 2 ACCEPTANCE TESTS WITH OBSTACLE
run_reference_server_2x2_world_with_obstacle_tests: run_reference_server_2x2_world_with_obstacle run_2x2_obstacle_acceptance_tests stop_reference_server

# VARIABLES
reference_server:= reference-server-0.2.1.jar
uss_victory_server := robot-worlds-server-0.2.0.jar
2x2_world_port:= 6666
2x2_world_with_obstacle_port:= 7676
1x1_world_port:= 6767

compile:
	@echo "\033[0;32mVerifying Dependencies and compiling..."
	mvn verify -DskipTests; mvn compile

release_build:
	@echo "\033[0;32mBuilding the release jar file..."
	cd Server ; mvn package -Pserver-release-build -DskipTests

development_build:
	@echo "\033[0;32mBuilding the release jar file..."
	cd Server ; mvn package -Pserver-development-build

run_all_non_server_unittests:
	@echo "\033[0;32mRunning unit tests..."
	cd Server; mvn surefire:test -Dtest=ForwardCommandTests
	cd Server; mvn surefire:test -Dtest=PositionTests
	cd Server; mvn surefire:test -Dtest=RobotTests
	cd Server; mvn surefire:test -Dtest=WorldTests

run_server_tests:
	@echo "\033[0;32mRunning all uss server tests..."
	cd Server; mvn test

run_1x1_acceptance_tests:
	@echo "\033[0;32mRunning 1x1 acceptance tests..."
	cd Server; mvn surefire:test -Dtest=LaunchRobotTests
	cd Server; mvn surefire:test -Dtest=RobotCommandTests

run_2x2_acceptance_tests:
	@echo "\033[0;32mRunning 2x2 acceptance tests..."
	cd Server; mvn surefire:test -Dtest=TwoByTwoWorldTests

run_2x2_obstacle_acceptance_tests:
	@echo "\033[0;32mRunning 2x2 acceptance tests with obstacle..."
	cd Server; mvn surefire:test -Dtest=TwoByTwoWorldWithObstacleTests

run_reference_server_jar:
	@echo "\033[0;32mRunning The Reference Server Jar File..."
	cd .libs ; java -jar $(reference_server) &
	@echo "\033[0;32mStarted reference server..."

run_reference_server_2x2_world:
	@echo "\033[0;32mRunning The Reference Server Jar File..."
	cd .libs ; java -jar $(reference_server) -s 2 &
	@echo "\033[0;32mStarted reference server..."

run_reference_server_2x2_world_with_obstacle:
	@echo "\033[0;32mRunning The Reference Server Jar File..."
	cd libs ; java -jar $(reference_server) -s 2 -o 1,1 &
	@echo "\033[0;32mStarted reference server..."

run_uss_victory_server_jar:
	@echo "\033[0;32mRunning The USS Victory Server..."
	java -jar output/$(uss_victory_server) &
	@echo "\033[0;32mStarted uss server..."

run_uss_victory_server_main_default:
	@echo "\033[0;32mRUNNING USS VICTORY SERVER MAIN CLASS..."
	cd Server; mvn exec:java -Dexec.mainClass="za.co.wethinkcode.robot.server.Server.MultiServer" &

run_uss_victory_server_main_2x2_with_obstacle:
	@echo "\033[0;32mRUNNING USS VICTORY SERVER 2x2 World with obstacle"
	cd Server; mvn exec:java -Dexec.mainClass="za.co.wethinkcode.robot.server.Server.MultiServer" -Dexec.args="-s 2 -o 1,1" &

run_uss_victory_server_main_2x2:
	@echo "\033[0;32mRUNNING USS VICTORY SERVER 2x2 World"
	cd Server; mvn exec:java -Dexec.mainClass="za.co.wethinkcode.robot.server.Server.MultiServer" -Dexec.args="-s 2" &

stop_reference_server:
	@echo "\033[0;32mStopping reference server..."
	fuser -k 5000/tcp

stop_uss_victory_server:
	@echo "\033[0;32mStopping uss victory..."
	fuser -k 5000/tcp

clean:
	@echo "\033[0;32mCleaning up..."
	rm -rf output Server/outptut Client/output && mvn clean

