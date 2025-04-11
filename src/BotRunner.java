import java.util.Random;

class BotRunner implements Runnable {
    private final HorseRaceGame horseRaceGame;

    Random rand = new Random();

    public BotRunner(HorseRaceGame horseRaceGame) {
        this.horseRaceGame = horseRaceGame;
    }

    public void run() {
        try {
            while (!horseRaceGame.isGameEnded() && horseRaceGame.getPlayer2().x < horseRaceGame.getWIDTH() - 40) {
                Thread.sleep(100);
                horseRaceGame.getPlayer2().move(rand.nextInt(15), 0);
                horseRaceGame.repaint();
            }
        } catch (InterruptedException e) {
            // Thread interrompida ao reiniciar o jogo
        }
    }
}
