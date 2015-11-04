package io.github.m0pt0pmatt.spongesurvivalgames.loot;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import io.github.m0pt0pmatt.spongesurvivalgames.exceptions.EmptyLootGeneratorException;

/**
 * Class which houses loot and can produce a piece of loot on command
 * @author Skyler
 *
 */
public class LootGenerator {
	
	private List<Loot> loot;
	
	private double weight;
	
	private static Random rand = new Random();
	
	public LootGenerator() {
		loot = new LinkedList<Loot>();
		weight = 0;
	}
	
	/**
	 * Adds a piece of loot to this generator's list of loot items.
	 * @param obj
	 */
	public void addLoot(Loot obj) {
		loot.add(obj);
		weight += obj.getWeight();
	}
	
	/**
	 * Clears out all previously-added loot items, returning this loot generator to a clean status
	 */
	public void clearLoot() {
		loot.clear();
		weight = 0;
	}
	
	/**
	 * Generates a random piece of loot from the supplied loot.
	 * @return A newly generated piece of loot, or <i>null</i> on error
	 * @throws EmptyLootGeneratorException when the list of loot items is empty
	 */
	public Loot generate() throws EmptyLootGeneratorException {
		if (loot.isEmpty()) {
			throw new EmptyLootGeneratorException();
		}
		
		double pos = rand.nextDouble() * weight;
		double index = 0.0;
		
		for (Loot obj : loot) {
			index += obj.getWeight();
			if (index >= pos) {
				//finally got up to the item with the generated weight
				return obj;
			}
		}
		
		//We should never reach here! This is an error! UNDEFINED
		return null;
	}
	
}
