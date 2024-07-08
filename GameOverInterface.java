/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asteroids;

import java.util.concurrent.atomic.AtomicInteger;
import javafx.scene.text.Text;

/**
 *
 * @author walee
 */
public class GameOverInterface {
    private Text text;
    private AtomicInteger points;
    
    
    public GameOverInterface() {
    this.text = new Text(10, 20, "Points: 0" + " Upgrades: 0");
    this.points = new AtomicInteger();
    }
    public AtomicInteger getPoints() {
        return this.points;
    }
    
    public void resetText() {
        this.text.setText("Points: 0" + " Upgrades: 0");
    }
    public void clearPoints() {
        this.points.set(0);
    }
    
    public Text getText() {
        return this.text;
    }
    public void setText(String text) {
        this.text.setText(text);
    }
}
