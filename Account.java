public class Account {
    public String name;
    public int level = 0;
    public int exp = 0;
    
    public Account() {
        name = "Guest" + (int)(Math.random() * 10000);
    }
}