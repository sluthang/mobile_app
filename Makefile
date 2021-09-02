.PHONY: release compile_jars run_reference_server run_uss_victory_server run_acceptance_tests 	verify_dependencies stop_reference_server stop_uss_victory_server

all: run_reference_server_jar run_acceptance_tests stop_reference_server

release: verify_dependencies run_uss_victory_server_main run_server_tests stop_uss_victory_server test_reference_server release_build

test_reference_server: run_reference_server_jar run_acceptance_tests stop_reference_server

test_uss_server: run_uss_victory_server_jar run_acceptance_tests stop_uss_victory_server

verify_dependencies:
	@echo "Checking dependencies..."
	mvn verify -DskipTests

release_build:
	@echo "\033[0;32mBuilding the release jar file..."
	cd Server ; mvn package -Prelease-build

development_build:
	@echo "Building the release jar file..."
	cd Server ; mvn package -Pdevelopment-build

run_server_tests:
	@echo "Running all uss server tests..."
	cd Server; mvn test

run_acceptance_tests:
	@echo "Running acceptance tests..."
	cd Server; mvn surefire:test -Dtest=LaunchRobotTests

run_reference_server_jar:
	@echo "Running The Reference Server Jar File..."
	cd .libs ; java -jar reference-server-0.1.0.jar &
	@echo "Started reference server..."

run_uss_victory_server_jar:
	@echo "Running The USS Victory Server..."
	cd Server ; java -jar target/Server-1.0-SNAPSHOT-jar-with-dependencies.jar &
	@echo "Started uss server..."

run_uss_victory_server_main:
	@echo "RUNNING USS VICTORY SERVER MAIN CLASS..."
	cd Server; mvn exec:java -Dexec.mainClass="za.co.wethinkcode.robot.server.Server.MultiServer" &

stop_reference_server:
	@echo "Stopping reference server..."
	fuser -k 5000/tcp

stop_uss_victory_server:
	@echo "Stopping uss victory..."
	fuser -k 5000/tcp