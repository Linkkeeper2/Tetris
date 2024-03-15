import java.util.ArrayList;

public class Tournament {
    public ArrayList<String[]> brackets;

    /**
     * Will construct a new tournament with the necessary amount of brackets.
     * Players length will be an even numebr.
     * 
     * @param players The list of players in the tournament
     */
    public Tournament(ArrayList<String> players) {
        brackets = new ArrayList<>();

        for (int i = 0; i < players.size(); i += 2) {
            String[] bracket = new String[2];

            bracket[0] = players.get(i);
            bracket[1] = players.get(i + 1);
            brackets.add(bracket);
        }
    }

    public String[] getBracket(int index) {
        return brackets.get(index);
    }
}
