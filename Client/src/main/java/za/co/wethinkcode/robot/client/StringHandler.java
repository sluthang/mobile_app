package za.co.wethinkcode.robot.client;

public class StringHandler {
    String previousResponse;

    public boolean launchedMiner (String in){
        String launchedMine = "{\"result\":\"OK\",\"data\":{\"mine\":10,\"repa" +
                "ir\":10,\"shields\":5,\"reload\":10,\"visibility\":10,\"positi" +
                "on\":[0,0]},\"state\":{\"shields\":5,\"position\":[0,0],\"shot" +
                "s\":0,\"direction\":\"NORTH\",\"status\":\"NORMAL\"}}";

        return (in.equals(launchedMine));
    }

    public boolean launchedSniper (String in){
        String launchedSnipe = "{\"result\":\"OK\",\"data\":{\"mine\":10,\"rep" +
                "air\":10,\"shields\":5,\"reload\":10,\"visibility\":10,\"posit" +
                "ion\":[0,0]},\"state\":{\"shields\":5,\"position\":[0,0],\"sho" +
                "ts\":1,\"direction\":\"NORTH\",\"status\":\"NORMAL\"}}";

        return(in.equals(launchedSnipe));
    }

    public boolean launchedTrooper (String in){
        String launchedTroop = "{\"result\":\"OK\",\"data\":{\"mine\":10,\"repa" +
                "ir\":10,\"shields\":5,\"reload\":10,\"visibility\":10,\"positi" +
                "on\":[0,0]},\"state\":{\"shields\":5,\"position\":[0,0],\"shot" +
                "s\":5,\"direction\":\"NORTH\",\"status\":\"NORMAL\"}}";
        return (in.equals(launchedTroop));
    }

    public boolean settingMine (String in){
        return (in.contains("SETMINE"));
    }

    public boolean justSetMine (String in){
        boolean minerWorked = previousResponse.contains("SETMINE");
        return  in.contains("Done") &&
                in.contains("NORMAL") && minerWorked;
    }

    public boolean mineActivate (String in){
        return (in.contains("Mine"));
    }

    public boolean mineDeath (String in){
        return in.contains("Mine") &&
                in.contains("DEAD");
    }

    public boolean inRepair (String in){
        return in.contains("REPAIR") &&
                in.contains("Repair");
    }

    public boolean doneRepairing (String in){
        boolean wasRepairing = previousResponse.contains("REPAIR") &&
                previousResponse.contains("Repair");
        return in.contains("Done") &&
                wasRepairing;
    }

    public boolean blocked (String in){
        return in.contains("Obstructed");
    }

    public boolean pitDeath (String in){
        return in.contains("Fell") &&
                in.contains("DEAD");
    }

    public boolean turnedRight(String in){
        boolean turnedNorthToEast = previousResponse.contains("NORTH") &&
                in.contains("EAST");
        boolean turnedEastToSouth = previousResponse.contains("EAST") &&
                in.contains("SOUTH");
        boolean turnedSouthToWest = previousResponse.contains("SOUTH") &&
                in.contains("WEST");
        boolean turnedWestToNorth = previousResponse.contains("WEST") &&
                in.contains("NORTH");
        return (turnedNorthToEast || turnedEastToSouth ||
                turnedWestToNorth || turnedSouthToWest);
    }

    public boolean turnedLeft(String in){
        boolean turnedNorthToWest = previousResponse.contains("NORTH") &&
                in.contains("WEST");
        boolean turnedWestToSouth = previousResponse.contains("WEST") &&
                in.contains("SOUTH");
        boolean turnedSouthToEast = previousResponse.contains("SOUTH") &&
                in.contains("EAST");
        boolean turnedEastToNorth = previousResponse.contains("EAST") &&
                in.contains("NORTH");
        return (turnedNorthToWest || turnedWestToSouth ||
                turnedSouthToEast || turnedEastToNorth);
    }

    public String checkPosition(String in){
        String[] whatever = in.split("]");
        String[] why = whatever[0].split(":");
        return why[why.length-1]+"]";
    }

    public int xPosition(String position){
        String[] positions = position.split(",");
        String X = positions[0].replace("["," ").trim();
        return Integer.parseInt(X);
    }

    public int yPosition(String position){
        String[] positions = position.split(",");
        String Y = positions[1].replace("]"," ").trim();
        return Integer.parseInt(Y);
    }

    public boolean movedForward(String in){
        if (in.contains("NORTH")){
            String previousPosition = checkPosition(previousResponse);
            int previousY = yPosition(previousPosition);
            String currentPosition = checkPosition(in);
            int currentY = yPosition(currentPosition);
            return (currentY > previousY);
        } if (in.contains("EAST")){
            String previousPosition = checkPosition(previousResponse);
            int previousX = xPosition(previousPosition);
            String currentPosition = checkPosition(in);
            int currentX = xPosition(currentPosition);
            return (currentX > previousX);
        } if (in.contains("WEST")){
            String previousPosition = checkPosition(previousResponse);
            int previousX = xPosition(previousPosition);
            String currentPosition = checkPosition(in);
            int currentX = xPosition(currentPosition);
            return (currentX < previousX);
        } if (in.contains("SOUTH")){
            String previousPosition = checkPosition(previousResponse);
            int previousY = yPosition(previousPosition);
            String currentPosition = checkPosition(in);
            int currentY = yPosition(currentPosition);
            return (currentY < previousY);
        }else{
            return false;
        }
    }

