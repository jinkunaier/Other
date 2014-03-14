/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package de.bund.bfr.knime.pmm.core.models.util;

import de.bund.bfr.knime.pmm.core.models.*;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

/**
 * <!-- begin-user-doc --> The <b>Switch</b> for the model's inheritance
 * hierarchy. It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object and proceeding up the
 * inheritance hierarchy until a non-null result is returned, which is the
 * result of the switch. <!-- end-user-doc -->
 * 
 * @see de.bund.bfr.knime.pmm.core.models.ModelsPackage
 * @generated
 */
public class ModelsSwitch<T> extends Switch<T> {
	/**
	 * The cached model package <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected static ModelsPackage modelPackage;

	/**
	 * Creates an instance of the switch. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public ModelsSwitch() {
		if (modelPackage == null) {
			modelPackage = ModelsPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @parameter ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns
	 * a non null result; it yields that result. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the first non-null result returned by a <code>caseXXX</code>
	 *         call.
	 * @generated
	 */
	@Override
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
		case ModelsPackage.MODEL: {
			Model model = (Model) theEObject;
			T result = caseModel(model);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelsPackage.MODEL_FORMULA: {
			ModelFormula modelFormula = (ModelFormula) theEObject;
			T result = caseModelFormula(modelFormula);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelsPackage.FORMULA_ELEMENT: {
			FormulaElement formulaElement = (FormulaElement) theEObject;
			T result = caseFormulaElement(formulaElement);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelsPackage.VARIABLE: {
			Variable variable = (Variable) theEObject;
			T result = caseVariable(variable);
			if (result == null)
				result = caseFormulaElement(variable);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelsPackage.VARIABLE_RANGE: {
			VariableRange variableRange = (VariableRange) theEObject;
			T result = caseVariableRange(variableRange);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelsPackage.PARAMETER: {
			Parameter parameter = (Parameter) theEObject;
			T result = caseParameter(parameter);
			if (result == null)
				result = caseFormulaElement(parameter);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelsPackage.PARAMETER_VALUE: {
			ParameterValue parameterValue = (ParameterValue) theEObject;
			T result = caseParameterValue(parameterValue);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelsPackage.PRIMARY_MODEL: {
			PrimaryModel primaryModel = (PrimaryModel) theEObject;
			T result = casePrimaryModel(primaryModel);
			if (result == null)
				result = caseModel(primaryModel);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelsPackage.SECONDARY_MODEL: {
			SecondaryModel secondaryModel = (SecondaryModel) theEObject;
			T result = caseSecondaryModel(secondaryModel);
			if (result == null)
				result = caseModel(secondaryModel);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelsPackage.TERTIARY_MODEL: {
			TertiaryModel tertiaryModel = (TertiaryModel) theEObject;
			T result = caseTertiaryModel(tertiaryModel);
			if (result == null)
				result = caseModel(tertiaryModel);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelsPackage.PRIMARY_MODEL_FORMULA: {
			PrimaryModelFormula primaryModelFormula = (PrimaryModelFormula) theEObject;
			T result = casePrimaryModelFormula(primaryModelFormula);
			if (result == null)
				result = caseModelFormula(primaryModelFormula);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelsPackage.SECONDARY_MODEL_FORMULA: {
			SecondaryModelFormula secondaryModelFormula = (SecondaryModelFormula) theEObject;
			T result = caseSecondaryModelFormula(secondaryModelFormula);
			if (result == null)
				result = caseModelFormula(secondaryModelFormula);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelsPackage.TERTIARY_MODEL_FORMULA: {
			TertiaryModelFormula tertiaryModelFormula = (TertiaryModelFormula) theEObject;
			T result = caseTertiaryModelFormula(tertiaryModelFormula);
			if (result == null)
				result = caseModelFormula(tertiaryModelFormula);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelsPackage.STRING_TO_STRING_MAP_ENTRY: {
			@SuppressWarnings("unchecked")
			Map.Entry<String, String> stringToStringMapEntry = (Map.Entry<String, String>) theEObject;
			T result = caseStringToStringMapEntry(stringToStringMapEntry);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelsPackage.STRING_TO_DOUBLE_MAP_ENTRY: {
			@SuppressWarnings("unchecked")
			Map.Entry<String, Double> stringToDoubleMapEntry = (Map.Entry<String, Double>) theEObject;
			T result = caseStringToDoubleMapEntry(stringToDoubleMapEntry);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelsPackage.STRING_TO_VARIABLE_RANGE_MAP_ENTRY: {
			@SuppressWarnings("unchecked")
			Map.Entry<String, VariableRange> stringToVariableRangeMapEntry = (Map.Entry<String, VariableRange>) theEObject;
			T result = caseStringToVariableRangeMapEntry(stringToVariableRangeMapEntry);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ModelsPackage.STRING_TO_PARAMETER_VALUE_MAP_ENTRY: {
			@SuppressWarnings("unchecked")
			Map.Entry<String, ParameterValue> stringToParameterValueMapEntry = (Map.Entry<String, ParameterValue>) theEObject;
			T result = caseStringToParameterValueMapEntry(stringToParameterValueMapEntry);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		default:
			return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Model</em>'. <!-- begin-user-doc --> This implementation returns
	 * null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModel(Model object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Model Formula</em>'. <!-- begin-user-doc --> This implementation
	 * returns null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Model Formula</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModelFormula(ModelFormula object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Formula Element</em>'. <!-- begin-user-doc --> This implementation
	 * returns null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Formula Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFormulaElement(FormulaElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Variable</em>'. <!-- begin-user-doc --> This implementation returns
	 * null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Variable</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseVariable(Variable object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Variable Range</em>'. <!-- begin-user-doc --> This implementation
	 * returns null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Variable Range</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseVariableRange(VariableRange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Parameter</em>'. <!-- begin-user-doc --> This implementation returns
	 * null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Parameter</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseParameter(Parameter object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Parameter Value</em>'. <!-- begin-user-doc --> This implementation
	 * returns null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Parameter Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseParameterValue(ParameterValue object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Primary Model</em>'. <!-- begin-user-doc --> This implementation
	 * returns null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Primary Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePrimaryModel(PrimaryModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Secondary Model</em>'. <!-- begin-user-doc --> This implementation
	 * returns null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Secondary Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSecondaryModel(SecondaryModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Tertiary Model</em>'. <!-- begin-user-doc --> This implementation
	 * returns null; returning a non-null result will terminate the switch. <!--
	 * end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Tertiary Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTertiaryModel(TertiaryModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Primary Model Formula</em>'. <!-- begin-user-doc --> This
	 * implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Primary Model Formula</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePrimaryModelFormula(PrimaryModelFormula object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Secondary Model Formula</em>'. <!-- begin-user-doc --> This
	 * implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Secondary Model Formula</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSecondaryModelFormula(SecondaryModelFormula object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>Tertiary Model Formula</em>'. <!-- begin-user-doc --> This
	 * implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>Tertiary Model Formula</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTertiaryModelFormula(TertiaryModelFormula object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>String To String Map Entry</em>'. <!-- begin-user-doc --> This
	 * implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>String To String Map Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseStringToStringMapEntry(Map.Entry<String, String> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>String To Double Map Entry</em>'. <!-- begin-user-doc --> This
	 * implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>String To Double Map Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseStringToDoubleMapEntry(Map.Entry<String, Double> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>String To Variable Range Map Entry</em>'. <!-- begin-user-doc -->
	 * This implementation returns null; returning a non-null result will
	 * terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>String To Variable Range Map Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseStringToVariableRangeMapEntry(
			Map.Entry<String, VariableRange> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>String To Parameter Value Map Entry</em>'. <!-- begin-user-doc -->
	 * This implementation returns null; returning a non-null result will
	 * terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>String To Parameter Value Map Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseStringToParameterValueMapEntry(
			Map.Entry<String, ParameterValue> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '
	 * <em>EObject</em>'. <!-- begin-user-doc --> This implementation returns
	 * null; returning a non-null result will terminate the switch, but this is
	 * the last case anyway. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '
	 *         <em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(EObject object) {
		return null;
	}

} // ModelsSwitch
