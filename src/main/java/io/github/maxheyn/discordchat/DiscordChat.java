package io.github.maxheyn.discordchat;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.io.IOException;

public class DiscordChat extends JavaPlugin {

    /** Called then the plugin is enabled. */
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        this.getServer().getPluginManager().registerEvents(new DiscordChatEvents(), this);
        try { ServerStart(); } catch (IOException e) { e.printStackTrace(); }
    }

    /** Called when the plugin is disabled. */
    @Override
    public void onDisable() {
        try { ServerStop(); } catch (IOException e) { e.printStackTrace(); }
    }

    /**
     * Slash Command handler
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("dcrl")) {
            reloadConfig();
            sender.sendMessage(ChatColor.BLUE + "The config file is reloaded.");
        }
        return false;
    }

    /**
     * Creates a new webhook message that gets sent to Discord, displaying that the
     * server has come online.
     * 
     * @throws IOException
     */
    private void ServerStart() throws IOException {
        if (!getConfigString("webhookUrl").equals(DiscordChatMetrics.defaultWebhookURL)) {
            DiscordWebhook webhook = new DiscordWebhook(getConfigString("webhookUrl"));
            webhook.addEmbed(new DiscordWebhook.EmbedObject()
                    .setTitle(getConfigString("serverStartMessage"))
                    .setColor(Color.GREEN));
            webhook.execute();
        } else {
            System.out.println(DiscordChatMetrics.defaultWebhookURLMessage);
        }
    }

    /**
     * Creates a new webhook message that gets sent to Discord, displaying that the
     * server has gone offline.
     * 
     * @throws IOException
     */
    private void ServerStop() throws IOException {
        if (!getConfigString("webhookUrl").equals(DiscordChatMetrics.defaultWebhookURL)) {
            DiscordWebhook webhook = new DiscordWebhook(getConfigString("webhookUrl"));
            webhook.addEmbed(new DiscordWebhook.EmbedObject()
                    .setTitle(getConfigString("serverStopMessage"))
                    .setColor(Color.RED));
            webhook.execute();
        } else {
            System.out.println(DiscordChatMetrics.defaultWebhookURLMessage);
        }
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
        return this.getConfig().getString(query);
    }
}