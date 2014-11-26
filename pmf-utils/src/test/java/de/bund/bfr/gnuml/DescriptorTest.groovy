/*******************************************************************************
 * Copyright (c) 2014 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package de.bund.bfr.gnuml;

import static org.junit.Assert.*

import org.junit.Test

/**
 * 
 */
public class DescriptorTest {
	
	@Test
	void shouldReturnInvalidSettings() {
		def root = new CompositeDescriptor(name: 'TimeSeries', descriptor:
			new TupleDescriptor(descriptors: [
				new AtomicDescriptor(name: 'time'),
				new AtomicDescriptor(name: 'concentration')
				]))
		println root.invalidSettings
		assertNotEquals([], root.invalidSettings)
	}
	
	/**
	 * A rather soft test for debugging.
	 */
	@Test
	void shouldPrettyPrintNesting() {
		def root = new CompositeDescriptor(name: 'TimeSeries', indexType: DataType.Double, descriptor:
			new TupleDescriptor(descriptors: [
				new AtomicDescriptor(name: 'time'),
				new AtomicDescriptor(name: 'concentration')
				]))
		assertNotNull(root.toString())
	}
	
}
