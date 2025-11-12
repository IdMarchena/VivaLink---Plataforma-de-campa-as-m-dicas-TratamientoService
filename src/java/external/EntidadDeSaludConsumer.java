/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package external;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class EntidadDeSaludConsumer {

    private static final String BASE_URL = "http://localhost:8080/EntidadDeSaludService/EntidadDeSaludServlet";

    public boolean entidadExiste(String identificador) {
        try {
            URL url = new URL(BASE_URL + "?action=buscarPorIdentificador&identificador=" + identificador);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            int responseCode = con.getResponseCode();
            if (responseCode != 200) return false;

            Scanner sc = new Scanner(con.getInputStream());
            StringBuilder response = new StringBuilder();
            while (sc.hasNext()) response.append(sc.nextLine());
            sc.close();

            // El servlet puede devolver "true"/"false" o JSON
            return response.toString().contains("true");
        } catch (IOException e) {
            return false;
        }
    }
}
