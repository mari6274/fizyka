import javax.swing.*;

/**
 * Created by mario on 08.06.15.
 */
public class FizykaApplet extends JApplet {
    @Override
    public void init() {
        super.init();
        Okno o = new Okno();
        setContentPane(o.getContentPane());
    }
}
