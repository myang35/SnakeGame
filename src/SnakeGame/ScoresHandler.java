/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SnakeGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marvin
 */
public class ScoresHandler {

    private File file;
    private ArrayList<Score> scores = new ArrayList<>();

    public ScoresHandler(File file) {
        this.file = file;

        createFileIfNotExist();
        refreshScores();
    }

    private void createFileIfNotExist() {
        if (!Files.exists(file.toPath())) {
            try {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.close();
                System.out.println("Created file: " + file.getPath());
            } catch (IOException e) {
                Logger.getLogger(ScoresHandler.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    private void refreshScores() {
        try ( Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                // Get data
                String scoreString = scanner.nextLine();
                String[] scoreData = scoreString.split(",");

                // Create Score
                String name = scoreData[0];
                int points = Integer.parseInt(scoreData[1]);
                Score score = new Score(name, points);

                // Add Score to list
                scores.add(score);
            }
        } catch (FileNotFoundException e) {
            Logger.getLogger(ScoresHandler.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void addScore(Score newScore) {
        try ( FileWriter fileWriter = new FileWriter(file)) {
            // Write to file
            for (Score score : scores) {
                fileWriter.append(score.toWriteFormat() + "\n");
            }
            fileWriter.append(newScore.toWriteFormat());

            // Add Score to list
            scores.add(newScore);
        } catch (IOException e) {
            Logger.getLogger(ScoresHandler.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void addScore(String name, int points) {
        Score score = new Score(name, points);
        addScore(score);
    }
    
    public Score getHighScore() throws IllegalStateException {
        if (scores.isEmpty()) {
            throw new IllegalStateException("There are no saved scores.");
        }
        
        Score highScore = scores.get(0);
        for (int i = 1; i < scores.size(); i++) {
            if (scores.get(i).getPoints() > highScore.getPoints()) {
                highScore = scores.get(i);
            }
        }
        
        return highScore;
    }

    public String read() {
        String dataList = "";
        try ( Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                dataList += data + "\n";
            }
        } catch (FileNotFoundException e) {
            Logger.getLogger(ScoresHandler.class.getName()).log(Level.SEVERE, null, e);
        }

        return dataList;
    }

    public void clear() {
        // Write file
        try ( FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write("");
        } catch (IOException e) {
            Logger.getLogger(ScoresHandler.class.getName()).log(Level.SEVERE, null, e);
        }
    }

}
