.PHONY: compile_jars run_reference_server run_uss_victory_server run_acceptance_tests 	verify_dependencies stop_reference_server stop_uss_victory_server

all: run_reference_server run_acceptance_tests stop_reference_server

test_reference_server: run_reference_server run_acceptance_tests stop_reference_server

test_uss_server: run_uss_victory_server run_acceptance_tests stop_uss_victory_server

verify_dependencies:
	@echo "Checking dependencies..."
	mvn verify -DskipTests

compile_jars:
	@echo "Compiling Jar Files..."
	cd Server ; mvn package -DskipTests

run_server_tests:
	@echo "Running all uss server tests..."
	cd Server; mvn test

run_acceptance_tests:
	@echo -e "${RED}Running acceptance tests..."
	cd Server; mvn surefire:test -Dtest=LaunchRobotTests

run_reference_server:
	@echo "Running The Reference Server Jar File..."
	cd .libs ; java -jar reference-server-0.1.0.jar &
	@echo "Started reference server..."

run_uss_victory_server:
	@echo "Running The USS Victory Server..."
	cd Server ; java -jar target/Server-1.0-SNAPSHOT-jar-with-dependencies.jar &
	sleep 1
	@echo "Started uss server..."

stop_reference_server:
	@echo "Stopping reference server..."
	fuser -k 5000/tcp

stop_uss_victory_server:
	@echo "Stopping uss victory..."
	fuser -k 5000/tcp
