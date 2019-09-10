package recover.utils;

import java.util.Collections;
import java.util.Random;
import java.util.Vector;

public class Noise {

	private Vector<Integer> permutation;

	private int octave;
	private int amplitude;
	private long seed;
	private double frequency;
	private Random random;

	public Noise(int octave, int amplitude, long seed, double frequency) {

		this.octave = octave;
		this.amplitude = amplitude;
		this.frequency = frequency;
		this.seed = seed;
		random = new Random(this.seed);

		permutation = new Vector<Integer>();
		for (int i = 0; i < 512; i++) {
			permutation.add(i / 2);
		}

		Collections.shuffle(permutation, random);

	}

	public double generate(double x, double y, double z) {

		double Xbuf = (x * frequency) / 1000.f;
		double Ybuf = (y * frequency) / 1000.f;
		double Zbuf = z;
		// output of the noise function
		double amp = amplitude;
		double Output = 0.0f;
		for (int i = 0; i < octave; i++) {
			x = Xbuf * (i + 1);
			y = Ybuf * (i + 1);
			z = Zbuf;
			amp = amp / 2;
			// Find the unit cube that contains the point
			int X = (int) Math.floor(x) & 255;
			int Y = (int) Math.floor(y) & 255;
			int Z = (int) Math.floor(z) & 255;
			// Find relative x, y,z of point in cube
			x -= Math.floor(x);
			y -= Math.floor(y);
			z -= Math.floor(z);
			// Compute fade curves for each of x, y, z
			double u = fading(x);
			double v = fading(y);
			double w = fading(z);
			// Hash coordinates of the 8 cube corners
			int A = permutation.get(X) + Y;
			int AA = permutation.get(A) + Z;
			int AB = permutation.get(A + 1) + Z;
			int B = permutation.get(X + 1) + Y;
			int BA = permutation.get(B) + Z;
			int BB = permutation.get(B + 1) + Z;
			// Add blended results from 8 corners of cube
			double res = lerp(w,
					lerp(v, lerp(u, grad(permutation.get(AA), x, y, z), grad(permutation.get(BA), x - 1, y, z)),
							lerp(u, grad(permutation.get(AB), x, y - 1, z),
									grad(permutation.get(BB), x - 1, y - 1, z))),
					lerp(v, lerp(u, grad(permutation.get(AA + 1), x, y, z - 1),
							grad(permutation.get(BA + 1), x - 1, y, z - 1)),
							lerp(u, grad(permutation.get(AB + 1), x, y - 1, z - 1),
									grad(permutation.get(BB + 1), x - 1, y - 1, z - 1))));
			Output += amp * ((res + 1.0) / 2.0);

			Xbuf *= 2;
			Ybuf *= 2;
		}
		return Output;
	}

	// Fading Polynomial Function
	private static double fading(double x) {
		return x * x * x * (x * (6 * x - 15) + 10);
	}

	// Linear Interpolation Fonction
	private static double lerp(double t, double a, double b) {
		return a + t * (b - a);
	}

	// Hash Gradient function
	private static double grad(int hash, double x, double y, double z) {
		int h = hash & 15;
		// Convert lower 4 bits of hash into 12 gradient directions
		double u = h < 8 ? x : y, v = h < 4 ? y : h == 12 || h == 14 ? x : z;
		return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
	}

	/// Setters
	public void setOctave(int octave) {
		if (octave < 1) {
			octave = 1;
		}
		if (octave > 8) {
			octave = 8;
		}
		this.octave = octave;
	}

	public void setAmplitude(int amplitude) {
		if (amplitude < 1) {
			amplitude = 1;
		}
		this.amplitude = amplitude;
	}

	public void setSeed(int seed) {
		this.seed = seed;
	}

	public void setFrequency(double frequency) {
		if (frequency <= 0.0) {
			frequency = 1.0;
		}
		this.frequency = frequency;
	}

}
