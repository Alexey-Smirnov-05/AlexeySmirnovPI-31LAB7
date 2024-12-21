import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Playlist implements Cloneable, Playable {
    protected List<Track> tracks; // protected для доступа из производных классов
    protected int currentTrack;
    private static int instanceCount = 0;

    public Playlist() {
        tracks = new ArrayList<>();
        currentTrack = 0;
        instanceCount++;
    }

    // Конструктор копирования
    public Playlist(Playlist other) {
        this.tracks = new ArrayList<>(other.tracks);
        this.currentTrack = other.currentTrack;
        instanceCount++;
    }

    @Override
    public Playlist clone() {
        try {
            Playlist cloned = (Playlist) super.clone();
            cloned.tracks = new ArrayList<>(this.tracks); // Глубокое клонирование
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Unexpected clone error", e);
        }
    }

    public Playlist shallowClone() {
        try {
            return (Playlist) super.clone(); // Мелкое клонирование
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Unexpected clone error", e);
        }
    }

    public static int getInstanceCount() {
        return instanceCount;
    }

    public void viewSongs() {
        for (int i = 0; i < tracks.size(); i++) {
            System.out.println((i + 1) + ". " + tracks.get(i).getTitle());
        }
    }

    public void addSong(Track song) {
        tracks.add(song);
    }

    public void removeSong(int index) {
        if (index >= 0 && index < tracks.size()) {
            tracks.remove(index);
        }
    }

    public int getTotalNumberOfTracks() {
        return tracks.size();
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public int getCurrentTrack() {
        return currentTrack;
    }

    public void setCurrentTrack(int currentTrack) {
        this.currentTrack = currentTrack;
    }

    public void loadTracksFromFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Ошибка при создании файла: " + e.getMessage());
            }
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                addSong(new Track(line));
            }
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке треков: " + e.getMessage());
        }
    }

    public void saveTracksToFile(String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Track track : tracks) {
                bw.write(track.getTitle());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении треков: " + e.getMessage());
        }
    }

    @Override
    public void play() {
        if (!tracks.isEmpty()) {
            System.out.println("Сейчас играет: " + tracks.get(currentTrack).getTitle());
        } else {
            System.out.println("Плейлист пуст.");
        }
    }

    public int findTrack(String title) {
        for (int i = 0; i < tracks.size(); i++) {
            if (tracks.get(i).getTitle().equals(title)) {
                return i;
            }
        }
        return -1; // Если не найдено
    }
    public void shuffleTracks() {
        long seed = System.nanoTime();
        Collections.shuffle(tracks, new Random(seed));
    }

    public String getTitle() {
        return "Base Playlist";
    }
}