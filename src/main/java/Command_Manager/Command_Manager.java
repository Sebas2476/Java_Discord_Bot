package Command_Manager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.*;
import net.dv8tion.jda.api.interactions.commands.build.*;
import net.dv8tion.jda.api.managers.AudioManager;
import java.util.*;
import org.jetbrains.annotations.NotNull;
import Music_Package.lavaplayer.*;



public class Command_Manager extends ListenerAdapter{

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event)  {
        String command = event.getName(); 
        String userTag = event.getUser().getAsTag();
        Member member = event.getMember(); //gets the member who used the command
        Guild guild = event.getGuild();
        GuildVoiceState memberVoiceState = member.getVoiceState();
        AudioManager audioManager = guild.getAudioManager();
        

        if (member == null || guild == null) {
            event.reply("Error: command must be used in a server." ).setEphemeral(true).queue();
            return;
        }

        //Create a Welcome command that greets the user **************************************************************************************
        if(command.equals("welcome")) { //if user types welcome 
            event.reply("Hello! **" + userTag + "** Nice to meet you :] ").setEphemeral(true).queue(); //the bot will respond back.
        }  //Create a Welcome command that greets the user ENDS **************************************************

            //JOIN FEATURE FUNCTIONALITY **************************************************
            if (command.equals("join")) { //join command
                event.deferReply().queue();
            if (member == null || member.getVoiceState() == null || !member.getVoiceState().inAudioChannel()) { //if no members exist OR the member in voice channel is null OR if no member is in the voice channel display the first event msg
                event.getHook().sendMessage("In order for me to join any voice channel, User must be in the channel First").queue();
                return; 
            }
            else if(memberVoiceState.getChannel() == null) { //if the channel is non existent then bot will not be able to find one. 
                event.getHook().sendMessage("Unable to find voice channel :( ").queue();
                return; 
            }
            else {
            audioManager.openAudioConnection(memberVoiceState.getChannel()); //connects to voice channel 
            event.getHook().sendMessage("Joined voice Channel: " + memberVoiceState.getChannel().getName()).queue();
            }
        }  //JOIN FEATURE FUNCTIONALITY ENDS **************************************************


            //LEAVE FEATURE FUNCTIONAILTY **************************************
            if (command.equals("leave")){ //leave command
                event.deferReply().queue();
            if (audioManager.isConnected()) { //if bot is connected to voice channel
            audioManager.closeAudioConnection(); //bot leaves voice channel 
            event.getHook().sendMessage("Leaving Voice Channel:" + memberVoiceState.getChannel().getName()).queue();
            }
        }   //LEAVE FEATURE FUNCTIONAILTY End **************************************
        
        
        //HELP FUNCTIONAILTY **********************************************************
        if (command.equals("help")){ //help command (provides instructions on what each command does)
            event.reply("**/Welcome**:  Greets the user\n" +
                        "**/Roles**:  Provides a list of the roles here on the server\n." + //work on this line
                        "**clearC**: Clears chat history and however amount of text you want to remove\n" + //work on this line
                        "**/Join**:  Makes the bot join the VC (voiceChannel) you are currently in (must be in the voice channel first in order for the bot to join)\n" +
                        "**/Leave**:  Makes the bot leave the VC (voiceChannel)\n").setEphemeral(true).queue();
        } //HELP FUNCTIONAILTY ENDS **********************************************************


        //ROLES FUNCTION BODY *******************************************************************
        if (command.equals("roles")) {
            event.deferReply().setEphemeral(true).queue();
            String response = "";

            for (Role role : event.getGuild().getRoles()){ //for each roles that is in a server
                response += role.getAsMention() + "\n";  //displays each role. 
            }
            event.getHook().sendMessage(response).queue();
        } 
        //ROLES FUNCTION BODY *******************************************************************

        //CLEAR CHAT FUNCTIONAILTY OR PURGE ******************************************************
        if (command.equals("purge")) { //clears
            OptionMapping amountOption = event.getOption("amount");
            if (amountOption == null) {
                event.reply("Please input however many messages you want to delete. ").setEphemeral(true).queue();
                return; 
            }
            int amount = amountOption.getAsInt();
            //ok so now check the amount within the discrod's limit 
            if(amount < 1 || amount > 100) {
                event.reply("can only delete in range from 1-100 msgs at once").setEphemeral(true).queue();
                return; 
            }

            //defer the reply as the removal of text will take the bot some time 
            event.deferReply(true).queue();
            //this will get the msgs and then delete them
            event.getChannel().getHistory().retrievePast(amount).queue(messages -> { 
                if (messages.isEmpty()) {
                    event.getHook().sendMessage("No messages found to be delted!").setEphemeral(true).queue();
                    return;
        }
        //delete the messages and send the confirmation that you did 
        event.getChannel().purgeMessages(messages);
        event.getHook().sendMessage("messages deleted" + messages.size() + "messages").setEphemeral(true).queue();
        
    }); //CLEAR CHAT FUNCTIONAILTY OR PURGE ENDS HERE ***************************************************************************
}

        //play Functionailty ***********************************************************
        if (command.equals("play")) {
            OptionMapping queryOption = event.getOption("query");
            if (queryOption == null) {
                event.reply("Please provide a URL or search query!").setEphemeral(true).queue();
                return;
            }
            
            //checking to see if user is in voice channel 
            if (member == null || member.getVoiceState() == null || member.getVoiceState().getChannel() == null) { //Apply the same condtionals as the join command before the bot enters the voice channel. 
                event.reply("you must be in the VC in order for me to play music >:[ ").setEphemeral(true).queue();
                return;
            }
                event.deferReply().queue();
                String trackUrl = queryOption.getAsString();  

                try{
                if(!audioManager.isConnected()) {
                    System.out.println("connecting to voice channel" + member.getVoiceState().getChannel().getName());
                    audioManager.openAudioConnection(member.getVoiceState().getChannel());
                    Thread.sleep(2000);
                    System.out.println("Voice Connection State: " + audioManager.getConnectionStatus());
                }


                //get the music manager and preform diagansitcs
                GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);
                System.out.println("Audio player Volume: " + musicManager.audioPlayer.getVolume());
                System.out.println("Is audio player playing: " + (musicManager.audioPlayer.getPlayingTrack() != null));

                event.getHook().sendMessage("Loading Track...").queue();
                PlayerManager.getInstance().loadAndPlay(event.getChannel().asTextChannel(), trackUrl);

        } catch (InterruptedException e) {
            e.printStackTrace();
            event.getHook().sendMessage("An error occurred while connecting to voice.").queue();
        }
    } //play Functionailty ENDSSSSSSSSSSS ******************************************************************************************


        // SKIP COMMAND ***********************************************************************************
    else if (command.equals("skip") || command.equals("s")) {
    if (member == null || member.getVoiceState() == null || member.getVoiceState().getChannel() == null) {
        event.reply("You need to be in a voice channel to skip tracks!").queue();
        return;
    }
    
    GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);
    musicManager.scheduler.nextTrack();
    event.reply("Skipped to the next track").queue();
} // SKIP COMMAND ENDS ***********************************************************************************


      //Rolling Dice Function *******************************************************
     
     
      if(command.equals("roll")) {
        OptionMapping diceOption = event.getOption("dice");

        if (diceOption == null){
            event.reply("Please choose a dice of your choice :] ").setEphemeral(true).queue();
        }
        String diceInput = diceOption.getAsString().toLowerCase();
        
        //if user add any of the dice options, adds a 1 as prefix
        if(diceInput.contains("d")) {
            
        }

        //check if format matches the pattern "1d6", 1d20, ect.
        if(!diceInput.contains("d")){
            
        }

        //Parse the dices
        String[] parts = diceInput.split("d");
        int sides = Integer.parseInt(parts[1]);
        int diceAmount = Integer.parseInt(parts[0]);

        if(sides < 1 || sides > 20){
            event.reply("please pick a dice from d1-d20").setEphemeral(true).queue();
        }

        int roll = (int)(Math.random() * sides) + 1;

        //build the result image
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append("**Results ** " + diceInput + "\n");
        resultBuilder.append("**You Rolled a ** " + roll + "\n");
        event.reply(resultBuilder.toString()).queue();
        }//Rolling Dice Function ENDS *******************************************************
    
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
        commandData.add(Commands.slash("purge", "Clears text chat")
        .addOption(OptionType.INTEGER, "amount", "input the num of the amount of messages you want to delete"));
        commandData.add(Commands.slash("play", "Plays music for the user (must provide name of the song or URl)")
        .addOption(OptionType.STRING, "query", "the song URL or search query", true));
        commandData.add(Commands.slash("skip", "Skips the song that is currently being played."));
        
        OptionData diceOption = new OptionData(OptionType.STRING, "dice", "Formate (1d6, 2d20, etc)", false);
        diceOption.addChoice("d4", "1d4");
        diceOption.addChoice("d6", "1d6");
        diceOption.addChoice("d8", "1d8");
        diceOption.addChoice("d10", "1d10");
        diceOption.addChoice("d12", "1d12");
        diceOption.addChoice("d20", "1d20");
        diceOption.addChoice("2d20", "2d20");
        diceOption.addChoice("3d6", "3d6");
        diceOption.addChoice("4d6", "4d6");

        commandData.add(Commands.slash("roll", "Roll dice (supports d1-d20)").addOptions(diceOption));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }
    

    //Global Command works on all guilds -- can run unlimited amounts of commands, BUT takes up to an hour to update :(  

}
    
