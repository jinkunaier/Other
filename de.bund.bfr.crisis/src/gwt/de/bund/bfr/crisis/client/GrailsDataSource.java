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
package de.bund.bfr.crisis.client;

import com.smartgwt.client.data.OperationBinding;
import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.DSOperationType;
import com.smartgwt.client.types.DSProtocol;

/**
 * @author heisea
 */
public class GrailsDataSource extends RestDataSource {
	/**
	 * Initializes GrailsDataSource.
	 */
	public GrailsDataSource(String id) {
		setID(id);
		setClientOnly(false);
		setDataFormat(DSDataFormat.JSON);

        DataSourceIntegerField pkField = new DataSourceIntegerField("id");  
        pkField.setHidden(true);  
        pkField.setPrimaryKey(true);  
        addField(pkField);
        
		// setup operations
		// 1. fetch
		OperationBinding fetch = createBinding(DSOperationType.FETCH, "fetch", DSProtocol.POSTPARAMS);
		// 2. update
		OperationBinding update = createBinding(DSOperationType.UPDATE, "save", DSProtocol.POSTPARAMS);
		// 3. add
		OperationBinding add = createBinding(DSOperationType.ADD, "save", DSProtocol.POSTPARAMS);
		// 4. remove
		OperationBinding remove = createBinding(DSOperationType.REMOVE, "delete", DSProtocol.POSTPARAMS);
		setOperationBindings(fetch, update, add, remove);
	}

	private OperationBinding createBinding(DSOperationType operationType, String actionName, DSProtocol dataProtocol) {
		String base = "de.bund.bfr.crisis";
		OperationBinding binding =
			new OperationBinding(operationType, "/" + base + "/" + getID() + "/" + actionName + ".json");
		binding.setDataProtocol(dataProtocol);
		return binding;
	}
}