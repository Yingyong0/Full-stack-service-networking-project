// Note: Gson dependency required for compilation
// Maven: <dependency><groupId>com.google.code.gson</groupId><artifactId>gson</artifactId><version>2.10.1</version></dependency>
// Gradle: implementation 'com.google.code.gson:gson:2.10.1'
// Or download manually: https://mvnrepository.com/artifact/com.google.code.gson/gson
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * JSON Processing Example
 * Corresponds to Python version: lec-06-prg-03/04/05/06-json-example.py
 * 
 * Note: Gson dependency required for compilation and execution
 */
public class JsonExample {
    
    // Inner class representing superhero data
    static class SuperHero {
        String squadName;
        String homeTown;
        int formed;
        String secretBase;
        boolean active;
        Member[] members;
    }
    
    static class Member {
        String name;
        int age;
        String secretIdentity;
        String[] powers;
    }
    
    public static void main(String[] args) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        // Example 1: Read data from JSON file
        try {
            System.out.println("=== Example 1: Read from JSON file ===");
            FileReader reader = new FileReader("lec-06-prg-03-json-example.json");
            SuperHero superHeroes = gson.fromJson(reader, SuperHero.class);
            reader.close();
            
            System.out.println("Home Town: " + superHeroes.homeTown);
            System.out.println("Active: " + superHeroes.active);
            System.out.println("Power: " + superHeroes.members[1].powers[2]);
        } catch (IOException e) {
            System.err.println("Failed to read JSON file: " + e.getMessage());
        }
        
        // Example 2: Create JSON object and write to file
        try {
            System.out.println("\n=== Example 2: Create and write JSON file ===");
            SuperHero superHeroes = createSuperHeroData();
            
            FileWriter writer = new FileWriter("lec-06-prg-04-json-example.json");
            gson.toJson(superHeroes, writer);
            writer.close();
            System.out.println("JSON data written to file");
        } catch (IOException e) {
            System.err.println("Failed to write JSON file: " + e.getMessage());
        }
        
        // Example 3: Convert object to JSON string
        System.out.println("\n=== Example 3: Object to JSON string ===");
        SuperHero superHeroes = createSuperHeroData();
        String jsonString = gson.toJson(superHeroes);
        System.out.println(jsonString);
        
        // Example 4: JSON string to object
        System.out.println("\n=== Example 4: JSON string to object ===");
        SuperHero parsed = gson.fromJson(jsonString, SuperHero.class);
        System.out.println("Home Town: " + parsed.homeTown);
    }
    
    private static SuperHero createSuperHeroData() {
        SuperHero superHeroes = new SuperHero();
        superHeroes.squadName = "Super hero squad";
        superHeroes.homeTown = "Metro City";
        superHeroes.formed = 2016;
        superHeroes.secretBase = "Super tower";
        superHeroes.active = true;
        
        Member member1 = new Member();
        member1.name = "Molecule Man";
        member1.age = 29;
        member1.secretIdentity = "Dan Jukes";
        member1.powers = new String[]{"Radiation resistance", "Turning tiny", "Radiation blast"};
        
        Member member2 = new Member();
        member2.name = "Madame Uppercut";
        member2.age = 39;
        member2.secretIdentity = "Jane Wilson";
        member2.powers = new String[]{"Million tonne punch", "Damage resistance", "Superhuman reflexes"};
        
        Member member3 = new Member();
        member3.name = "Eternal Flame";
        member3.age = 1000000;
        member3.secretIdentity = "Unknown";
        member3.powers = new String[]{"Immortality", "Heat Immunity", "Inferno", "Teleportation", "Interdimensional travel"};
        
        superHeroes.members = new Member[]{member1, member2, member3};
        
        return superHeroes;
    }
}

