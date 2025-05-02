package Events;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.requests.GatewayIntent;


public class DiscordBot {
    

    public static void main(String[] args)  throws Exception {
        Dotenv dotenv = Dotenv.load();
        String token = dotenv.get("BOT_TOKEN");

        // Create the JDA instance using the createDefault method
        JDA jda = JDABuilder.createDefault(token)
            .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
            .addEventListeners(new Greeting_Event())
            .addEventListeners(new Channel_Create_Event())
            .build();
    }
}

//can you see this comment? 
