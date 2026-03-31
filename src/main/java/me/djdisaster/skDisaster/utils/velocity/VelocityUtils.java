package me.djdisaster.skDisaster.utils.velocity;

import me.djdisaster.skDisaster.SkDisaster;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

public class VelocityUtils implements PluginMessageListener {

    private static final HashMap<String, Object> savedValues = new HashMap<>();
    private static final HashMap<String, Queue<Runnable>> waiters = new HashMap<>();
    private static final Set<String> inFlight = new HashSet<>();
    private static final Map<String, Queue<Consumer<Object>>> callbacks = new HashMap<>();

    public static Object getSavedValue(String index) {
        return savedValues.get(index);
    }

    public static int getInt(String index) {
        Object value = savedValues.get(index);
        if (value == null) {
            return -1;
        }
        return (int) value;
    }

    public static String getString(String index) {
        Object value = savedValues.get(index);
        if (value == null) {
            return null;
        }
        return (String) value;
    }

    public static String[] getStringArray(String index) {
        Object value = savedValues.get(index);
        if (value == null) {
            return null;
        }
        return (String[]) value;
    }

    public static InetData getInetData(String index) {
        Object value = savedValues.get(index);
        if (value == null) {
            return null;
        }
        return (InetData) value;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(message))) {
            String subchannel = in.readUTF();

            if (subchannel.equals("PlayerCount")) {
                String server = in.readUTF();
                int count = in.readInt();
                String key = "PlayerCount-" + server;
                savedValues.put(key, count);
                resolve(key, count);
            } else if (subchannel.equals("PlayerList")) {
                String server = in.readUTF();
                String list = in.readUTF();
                String[] players = list.isEmpty() ? new String[0] : list.split(", ");
                String key = "PlayerList-" + server;
                savedValues.put(key, players);
                resolve(key, players);
            } else if (subchannel.equals("GetServers")) {
                String list = in.readUTF();
                String[] servers = list.isEmpty() ? new String[0] : list.split(", ");
                String key = "GetServers";
                savedValues.put(key, servers);
                resolve(key, servers);
            } else if (subchannel.equals("GetServer")) {
                String server = in.readUTF();
                String key = "GetServer";
                savedValues.put(key, server);
                resolve(key, server);
            } else if (subchannel.equals("UUIDOther")) {
                String username = in.readUTF();
                String uuid = in.readUTF();
                String key = "UUIDOther-" + username.toLowerCase();
                savedValues.put(key, uuid);
                resolve(key, uuid);
            } else if (subchannel.equals("IPOther")) {
                String username = in.readUTF();
                String ip = in.readUTF();
                int port = in.readInt();
                InetData data = new InetData(ip, port);
                String key = "IPOther-" + username.toLowerCase();
                savedValues.put(key, data);
                resolve(key, data);
            } else if (subchannel.equals("ServerIP")) {
                String server = in.readUTF();
                String ip = in.readUTF();
                int port = in.readUnsignedShort();
                InetData data = new InetData(ip, port);
                String key = "ServerIP-" + server.toLowerCase();
                savedValues.put(key, data);
                resolve(key, data);
            }
        } catch (IOException e) {
            SkDisaster.getInstance().getLogger().severe("error in VelocityUtils please report this.");
            throw new RuntimeException(e);
        }
    }

    public static void fetch(String key, Runnable resume, Payload payload) {
        waiters.computeIfAbsent(key, k -> new ArrayDeque<>()).add(resume);

        if (inFlight.contains(key)) {
            return;
        }
        inFlight.add(key);

        Player sender = Bukkit.getOnlinePlayers().stream().findFirst().orElse(null);
        if (sender == null) {
            finish(key);
            return;
        }

        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(bytes);
            payload.write(out);
            sender.sendPluginMessage(
                    SkDisaster.getInstance(),
                    "BungeeCord",
                    bytes.toByteArray()
            );
        } catch (IOException e) {
            finish(key);
        }
    }

    public static void fetchValue(String key, Consumer<Object> consumer, Payload payload) {
        callbacks.computeIfAbsent(key, k -> new ArrayDeque<>()).add(consumer);

        if (inFlight.contains(key)) {
            return;
        }
        inFlight.add(key);

        Player sender = Bukkit.getOnlinePlayers().stream().findFirst().orElse(null);
        if (sender == null) {
            finish(key);
            return;
        }

        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(bytes);
            payload.write(out);
            sender.sendPluginMessage(
                    SkDisaster.getInstance(),
                    "BungeeCord",
                    bytes.toByteArray()
            );
        } catch (IOException e) {
            finish(key);
        }
    }

    private static void resolve(String key, Object value) {
        Queue<Consumer<Object>> callbackQueue = callbacks.remove(key);
        if (callbackQueue != null) {
            while (!callbackQueue.isEmpty()) {
                callbackQueue.poll().accept(value);
            }
        }
        finish(key);
    }

    private static void finish(String key) {
        inFlight.remove(key);
        Queue<Runnable> q = waiters.remove(key);
        if (q != null) {
            while (!q.isEmpty()) {
                q.poll().run();
            }
        }
    }

    private static Player getSender() {
        return Bukkit.getOnlinePlayers().stream().findFirst().orElse(null);
    }

    private static void send(Payload payload) {
        Player sender = getSender();
        if (sender == null) {
            return;
        }

        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(bytes);
            payload.write(out);
            sender.sendPluginMessage(
                    SkDisaster.getInstance(),
                    "BungeeCord",
                    bytes.toByteArray()
            );
        } catch (IOException ignored) {
        }
    }

    public static void connectOther(String player, String server) {
        send(out -> {
            out.writeUTF("ConnectOther");
            out.writeUTF(player);
            out.writeUTF(server);
        });
    }

    public static void kickPlayer(String player, String reason) {
        send(out -> {
            out.writeUTF("KickPlayer");
            out.writeUTF(player);
            out.writeUTF(reason);
        });
    }

    public static void message(String player, String message) {
        send(out -> {
            out.writeUTF("Message");
            out.writeUTF(player);
            out.writeUTF(message);
        });
    }

    public static void forward(String server, String channel, byte[] data) {
        send(out -> {
            out.writeUTF("Forward");
            out.writeUTF(server);
            out.writeUTF(channel);
            out.writeShort(data.length);
            out.write(data);
        });
    }

    public static void forwardToPlayer(String player, String channel, byte[] data) {
        send(out -> {
            out.writeUTF("ForwardToPlayer");
            out.writeUTF(player);
            out.writeUTF(channel);
            out.writeShort(data.length);
            out.write(data);
        });
    }

    public static void forwardToPlayer(String player, String channel, Payload payload) {
        try {
            ByteArrayOutputStream msgBytes = new ByteArrayOutputStream();
            DataOutputStream msgOut = new DataOutputStream(msgBytes);
            payload.write(msgOut);
            forwardToPlayer(player, channel, msgBytes.toByteArray());
        } catch (IOException ignored) {
        }
    }

    public static void requestPlayerCount(String server, Consumer<Integer> consumer) {
        String normalized = server == null || server.isEmpty() ? "ALL" : server;
        String key = "PlayerCount-" + normalized;
        fetchValue(
                key,
                value -> consumer.accept((Integer) value),
                out -> {
                    out.writeUTF("PlayerCount");
                    out.writeUTF(normalized);
                }
        );
    }

    public static void requestPlayerList(String server, Consumer<String[]> consumer) {
        String normalized = server == null || server.isEmpty() ? "ALL" : server;
        String key = "PlayerList-" + normalized;
        fetchValue(
                key,
                value -> consumer.accept((String[]) value),
                out -> {
                    out.writeUTF("PlayerList");
                    out.writeUTF(normalized);
                }
        );
    }

    public static void requestServers(Consumer<String[]> consumer) {
        String key = "GetServers";
        fetchValue(
                key,
                value -> consumer.accept((String[]) value),
                out -> out.writeUTF("GetServers")
        );
    }

    public static void requestCurrentServer(Consumer<String> consumer) {
        String key = "GetServer";
        fetchValue(
                key,
                value -> consumer.accept((String) value),
                out -> out.writeUTF("GetServer")
        );
    }

    public static void requestUUIDOther(String player, Consumer<String> consumer) {
        String key = "UUIDOther-" + player.toLowerCase();
        fetchValue(
                key,
                value -> consumer.accept((String) value),
                out -> {
                    out.writeUTF("UUIDOther");
                    out.writeUTF(player);
                }
        );
    }

    public static void requestIPOther(String player, Consumer<InetData> consumer) {
        String key = "IPOther-" + player.toLowerCase();
        fetchValue(
                key,
                value -> consumer.accept((InetData) value),
                out -> {
                    out.writeUTF("IPOther");
                    out.writeUTF(player);
                }
        );
    }

    public static void requestServerIP(String server, Consumer<InetData> consumer) {
        String key = "ServerIP-" + server.toLowerCase();
        fetchValue(
                key,
                value -> consumer.accept((InetData) value),
                out -> {
                    out.writeUTF("ServerIP");
                    out.writeUTF(server);
                }
        );
    }

    @FunctionalInterface
    public interface Payload {
        void write(DataOutputStream out) throws IOException;
    }

    public static class InetData {
        private final String ip;
        private final int port;

        public InetData(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }

        public String getIp() {
            return ip;
        }

        public int getPort() {
            return port;
        }
    }
}