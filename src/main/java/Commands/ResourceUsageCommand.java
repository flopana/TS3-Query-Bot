package Commands;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

public class ResourceUsageCommand implements ICommand{
    @Override
    public void execute(TextMessageEvent e, TS3Api ts3Api) {
       StringBuilder message = new StringBuilder();
       double ram_usage = (double)(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1024/1024;

       message.append("Resource Statistics:").append("\n\n");
       message.append("RAM: ").append(String.format("%.2f", ram_usage)).append(" MiB").append("\n");
       message.append("Threads: ").append(Thread.activeCount()).append("\n");

       ts3Api.sendPrivateMessage(e.getInvokerId(), message.toString());
    }
}
