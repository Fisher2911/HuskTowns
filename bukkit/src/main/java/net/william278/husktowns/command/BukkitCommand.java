/*
 * This file is part of HuskTowns, licensed under the Apache License 2.0.
 *
 *  Copyright (c) William278 <will27528@gmail.com>
 *  Copyright (c) contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package net.william278.husktowns.command;

import me.lucko.commodore.CommodoreProvider;
import net.william278.husktowns.BukkitHuskTowns;
import net.william278.husktowns.user.BukkitUser;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BukkitCommand implements CommandExecutor, TabCompleter {

    private final BukkitHuskTowns plugin;
    private final Command command;

    public BukkitCommand(@NotNull Command command, @NotNull BukkitHuskTowns plugin) {
        this.command = command;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command,
                             @NotNull String label, @NotNull String[] args) {
        this.command.execute(sender instanceof Player player ? BukkitUser.adapt(player) : plugin.getConsole(), args);
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command,
                                      @NotNull String alias, @NotNull String[] args) {
        return this.command.suggest(sender instanceof Player player ? BukkitUser.adapt(player) : plugin.getConsole(), args);
    }

    public void register() {
        // Register with bukkit
        final PluginCommand pluginCommand = Objects.requireNonNull(plugin.getCommand(command.getName()));
        pluginCommand.setExecutor(this);
        pluginCommand.setTabCompleter(this);

        // Register commodore TAB completion
        if (CommodoreProvider.isSupported() && plugin.getSettings().doBrigadierTabCompletion()) {
            BrigadierUtil.registerCommodore(plugin, pluginCommand, command);
        }

        // Register permissions
        registerPermissions(command, plugin);
    }

    protected static void registerPermissions(@NotNull Command command, @NotNull BukkitHuskTowns plugin) {
        // Register permissions
        final PluginManager manager = plugin.getServer().getPluginManager();
        command.getChildren()
                .stream().map(child -> new Permission(child.getPermission(), child.getUsage(),
                        child.isOperatorCommand() ? PermissionDefault.OP : PermissionDefault.TRUE))
                .forEach(manager::addPermission);
        manager.addPermission(new Permission(command.getPermission(), "/" + command.getName(),
                command.isOperatorCommand() ? PermissionDefault.OP : PermissionDefault.TRUE));

        // Register master permission
        final Map<String, Boolean> childNodes = new HashMap<>();
        command.getChildren().forEach(child -> childNodes.put(child.getPermission(), true));
        manager.addPermission(new Permission(command.getPermission() + ".*", command.getUsage(),
                PermissionDefault.FALSE, childNodes));
    }
}
