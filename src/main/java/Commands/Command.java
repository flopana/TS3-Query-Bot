package Commands;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

public interface Command {
    void execute(TextMessageEvent e, TS3Api ts3Api);
}
