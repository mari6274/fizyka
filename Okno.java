import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

public class Okno extends JFrame implements Runnable {

	JTextArea textArea;
	Wykres wykres;
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
        final int spinnerColumns = 5;

        JPanel panel = new JPanel(new FlowLayout());
        getContentPane().add(panel, "North");
        panel.add(new JLabel("kat a"));
        alphaSpinner = new JSpinner(new SpinnerNumberModel(45, 0, 90, 0.01));
        defaultEditor = (JSpinner.DefaultEditor) alphaSpinner.getEditor();
        defaultEditor.getTextField().setColumns(spinnerColumns);
        panel.add(alphaSpinner);
        panel.add(new JLabel("predkosc v"));
        vSpinner = new JSpinner(new SpinnerNumberModel(100, 0, 10000, 0.01));
        defaultEditor = (JSpinner.DefaultEditor) vSpinner.getEditor();
        defaultEditor.getTextField().setColumns(spinnerColumns);
        panel.add(vSpinner);
        panel.add(new JLabel("m/s"));

        final Runnable runnable = this;
        JButton startButton = new JButton("start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                zadanie = new Thread(runnable);
                zadanie.start();
            }
        });
        panel.add(startButton);
        JButton stopButton = new JButton("stop");
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zadanie.stop();
            }
        });
        panel.add(stopButton);

        wykres = new Wykres();
        getContentPane().add(wykres, "Center");
		textArea = new JTextArea(10, 60);
		textArea.setEditable(false);
		getContentPane().add(new JScrollPane(textArea), "South");
	}

	void newSimulation(double angle, double v) {
		try {
			RzutUkosny obliczenia = new RzutUkosny(angle, v);
			
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMaximumFractionDigits(3);
			
			textArea.setText("# Rzut Ukosny:\n");
			textArea.append("Kat: " + nf.format(obliczenia.angle) + "[rad]\n");
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
        newSimulation((Double) alphaSpinner.getValue(), (Double) vSpinner.getValue());
    }
}
