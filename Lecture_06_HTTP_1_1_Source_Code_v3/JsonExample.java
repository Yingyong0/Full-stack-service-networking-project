import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JsonExample {
    
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
        
        System.out.println("\n=== Example 3: Object to JSON string ===");
        SuperHero superHeroes = createSuperHeroData();
        String jsonString = gson.toJson(superHeroes);
        System.out.println(jsonString);
        
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
