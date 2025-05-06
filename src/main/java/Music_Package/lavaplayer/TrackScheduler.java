package Music_Package.lavaplayer;
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
        this.audioPlayer.addListener(this);
    }

    //CREATES QUEUE
    public void queue(AudioTrack track) {
        System.out.println("Attempting to queue track: " + track.getInfo().title);


        if(this.audioPlayer.getPlayingTrack() == null) {
            System.out.println("No Track currently playing, starting track directly");
            boolean success = this.audioPlayer.startTrack(track, false);
            System.out.println("Track started result: " + success);

        }

        else {
             System.out.println("Adding track to queue since player is already playing");
        this.queue.offer(track);
        System.out.println("Track added to queue. Queue size: " + this.queue.size());
    }
}


    public void nextTrack() {
        this.audioPlayer.startTrack(this.queue.poll(), false);
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track){
        System.out.println("Track Started: " + track.getInfo().title);
        System.out.println("Volume: " + player.getVolume());
        System.out.println("Player Paused: " + player.isPaused());
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        System.out.println("Track ended: " + track.getInfo().title + " with reason: " + endReason);
        if(endReason.mayStartNext){
            System.out.println("Starting next track");
            nextTrack();
        }
    }

    //CLEARS THE QUEUE 
    public void clearQueue() {
        this.queue.clear();
    }

    //gets the current size of the Queue
    public int getQueueSize() {
        return this.queue.size();
    }

    //removes a track at a specific index
    public boolean removeTrack(int index) {
        if (index < 0 || index >= queue.size()) {
            return false; 
        }
        AudioTrack[] tracks = queue.toArray(new AudioTrack[0]);
        AudioTrack trackToRemove = tracks[index];

        return queue.remove(trackToRemove);
    }

    //Shuffels the current queue 
    public void shuffleQueue() {
        AudioTrack[] tracks = queue.toArray(new AudioTrack[0]); 
        queue.clear();

        for (int i = 0; i < tracks.length; i++) {
            int randomIndex = (int) (Math.random() * (tracks.length - i) + i);
            AudioTrack temp = tracks[i];
            tracks[i] = tracks[randomIndex];
            tracks[randomIndex] = temp;

            queue.offer(tracks[i]);
         }
        
    }

}


//MAKE SURE TO REGISTER THIS TO THE BOT 