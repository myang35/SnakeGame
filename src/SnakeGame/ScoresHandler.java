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
import java.time.LocalDateTime;
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
    private ArrayList<Integer> scores = new ArrayList<>();

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
                int score = Integer.parseInt(scoreData[0]);

                // Add Score to list
                scores.add(score);
            }
        } catch (FileNotFoundException e) {
            Logger.getLogger(ScoresHandler.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void addScore(int newScore) {
        try ( FileWriter fileWriter = new FileWriter(file, true)) {
            // Write to file
            fileWriter.append(newScore + "," + LocalDateTime.now().toString() + "\n");

            // Add Score to list
            scores.add(newScore);
        } catch (IOException e) {
            Logger.getLogger(ScoresHandler.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public int getHighScore() throws IllegalStateException {
        if (scores.isEmpty()) {
            throw new IllegalStateException("There are no saved scores.");
        }
        
        int highScore = scores.get(0);
        for (int i = 1; i < scores.size(); i++) {
            if (scores.get(i) > highScore) {
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