    public boolean movedBackward(String in){
        if (in.contains("NORTH")){
            String previousPosition = checkPosition(previousResponse);
            int previousY = yPosition(previousPosition);
            String currentPosition = checkPosition(in);
            int currentY = yPosition(currentPosition);
            return (currentY < previousY);
        } if (in.contains("EAST")){
            String previousPosition = checkPosition(previousResponse);
            int previousX = xPosition(previousPosition);
            String currentPosition = checkPosition(in);
            int currentX = xPosition(currentPosition);
            return (currentX < previousX);
        } if (in.contains("WEST")){
            String previousPosition = checkPosition(previousResponse);
            int previousX = xPosition(previousPosition);
            String currentPosition = checkPosition(in);
            int currentX = xPosition(currentPosition);
            return (currentX > previousX);
        } if (in.contains("SOUTH")){
            String previousPosition = checkPosition(previousResponse);
            int previousY = yPosition(previousPosition);
            String currentPosition = checkPosition(in);
            int currentY = yPosition(currentPosition);
            return (currentY > previousY);
        }else{
            return false;
        }
    }

    public String convertJSON(String in){
        if (launchedMiner(in)){
            previousResponse = in;
            return "Launched Robot Miner (Sets mines, can't shoot)";

        } if (launchedSniper(in)){
            previousResponse = in;
            return "Launched Robot Sniper (has 1 bullet, 5 step range)";

        } if (launchedTrooper(in)){
            previousResponse = in;
            return "Launched Robot Trooper (has 5 bullets, 1 step range)";

        } if (settingMine(in)){
            previousResponse = in;
            return "Busy setting a mine...";

        } if (justSetMine(in)){
            previousResponse = in;
            return "Done setting mine";

        } if (mineActivate(in)){
            previousResponse = in;
            return "You have stepped on a mine";

        } if (mineDeath(in)){
            previousResponse = in;
            return "You are dead (cause of death: mine)";

        } if (inRepair(in)){
            previousResponse = in;
            return "Busy repairing robot...";

        } if (doneRepairing(in)){
            previousResponse = in;
            return "Robot is repaired";

        } if (blocked(in)){
            previousResponse = in;
            return "Robot's path is obstructed, robot did not move";

        } if (pitDeath(in)){
            previousResponse = in;
            return "You are dead (cause of death: pit)";

        } if (turnedRight(in)){
            previousResponse = in;
            return "You have turned right";

        } if (turnedLeft(in)){
            previousResponse = in;
            return "You have turned left";

        } if (movedForward(in) && in.contains("NORTH")){
            String previousPosition = checkPosition(previousResponse);
            int previousY = yPosition(previousPosition);
            String currentPosition = checkPosition(in);
            int currentY = yPosition(currentPosition);
            int steps = currentY - previousY;
            previousResponse = in;
            return "You moved forward by "+steps+" steps.";

        } if (movedForward(in) && in.contains("EAST")){
            String previousPosition = checkPosition(previousResponse);
            int previousX = xPosition(previousPosition);
            String currentPosition = checkPosition(in);
            int currentX = xPosition(currentPosition);
            int steps = currentX - previousX;
            previousResponse = in;
            return "You moved forward by "+steps+" steps.";

        } if (movedForward(in) && in.contains("WEST")){
            String previousPosition = checkPosition(previousResponse);
            int previousX = xPosition(previousPosition);
            String currentPosition = checkPosition(in);
            int currentX = xPosition(currentPosition);
            int steps = previousX - currentX;
            previousResponse = in;
            return "You moved forward by "+steps+" steps.";

        } if (movedForward(in) && in.contains("SOUTH")){
            String previousPosition = checkPosition(previousResponse);
            int previousY = yPosition(previousPosition);
            String currentPosition = checkPosition(in);
            int currentY = yPosition(currentPosition);
            int steps = previousY - currentY;
            previousResponse = in;
            return "You moved forward by "+steps+" steps.";

        } if (movedBackward(in) && in.contains("NORTH")){
            String previousPosition = checkPosition(previousResponse);
            int previousY = yPosition(previousPosition);
            String currentPosition = checkPosition(in);
            int currentY = yPosition(currentPosition);
            int steps = previousY - currentY;
            previousResponse = in;
            return "You moved backward by "+steps+" steps.";

        }if (movedBackward(in) && in.contains("WEST")){
            String previousPosition = checkPosition(previousResponse);
            int previousX = xPosition(previousPosition);
            String currentPosition = checkPosition(in);
            int currentX = xPosition(currentPosition);
            int steps = previousX - currentX;
            previousResponse = in;
            return "You moved backward by "+(-steps)+" steps.";

        }if (movedBackward(in) && in.contains("SOUTH")){
            String previousPosition = checkPosition(previousResponse);
            int previousY = yPosition(previousPosition);
            String currentPosition = checkPosition(in);
            int currentY = yPosition(currentPosition);
            int steps = currentY - previousY;
            previousResponse = in;
            return "You moved backward by "+steps+" steps.";

        }if (movedBackward(in) && in.contains("EAST")){
            String previousPosition = checkPosition(previousResponse);
            int previousX = xPosition(previousPosition);
            String currentPosition = checkPosition(in);
            int currentX = xPosition(currentPosition);
            int steps = currentX - previousX;
            previousResponse = in;
            return "You moved backward by "+(-steps)+" steps.";

        }else{
            previousResponse = in;
            return "what?";
        }
    }
}
