import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class HorseRaceGame extends JPanel implements ActionListener {
    private Timer timer;
    private final int DELAY = 30;

    public int getHEIGHT() {
        return HEIGHT;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    private final int WIDTH = 800, HEIGHT = 400;

    public Horse getPlayer2() {
        return player2;
    }

    public Horse getPlayer1() {
        return player1;
    }

    private Horse player1, player2;
    private boolean isTwoPlayer = false;
    private Obstacle[] obstacles;

    public boolean isGameEnded() {
        return gameEnded;
    }

    private boolean gameEnded = false;
    private String winner = "";
    private Thread botThread;

    public HorseRaceGame(boolean isTwoPlayer) {
        this.isTwoPlayer = isTwoPlayer;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(87, 59, 12));
        setFocusable(true);

        player1 = new Horse(this, 0, HEIGHT / 3, Color.BLUE);
        player2 = new Horse(this, 0, HEIGHT * 2 / 3, Color.RED);

        generateObstacles();

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (gameEnded) return;

                int key = e.getKeyCode();

                // Player 1 - WASD
                if (key == KeyEvent.VK_W) player1.move(0, -10);
                if (key == KeyEvent.VK_S) player1.move(0, 10);
                if (key == KeyEvent.VK_A) player1.move(-10, 0);
                if (key == KeyEvent.VK_D) player1.move(10, 0);

                // Player 2 - Arrows
                if (isTwoPlayer) {
                    if (key == KeyEvent.VK_UP) player2.move(0, -10);
                    if (key == KeyEvent.VK_DOWN) player2.move(0, 10);
                    if (key == KeyEvent.VK_LEFT) player2.move(-10, 0);
                    if (key == KeyEvent.VK_RIGHT) player2.move(10, 0);
                }
            }
        });

        timer = new Timer(DELAY, this);
        timer.start();

        if (!isTwoPlayer) {
            botThread = new Thread(new BotRunner(this));
            botThread.start();
        }
    }

    private void generateObstacles() {
        obstacles = new Obstacle[10];
        Random rand = new Random();
        for (int i = 0; i < obstacles.length; i++) {
            int x = rand.nextInt(WIDTH - 100) + 100;
            int y = rand.nextInt(HEIGHT - 50);
            obstacles[i] = new Obstacle(x, y);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (gameEnded) return;
        checkCollisions(player1);
        if (isTwoPlayer) checkCollisions(player2);
        repaint();
    }

    private void checkCollisions(Horse player) {
        for (Obstacle obs : obstacles) {
            if (player.getBounds().intersects(obs.getBounds())) {
                player.move(-5, 0); // hit penalty
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackground(g);
        player1.draw(g);
        if (isTwoPlayer || !isTwoPlayer) player2.draw(g);
        for (Obstacle obs : obstacles) obs.draw(g);
        checkWinCondition(g);
    }

    private void drawBackground(Graphics g) {
        g.setColor(new Color(150, 111, 51));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.GRAY);
        for (int i = 0; i < HEIGHT; i += 40) {
            g.drawLine(0, i, WIDTH, i);
        }
    }

    private void checkWinCondition(Graphics g) {
        if (!gameEnded) {
            if (player1.x >= WIDTH - 40) {
                timer.stop();
                winner = "Player 1 venceu!";
                gameEnded = true;
            } else if (player2.x >= WIDTH - 40) {
                timer.stop();
                winner = "Player 2 venceu!";
                gameEnded = true;
            }
        }

        if (gameEnded) {
            g.setColor(Color.YELLOW);
            g.drawString(winner, WIDTH / 2 - 50, HEIGHT / 2);
            showEndOptions();
            return;
        }
    }

    private void showEndOptions() {
        SwingUtilities.invokeLater(() -> {
            String[] options = isTwoPlayer ? new String[]{"Jogar Novamente", "Contra Bot"} : new String[]{"Jogar Novamente", "2 Jogadores"};
            int option = JOptionPane.showOptionDialog(null, winner + "\nDeseja jogar novamente?",
                    "Fim de Jogo",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (option == 0) {
                restartGame(isTwoPlayer);
            } else if (option == 1) {
                restartGame(!isTwoPlayer);
            }
        });
    }

    private void restartGame(boolean twoPlayers) {
        if (botThread != null && botThread.isAlive()) {
            botThread.interrupt();
        }
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.dispose();
        JFrame newFrame = getNewFrame();
        HorseRaceGame game = new HorseRaceGame(twoPlayers);
        newFrame.add(game);
        newFrame.pack();
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.setLocationRelativeTo(null);
        newFrame.setVisible(true);
    }

    private static JFrame getNewFrame() {
        return new JFrame("Corrida de Cavalos - Medieval Race");
    }

    public static void main(String[] args) {
        boolean isTwoPlayer = JOptionPane.showConfirmDialog(null, "Dois jogadores?", "Modo de Jogo", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
        JFrame frame = new JFrame("Corrida de Cavalos - Medieval Race");
        HorseRaceGame game = new HorseRaceGame(isTwoPlayer);
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
