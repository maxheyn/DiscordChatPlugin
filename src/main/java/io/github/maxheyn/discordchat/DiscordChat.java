package io.github.maxheyn.discordchat;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.io.IOException;

public class DiscordChat extends JavaPlugin {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(new DiscordChatEvents(this.getConfig()), this);
        try { ServerStart(); } catch (IOException e) { e.printStackTrace(); }
    }

    @Override
    public void onDisable() {
        try { ServerStop(); } catch (IOException e) { e.printStackTrace(); }
    }

    private void ServerStart() throws IOException {
        if (getConfigString("webhookUrl") != DiscordChatMetrics.defaultWebhookURL) {
            DiscordWebhook webhook = new DiscordWebhook(getConfigString("webhookUrl"));
            webhook.addEmbed(new DiscordWebhook.EmbedObject()
                    .setTitle("T O A D C R A F T Server is now online!")
                    .setColor(Color.GREEN));
            webhook.execute();
        } else {
            System.out.println("No webhook url provided!");
        }
    }

    private void ServerStop() throws IOException {
        if (getConfigString("webhookUrl") != DiscordChatMetrics.defaultWebhookURL) {
            DiscordWebhook webhook = new DiscordWebhook(getConfigString("webhookUrl"));
            webhook.addEmbed(new DiscordWebhook.EmbedObject()
                    .setTitle("T O A D C R A F T Server has gone offline.")
                    .setColor(Color.RED));
            webhook.execute();
        } else {
            System.out.println("No webhook url provided!");
        }
    }

    private String getConfigString(String query) {
        return this.getConfig().getString(query);
    }

}