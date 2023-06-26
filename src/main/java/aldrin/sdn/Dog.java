package aldrin.sdn;

import java.util.List;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("Dog")
public class Dog extends Pet {



	@Relationship(value = "CHEW_TOY", direction = Relationship.Direction.OUTGOING)
	private List<ChewToy> chewToys;

	public List<ChewToy> getChewToys() {
		return chewToys;
	}

	public void setChewToys(List<ChewToy> chewToys) {
		this.chewToys = chewToys;
	}
}
