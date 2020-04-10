package su.bzz.springcourse.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import su.bzz.springcourse.ClassicalMusic;
import su.bzz.springcourse.MusicPlayer;
import su.bzz.springcourse.PopMusic;
import su.bzz.springcourse.RockMusic;

@Configuration
@ComponentScan("su.bzz.springcourse")
@PropertySource("classpath:musicPlayer.properties")
public class SpringConfig {
    @Bean
    public ClassicalMusic classicalMusic() {
        return new ClassicalMusic();
    }
    @Bean
    public PopMusic popMusic(){
        return new PopMusic();
    }
    @Bean
    public RockMusic rockMusic(){
        return new RockMusic();
    }
    @Bean
    public MusicPlayer musicPlayer (){
        return new MusicPlayer(classicalMusic(), popMusic(), rockMusic());
    }

}
