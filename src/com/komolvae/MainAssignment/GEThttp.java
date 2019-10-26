import com.sun.org.apache.xpath.internal.operations.Bool;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class GEThttp {

    private String BASE_URL; // Base URL (address) of the server
    private int sessionID; // My personal session ID.
    private String email; // My NTNU email adress.
    private String phoneNmbr; // My personal phonenumber.
    private JSONObject jsonId; //JSON Object whit session ID.
    private String recievedMessage;

    public static void main(String[] args) {
        GEThttp getHttp = new GEThttp("datakomm.work", 80);
        getHttp.authorize();
    }

    /**
     * Create an HTTP POST example
     *
     * @param host Will send request to this host: IP address or domain
     * @param port Will use this port
     */
    public GEThttp(String host, int port) {
        BASE_URL = "http://" + host + ":" + port + "/";
    }


    private void authorize() {
        this.email = "komolvae@stud.ntnu.no";
        this.phoneNmbr = "97030747";

        JSONObject json = new JSONObject();
        json.put("email", this.email);
        json.put("phone", this.phoneNmbr);
        System.out.println("Posting this JSON data to server");
        System.out.println(json.toString());
        // TODO: change path to something correct
        sendPost("dkrest/auth", json);
        requestTask1();
    }

    private void requestTask1() {
        JSONObject jsonTask = new JSONObject();
        System.out.println("Requesting task 1 from server");
        sendPostRequestTask1( "dkrest/gettask/1?sessionId=" + this.sessionID, jsonTask);
        task1(); //Answer Task 1

    }

    private void requestTask2() {
        JSONObject jsonTask = new JSONObject();
        System.out.println("Requesting task 2 from server");
        sendPostRequestTask2( "dkrest/gettask/2?sessionId=" + this.sessionID, jsonTask);
        task2();
    }


    private void task1() {
        String msg = "Hello";

        JSONObject jsonTask1 = new JSONObject();
        jsonTask1.put("sessionId", this.sessionID);
        jsonTask1.put("msg", msg);
        System.out.println("Answering task 1:\n");
        System.out.println(jsonTask1.toString());
        sendPostRequestTask1("dkrest/solve", jsonTask1);
        requestTask2();
    }

    private void task2(){
        String msg = this.recievedMessage;

        JSONObject jsonTask2 = new JSONObject();
        jsonTask2.put("sessionId", this.sessionID);
        jsonTask2.put("msg", msg);
        System.out.println("Answering task 2:\n");
        System.out.println(jsonTask2.toString());
        sendPostRequestTask1("dkrest/solve", jsonTask2);

    }


    private void sendPostRequestTask1(String path, JSONObject jsonData) {
        try {
            String url = BASE_URL + path;
            URL urlObj = new URL(url);
            System.out.println("Sending HTTP POST to " + url);
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            OutputStream os = con.getOutputStream();
            os.write(jsonData.toString().getBytes());
            os.flush();

            int responseCode = con.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Server reached");

                // Response was OK, read the body (data)
                InputStream stream = con.getInputStream();
                String responseBody = convertStreamToString(stream);
                stream.close();
                System.out.println("Response from the server:");
                System.out.println(responseBody);
            } else {
                String responseDescription = con.getResponseMessage();
                System.out.println("Request failed, response code: " + responseCode + " (" + responseDescription + ")");
            }
        } catch (ProtocolException e) {
            System.out.println("Protocol nto supported by the server");
        } catch (IOException e) {
            System.out.println("Something went wrong: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void sendPostRequestTask2(String path, JSONObject jsonData) {
        try {
            String url = BASE_URL + path;
            URL urlObj = new URL(url);
            System.out.println("Sending HTTP POST to " + url);
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            OutputStream os = con.getOutputStream();
            os.write(jsonData.toString().getBytes());
            os.flush();

            int responseCode = con.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Server reached");

                // Response was OK, read the body (data)
                InputStream stream = con.getInputStream();
                String responseBody = convertStreamToString(stream);
                stream.close();
                System.out.println("Response from the server:");
                System.out.println(responseBody);
                JSONArray jsonTask2 = new JSONArray(responseBody);
                this.recievedMessage = jsonTask2.getString(0);
                System.out.println("Arguments from task 2: " + this.recievedMessage + "\n");
            } else {
                String responseDescription = con.getResponseMessage();
                System.out.println("Request failed, response code: " + responseCode + " (" + responseDescription + ")");
            }
        } catch (ProtocolException e) {
            System.out.println("Protocol nto supported by the server");
        } catch (IOException e) {
            System.out.println("Something went wrong: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Send HTTP POST
     *
     * @param path     Relative path in the API.
     * @param jsonData The data in JSON format that will be posted to the server
     */
    private void sendPost(String path, JSONObject jsonData) {
        try {
            String url = BASE_URL + path;
            URL urlObj = new URL(url);
            System.out.println("Sending HTTP POST to " + url);
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            OutputStream os = con.getOutputStream();
            os.write(jsonData.toString().getBytes());
            os.flush();

            int responseCode = con.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Server reached");

                // Response was OK, read the body (data)
                InputStream stream = con.getInputStream();
                String responseBody = convertStreamToString(stream);
                stream.close();
                System.out.println("Response from the server:");
                System.out.println(responseBody);
                this.jsonId = new JSONObject(responseBody);
                this.sessionID = jsonId.getInt("sessionId");
                System.out.println("SessionID: " + this.sessionID + "\n");
            } else {
                String responseDescription = con.getResponseMessage();
                System.out.println("Request failed, response code: " + responseCode + " (" + responseDescription + ")");
            }
        } catch (ProtocolException e) {
            System.out.println("Protocol nto supported by the server");
        } catch (IOException e) {
            System.out.println("Something went wrong: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Read the whole content from an InputStream, return it as a string
     *
     * @param is Inputstream to read the body from
     * @return The whole body as a string
     */
    private String convertStreamToString(InputStream is) {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder();
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
                response.append('\n');
            }
        } catch (IOException ex) {
            System.out.println("Could not read the data from HTTP response: " + ex.getMessage());
        }
        return response.toString();

    }
}