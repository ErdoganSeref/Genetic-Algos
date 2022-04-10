package knapsack_ga;
import java.util.Random;
import java.math.BigInteger;

final public class Individual implements Comparable<Individual>{
	
	final private BigInteger genotype;
	private int fitness;

	private static final Random RANDOM = new Random();

	public Individual(BigInteger genotype) {
		this.genotype = genotype;
	}
	
	public void setFitnessFromKnapsack(Knapsack k) {
		this.fitness = getFitnessFromKnapsack(k);
	}

	public Individual mutate(float mutationRate) {
		BigInteger mutatedGenotype = genotype;
		for (int i = 0; i < genotype.bitCount(); i++) {
			if (RANDOM.nextFloat() < mutationRate) {
				mutatedGenotype = mutatedGenotype.flipBit(i);
			}
		}
		return new Individual(mutatedGenotype);
	}

	public Individual[] crossover(Individual other, float crossoverRate) {
		Individual[] children = new Individual[2];
		if (RANDOM.nextFloat() < crossoverRate) {
			int crossoverPoint = genotype.bitCount() / 2;
			BigInteger k = BigInteger.ONE.shiftLeft(crossoverPoint);
			BigInteger[] genotypeSlicesOfThis = genotype.divideAndRemainder(k);
			BigInteger[] genotypeSlicesOfOther = other.genotype.divideAndRemainder(k);
			children[0] = new Individual(genotypeSlicesOfThis[0].shiftLeft(crossoverPoint).xor(genotypeSlicesOfOther[1]));
			children[1] = new Individual(genotypeSlicesOfOther[0].shiftLeft(crossoverPoint).xor(genotypeSlicesOfThis[1]));;
		}
		else {
			children[0] = this;
			children[1] = other;
		}
		return children;
	}

	public Individual fight(Individual other) {
		if (this.compareTo(other) > 0) {
			return this;
		}
		else if (this.compareTo(other) < 0) {
			return other;
		}
		// when tie then fair random selection
		else {
			if (RANDOM.nextFloat() > 0.5) {
				return this;
			}
			else {
				return other;
			}
		}
	}

	private int getFitnessFromKnapsack(Knapsack k) {
		int capacityLeft = k.getCapacity();
		int fitnessAchieved = 0;

		for (int i = 0; i < genotype.bitCount(); i++) {
			if (genotype.testBit(i) && k.getWeight(i) <= capacityLeft) {
				fitnessAchieved += k.getValue(i);
				capacityLeft -= k.getWeight(i);
			}
		}
		return fitnessAchieved;
	}

	public int getFitness() {
		return fitness;
	}

	@Override
	public int compareTo(Individual other) {
		return Integer.compare(this.fitness, other.fitness);
	}
}
