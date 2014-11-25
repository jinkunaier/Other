/*******************************************************************************
 * Copyright (c) 2014 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This file is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this file. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package de.bund.bfr.knime.hdfs.port;

import javax.swing.JComponent;

import org.knime.core.node.port.PortObjectSpec;

import de.bund.bfr.knime.hdfs.HDFSFile;

/**
 * Represents a connection to the Flink job manager. Currently, the connection is only virtual: With every job
 * submission, we send a completely independent request. However, this special port allows users to keep their Flink
 * configuration at one place through the {@link de.bund.bfr.knime.flink.jm.FlinkJobManagerConnectionNodeModel}.
 */
public class HDFSFileObjectSpec implements PortObjectSpec {
	private HDFSFile settings = new HDFSFile();

	/**
	 * Returns the settings.
	 * 
	 * @return the settings
	 */
	public HDFSFile getFile() {
		return this.settings;
	}

	/*
	 * (non-Javadoc)
	 * @see org.knime.core.node.port.PortObjectSpec#getViews()
	 */
	@Override
	public JComponent[] getViews() {
		return new JComponent[] { new HDFSFileObjectView(this.settings) };
	}

	/**
	 * Sets the settings to the specified value.
	 * 
	 * @param settings
	 *        the settings to set
	 */
	public void setFile(HDFSFile settings) {
		if (settings == null)
			throw new NullPointerException("settings must not be null");

		this.settings = settings;
	}

	public static PortObjectSpecSerializer<HDFSFileObjectSpec> getPortObjectSpecSerializer() {
		return new HDFSFileObjectSpecSerializer();
	}
}
