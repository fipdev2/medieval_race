import java.awt.*;

class Obstacle {
    int x, y;

    Obstacle(int x, int y) {
        this.x = x;
        this.y = y;
    }

    void draw(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, 20, 20);
        g.setColor(Color.BLACK);
        g.drawString("🪨", x, y + 15);
    }

    Rectangle getBounds() {
        return new Rectangle(x, y, 20, 20);
    }
}
