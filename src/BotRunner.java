class BotRunner implements Runnable {
    private final HorseRaceGame horseRaceGame;

    public BotRunner(HorseRaceGame horseRaceGame) {
        this.horseRaceGame = horseRaceGame;
    }

    public void run() {
        try {
            while (!horseRaceGame.isGameEnded() && horseRaceGame.getPlayer2().x < horseRaceGame.getWIDTH() - 40) {
                Thread.sleep(100);
                horseRaceGame.getPlayer2().move(2, 0);
                horseRaceGame.repaint();
            }
        } catch (InterruptedException e) {
            // Thread interrompida ao reiniciar o jogo
        }
    }
}
