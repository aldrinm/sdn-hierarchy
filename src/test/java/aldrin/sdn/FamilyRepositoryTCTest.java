package aldrin.sdn;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
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
class FamilyRepositoryTCTest {

	private static Neo4jContainer<?> neo4jContainer;

	@Autowired
	private FamilyRepository familyRepository;
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
		Family family = new Family();
		family.setName("Smiths");

		// Create a dog and a cat
		Dog dog = new Dog();
		dog.setName("Buddy");
		dog.setChewToys(List.of(new ChewToy("bone")));

		Cat cat = new Cat();
		cat.setName("Whiskers");
		cat.setSleepSpots(List.of(new SleepSpot("green window")));

		family.setPets(List.of(dog, cat));

		Family savedFamily = familyRepository.save(family);

		Optional<Family> retrievedFamily = familyRepository.findById(savedFamily.getId());

		assertThat(retrievedFamily).isNotEmpty();
		List<Pet> retrievedPets = retrievedFamily.get().getPets();
		List<Dog> dogs = retrievedPets.stream().filter(Dog.class::isInstance).map(a -> (Dog)a).toList();
		List<Cat> cats = retrievedPets.stream().filter(Cat.class::isInstance).map(a -> (Cat)a).toList();

		assertThat(dogs).flatExtracting("chewToys").isNotEmpty();
		assertThat(cats).flatExtracting("sleepSpots").isNotEmpty();



	}
}

