package Events;
import Command_Manager.Command_Manager;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.*;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class BotManager {
    private static BotManager instance; 
    private JDA jda;
    private final String token;
    private Dotenv dotenv; 
    private Activity currenActivity;
    private OnlineStatus currentStatus;

    // java bot Constructor 
    private BotManager() {
        this.dotenv = Dotenv.load();
        this.token = dotenv.get("BOT_TOKEN");
        this.currenActivity = Activity.playing("Discord :)");
        this.currentStatus = OnlineStatus.ONLINE; 
    }

    public static BotManager getInstance() {
       if (instance == null) {
           instance = new BotManager();
         }
         return instance; 
    }

    public void intialize() throws Exception  {
       jda = JDABuilder.createDefault(token)
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
       .enableCache(CacheFlag.VOICE_STATE)
       .build();

       
       jda.addEventListener(new Greeting_Event());
       jda.addEventListener(new Channel_Create_Event());
       jda.addEventListener(new Command_Manager());
       jda.awaitReady();
       System.out.println("Bot is ready!");
    }

    //Lets intialize the Getter method. 
    public JDA getJDA() {
       return jda;
    }

    public Activity getCurrentActivity() {
       return currenActivity; 
    }

    public OnlineStatus getCurrentStatus() {
       return currentStatus;
    }

    //Now lets get the SETTERS method
    public void setActivity(Activity activity){
       this.currenActivity = activity; 
       if(jda != null) {
           jda.getPresence().setActivity(activity);
       }
    }

    public void setPlayingActivity(String name) {
       setActivity(Activity.playing(name));
    }

    public void setListeningActivity(String name){
       setActivity(Activity.listening(name));
    }

}
