/*******************************************************************************
 * PMM-Lab � 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab � 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Joergen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thoens (BfR)
 * Annemarie Kaesbohrer (BfR)
 * Bernd Appel (BfR)
 * 
 * PMM-Lab is a project under development. Contributions are welcome.
 * 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package de.bund.bfr.knime.pmm.util.fitting;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.nfunk.jep.ParseException;

import de.bund.bfr.knime.pmm.core.CombineUtilities;
import de.bund.bfr.knime.pmm.core.Utilities;
import de.bund.bfr.knime.pmm.core.data.Condition;
import de.bund.bfr.knime.pmm.core.models.ModelsFactory;
import de.bund.bfr.knime.pmm.core.models.Parameter;
import de.bund.bfr.knime.pmm.core.models.ParameterValue;
import de.bund.bfr.knime.pmm.core.models.PrimaryModel;
import de.bund.bfr.knime.pmm.core.models.SecondaryModel;
import de.bund.bfr.knime.pmm.core.models.Variable;
import de.bund.bfr.knime.pmm.core.models.VariableRange;

public class SecondaryEstimationThread implements Runnable {

	private SecondaryModel dataModel;

	private Map<String, Point2D.Double> guesses;

	private boolean enforceLimits;
	private int nParameterSpace;
	private int nLevenberg;
	private boolean stopWhenSuccessful;

	private AtomicInteger runningThreads;
	private AtomicInteger finishedThreads;

	private List<String> warnings;

	public SecondaryEstimationThread(SecondaryModel dataModel,
			Map<String, Point2D.Double> guesses, boolean enforceLimits,
			int nParameterSpace, int nLevenberg, boolean stopWhenSuccessful,
			AtomicInteger runningThreads, AtomicInteger finishedThreads) {
		this.dataModel = dataModel;
		this.guesses = guesses;
		this.enforceLimits = enforceLimits;
		this.nParameterSpace = nParameterSpace;
		this.nLevenberg = nLevenberg;
		this.stopWhenSuccessful = stopWhenSuccessful;
		this.runningThreads = runningThreads;
		this.finishedThreads = finishedThreads;

		warnings = new ArrayList<String>();
	}

	@Override
	public void run() {
		try {
			SecondaryModel newDataModel = EcoreUtil.copy(dataModel);

			CombineUtilities.applyAssignmentsAndConversion(newDataModel);

			String depVar = newDataModel.getModelFormula().getDepVar()
					.getName();
			List<Double> targetValues = new ArrayList<Double>();
			Map<String, List<Double>> argumentValues = new LinkedHashMap<String, List<Double>>();

			for (String param : Utilities.getConditions(newDataModel.getData())) {
				argumentValues.put(param, new ArrayList<Double>());
			}

			for (PrimaryModel data : newDataModel.getData()) {
				Double value = null;
				Double minValue = null;
				Double maxValue = null;
				boolean valueMissing = false;

				for (Parameter param : data.getModelFormula().getParams()) {
					if (param.getName().equals(depVar)) {
						value = data.getParamValues().get(param.getName())
								.getValue();
						minValue = param.getMin();
						maxValue = param.getMax();

						if (value == null) {
							valueMissing = true;
							break;
						}
					}
				}

				if (valueMissing) {
					continue;
				}

				if ((minValue != null && value < minValue)
						|| (maxValue != null && value > maxValue)) {
					warnings.add("Some primary parameters are out of their range of values");
				}

				targetValues.add(value);

				for (String arg : argumentValues.keySet()) {
					Double argValue = null;

					for (Condition cond : data.getData().getConditions()) {
						if (arg.equals(cond.getParameter().getName())) {
							argValue = cond.getValue();
							break;
						}
					}

					argumentValues.get(arg).add(argValue);
				}
			}

			String formula = newDataModel.getModelFormula().getFormula();
			List<String> parameters = new ArrayList<String>();
			List<Double> minParameterValues = new ArrayList<Double>();
			List<Double> maxParameterValues = new ArrayList<Double>();
			List<Double> minGuessValues = new ArrayList<Double>();
			List<Double> maxGuessValues = new ArrayList<Double>();

			for (Parameter param : newDataModel.getModelFormula().getParams()) {
				parameters.add(param.getName());
				minParameterValues.add(param.getMin());
				maxParameterValues.add(param.getMax());

				if (guesses != null && guesses.containsKey(param.getName())) {
					Point2D.Double guess = guesses.get(param.getName());

					if (!Double.isNaN(guess.x)) {
						minGuessValues.add(guess.x);
					} else {
						minGuessValues.add(null);
					}

					if (!Double.isNaN(guess.y)) {
						maxGuessValues.add(guess.y);
					} else {
						maxGuessValues.add(null);
					}
				} else {
					minGuessValues.add(param.getMin());
					maxGuessValues.add(param.getMax());
				}
			}

			Utilities.removeNullValues(targetValues, argumentValues);

			List<Double> parameterValues = Collections.nCopies(
					parameters.size(), null);
			List<Double> parameterErrors = Collections.nCopies(
					parameters.size(), null);
			List<Double> parameterTValues = Collections.nCopies(
					parameters.size(), null);
			List<Double> parameterPValues = Collections.nCopies(
					parameters.size(), null);
			List<List<Double>> covariances = new ArrayList<List<Double>>();

			for (int i = 0; i < parameters.size(); i++) {
				List<Double> nullList = Collections.nCopies(parameters.size(),
						null);

				covariances.add(nullList);
			}

			Double sse = null;
			Double mse = null;
			Double rmse = null;
			Double r2 = null;
			Double aic = null;
			Integer dof = null;
			List<Double> minValues = Collections.nCopies(argumentValues.size(),
					null);
			List<Double> maxValues = Collections.nCopies(argumentValues.size(),
					null);
			boolean successful = false;
			ParameterOptimizer optimizer = null;

			if (!targetValues.isEmpty()) {
				optimizer = new ParameterOptimizer(formula, parameters,
						minParameterValues, maxParameterValues, minGuessValues,
						maxGuessValues, targetValues, argumentValues,
						enforceLimits);
				optimizer.optimize(new AtomicInteger(), nParameterSpace,
						nLevenberg, stopWhenSuccessful);
				successful = optimizer.isSuccessful();
			}

			if (successful) {
				parameterValues = optimizer.getParameterValues();
				parameterErrors = optimizer.getParameterStandardErrors();
				parameterTValues = optimizer.getParameterTValues();
				parameterPValues = optimizer.getParameterPValues();
				covariances = optimizer.getCovariances();
				sse = optimizer.getSSE();
				mse = optimizer.getMSE();
				rmse = optimizer.getRMSE();
				r2 = optimizer.getR2();
				aic = optimizer.getAIC();
				dof = targetValues.size() - parameters.size();
				minValues = new ArrayList<Double>();
				maxValues = new ArrayList<Double>();

				for (List<Double> values : argumentValues.values()) {
					minValues.add(Collections.min(values));
					maxValues.add(Collections.max(values));
				}
			}

			for (int i = 0; i < dataModel.getModelFormula().getParams().size(); i++) {
				Parameter param = dataModel.getModelFormula().getParams()
						.get(i);
				ParameterValue paramValue = ModelsFactory.eINSTANCE
						.createParameterValue();

				paramValue.setValue(parameterValues.get(i));
				paramValue.setError(parameterErrors.get(i));
				paramValue.setT(parameterTValues.get(i));
				paramValue.setP(parameterPValues.get(i));

				for (int j = 0; j < dataModel.getModelFormula().getParams()
						.size(); j++) {
					Parameter otherParam = dataModel.getModelFormula()
							.getParams().get(j);

					paramValue.getCorrelations().put(otherParam.getName(),
							covariances.get(i).get(j));
				}

				dataModel.getParamValues().put(param.getName(), paramValue);
			}

			for (int i = 0; i < dataModel.getModelFormula().getIndepVars()
					.size(); i++) {
				Variable indep = dataModel.getModelFormula().getIndepVars()
						.get(i);
				VariableRange indepRange = ModelsFactory.eINSTANCE
						.createVariableRange();

				indepRange.setMin(minValues.get(i));
				indepRange.setMax(maxValues.get(i));
				dataModel.getVariableRanges().put(indep.getName(), indepRange);
			}

			dataModel.setId(Utilities.getRandomId());
			dataModel.setSse(sse);
			dataModel.setMse(mse);
			dataModel.setRmse(rmse);
			dataModel.setR2(r2);
			dataModel.setAic(aic);
			dataModel.setDegreesOfFreedom(dof);

			runningThreads.decrementAndGet();
			finishedThreads.incrementAndGet();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public List<String> getWarnings() {
		return warnings;
	}
}
