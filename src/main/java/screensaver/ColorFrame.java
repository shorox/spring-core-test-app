package screensaver;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

@Component
public abstract class ColorFrame extends JFrame {

  public ColorFrame() {
    setSize(200, 200);
    setVisible(true);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
  }

  public void showOnRandomPlace() {
    Random random = new Random();
    setLocation(random.nextInt(1200), random.nextInt(600));
    getContentPane().setBackground(getColor());
    repaint();
  }

  @Lookup
  protected abstract Color getColor();
}
