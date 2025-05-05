package Music_Package.lavaplayer;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {

    private static PlayerManager INSTANCE; 
    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;



    public PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);

        

    }

    public GuildMusicManager getMusicManager(Guild guild){
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return guildMusicManager;
        });

        }

        public void loadAndPlay(TextChannel channel, String trackURL){
            System.out.println("loading track: " + trackURL);
            final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());

            if(!channel.getGuild().getAudioManager().isConnected()) {
                var member = channel.getGuild().getMemberById(channel.getJDA().getSelfUser().getId());
                if(member != null) {
                    var voiceState = member.getVoiceState();
                    if (voiceState != null && voiceState.getChannel() != null) {
                        channel.getGuild().getAudioManager().openAudioConnection(voiceState.getChannel());
                        System.out.println("connecting to voice channel!");
                    }               
                 }
            }

            this.audioPlayerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler() {
                
            @Override
            public void trackLoaded(AudioTrack track) {
                System.out.println("Track loaded: " + track.getInfo().title);
                channel.sendMessage("Adding to queue **" + track.getInfo().title + "** by **" + track.getInfo().author + "**").queue();
                musicManager.scheduler.queue(track);

                System.out.println("current playing track: " + musicManager.audioPlayer.getPlayingTrack());
                System.out.println("current player volume: " + musicManager.audioPlayer.getVolume());
                System.out.println("Is audio manger connected: " + channel.getGuild().getAudioManager().isConnected());
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                System.out.println("Playlist loaded with " + playlist.getTracks().size() + " tracks");
                
                final List<AudioTrack> tracks = playlist.getTracks();
                if (!tracks.isEmpty()) {
                    channel.sendMessage("Adding to Queue **" + tracks.get(0).getInfo().title + "** (first track of playlist **" + playlist.getName() + "**)").queue();
                    musicManager.scheduler.queue(tracks.get(0));
                    
                    // Load next track as well
                    if (tracks.size() > 1) {
                        channel.sendMessage("Adding next track from playlist: **" + tracks.get(1).getInfo().title + "**").queue();
                        musicManager.scheduler.queue(tracks.get(1));
                   
                    }
                }
            }

                @Override
                public void noMatches(){
                    System.out.println("no matches found for: " + trackURL);
                    channel.sendMessage("nothing found by **" + trackURL + "**").queue();
                }

                @Override
                public void loadFailed(FriendlyException e) {

                }
                
            });
    }

        public static PlayerManager getInstance() {
            if (INSTANCE == null) {
                INSTANCE = new PlayerManager();
                        }
                return INSTANCE; 
        }
        
}

