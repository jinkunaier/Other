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
package de.bund.bfr.pmf

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import java.nio.charset.Charset
import java.nio.file.Paths

import org.sbml.jsbml.SBMLDocument

import de.bund.bfr.numl.ConformityMessage
import de.bund.bfr.numl.NuMLDocument
import de.bund.bfr.numl.NuMLWriter
import de.bund.bfr.pmf.sbml.SBMLAdapter;

/**
 * The base {@link PMFDocument} consisting of several NuML data files and 1 SBML model (for now).
 */
@EqualsAndHashCode(includes = ['models', 'dataSets'])
@ToString(includes = ['models', 'dataSets'])
class PMFDocument {
	Map<String, SBMLDocument> models = new ObservableMap()
	Map<String, NuMLDocument> dataSets = new ObservableMap()

	PMFDocument() {
		models.addPropertyChangeListener({
			models.each {
				PMFUtil.wrap(it.value)
			}
		})
		dataSets.addPropertyChangeListener({
			dataSets.each {
				PMFUtil.wrap(it.value)
			}
		})
	}

	// mostly for additional validation (XPath evaluation etc)
	Map<Object, Closure<InputStream>> documentStreamFactories = [:]

	/**
	 * Returns the input stream for a {@link SBMLDocument} or {@link NuMLDocument}
	 */
	InputStream getInputStream(Object doc) {
		documentStreamFactories[doc] ? documentStreamFactories[doc]() : createFallbackInputStream(doc)
	}

	static final fileTypeWriters = [(SBMLDocument): SBMLAdapter, (NuMLDocument): NuMLWriter]

	def createFallbackInputStream(Object doc) {
		def fileTypeWriter = fileTypeWriters[doc.class]
		if(!fileTypeWriter)
			throw new IllegalArgumentException("Unknown document type ${doc?.class}")
		def xmlString = fileTypeWriter.newInstance().toString(doc)
		new ByteArrayInputStream(xmlString.getBytes(Charset.forName("utf-8")))
	}

	/**
	 * Returns the {@link SBMLDocument} or {@link NuMLDocument} identified by the given xlink. 
	 * Optionally a baseDoc can be used to resolve relative paths.
	 */
	def resolve(String xlinkRef, Object baseDoc = null) {
		// first try look directly for it
		def doc = models[xlinkRef] ?: dataSets[xlinkRef]
		if(!doc && baseDoc) {
			def baseDocKey = (models.find { it.value.is(baseDoc) } ?: dataSets.find { it.value.is(baseDoc) })?.key
			if(baseDocKey) {
				def basePath = Paths.get(new URI(baseDocKey))
				def fullPath = basePath.parent.resolve(xlinkRef)
				doc = (models.find { Paths.get(new URI(it.key)) == fullPath } ?: dataSets.find { Paths.get(new URI(it.key)) == fullPath })?.value
			}
		}
		if(!doc)
			throw new IllegalArgumentException("Unknown document $xlinkRef")
		doc
	}

	/**
	 * Sets the dataSets to the specified value.
	 *
	 * @param dataSets the dataSets to set
	 */
	public void setDataSets(Map<String, NuMLDocument> dataSets) {
		if (dataSets == null)
			throw new NullPointerException("dataSets must not be null");

		this.dataSets.clear()
		this.dataSets.putAll(dataSets)
	}

	/**
	 * Returns all invalid settings of this PMF document. <br/>
	 * The invalid settings can be either detected in the underlying model, in the data files, or with the additional set of PMF specification rules.
	 */
	List<ConformityMessage> getInvalidSettings(String prefix = 'pmf') {
		def messages = dataSets.collect { name, numl -> numl.getInvalidSettings("$prefix/$name") }.flatten()
		messages += models.collect { name, sbml -> PMFUtil.getInvalidSettings(sbml, "$prefix/$name", this) }.flatten()
		ValidationRule.values()*.validate(this, messages)
		messages
	}

	/**
	 * Sets the models to the specified value.
	 *
	 * @param models the models to set
	 */
	public void setModels(Map<String, SBMLDocument> models) {
		if (models == null)
			throw new NullPointerException("models must not be null");

		this.models.clear()
		this.models.putAll(models)
	}	

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataSets == null) ? 0 : dataSets.hashCode());
		result = prime * result + ((models == null) ? 0 : models.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PMFDocument other = (PMFDocument) obj;
		if (dataSets == null) {
			if (other.dataSets != null)
				return false;
		} else if (!dataSets.equals(other.dataSets))
			return false;
		if (models == null) {
			if (other.models != null)
				return false;
		} else if (!models.equals(other.models))
			return false;
		return true;
	}
	
	
}
