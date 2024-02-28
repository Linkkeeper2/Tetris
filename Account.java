import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Account {
    public String name;
    public int level = 0;
    public int exp = 0;
    
    public Account() {
        name = "Guest" + (int)(Math.random() * 10000);
    }

    public void login() {
        try {
            File account = new File("Account.txt");
            Scanner reader = new Scanner(account);
            
            if (reader.hasNext()) {
                String accName = reader.nextLine();
                String accPassword = reader.nextLine();

                MyGame.database.linkAccount(accName, accPassword);
            } else {
                reader.close();
                return;
            }
            

            reader.close();
        } catch (FileNotFoundException e) {}
    }

    public void addExp(int val) {
        exp += val;
        MyGame.status.addMessage("Earned " + val + " EXP!", 3000);

        while (exp >= 50 + (level * 50)) {
            exp -= 50 + (level * 50);
            level++;
            MyGame.status.addMessage("Leveled up to Level " + level + "!", 3000);
        }

        if (!name.startsWith("Guest")) MyGame.database.updateAccount(name);
    }
}