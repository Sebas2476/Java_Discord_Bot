package Music_Package.lavaplayer;
import com.sedmelluq.discord.lavaplayer.player.*;

public class GuildMusicManager {
    public final AudioPlayer audioPlayer; 
    public final TrackScheduler scheduler; 
    private final AudioPlayerSendHandler sendHandler; 


    public GuildMusicManager(AudioPlayerManager manager) { //Constructor 
        this.audioPlayer = manager.createPlayer();

        //set volume 
        this.audioPlayer.setVolume(70);
        this.scheduler = new TrackScheduler(this.audioPlayer);
        this.audioPlayer.addListener(this.scheduler);
        this.sendHandler = new AudioPlayerSendHandler(this.audioPlayer);
        System.out.println("GuildMusicManager created with volume level: " + this.audioPlayer.getVolume());
    }


    public AudioPlayerSendHandler getSendHandler() {
        return this.sendHandler;
    }

    

}


//MAKE SURE TO REGISTER THIS TO THE BOT 