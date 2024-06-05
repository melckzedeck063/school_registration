package com.example.smart_tourism.utils.paginationutil;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author developers@PMO-Dashboard
 *
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchFieldsDto {

	public enum SearchOperationType {
		Equals, NotEquals, Like, LessThan, GreaterThan, In
	}

	private String fieldName;
	private Object fieldValue;
	private List<Object> fieldValues;

	@Enumerated(EnumType.STRING)
	private SearchOperationType searchType;
}
