package aldrin.sdn;


import java.util.List;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface PetRepository extends Neo4jRepository<Pet, Long> {

	@Query("MATCH (a:Dog) RETURN a")
	List<Dog> findAllDogs();

	@Query("MATCH (a:Cat) RETURN a")
	List<Cat> findAllCats();

	@Query("MATCH (a:Pet) RETURN a")
	List<Pet> findAllPets();
}
