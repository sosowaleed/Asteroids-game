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
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SaveUserData {
    
    private String time;
    private int points;
    
    public SaveUserData (String time, int points) {
        this.time = time;
        this.points = points;
    }
    
    public void saveData () {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("user_data.txt", true))) {
            String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:SS"));
            writer.write("Date: " + dateTime + ", Time: " + this.time + " Seconds, Points: " + this.points + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void deleteAllData () {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("user_Data.txt"))) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
