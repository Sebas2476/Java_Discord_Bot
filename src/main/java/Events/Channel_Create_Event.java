package Events;

import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

//make the bot say that a channel has been created 
public class Channel_Create_Event extends ListenerAdapter {
    public void onChannelCreate(ChannelCreateEvent event){

        //lets make a way for the bot to check if the user creates a text channel or not.

        if (event.getChannelType().isGuild() && event.getChannelType().isMessage()){
        event.getChannel().asTextChannel().sendMessage("@everyone A new channel " + event.getChannel().getName()  + " has been created, check it out!").queue(); //This will alert everyone when a channel gets created.
        }
    }

}