/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Table
 *  net.minecraft.server.v1_7_R4.ChatSerializer
 *  net.minecraft.server.v1_7_R4.IChatBaseComponent
 *  org.apache.commons.lang.StringEscapeUtils
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.scoreboard.Scoreboard
 *  org.bukkit.scoreboard.Team
 *  org.spigotmc.ProtocolInjector
 *  org.spigotmc.ProtocolInjector$PacketTabHeader
 */
package me.vertises.aztec.tablist;

import com.google.common.collect.Table;
import java.util.Arrays;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;
import me.vertises.aztec.tablist.ClientVersion;
import me.vertises.aztec.tablist.TablistEntrySupplier;
import me.vertises.aztec.tablist.TablistManager;
import me.vertises.aztec.tablist.reflection.Reflection;
import me.vertises.aztec.tablist.reflection.ReflectionConstants;
import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.spigotmc.ProtocolInjector;

public class Tablist {
    public static final Object[] GAME_PROFILES;
    public static final String[] TAB_NAMES;
    public static String[] BLANK_SKIN;
    public final ClientVersion version;
    public final Player player;
    public boolean initiated;

    static {
        BLANK_SKIN = new String[]{"eyJ0aW1lc3RhbXAiOjE0MTEyNjg3OTI3NjUsInByb2ZpbGVJZCI6IjNmYmVjN2RkMGE1ZjQwYmY5ZDExODg1YTU0NTA3MTEyIiwicHJvZmlsZU5hbWUiOiJsYXN0X3VzZXJuYW1lIiwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzg0N2I1Mjc5OTg0NjUxNTRhZDZjMjM4YTFlM2MyZGQzZTMyOTY1MzUyZTNhNjRmMzZlMTZhOTQwNWFiOCJ9fX0=", "u8sG8tlbmiekrfAdQjy4nXIcCfNdnUZzXSx9BE1X5K27NiUvE1dDNIeBBSPdZzQG1kHGijuokuHPdNi/KXHZkQM7OJ4aCu5JiUoOY28uz3wZhW4D+KG3dH4ei5ww2KwvjcqVL7LFKfr/ONU5Hvi7MIIty1eKpoGDYpWj3WjnbN4ye5Zo88I2ZEkP1wBw2eDDN4P3YEDYTumQndcbXFPuRRTntoGdZq3N5EBKfDZxlw4L3pgkcSLU5rWkd5UH4ZUOHAP/VaJ04mpFLsFXzzdU4xNZ5fthCwxwVBNLtHRWO26k/qcVBzvEXtKGFJmxfLGCzXScET/OjUBak/JEkkRG2m+kpmBMgFRNtjyZgQ1w08U6HHnLTiAiio3JswPlW5v56pGWRHQT5XWSkfnrXDalxtSmPnB5LmacpIImKgL8V9wLnWvBzI7SHjlyQbbgd+kUOkLlu7+717ySDEJwsFJekfuR6N/rpcYgNZYrxDwe4w57uDPlwNL6cJPfNUHV7WEbIU1pMgxsxaXe8WSvV87qLsR7H06xocl2C0JFfe2jZR4Zh3k9xzEnfCeFKBgGb4lrOWBu1eDWYgtKV67M2Y+B3W5pjuAjwAxn0waODtEn/3jKPbc/sxbPvljUCw65X+ok0UUN1eOwXV5l2EGzn05t3Yhwq19/GxARg63ISGE8CKw="};
        GAME_PROFILES = new Object[80];
        TAB_NAMES = new String[80];
        int i = 0;
        while (i < 80) {
            int x = i % 4;
            int y = i / 4;
            String name = String.valueOf(x > 9 ? new StringBuilder("\u00a7").append(String.valueOf(x).toCharArray()[0]).append("\u00a7").append(String.valueOf(x).toCharArray()[1]).toString() : new StringBuilder("\u00a70\u00a7").append(x).toString()) + (y > 9 ? new StringBuilder("\u00a7").append(String.valueOf(y).toCharArray()[0]).append("\u00a7").append(String.valueOf(y).toCharArray()[1]).toString() : new StringBuilder("\u00a70\u00a7").append(String.valueOf(y).toCharArray()[0]).toString());
            UUID id = UUID.randomUUID();
            Object profile = ReflectionConstants.GAME_PROFILE_CONSTRUCTOR.invoke(id, name);
            Tablist.TAB_NAMES[i] = name;
            Tablist.GAME_PROFILES[i] = profile;
            ++i;
        }
    }

    public Tablist(Player player) {
        this.player = player;
        this.version = ClientVersion.getVersion(player);
        this.addFakePlayers();
        this.update();
    }

    public void sendPacket(Player player, Object packet) {
        Object handle = ReflectionConstants.GET_HANDLE_METHOD.invoke((Object)player, new Object[0]);
        Object connection = ReflectionConstants.PLAYER_CONNECTION.get(handle);
        ReflectionConstants.SEND_PACKET.invoke(connection, packet);
    }

