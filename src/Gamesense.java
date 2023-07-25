import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Gamesense {

    public String getEngineAddress() {
        System.out.println("Reading Server-info ...");
        String address = null;
        String corePropsPath = null;
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            corePropsPath = System.getenv("PROGRAMDATA") + "\\SteelSeries\\SteelSeries Engine 3\\coreProps.json";
        } else if (os.contains("mac")) {
            corePropsPath = "/Library/Application Support/SteelSeries Engine 3/coreProps.json";
        } else {
            System.err.println("Unsupported operating system: " + os);
            return address;
        }

        try {
            File corePropsFile = new File(corePropsPath);
            if (corePropsFile.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(corePropsFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("\"address\":")) {
                        Pattern pattern = Pattern.compile("\"address\":\\s*\"(\\S+)");
                        Matcher matcher = pattern.matcher(line);
                        if (matcher.find()) {
                            address = matcher.group(1);
                            address = address.split(String.valueOf("\""))[0];
                        }
                    }
                }
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (address != null) {
            System.out.println("Server runnig on: " + address);
        } else {
            System.err.println("SteelSeries Engine is not running or coreProps.json not found.");
            System.exit(1);
        }
        return address;
    }
    private void sendJson(String address, String jsonString) {
        try {
            // Replace <SSE3 url> with the actual URL of the SteelSeries Engine 3
            URL obj = new URL(address);

            // Open a connection
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            System.out.println("sending JSON");
            // Send the JSON object as POST data
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(jsonString);
            wr.flush();
            wr.close();

            // Get the response
            int responseCode = con.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // You can check the responseCode to see if the event was processed successfully

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendGameEvent(String address, String game, String event, int value) {
         address = "http://" + address + "/game_event";
        String jsonString = "{"
                + "\"game\": \"" + game + "\","
                + "\"event\": \"" + event + "\","
                + "\"data\": {"
                + "\"value\": " + value
                + "}"
                + "}";
        System.out.println("sendinging Event");
        sendJson(address, jsonString);
    }

    public void setGameMetadata(String address, String game, String gameDisplayName, String developer, int deinitializeTimerLength) {
        address = "http://"+address+"/game_metadata";

        String jsonString = "{"
                + "\"game\": \"" + game + "\","
                + "\"game_display_name\": \"" + gameDisplayName + "\","
                + "\"developer\": \"" + developer + "\","
                + "\"deinitialize_timer_length_ms\": " + deinitializeTimerLength
                + "}";
        System.out.println("creating Metadata");
        sendJson(address, jsonString);
    }

    public void registerEvent(String address, String game, String event, int minValue, int maxValue, int iconId, boolean valueOptional) {
        address = "http://"+address+"/register_game_event";
            // Create the JSON object
            String jsonString = "{"
                    + "\"game\": \"" + game + "\","
                    + "\"event\": \"" + event + "\","
                    + "\"min_value\": " + minValue + ","
                    + "\"max_value\": " + maxValue + ","
                    + "\"icon_id\": " + iconId + ","
                    + "\"value_optional\": " + valueOptional
                    + "}";
        System.out.println("creating Event");
            sendJson(address,jsonString);
        }


}
