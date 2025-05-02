package Events;
import Events.Greeting_Event;
import Events.Channel_Create_Event;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.requests.GatewayIntent;


public class DiscordBot {

    public static void main(String[] args)  throws Exception {

        // Create the JDA instance using the createDefault method
        JDA jda = JDABuilder.createDefault("MTM2Njg2NTM1NDYxMzA2Nzg1Nw.Gek0VF.FTrSIHCxiDSXu-WIL3Fo8WA3awCNta5TL2JDpQ")
            .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
            .addEventListeners(new Greeting_Event())
            .addEventListeners(new Channel_Create_Event())
            .build();
    }
}

//can you see this comment? 
