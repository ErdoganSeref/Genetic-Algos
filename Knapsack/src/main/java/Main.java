package knapsack_ga;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.JCommander;

public class Main {

	@Parameter(names = { "--populationSize", "-pS" }, description = "Size of the population", required=false)
	int populationSize = 100;

	@Parameter(names = "-iterations", description = "Number of generations to be evolved from the initial population", required=false)
	int iterations = 100;

	@Parameter(names = { "--mutationRate", "-mR" }, description = "Probability of gene mutating", required=false)
	float mutationRate = 0.1f;

	@Parameter(names = { "--crossoverRate", "-cR" }, description = "Probability of crossover when recombining", required=false)
	float crossoverRate = 0.1f;

	@Parameter(names = { "--knapsackCapacity", "-kC" }, description = "Capacity of Knapsack", required=false)
	int knapsackCapacity = 20;

	@Parameter(names = { "--knapsackSize", "-kS" }, description = "Number of elements to choose from", required=false)
	int knapsackSize = 10;

	public static void main(String[] argv) {
		Main main = new Main();
		JCommander.newBuilder()
			.addObject(argv)
			.build()
			.parse(argv);
		main.run();
	}

	public void run() {
		World world = new World(populationSize, new Knapsack(knapsackSize, knapsackCapacity), mutationRate, crossoverRate);
		for (int i = 0; i < iterations; i++) {
			world.getNextGeneration();
		}

		System.out.println("The fittest individual has a fitness of " + world.getFittestIndividual().getFitness());
		System.out.println("The average fitness is " + world.getAverageFitness());
	}
}
