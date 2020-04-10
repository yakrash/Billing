package su.bzz.springcourse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


public class MusicPlayer  {

    private List<Music> musicList = new ArrayList<>();

    private int volume;
    private String name;

    public MusicPlayer (ClassicalMusic classicalMusic, PopMusic popMusic, RockMusic rockMusic){
        this.musicList.add(classicalMusic);
        this.musicList.add(popMusic);
        this.musicList.add(rockMusic);
    }

//    public void setMusicList (List<Music> musicList){
//        this.musicList = musicList;
//    }

    public void playMusic() {
        System.out.println("Plaing:");
        for (Music music : musicList) {
            System.out.println(music.getSong());
        }
        System.out.println(musicList.get(0).getSong());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
