import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Account {
    public String name;
    public int level = 0;
    public int exp = 0;
    public int highestLevel = 0;
    public int prestige = 0;
    public int inputDelay = 150;
    public ArrayList<Integer> controls = new ArrayList<>();
    public String skin = "gfx/PaletteBattle.png";

    public Account() {
        name = "Guest" + (int) (Math.random() * 10000);
        controls.add(32);
        controls.add(37);
        controls.add(38);
        controls.add(39);
        controls.add(40);
        controls.add(47);
        controls.add(67);
        controls.add(90);
    }

    public void login() {
        try {
            File account = new File("Account.txt");
            Scanner reader = new Scanner(account);

            if (reader.hasNext()) {
                String accName = reader.nextLine();
                String accPassword = reader.nextLine();

                if (MyGame.database.getAccount(accName) != null) {
                    MyGame.database.linkAccount(accName, accPassword);
                }
            } else {
                reader.close();
                return;
            }

            reader.close();
        } catch (FileNotFoundException e) {
        }
    }

    public void addExp(int val) {
        exp += val;
        MyGame.status.addMessage("Earned " + val + " EXP!", 3000);

        while (exp >= 50 + (level * 50)) {
            exp -= 50 + (level * 50);
            level++;
            MyGame.status.addMessage("Leveled up to Level " + level + "!", 3000);

            if (level >= 50)
                prestige();
        }

        if (!name.startsWith("Guest"))
            MyGame.database.updateAccount(name);
    }

    private void prestige() {
        level = 0;
        exp = 0;
        prestige++;
        MyGame.status.addMessage("You are now Prestige " + prestige + "!", 3000);
    }
}