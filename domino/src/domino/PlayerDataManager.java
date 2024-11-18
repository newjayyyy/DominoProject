package domino;

import java.io.*;
import java.util.*;

public class PlayerDataManager {
    private static final String FILE_PATH = "player_data.txt";

    public static List<String> readPlayerData() {
        List<String> playerDataList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                playerDataList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return playerDataList;
    }

    public static void appendPlayerData(String newPlayerData) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            bw.write(newPlayerData);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void updatePlayerScore(String username, int newScore) {
        List<String> playerDataList = readPlayerData();
        for (int i = 0; i < playerDataList.size(); i++) {
            String[] data = playerDataList.get(i).split(",");
            if (data[0].equals(username)) {
                data[2] = String.valueOf(newScore);  // 점수 업데이트
                playerDataList.set(i, String.join(",", data));
                break;
            }
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String playerData : playerDataList) {
                bw.write(playerData);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
