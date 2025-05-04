package Music_Package;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.player.event.*;
import com.sedmelluq.discord.lavaplayer.track.*;


public class TrackScheduler extends AudioEventAdapter {

    public final AudioPlayer audioPlayer;
    public final BlockingQueue<AudioTrack> queue;

    public TrackScheduler(AudioPlayer audioPlayer){
        this.audioPlayer = audioPlayer;
        this.queue = new LinkedBlockingDeque<>();
    }

    public void queue(AudioTrack track) {
        if(!this.audioPlayer.startTrack(track, true)){
            this.queue.offer(track);
        }
    }

    public void nextTrack() {
        this.audioPlayer.startTrack(this.queue.poll(), false);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if(endReason.mayStartNext){
            nextTrack();
        }
    }

}
