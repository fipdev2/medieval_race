import java.awt.*;

class Horse {
    private final HorseRaceGame horseRaceGame;
    int x, y;
    Color color;

    Horse(HorseRaceGame horseRaceGame, int x, int y, Color color) {
        this.horseRaceGame = horseRaceGame;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    void move(int dx, int dy) {
        x += dx;
        y += dy;
        if (y < 0) y = 0;
        if (y > horseRaceGame.getHEIGHT() - 40) y = horseRaceGame.getHEIGHT() - 40;
    }

    void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, 40, 30);
        g.setColor(Color.BLACK);
        g.drawString("🐎", x + 10, y + 20);
    }

    Rectangle getBounds() {
        return new Rectangle(x, y, 40, 30);
    }
}
