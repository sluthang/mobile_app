.PHONY: release compile_jars run_reference_server run_uss_victory_server run_1x1_acceptance_tests verify_dependencies stop_reference_server stop_uss_victory_server run_uss_victory_server_main_2x2_with_obstacle run_uss_victory_server_main_2x2

all: run_reference_server_jar run_1x1_acceptance_tests stop_reference_server

release: verify_dependencies run_uss_victory_server_main_default run_server_tests stop_uss_victory_server test_reference_server release_build

test_reference_server: run_reference_server_jar run_1x1_acceptance_tests stop_reference_server

test_uss_server: run_uss_victory_server_jar run_1x1_acceptance_tests stop_uss_victory_server

run_victory_tests_2x2_world: run_uss_victory_server_main_2x2 run_2x2_acceptance_tests stop_uss_victory_server

run_victory_tests_2x2_world_with_obstacle: run_uss_victory_server_main_2x2_with_obstacle run_2x2_obstacle_acceptance_tests stop_uss_victory_server

run_reference_server_2x2_world_tests: run_reference_server_2x2_world run_2x2_acceptance_tests stop_reference_server

run_reference_server_2x2_world_with_obstacle_tests: run_reference_server_2x2_world_with_obstacle run_2x2_obstacle_acceptance_tests stop_reference_server

verify_dependencies:
	@echo "Checking dependencies..."
	mvn verify -DskipTests

release_build:
	@echo "\033[0;32mBuilding the release jar file..."
	cd Server ; mvn package -Pserver-release-build -DskipTests

development_build:
	@echo "Building the release jar file..."
	cd Server ; mvn package -Pserver-development-build

run_all_non_server_unittests:
	@echo "Running unit tests..."
	cd Server; mvn surefire:test -Dtest=ForwardCommandTests
	cd Server; mvn surefire:test -Dtest=PositionTests
	cd Server; mvn surefire:test -Dtest=RobotTests
	cd Server; mvn surefire:test -Dtest=WorldTests

run_server_tests:
	@echo "Running all uss server tests..."
	cd Server; mvn test

run_1x1_acceptance_tests:
	@echo "Running 1x1 acceptance tests..."
	cd Server; mvn surefire:test -Dtest=LaunchRobotTests
	cd Server; mvn surefire:test -Dtest=RobotCommandTests

run_2x2_acceptance_tests:
	@echo "Running 2x2 acceptance tests..."
	cd Server; mvn surefire:test -Dtest=TwoByTwoWorldTests

run_2x2_obstacle_acceptance_tests:
	@echo "Running 2x2 acceptance tests with obstacle..."
	cd Server; mvn surefire:test -Dtest=TwoByTwoWorldWithObstacleTests

run_reference_server_jar:
	@echo "Running The Reference Server Jar File..."
	cd .libs ; java -jar reference-server-0.1.0.jar &
	@echo "Started reference server..."

run_reference_server_2x2_world:
	@echo "Running The Reference Server Jar File..."
	cd libs ; java -jar reference-server-0.2.0.jar -s 2&
	@echo "Started reference server..."

run_reference_server_2x2_world_with_obstacle:
	@echo "Running The Reference Server Jar File..."
	cd libs ; java -jar reference-server-0.2.0.jar -s 2 -o 1,1 &
	@echo "Started reference server..."

run_uss_victory_server_jar:
	@echo "Running The USS Victory Server..."
	java -jar output/robot-worlds-server-0.2.0.jar &
	@echo "Started uss server..."

run_uss_victory_server_main_default:
	@echo "RUNNING USS VICTORY SERVER MAIN CLASS..."
	cd Server; mvn exec:java -Dexec.mainClass="za.co.wethinkcode.robot.server.Server.MultiServer" &

run_uss_victory_server_main_2x2_with_obstacle:
	@echo "RUNNING USS VICTORY SERVER 2x2 World with obstacle"
	cd Server; mvn exec:java -Dexec.mainClass="za.co.wethinkcode.robot.server.Server.MultiServer" -Dexec.args="-s 2 -o 1,1" &

run_uss_victory_server_main_2x2:
	@echo "RUNNING USS VICTORY SERVER 2x2 World"
	cd Server; mvn exec:java -Dexec.mainClass="za.co.wethinkcode.robot.server.Server.MultiServer" -Dexec.args="-s 2" &

stop_reference_server:
	@echo "Stopping reference server..."
	fuser -k 5000/tcp

stop_uss_victory_server:
	@echo "Stopping uss victory..."
	fuser -k 5000/tcp
