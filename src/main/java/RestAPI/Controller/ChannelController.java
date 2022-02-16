package RestAPI.Controller;

import RestAPI.WebServer;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;

public class ChannelController {
    public static Route getChannels = new Route() {
        @Override
        public Object handle(Request request, Response response) throws Exception {
            JsonArray channelArray = new JsonArray();
            TS3Api ts3Api = WebServer.getTs3Api();
            ts3Api.getChannelInfo(2);
            List<Channel> channels = ts3Api.getChannels();

            for (Channel channel : channels){
                channelArray.add(channelToJsonObject(channel));
            }

            return channelArray.toString();
        }
    };

    public static JsonObject channelToJsonObject(Channel channel){
        JsonObject obj = new JsonObject();

        obj.addProperty("channelId", channel.getId());
        obj.addProperty("neededSubscribePower", channel.getNeededSubscribePower());
        obj.addProperty("totalClients", channel.getTotalClients());
        obj.addProperty("totalClientsFamily", channel.getTotalClientsFamily());
        obj.addProperty("bannerGraphicsURL", channel.getBannerGraphicsUrl());
        obj.addProperty("isEmpty", channel.isEmpty());
        obj.addProperty("isEmptyFamily", channel.isFamilyEmpty());
        obj.addProperty("isDefault", channel.isDefault());
        obj.addProperty("isPermanent", channel.isPermanent());
        obj.addProperty("isSemiPermanent", channel.isSemiPermanent());
        obj.addProperty("codecQuality", channel.getCodecQuality());
        obj.addProperty("iconId", channel.getIconId());
        obj.addProperty("maxClients", channel.getMaxClients());
        obj.addProperty("maxFamilyClients", channel.getMaxFamilyClients());
        obj.addProperty("name", channel.getName());
        obj.addProperty("neededTalkPower", channel.getNeededTalkPower());
        obj.addProperty("order", channel.getOrder());
        obj.addProperty("parentChannelId", channel.getParentChannelId());
        obj.addProperty("secondsEmpty", channel.getSecondsEmpty());
        obj.addProperty("topic", channel.getTopic());
        obj.addProperty("hasPassword", channel.hasPassword());

        return obj;
    }
}












