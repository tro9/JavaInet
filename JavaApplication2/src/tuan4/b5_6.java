/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuan4;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class b5_6 {
    public static void main(String[] args) {
        try {
            URL url = new URL("https://www.google.com.vn/?hl=vi");
            InputStreamReader reader = new InputStreamReader(url.openStream());
            BufferedWriter writer = new BufferedWriter(new FileWriter("F:/DN/JavaMAng/output.html"));

            
            int c;
            while ((c = reader.read()) != -1) {
                writer.write(c);
            }
            
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
