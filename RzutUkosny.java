public class RzutUkosny {
	
	final double g = 9.80665;
	
	final public double angle;
	final public double vel;
    final public double h;
    final public double m;

	final public double velX, velY;

	public RzutUkosny(double masa, double wysokoscPoczatkowa, double katRzutu, double predkoscPoczatkowa) {
		
		double tmp = Math.toRadians(katRzutu);
		if (tmp < 0) {
			tmp = 0;
		} else if (tmp > 90) {
			tmp = 90;
		}
		
		vel = predkoscPoczatkowa;
		angle = tmp;
        h = wysokoscPoczatkowa;
        m = masa;
		
		velX = vel * Math.cos(angle);
		velY = vel * Math.sin(angle);
	}

	// zwraca wartosc y dla danego x
	public double getYForX(double x) {
		return ((velY/velX) * x - (g * Math.pow(x, 2) / (2 * Math.pow(velX, 2))) + h);
	}


	// zwraca wartosc x w chwili czasu t
	public double getXForT(double t)
    {
		return velX * t;
	}

	// zwraca wartosc y w chwili czasu t
	public double getYForT(double t) {
		return (velY * t - g * Math.pow(t, 2) / 2) + h;
	}


    public double predkoscPozioma() {
        return velX;
    }

    public double predkoscPionowa() {
        return velY;
    }

    public double zasieg() {
        return predkoscPozioma() * czasRzutu();
    }

    public double wysokoscMaksymalna() {
        return Math.pow(vel, 2) * Math.pow(Math.sin(angle), 2) / (2 * g) + h;
    }

    public double czasWznoszenia() {
        return vel * Math.sin(angle) / g;
    }

    public double czasRzutu() {
        return czasWznoszenia() + Math.sqrt((2*h)/g + Math.pow(predkoscPionowa(), 2)/Math.pow(g,2));
    }

    public double energiaPotencjalna(double h) {
        return m*g*h;
    }
}
