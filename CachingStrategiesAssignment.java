import java.util.*;

public class CachingStrategiesAssignment {

    // Simulated Database and Cache
    private static final Map<String, String> database = new HashMap<>();
    private static final Map<String, String> cache = new HashMap<>();

    // Write-Behind Queue
    private static final List<String> writeBehindQueue = new ArrayList<>();

    public static void main(String[] args) {

        // Initialize Database
        database.put("apple", "$1.20");
        database.put("banana", "$0.80");

        System.out.println("--- TESTING YOUR CACHING STRATEGIES ---");

        // Test 1: Cache-Aside Read
        System.out.println("\n[Test 1] Testing Cache-Aside Read for 'apple':");
        System.out.println("Result 1: " + cacheAsideRead("apple"));
        System.out.println("Result 2: " + cacheAsideRead("apple"));

        // Test 2: Read-Through
        System.out.println("\n[Test 2] Testing Read-Through for 'banana':");
        System.out.println("Result 1: " + readThrough("banana"));

        // Test 3: Write-Through
        System.out.println("\n[Test 3] Testing Write-Through for 'orange' = '$1.50':");
        writeThrough("orange", "$1.50");
        System.out.println("DB holds orange: " + database.get("orange"));
        System.out.println("Cache holds orange: " + cache.get("orange"));

        // Test 4: Write-Around
        System.out.println("\n[Test 4] Testing Write-Around for 'grape' = '$2.00':");
        writeAround("grape", "$2.00");
        System.out.println("Cache holds grape: " + cache.get("grape"));

        // Test 5: Write-Behind
        System.out.println("\n[Test 5] Testing Write-Behind for 'mango' = '$3.00':");
        writeBehind("mango", "$3.00");
        System.out.println("Cache holds mango: " + cache.get("mango"));
        System.out.println("Queue holds mango write: " + writeBehindQueue);
    }

    // 1. Cache-Aside Read
    public static String cacheAsideRead(String key) {

        if (cache.containsKey(key)) {
            System.out.println("Cache HIT.");
            return cache.get(key);
        }

        System.out.println("Cache MISS. Querying DB...");
        String value = database.get(key);

        if (value != null) {
            cache.put(key, value);
        }

        return value;
    }

    // 2. Read-Through
    public static String readThrough(String key) {

        if (!cache.containsKey(key)) {
            System.out.println("Cache querying DB internally...");

            String value = database.get(key);

            if (value != null) {
                cache.put(key, value);
            }
        }

        return cache.get(key);
    }

    // 3. Write-Through
    public static void writeThrough(String key, String value) {

        cache.put(key, value);
        database.put(key, value);

        System.out.println("Updating Cache and Database synchronously.");
    }

    // 4. Write-Around
    public static void writeAround(String key, String value) {

        database.put(key, value);
        cache.remove(key);

        System.out.println("Writing directly to Database and evicting from Cache.");
    }

    // 5. Write-Behind
    public static void writeBehind(String key, String value) {

        cache.put(key, value);
        writeBehindQueue.add(key + "=" + value);

        System.out.println("Writing to Cache and queuing for DB write.");
    }
}