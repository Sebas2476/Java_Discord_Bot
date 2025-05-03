package Events;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Greeting_Event extends ListenerAdapter {
    private String guildName;
    private String User_Name;
    private String Message;
    private String messageSent;

    //Here in this class you will create two classes, one will greet a user when they join a server (GuildMemberJoinEvent)
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        guildName = event.getGuild().getName();
        User_Name = event.getMember().getUser().getName();
        Message = "Welcome, " + User_Name + " to the " + guildName + "! " + " Hopefully you enjoy your stay :)";
            event.getGuild().getSystemChannel().sendMessage(Message).queue();
    }
    //the other class will serve as a way for the bot to respond to certain keywords (OnMessageRecievedEvent)
    public void onMessageReceived(MessageReceivedEvent event){
        messageSent = event.getMessage().getContentRaw();
        User_Name = event.getMember().getUser().getName();
        
        if (messageSent.equalsIgnoreCase("hello")){
            event.getChannel().sendMessage("Hi " + "@" + User_Name).queue();
        }
    }
    
    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event){
        guildName = event.getGuild().getName();
        User_Name = event.getUser().getName(); 
        Message = "Goodbye, " + User_Name + " Hope you enjoyed your stay at " + guildName + " :(";
        event.getGuild().getSystemChannel().sendMessage(Message).queue();
    }

}


