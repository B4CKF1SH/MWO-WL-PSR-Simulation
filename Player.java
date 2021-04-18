package de.b4ckf1sh.stuff;

public class Player {

    private int playerStrength = (int) (Math.random() * 10000);

    private int psr = 2500;

    public int getPSR() {
        return psr;
    }

    public void changePSR(int amount) {
        psr += amount;
    }

    public int getPlayerStrength() {
        return playerStrength;
    }

    @Override
    public String toString() {
        return String.format("Player | Strength: %d | PSR: %d", playerStrength, psr);
    }
}
