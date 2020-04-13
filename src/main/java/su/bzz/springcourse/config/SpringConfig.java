package su.bzz.springcourse.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import su.bzz.springcourse.music.ClassicalMusic;
import su.bzz.springcourse.music.MusicPlayer;
import su.bzz.springcourse.music.PopMusic;
import su.bzz.springcourse.music.RockMusic;

@Configuration
@ComponentScan("su.bzz.springcourse")
@PropertySource("classpath:musicPlayer.properties")
public class SpringConfig {
//    @Bean
//    public ClassicalMusic classicalMusic() {
//        return new ClassicalMusic();
//    }
    @Bean
    public PopMusic popMusic(){
        return new PopMusic();
    }
    @Bean
    public RockMusic rockMusic(){
        return new RockMusic();
    }

    @Autowired
    private ClassicalMusic classicalMusic;
    @Bean
    public MusicPlayer musicPlayer (){
        return new MusicPlayer(classicalMusic, popMusic(), rockMusic());
    }

}
