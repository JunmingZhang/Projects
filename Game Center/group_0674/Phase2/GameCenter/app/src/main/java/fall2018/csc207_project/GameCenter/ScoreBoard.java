package fall2018.csc207_project.GameCenter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observer;
import java.util.TreeMap;

public abstract class ScoreBoard implements Observer, Serializable {

    private static final long serialVersionUID = 4L;

    /**
     * fixed length top scores among all users
     */
    private int[] topScores;

    /**
     * maps index in topScores to matching userName
     */
    private Map<Integer, String> indexList;

    /**
     * maps userName to array of highest scores
     */
    private HashMap<String, Integer[]> topUserScores = new HashMap<String, Integer[]>();

    /**
     * highest scores keep per user
     */
    private int perUser = 10;

    /**
     * number of highest scores to keep among all users
     */
    private int totalLength = 10;

    /**
     * initialize a default ScoreBoard
     */
    public ScoreBoard() {
        // each user only appear one time on top board
        this.topScores = new int[totalLength];
        this.indexList = new HashMap<Integer, String>();
    }

    public Map<Integer, String> getIndexList() {
        return indexList;
    }

    public int[] getTopScores() {
        return topScores;
    }

    /**
     * initialize a ScoreBoard with custom length
     *
     * @param totalLength numbers of highest scores to keep among all users
     * @param perUser     numbers of highest scores per user
     */
    ScoreBoard(int totalLength, int perUser) {
        this.totalLength = totalLength;
        this.perUser = perUser;
        this.topScores = new int[totalLength];
        this.indexList = new HashMap<Integer, String>();
    }


    /**
     * Add a new score to the per user scoreboard.
     * Create the array of scores if this is the first score of the user
     * The score will only be added if its among the highest scores that user achieve
     *
     * @param userName which user the score belongs to
     * @param score    the score needed to be add
     */
    protected void addNewScore(String userName, Integer score) {
        if (topUserScores.containsKey(userName)) {
            Integer[] userScores = topUserScores.get(userName);
            //put scores from lowest to highest first so the lower score will be replace first
            Arrays.sort(userScores);
            for (int i = 0; i < userScores.length; i++) {
                // replace the first element in userScore that's less than new score
                if (userScores[i].compareTo(score) < 0) {
                    userScores[i] = score;
                    // update topScores after a new score is added
                    updateTopScores(topUserScores);
                    break;
                }
            }
            //Put the scores from highest to lowest after done adding score
            Arrays.sort(userScores, Collections.<Integer>reverseOrder());
        } else {
            Integer[] scoreArray = new Integer[perUser];
            for (int i = 0; i < scoreArray.length; i++) {
                scoreArray[i] = 0;
            }
            scoreArray[0] = score;
            topUserScores.put(userName, scoreArray);
            // update topScores after a new score is added
            updateTopScores(topUserScores);
        }
    }

    /**
     * return an integer array of scores for particular user
     *
     * @param userName user name of the player
     * @return array of userScores
     */
    public Integer[] getPlayerScore(String userName) {
        if (topUserScores.containsKey(userName)) {
            Integer[] userScores = topUserScores.get(userName);
            Arrays.sort(userScores, Collections.reverseOrder());
            return userScores;

        } //Can use getOrDefault here instead

        return new Integer[perUser];
    }

    /**
     * return the highest score of for specific user
     *
     * @param name the name of the user
     * @return the highest score
     */
    public int getPlayerTopScore(String name) {
        if (topUserScores.get(name) == null)
            return 0;
        else
            return topUserScores.get(name)[0];
    }

    /**
     * Update the top scores
     *
     * @param userScores scores of all users on the board
     */
    private void updateTopScores(HashMap<String, Integer[]> userScores) {
        Comparator<String> comparator = new MapValueComparator(userScores);
        TreeMap<String, Integer[]> sorted = new TreeMap<String, Integer[]>(comparator);
        sorted.putAll(userScores);
        int count = 0;
        indexList.clear();
        for (Map.Entry<String, Integer[]> entry : sorted.entrySet()) {
            if (count >= topScores.length) break;
            if (topScores[count] < entry.getValue()[0])
                topScores[count] = entry.getValue()[0];
            indexList.put(count, entry.getKey());

            count++;
        }

    }

    /**
     * @param setting information needed for calculate socres
     * @return scores based on setting
     */
    public abstract int calculateScore(List<Object> setting);

    /**
     * comparator for map values
     */
    class MapValueComparator implements Comparator<String> {
        /**
         * map that needed to be sorted
         */
        HashMap<String, Integer[]> inputMap = new HashMap<String, Integer[]>();

        /**
         * input for comparator
         *
         * @param map the map input for sorting
         */
        MapValueComparator(HashMap<String, Integer[]> map) {
            this.inputMap.putAll(map);
        }

        /**
         * Comparator for descending order.
         *
         * @param o1 userName1
         * @param o2 userName2
         * @return int that shows result of comparison
         */
        @Override
        public int compare(String o1, String o2) {
            if (inputMap.get(o1)[0] > inputMap.get(o2)[0]) {
                return -1;
            } else if (inputMap.get(o1)[0] <= inputMap.get(o2)[0]) {
                return 1;
            } else {
                return 0;
            }
        }
    }

}
