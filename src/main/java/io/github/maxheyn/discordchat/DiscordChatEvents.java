package io.github.maxheyn.discordchat;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.*;
import java.io.IOException;

public class DiscordChatEvents implements Listener {

    private FileConfiguration config;
    public DiscordChatEvents(FileConfiguration config) {
        this.config = config;
    }

    @EventHandler
    public void playerChats(AsyncPlayerChatEvent event) throws IOException {
        if (getConfigString("webhookUrl") != DiscordChatMetrics.defaultWebhookURL) {
            String message = event.getMessage();
            String player = event.getPlayer().getName();

            DiscordWebhook webhook = new DiscordWebhook(getConfigString("webhookUrl"));
            webhook.setAvatarUrl(getPlayerHelmIcon(player));
            webhook.setUsername(player);
            webhook.setContent(message);
            webhook.execute();
        } else {
            System.out.println("No webhook url provided!");
        }

    }

    @EventHandler
    public void playerJoins(PlayerJoinEvent event) throws IOException {
        if (getConfigString("webhookUrl") != DiscordChatMetrics.defaultWebhookURL) {
            String player = event.getPlayer().getName();

            DiscordWebhook webhook = new DiscordWebhook(getConfigString("webhookUrl"));
            webhook.addEmbed(new DiscordWebhook.EmbedObject()
                    .setTitle("**" + player + "** has come online.")
                    .setDescription("Say hello if you see them!")
                    .setThumbnail(getPlayerHelmIcon(player))
                    .setColor(Color.GREEN));
            webhook.execute();
        } else {
            System.out.println("No webhook url provided!");
        }

    }

    @EventHandler
    public void playerQuits(PlayerQuitEvent event) throws IOException {
        if (getConfigString("webhookUrl") != DiscordChatMetrics.defaultWebhookURL) {
            String player = event.getPlayer().getName();

            DiscordWebhook webhook = new DiscordWebhook(getConfigString("webhookUrl"));
            webhook.addEmbed(new DiscordWebhook.EmbedObject()
                    .setTitle("**" + player + "** has gone offline.")
                    .setDescription("See you next time!")
                    .setThumbnail(getPlayerHelmIcon(player))
                    .setColor(Color.RED));
            webhook.execute();
        } else {
            System.out.println("No webhook url provided!");
        }

    }

    private String getPlayerHelmIcon(String player) {
        return "https://cravatar.eu/helmhead/" + player + "/190.png";
    }

    private String getConfigString(String query) {
        return config.getString(query);
    }
}
