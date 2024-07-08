/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asteroids;

/**
 *
 * @author walee
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class ReadUserData {
    
    private List<String> attempts;
    
    public ReadUserData () {
        this.attempts = new ArrayList<>();
    }
    
    public List<String> read() {
        try (BufferedReader reader = new BufferedReader( new FileReader("user_data.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                this.attempts.add(line);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return this.attempts;
    }
}
