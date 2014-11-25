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
package de.bund.bfr.knime.hdfs.port;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.bund.bfr.knime.hdfs.HDFSFile;

final class HDFSFileObjectView extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7638638767267140428L;

	HDFSFileObjectView(final HDFSFile file) {
		super(new BorderLayout());
		super.setName("Jobmanager connection");
		StringBuilder buf = new StringBuilder("<html><body>");
		buf.append("<h2>Flink program</h2>");
		buf.append("<br/>");
		buf.append("<strong>Location:</strong><br/>");
		buf.append("<tt>" + file.getLocation() + "</tt>");
		buf.append("<br/>");
		buf.append("<strong>HDFS:</strong><br/>");
		buf.append("<tt>" + file.getHdfsSettings().getAddress() + "</tt>");
		buf.append("</body></html>");
		final JScrollPane jsp = new JScrollPane(new JLabel(buf.toString()));
		super.add(jsp, BorderLayout.CENTER);
	}
}