package de.b4ckf1sh.stuff;

import com.matchmaking.MatchmakerImpl;
import com.matchmaking.helper.NearestNeighbourPlayerFinder;
import com.matchmaking.helper.RatingBasedTeamBuilder;
import com.matchmaking.model.Match;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static final int NUM_ITERATIONS = 1;
    public static final int NUM_PLAYERS = 2000;
    public static final int NUM_MATCHES = 19000;
    public static final int PSR_CHANGE = 25;

    public static final Random RANDOM = new Random();

    private static List<Player> players;

    public static void main(String[] args) {
        for (int iterationNum = 0; iterationNum < NUM_ITERATIONS; iterationNum++) {
            initPlayers();

            for (int matchNum = 0; matchNum < NUM_MATCHES; matchNum++) {
                System.out.println(matchNum);
                Player[] m = getMatch();


                List<Player> t1 = new ArrayList<>();
                List<Player> t2 = new ArrayList<>();

                for (int i = 0; i < 12; i++) {
                    t1.add(m[i]);
                    t2.add(m[i + 12]);
                }

                int t1avg = 0;
                for (Player p : t1) {
                    t1avg += p.getPlayerStrength();
                }
                t1avg /= 12;

                int t2avg = 0;
                for (Player p : t2) {
                    t2avg += p.getPlayerStrength();
                }
                t2avg /= 12;

                double t1avgmod = t1avg + (Math.random() * 200) - 100;

                int t1Win = t1avgmod > t2avg ? 1 : -1;

                for (Player p : t1) {
                    p.changePSR(t1Win * PSR_CHANGE);
                }
                
                for (Player p : t2) {
                    p.changePSR(-t1Win * PSR_CHANGE);
                }
            }

            List<Player> sorted = players.stream().sorted(Comparator.comparingInt(Player::getPSR)).collect(Collectors.toList());
            for (Player p : sorted) {
                System.out.printf("%d%n", p.getPlayerStrength());
            }

            for (Player p : sorted) {
                System.out.printf("%d%n", p.getPSR());
            }
        }
    }

    private static void initPlayers() {
        players = new ArrayList<>();

        for (int i = 0; i < NUM_PLAYERS; i++) {
            Player p = new Player();
            players.add(p);
        }
    }

    private static Player[] getMatch() {
        Collections.shuffle(players);
        MatchmakerImpl mm = new MatchmakerImpl(new NearestNeighbourPlayerFinder(), new RatingBasedTeamBuilder());

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            mm.enterMatchmaking(
                    new com.matchmaking.model.Player(
                            String.valueOf(i), 0, 0, player.getPSR()
                    ));
        }

        Match m = null;

        while (m == null) {
           m = mm.findMatch(12);
        }
        List<Player> match = new ArrayList<>();
        for (com.matchmaking.model.Player p : m.getTeam1()) {
            match.add(players.get(Integer.parseInt(p.getName())));
        }
        for (com.matchmaking.model.Player p : m.getTeam2()) {
            match.add(players.get(Integer.parseInt(p.getName())));
        }

        return match.toArray(new Player[0]);
    }
}
