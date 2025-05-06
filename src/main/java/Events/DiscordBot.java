package Events;
import Command_Manager.Command_Manager;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.*;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;


public class DiscordBot {
    public static void main(String[] args)  throws Exception {
        Dotenv dotenv = Dotenv.load();
        String token = dotenv.get("BOT_TOKEN");

        // Create the JDA instance using the createDefault method
        JDA jda = JDABuilder.createDefault(token)
        .enableIntents(
        GatewayIntent.DIRECT_MESSAGES, 
        GatewayIntent.GUILD_MESSAGES, 
        GatewayIntent.GUILD_MESSAGE_REACTIONS, 
        GatewayIntent.GUILD_VOICE_STATES, 
        GatewayIntent.MESSAGE_CONTENT, 
        GatewayIntent.GUILD_MEMBERS)//This gives the bot Perms on what it's able to do, from joining the voice channel, to sending messages to user's to even giving announcments. 
        .setChunkingFilter(ChunkingFilter.ALL) 
        .setActivity(Activity.playing("Discord")) //We can display what the bot is currently playing
        .setStatus(OnlineStatus.ONLINE) //Status of our bot (can be set offline/online)
        .setMemberCachePolicy(MemberCachePolicy.ALL) //This will keep track of the user's info and when a user joins a server
        .addEventListeners(new Greeting_Event())
        .addEventListeners(new Channel_Create_Event())
        .addEventListeners(new Command_Manager())   
        .enableCache(CacheFlag.VOICE_STATE)
        .build();

        jda.awaitReady();

        System.out.println("Bot is ready!");
    }

}


