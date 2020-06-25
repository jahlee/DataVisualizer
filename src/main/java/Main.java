import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

/*
Java Google QuickStart information retrieved from https://developers.google.com/sheets/api/quickstart/java
DataVisualizer project created by Josh Lee
Version 1: June 24, 2020
Data includes days we cheated on our diet, inputted by me and two friends: Dyle and Hyun
--> data1 = Dyle
--> data2 = Hyun
--> data3 = Josh (me)
*/

public class Main {

    private static final String APPLICATION_NAME = "Google Sheets API Java DataVisualizer";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = Main.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    /**
     * Retrieves personal spreadsheet information and creates a graph using the data from:
     * https://docs.google.com/spreadsheets/d/1VxAPxoq9ImYWdasVifosW2OwY1aJWftXFNmdM0t5ChE/edit?usp=sharing
     */
    public static void main(String... args) throws IOException, GeneralSecurityException
    {
        // get setup colors
        SetupColors sc = new SetupColors();
        while (sc.isRunning())      // wait until colors are chosen
        {
            try {
                Thread.sleep(1500);     // "refresh" every 1.5 seconds (1500ms)
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        Color[] chosenColors = sc.getColors();

        // setup visuals
        DataVisualizer dv = new DataVisualizer(chosenColors);
        JFrame f = new JFrame("Data on Display");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(dv);
        f.setSize(dv.getWidth(), dv.getHeight());


        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1VxAPxoq9ImYWdasVifosW2OwY1aJWftXFNmdM0t5ChE";
        final String range = "A2:H";
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty())
        {
            System.out.println("No data found.");
        }
        else {
            // add list of values to DataVisualizer class
            dv.importData(values);
        }
        f.setVisible(true);
    }
}