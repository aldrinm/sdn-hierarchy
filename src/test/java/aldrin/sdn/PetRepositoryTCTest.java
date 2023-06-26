package aldrin.sdn;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Neo4jContainer;

@DataNeo4jTest
class PetRepositoryTCTest {

	private static Neo4jContainer<?> neo4jContainer;

	@Autowired
	private PetRepository petRepository;

	@BeforeAll
	static void initializeNeo4j() {

		neo4jContainer = new Neo4jContainer<>().withAdminPassword("pass1");
		neo4jContainer.start();
	}

	@AfterAll
	static void stopNeo4j() {
		neo4jContainer.close();
	}

	@DynamicPropertySource
	static void neo4jProperties(DynamicPropertyRegistry registry) {

		registry.add("spring.neo4j.uri", neo4jContainer::getBoltUrl);
		registry.add("spring.neo4j.authentication.username", () -> "neo4j");
		registry.add("spring.neo4j.authentication.password", neo4jContainer::getAdminPassword);
	}

	@Test
	void findPetDetails(@Autowired Neo4jClient client) {
		// Create a dog and a cat
		Dog dog = new Dog();
		dog.setName("Buddy");
		dog.setChewToys(List.of(new ChewToy("bone")));
		petRepository.save(dog);

		Cat cat = new Cat();
		cat.setName("Whiskers");
		cat.setSleepSpots(List.of(new SleepSpot("green window")));
		petRepository.save(cat);

		List<Pet> allPets = petRepository.findAllPets();

		List<Dog> dogs = allPets.stream().filter(Dog.class::isInstance).map(a -> (Dog)a).toList();
		List<Cat> cats = allPets.stream().filter(Cat.class::isInstance).map(a -> (Cat)a).toList();

		assertThat(dogs).flatExtracting("chewToys").isNotEmpty();
		assertThat(cats).flatExtracting("sleepSpots").isNotEmpty();

	}
}

