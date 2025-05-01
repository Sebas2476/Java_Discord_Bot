package Events;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Greeting_Event extends ListenerAdapter {

    public void onMessageReceived(MessageReceivedEvent event){
        String messageSent = event.getMessage().getContentRaw();
        String User_Name = event.getMember().getUser().getName();

        if (messageSent.equalsIgnoreCase("hello")){
            event.getChannel().sendMessage("Hi " + User_Name + "!").queue();
        }
    }
}
