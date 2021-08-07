package Commands;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

public interface ICommand {
    void execute(TextMessageEvent e, TS3Api ts3Api);
}
