package Commands;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

import java.security.SecureRandom;

public class GenerateCryptoSafeRandomStringCommand implements ICommand{
    @Override
    public void execute(TextMessageEvent e, TS3Api ts3Api) {
        final char[] CHARS_ARRAY = {
                'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
                'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
                '1','2','3','4','5','5','6','7','8','9','0'
        };
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();

        int length = Integer.parseInt(e.getMessage().substring(26));

        if (length > 1024){
            ts3Api.sendPrivateMessage(e.getInvokerId(), "Max Length allowed is 1024");
            return;
        }

        for (int i = 0; i < length; i++) {
            stringBuilder.append(CHARS_ARRAY[secureRandom.nextInt(CHARS_ARRAY.length)]);
        }

        ts3Api.sendPrivateMessage(e.getInvokerId(), stringBuilder.toString());
    }
}
