package tuan4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class b4 {
    public static void main(String[] args) {
        try {
            URL url = new URL("https://www.google.com.vn/?hl=vi");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
