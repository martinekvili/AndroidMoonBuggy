package hu.bme.androidhazi.moonbuggy.data;

import java.util.Map;

import hu.bme.androidhazi.moonbuggy.data.orm.HighscoreEntity;

/**
 * Created by vilmo on 2016. 05. 16..
 */
public class HighscoreViewModel {

    private int number;
    private String name;
    private int score;

    public HighscoreViewModel(HighscoreEntity entity) {
        this.name = entity.getName();
        this.score = entity.getScore();
    }

    public HighscoreViewModel(Map<String, Object> value) {
        this.name = (String) value.get("name");
        this.score = ((Long) value.get("score")).intValue();
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
}
