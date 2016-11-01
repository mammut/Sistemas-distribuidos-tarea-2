BUILD_PATH=build
BIN_PATH=$(BUILD_PATH)/bin

main: server client

client: src/cl/utfsm/Client.java
	mkdir -p $(BIN_PATH)
	javac -sourcepath src -d $(BIN_PATH) $<

server: src/cl/utfsm/Server.java
	mkdir -p $(BIN_PATH)
	javac -sourcepath src -d $(BIN_PATH) $<

rmiregistry:
	cd $(BIN_PATH); rmiregistry &

run-client: client
	java -classpath $(BIN_PATH)  cl.utfsm.Client

run-server: rmiregistry server
	java -classpath $(BIN_PATH) -Djava.rmi.server.codebase=file:$(BIN_PATH)/ cl.utfsm.Server; killall rmiregistry; echo "\nDone"

clean:
	rm -rf $(BUILD_PATH)

