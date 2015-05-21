import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

public class Okno extends JFrame implements Runnable {

	JTextArea textArea;
	Wykres wykres;
    JSpinner mSpinner;
    JSpinner hSpinner;
    JSpinner alphaSpinner;
    JSpinner vSpinner;
    private Thread zadanie;

    public Okno() {
		super();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Rzut ukosny");
        setLocation(1400, 20);

		getContentPane().setLayout(new BorderLayout());

        JSpinner.DefaultEditor defaultEditor;
        final int spinnerColumns = 4;

        JPanel panel = new JPanel(new FlowLayout());
        getContentPane().add(panel, "North");

        panel.add(new JLabel("Masa [kg]"));
        mSpinner = new JSpinner(new SpinnerNumberModel(10, 0, 10000, 0.1));
        defaultEditor = (JSpinner.DefaultEditor) mSpinner.getEditor();
        defaultEditor.getTextField().setColumns(spinnerColumns);
        panel.add(mSpinner);
        panel.add(new JLabel("Wysokość [m]"));
        hSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 0.1));
        defaultEditor = (JSpinner.DefaultEditor) hSpinner.getEditor();
        defaultEditor.getTextField().setColumns(spinnerColumns);
        panel.add(hSpinner);
        panel.add(new JLabel("Kąt [st]"));
        alphaSpinner = new JSpinner(new SpinnerNumberModel(45, 0, 90, 0.1));
        defaultEditor = (JSpinner.DefaultEditor) alphaSpinner.getEditor();
        defaultEditor.getTextField().setColumns(spinnerColumns);
        panel.add(alphaSpinner);
        panel.add(new JLabel("Prędkość [m/s]"));
        vSpinner = new JSpinner(new SpinnerNumberModel(100, 0, 10000, 0.1));
        defaultEditor = (JSpinner.DefaultEditor) vSpinner.getEditor();
        defaultEditor.getTextField().setColumns(spinnerColumns);
        panel.add(vSpinner);

        final Runnable runnable = this;
        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (zadanie != null) {
                    zadanie.stop();
                }
                zadanie = new Thread(runnable);
                zadanie.start();
            }
        });
        panel.add(startButton);
        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zadanie.stop();
            }
        });
        panel.add(stopButton);

        wykres = new Wykres();
        getContentPane().add(wykres, "Center");
		textArea = new JTextArea(12, 60);
		textArea.setEditable(false);
		getContentPane().add(new JScrollPane(textArea), "South");
	}

	void newSimulation(double m, double h, double angle, double v) {
		try {
			RzutUkosny obliczenia = new RzutUkosny(m, h, angle, v);
			
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMaximumFractionDigits(3);
			
			textArea.setText("# Rzut Ukosny:\n");
            textArea.append("Masa: " + nf.format(obliczenia.m) + "[kg]\n");
            textArea.append("Wysokość początkowa: " + nf.format(obliczenia.h) + "[m]\n");
			textArea.append("Kąt: " + nf.format(obliczenia.angle) + "[rad]\n");
			textArea.append("Predkosc: " + nf.format(obliczenia.vel) + "[m/s]\n");
            textArea.append("Predkosc pozioma: " + nf.format(obliczenia.predkoscPozioma()) + "[m/s]\n");
            textArea.append("Predkosc pionowa: " + nf.format(obliczenia.predkoscPionowa()) + "[m/s]\n");
            textArea.append("Zasięg: " + nf.format(obliczenia.zasieg()) + "[m]\n");
            textArea.append("Wysokość maksymalna: " + nf.format(obliczenia.wysokoscMaksymalna()) + "[m]\n");
            textArea.append("Czas wznoszenia: " + nf.format(obliczenia.czasWznoszenia()) + "[s]\n");
            textArea.append("Czas rzutu: " + nf.format(obliczenia.czasRzutu()) + "[s]\n");
			
			wykres.newSimulation(obliczenia);
		} catch (Exception e) {
			textArea.setText("Wystapil blad!");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Okno o = new Okno();
		o.pack();
		o.setLocationRelativeTo(null);
		o.setVisible(true);
	}

    @Override
    public void run() {
        newSimulation((Double) mSpinner.getValue(), (Double) hSpinner.getValue(), (Double) alphaSpinner.getValue(), (Double) vSpinner.getValue());
    }
}
