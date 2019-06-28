package ch.schaefer.flowable;

import org.flowable.variable.api.types.ValueFields;
import org.flowable.variable.api.types.VariableType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonType implements VariableType {

	private static final Logger LOGGER = LoggerFactory.getLogger(JsonType.class);

	protected ObjectMapper objectMapper;

	public JsonType(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public String getTypeName() {
		return "jsonObject";
	}

	@Override
	public boolean isCachable() {
		return true;
	}

	@Override
	public Object getValue(ValueFields valueFields) {
		Object objectValue = null;
		String textValue = valueFields.getTextValue();
		if (textValue != null && textValue.length() > 0) {
			try {
				objectValue = objectMapper.readValue(textValue, Object.class);
			} catch (Exception e) {
				LOGGER.error("Error reading json variable {}", valueFields.getName(), e);
			}
		}
		return objectValue;
	}

	@Override
	public void setValue(Object valueObject, ValueFields valueFields) {
		try {
			valueFields.setTextValue(valueObject != null ? objectMapper.writeValueAsString(valueObject) : null);
		} catch (Exception e) {
			LOGGER.error("Error writing json variable {}", valueFields.getName(), e);
		}
	}

	@Override
	public boolean isAbleToStore(Object value) {
		if (value == null) {
			return true;
		}
		if (value.getClass().isAnnotationPresent(JsonVariable.class)) {
			return true;
		}
		return false;
	}
}