    public Tablist update() {
        int magic;
        TablistManager manager = TablistManager.INSTANCE;
        if (!this.initiated || manager == null) {
            return this;
        }
        Table<Integer, Integer, String> entries = manager.supplier.getEntries(this.player);
        boolean useProfiles = this.version.ordinal() != 0;
        int n = magic = useProfiles ? 4 : 3;
        if (useProfiles) {
            String footer;
            String header = manager.supplier.getHeader(this.player);
            if (header == null) {
                header = "";
            }
            if ((footer = manager.supplier.getFooter(this.player)) == null) {
                footer = "";
            }
            ProtocolInjector.PacketTabHeader packet = new ProtocolInjector.PacketTabHeader(ChatSerializer.a((String)("{text:\"" + StringEscapeUtils.escapeJava((String)header) + "\"}")), ChatSerializer.a((String)("{text:\"" + StringEscapeUtils.escapeJava((String)footer) + "\"}")));
            this.sendPacket(this.player, (Object)packet);
        }
        int i = 0;
        while (i < magic * 20) {
            int x = i % magic;
            int y = i / magic;
            String text = (String)entries.get((Object)x, (Object)y);
            if (text == null) {
                text = "";
            }
            String name = TAB_NAMES[i];
            Scoreboard scoreboard = this.player.getScoreboard();
            if (scoreboard == Bukkit.getScoreboardManager().getMainScoreboard()) {
                return this;
            }
            this.player.setScoreboard(scoreboard);
            Team team = scoreboard.getTeam(name);
            if (team == null) {
                team = scoreboard.registerNewTeam(name);
            }
            if (!team.hasEntry(name)) {
                team.addEntry(name);
            }
            String prefix = "";
            String suffix = "";
            if (text.length() < 17) {
                prefix = text;
            } else {
                String left = text.substring(0, 16);
                String right = text.substring(16, text.length());
                if (left.endsWith("\u00a7")) {
                    left = left.substring(0, left.length() - 1);
                    right = "\u00a7" + right;
                }
                String last = ChatColor.getLastColors((String)left);
                right = String.valueOf(last) + right;
                prefix = left;
                suffix = StringUtils.left((String)right, (int)16);
            }
            team.setPrefix(prefix);
            team.setSuffix(suffix);
            ++i;
        }
        return this;
    }

    public Tablist hideRealPlayers() {
        if (!this.initiated) {
            return this;
        }
        boolean useProfiles = this.version.ordinal() != 0;
        Stream.of(Bukkit.getOnlinePlayers()).forEach(other -> {
            if (!this.player.canSee(other)) {
                return;
            }
            Object packet = ReflectionConstants.TAB_PACKET_CONSTRUCTOR.invoke(new Object[0]);
            if (useProfiles) {
                Object profile = ReflectionConstants.GET_PROFILE_METHOD.invoke((Object)other, new Object[0]);
                ReflectionConstants.TAB_PACKET_ACTION.set(packet, profile);
            } else {
                ReflectionConstants.TAB_PACKET_NAME.set(packet, other.getName());
            }
            ReflectionConstants.TAB_PACKET_ACTION.set(packet, 4);
            this.sendPacket(this.player, packet);
        }
        );
        return this;
    }

    public Tablist hideFakePlayers() {
        if (!this.initiated) {
            return this;
        }
        boolean useProfiles = this.version.ordinal() != 0;
        Arrays.stream(GAME_PROFILES).forEach(other -> {
            Object packet = ReflectionConstants.TAB_PACKET_CONSTRUCTOR.invoke(new Object[0]);
            if (useProfiles) {
                ReflectionConstants.TAB_PACKET_PROFILE.set(packet, other);
            } else {
                String name = ReflectionConstants.GAME_PROFILE_NAME.get(other);
                ReflectionConstants.TAB_PACKET_NAME.set(packet, name);
            }
            ReflectionConstants.TAB_PACKET_ACTION.set(packet, 4);
            this.sendPacket(this.player, packet);
        }
        );
        return this;
    }

    public Tablist addFakePlayers() {
        if (this.initiated) {
            return this;
        }
        boolean useProfiles = this.version.ordinal() != 0;
        int magic = useProfiles ? 4 : 3;
        int i = 0;
        while (i < magic * 20) {
            int x = i % magic;
            int y = i / magic;
            Object packet = ReflectionConstants.TAB_PACKET_CONSTRUCTOR.invoke(new Object[0]);
            Object profile = GAME_PROFILES[i];
            if (useProfiles) {
                ReflectionConstants.TAB_PACKET_PROFILE.set(packet, profile);
            } else {
                String name = ReflectionConstants.GAME_PROFILE_NAME.get(profile);
                ReflectionConstants.TAB_PACKET_NAME.set(packet, name);
            }
            ReflectionConstants.TAB_PACKET_ACTION.set(packet, 0);
            this.sendPacket(this.player, packet);
            ++i;
        }
        this.initiated = true;
        return this;
    }

    public void clear() {
        this.player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        System.gc();
    }
}

