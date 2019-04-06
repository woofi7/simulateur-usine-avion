package simulation.Model;

import javax.swing.*;
import java.util.Base64;

public class Item {
    private static final String DEFAULT_IMAGE = "iVBORw0KGgoAAAANSUhEUgAAACEAAAAhCAYAAABX5MJvAAACs0lEQVR42u2Xv0tbURTHkziZgItrBROTapYMAeOg6H8gKA6Cg1NF0hJK6dJREPtHiJIsolOgnbJk1M0/QAThgUJAhBbfIvF9e7/3R1607yX3mUCH5sCXxHfuPX44557zbmIAYv9asRHECMIWIsiglBTKCpWF6kKOUFvL0c/Kek0yOFJsIIiC0JHQrdzaW7d6bWEoEOKvOCYmvojPRyFPbpueBvb2gPNzoNVS4nc+o0+BeHrPNmO8GUICTE5+QybjyuCFAlCtAk9PCDX6ajWgVALicUb7JfS5GyQqxDamplzMzgJzc8D6OuA46GueB9zcADs7JisE+RAJAv4ZeEQqpQCMVleBhwf1j2yMICojLE0RESCS+mB5mJ8HDg+BpSUfZHMTuLqyA2FGFhbMGTmSsS0hsp0uYH3bbeDyElheVhD5vAJhRmxKwxiqLHdCOVuIcqcLzCFksGbTBzGl4RnplxHG8LumbAtRl262XLcxI69BtraA6+v+GWEsBVG3hXCkm70flF6WpvuMrK0BrtsbgrEUhGML0ZZuDqEge34GGo2/QZiRsNLc3xuIti2EcnNjmBHk4gJYWXlZmrA54kNg8HK8NoJ0ZyRsjnBdxHL8kO79/f4QQaUJmiMHBwbipy3ER+lOp1VH2ID0miOMwVgKomIL8V6oJZecnNhNxl5z5PTUADBmPsrYrspRu7ho99IKmyMbG0CxaMZ2TSgV5QVWEnKRSACVCqwtaI6oaenqmJFf5btCv+VSgtiMaJOR4+OXb990uiE+42+5TySEvkoQZoSlOTvrfVjp4xqu5TVgZgbiPtJELpcZ5Ho3JvRJp9PrdA1bjr3PIUTxO5/5XeDJPePj35HNvhvWRbekD1bL4qLb0mtLw75tx+TJZoux1zl0zGQ1k1A9q+g1qYGu/KNfYP8txB82zb5Fuy8sKwAAAABJRU5ErkJggg==";
    public static final Item DEFAULT = new Item("test");
    private String name;
    private ImageIcon icon;

    public Item(String name) {
        this.name = name;

        switch (name) {
            case "metal":
                this.icon = new ImageIcon("src/ressources/metal.png");
                break;
            case "aile":
                this.icon = new ImageIcon("src/ressources/aile.png");
                break;
            case "moteur":
                this.icon = new ImageIcon("src/ressources/moteur.png");
                break;
            case "avion":
                this.icon = new ImageIcon("src/ressources/avion.png");
                break;
            default:
                byte[] iconData = Base64.getDecoder().decode(DEFAULT_IMAGE);
                this.icon = new ImageIcon(iconData);
                break;
        }
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public String getName() {
        return this.name;
    }
}
