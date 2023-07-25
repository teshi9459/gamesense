import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Gamesense SSG = new Gamesense();
        String address = SSG.getEngineAddress();
        SSG.setGameMetadata(address,"MY_GAME", "MY_GAME","Teshi", 20000);
        SSG.registerEvent(address, "MY_GAME", "TEST", 0, 1, 2, false);
        for (int i = 0; i < 20; i++) {
            int value = 0;
            if (new Random().nextBoolean()) value++;
            SSG.sendGameEvent(address,"MY_GAME", "TEST", value);
            try {
                int delayInMilliseconds = 19500;
                Thread.sleep(delayInMilliseconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

}