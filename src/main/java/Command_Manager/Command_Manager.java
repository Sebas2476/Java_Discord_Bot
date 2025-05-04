package Command_Manager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.*;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.*;
import net.dv8tion.jda.api.managers.AudioManager;
import java.util.*;
import org.jetbrains.annotations.NotNull; 

public class Command_Manager extends ListenerAdapter{

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event)  {
        String command = event.getName(); 
        String userTag = event.getUser().getAsTag();
        Member member = event.getMember(); //gets the member who used the command
        GuildVoiceState memberVoiceState = member.getVoiceState(); //get's the voice channel 
        Guild guild = event.getGuild();
        AudioManager audioManager = guild.getAudioManager();
        

        if(command.equals("welcome")) { //welcome command
            event.reply("Hello! **" + userTag + "** you have summoned me :D").setEphemeral(true).queue();
        }

            //JOIN FEATURE FUNCTIONALITY 
            if (command.equals("join")) { //join command
                event.deferReply().queue();
            
            if (member == null || member.getVoiceState() == null || !member.getVoiceState().inAudioChannel()) { //if no members exist OR the member in voice channel is null OR if no member is in the voice channel display the first event msg
                event.getHook().sendMessage("In order for me to join any voice channel, User must be in the channel First").queue();
                return; 
            }
            
            else if(memberVoiceState.getChannel() == null) { //if channel is non existent display this msg 
                event.getHook().sendMessage("Unable to find voice channel :( ").queue();
                return; 
            }
            else {
            audioManager.openAudioConnection(memberVoiceState.getChannel()); //connects to voice channel 
            event.getHook().sendMessage("Joined voice Channel: " + memberVoiceState.getChannel().getName()).queue();
            }
        }
            //LEAVE FEATURE FUNCTIONAILTY
            if (command.equals("leave")){ //leave command
                event.deferReply().queue();
            if (audioManager.isConnected()) {
            audioManager.closeAudioConnection();
            event.getHook().sendMessage("Leaving Voice Channel:" + memberVoiceState.getChannel().getName()).queue();
            }
        }
        
        
        //HELP FUNCTIONAILTY
        if (command.equals("help")){ //help command (provides instructions on what each command does)
            event.reply("**/Welcome**:  Greets the user\n" +
                        "**/Roles**:  Provides a list of the roles here on the server\n." + //work on this line
                        "**clearC**: Clears chat history and however amount of text you want to remove\n" + //work on this line
                        "**/Join**:  Makes the bot join the VC (voiceChannel) you are currently in (must be in the voice channel first in order for the bot to join)\n" +
                        "**/Leave**:  Makes the bot leave the VC (voiceChannel)\n").setEphemeral(true).queue();
        }

        //ROLES FUNCTIONAILTY 
        if (command.equals("roles")) {
            event.deferReply().setEphemeral(true).queue();
            String response = "";
            for (Role role : event.getGuild().getRoles()){
                response += role.getAsMention() + "\n";
            }
            event.getHook().sendMessage(response).queue();
        }

        if (command.equals("ClearC")) {
            event.deferReply().setEphemeral(true).queue();
            String response = "";
            for (Role role : event.getGuild().getRoles()){
                response += role.getAsMention() + "\n";
            }
            event.getHook().sendMessage(response).queue();
        }
    }


    //Guild Command --instantly updated (max of commands can only be up to 100 :( )
    
    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("welcome", "Get Welcomed by the bot"));
        commandData.add(Commands.slash("join", "Joins the Voice Channel that you are in (must be in the VC)"));
        commandData.add(Commands.slash("leave", "Makes the bot Leave when you want it to"));
        commandData.add(Commands.slash("help", "Provides a list of what each command does"));
        commandData.add(Commands.slash("roles", "Provides a list of each Role currently in the server"));
        commandData.add(Commands.slash("clearC", "Clears text chat"));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

    public void onGuildJoin(GuildJoinEvent event){

    }

    //Global Command works on all guilds -- can run unlimited amounts of commands, BUT takes up to an hour to update :(  

}
    
