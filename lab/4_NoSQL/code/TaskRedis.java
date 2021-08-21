import redis.clients.jedis.Jedis;

import java.util.HashMap;

public class TaskRedis {

    public static void main(String[] args) {
        // Jedis 实例负责和 Redis 节点通信
        Jedis jedis = new Jedis("192.168.3.9", 6379);

        // Manual configuration of Redis may be required for remote access.
        //
        // 1) remove `bind localhost` or `bind 127.0.0.1` in redis.conf
        // 2) `protected-mode` => no
        // 3) requirepass [YourPassword]

        String password = "redis1111";
        jedis.auth(password);

        // 1:
        String key = "student.scofield";
        jedis.hset(key, new HashMap<>() {{ // hset with Map is not allowed in old veriosn redis
            put("English", "45");
            put("Math", "89");
            put("Scomputer", "100");
        }});
        // 2:
        String hget = jedis.hget(key, "English");
        System.out.println(key + " English >> " + hget);
    }
}
