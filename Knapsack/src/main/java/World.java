package knapsack_ga;
import java.util.Random;
import java.math.BigInteger;

final public class World {
	
	final private Individual[] population;
	final private int averageFitness;
	final private Individual fittestIndividual;

	final private Knapsack knapsack;

	final private float mutationRate;
	final private float crossoverRate;
	
	private static final Random RANDOM = new Random();

	public World(int size, Knapsack knapsack, float mutationRate, float crossoverRate) {
		population = new Individual[size];

		for (int i = 0; i < size; i++) {
			// maintain invariant genotype.size <= Knapsack.size
			population[i] = new Individual(new BigInteger(knapsack.getSize(), RANDOM));
			population[i].setFitnessFromKnapsack(knapsack);
		}
		this.knapsack = knapsack;
		this.averageFitness = getAverageFitnessFromPopulation();
		this.fittestIndividual = getFittestIndividualFromPopulation();
		this.crossoverRate = crossoverRate;
		this.mutationRate = mutationRate;
	}	

	public World(Individual[] population, Knapsack knapsack, float mutationRate, float crossoverRate) {
		this.population = population;
		this.knapsack = knapsack;
		this.averageFitness = getAverageFitnessFromPopulation();
		this.fittestIndividual = getFittestIndividualFromPopulation();
		this.crossoverRate = crossoverRate;
		this.mutationRate = mutationRate;

		for (int i = 0; i < population.length; i++) {
			population[i].setFitnessFromKnapsack(knapsack);
		}
	}	

	private World mutate() {
		Individual[] mutants = new Individual[population.length];
		for (int i = 0; i < population.length; i++) {
			mutants[i] = population[i].mutate(mutationRate);
		}
		return new World(mutants, knapsack, mutationRate, crossoverRate);
	}

	// perform tournament selection 
	private Individual[] select() {
		Individual[] survivors = new Individual[population.length / 2];

		for (int i = 0; i < survivors.length; i++) {
			// survivor is the winner of a fight between 2 random chosen individuals
			survivors[i] = population[RANDOM.nextInt(population.length)].fight(population[RANDOM.nextInt(population.length)]);
		}
		return survivors;
	}

	private World recombine() {
		Individual[] nextGeneration = new Individual[population.length];
		Individual[] parents = select();

		int j = 0;
		Individual[] children = new Individual[2];

		for (int i = 0; i < parents.length; i++) {
			children = parents[i].crossover(parents[parents.length - (i + 1)], crossoverRate);
			nextGeneration[j] = children[0];
			nextGeneration[j+1] = children[1];
			j += 2;
		}

		return new World(nextGeneration, knapsack, mutationRate, crossoverRate);
	}

	public World getNextGeneration() {
		return recombine().mutate();
	}

	public int getAverageFitness() {
		return averageFitness;
	}

	public Individual getFittestIndividual() {
		return fittestIndividual;
	}

	public float getMutationRate() {
		return mutationRate;
	}

	public float getCrossoverRate() {
		return crossoverRate;
	}

	private int getAverageFitnessFromPopulation() {
		int sum = 0;

		for (Individual i : population) {
			sum += i.getFitness();
		}

		return sum / population.length;
	}

	private Individual getFittestIndividualFromPopulation() {
		Individual fittestIndividualSoFar = population[0];
		
		for (int i = 1; i < population.length; i++) {
			if (fittestIndividualSoFar.compareTo(population[i]) < 0) {
				fittestIndividualSoFar = population[i];
			}
		}
		return fittestIndividualSoFar;
	}
}
