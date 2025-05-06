package Music_Package.lavaplayer;
import com.sedmelluq.discord.lavaplayer.format.StandardAudioDataFormats;
import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import java.nio.*;
import net.dv8tion.jda.api.audio.*;

public class AudioPlayerSendHandler implements AudioSendHandler {
    private final AudioPlayer audioPlayer; 
    private final ByteBuffer buffer; 
    private MutableAudioFrame frame;
    private int logCounter = 0; //counter tolimit loging freq

    public AudioPlayerSendHandler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.buffer = ByteBuffer.allocate(StandardAudioDataFormats.DISCORD_OPUS.maximumChunkSize());
        this.frame = new MutableAudioFrame();
        this.frame.setBuffer(buffer);

        audioPlayer.setVolume((70));
        System.out.println("AudioPlayerSendHandler initialized with volume: " + audioPlayer.getVolume());
       
    }

    @Override
    public boolean canProvide() {
        buffer.clear();
        boolean canProvide = audioPlayer.provide(frame);

        if (!canProvide) {
            if (logCounter >= 100) {
            System.out.println("cannot provide audio data");
            logCounter = 0;
            }
        }
        if (canProvide) {
            buffer.flip();
        }
        return canProvide;
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        return buffer;

    }

    @Override
    public boolean isOpus() {
        return true;
    }

}


//MAKE SURE TO REGISTER THIS TO THE BOT 