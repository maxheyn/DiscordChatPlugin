package io.github.maxheyn.discordchat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.awt.*;
import java.util.List;
import java.io.IOException;
import java.util.Random;

public class DiscordChatEvents implements Listener {

    private final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("DiscordChat");
    private FileConfiguration config = plugin.getConfig();

    /**
     * Creates a new webhook message that gets sent to Discord, displaying the
     * contents of player chat messages alongside their Minecraft username.
     * 
     * @param event fired whenever a player quits/logs out of the server
     * @throws IOException
     */
    @EventHandler
    public void playerChats(AsyncPlayerChatEvent event) throws IOException {
        if (!getConfigString("webhookUrl").equals(DiscordChatMetrics.defaultWebhookURL)) {
            String message = event.getMessage();
            String player = event.getPlayer().getName();

            DiscordWebhook webhook = new DiscordWebhook(getConfigString("webhookUrl"));
            webhook.setAvatarUrl(getPlayerHelmIcon(player, getConfigBool("playerHeadIsometric")));
            webhook.setUsername(player);
            webhook.setContent(message);
            webhook.execute();
        } else {
            System.out.println(DiscordChatMetrics.defaultWebhookURLMessage);
        }

    }

    /**
     * Creates a new webhook message that gets sent to Discord, displaying which player
     * has joined/logged on to the server.
     * 
     * @param event fired whenever a player joins/logs onto the server
     * @throws IOException
     */
    @EventHandler
    public void playerJoins(PlayerJoinEvent event) throws IOException {
        if (!getConfigString("webhookUrl").equals(DiscordChatMetrics.defaultWebhookURL)) {
            String player = event.getPlayer().getName();

            DiscordWebhook webhook = new DiscordWebhook(getConfigString("webhookUrl"));
            webhook.addEmbed(new DiscordWebhook.EmbedObject()
                    .setTitle("**" + player + "** has come online.")
                    .setDescription(getConfigStringList("playerLoginToasts").get(new Random().nextInt(getConfigStringList("playerLoginToasts").size())))
                    .setThumbnail(getPlayerHelmIcon(player, getConfigBool("playerHeadIsometric")))
                    .setColor(Color.GREEN));
            webhook.execute();
        } else {
            System.out.println(DiscordChatMetrics.defaultWebhookURLMessage);
        }
    }

    /**
     * Creates a new webhook message that gets sent to Discord, displaying which player
     * has left/disconnected from the server.
     * 
     * @param event fired whenever a player quits/logs out of the server
     * @throws IOException
     */
    @EventHandler
    public void playerQuits(PlayerQuitEvent event) throws IOException {
        if (!getConfigString("webhookUrl").equals(DiscordChatMetrics.defaultWebhookURL)) {
            String player = event.getPlayer().getName();

            DiscordWebhook webhook = new DiscordWebhook(getConfigString("webhookUrl"));
            webhook.addEmbed(new DiscordWebhook.EmbedObject()
                    .setTitle("**" + player + "** has gone offline.")
                    .setDescription(getConfigStringList("playerLogoutToasts").get(new Random().nextInt(getConfigStringList("playerLogoutToasts").size())))
                    .setThumbnail(getPlayerHelmIcon(player, getConfigBool("playerHeadIsometric")))
                    .setColor(Color.RED));
            webhook.execute();
        } else {
            System.out.println(DiscordChatMetrics.defaultWebhookURLMessage);
        }
    }

    /** 
     * Helper function to get an image of a player's head.
     * 
     * @param player The username of the player as a String
     * @return the URL for a player helmhead icon, an isometric view 
     *         of the player's current skin's head. Example: 
     *         https://cravatar.eu/helmhead/kingbdogz/128.png 
     */
    private String getPlayerHelmIcon(String player, boolean isometric) {
        if (isometric) return "https://cravatar.eu/helmhead/" + player + "/128.png";
        else return "https://cravatar.eu/helmavatar/" + player + "/128.png";
    }

    /** 
     * Helper function to retrieve values from the config file 
     * given a query.
     *  
     * @param query The key/query you are trying to access the 
     *         value of in the config file
     * @return the value that corresponds with the given query 
     *         from the config file. Example: getConfigString("webhookUrl");
     *         returns the value of webhookUrl
     */
    private String getConfigString(String query) {
        return config.getString(query);
    }

    /** 
     * Helper function to retrieve values from the config file 
     * given a query.
     *  
     * @param query The key/query you are trying to access the 
     *         value of in the config file
     * @return the values that correspond with the given query 
     *         from the config file. Example: getConfigStringList("playerLogoutToasts");
     *         returns a list of messages
     */
    private List<String> getConfigStringList(String query) {
        return config.getStringList(query);
    }

        /** 
     * Helper function to retrieve values from the config file 
     * given a query.
     *  
     * @param query The key/query you are trying to access the
     *         value of in the config file
     * @return the value that corresponds with the given query
     *         from the config file. Example: getConfigBool("webhookUrl");
     *         returns the value of webhookUrl
     */
    private boolean getConfigBool(String query) {
        return config.getBoolean(query);
    }
}
