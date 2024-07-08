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
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;

public class Projectile extends Character {
    
    public Projectile(int x, int y) {
        super(new Polygon(0, 0, 5, -2, 5, 5, -2, 2), x, y);
        this.getCharacter().setFill(Color.GREEN); // Set the projectile's color to green
    }
}
