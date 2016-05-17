package hu.bme.androidhazi.moonbuggy.data.orm;

import com.orm.SugarRecord;

/**
 * Created by vilmo on 2016. 05. 16..
 */
public class HighscoreEntity extends SugarRecord {

    String name;
    int score;

    public HighscoreEntity() {
    }

    public HighscoreEntity(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
}
