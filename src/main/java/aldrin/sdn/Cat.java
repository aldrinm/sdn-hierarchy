package aldrin.sdn;

import java.util.List;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("Cat")
public class Cat extends Pet {


	@Relationship(value = "SLEEP_SPOT", direction = Relationship.Direction.OUTGOING)
	private List<SleepSpot> sleepSpots;

	public List<SleepSpot> getSleepSpots() {
		return sleepSpots;
	}

	public void setSleepSpots(List<SleepSpot> sleepSpots) {
		this.sleepSpots = sleepSpots;
	}
}
