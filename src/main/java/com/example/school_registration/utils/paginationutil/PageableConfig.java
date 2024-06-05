/**
 * @author developers@Shared-Helpdesk
 *
 * 
 */
package com.example.smart_tourism.utils.paginationutil;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

/**
 * @author developers@PMO-Dashboard
 *
 */
@Service
public class PageableConfig {

	public PageRequest pageable(PageableParam pageableParam) {

		if (pageableParam == null) {
			pageableParam = new PageableParam("id", "DESC", 20, 0);
		}

		Direction direction = Direction.DESC;
		if (pageableParam.getSortDirection() != null && pageableParam.getSortDirection().contentEquals("ASC")) {
			direction = Direction.ASC;
		}
		if (pageableParam.getSortBy() == null || pageableParam.getSortBy().isEmpty()) {
			pageableParam.setSortBy("id");
		}
		int size = 20;
		int page = 0;

		if (pageableParam.getSize() != null && pageableParam.getSize() > 0) {
			size = pageableParam.getSize();
		}

		if (pageableParam.getFirst() != null && pageableParam.getFirst() >= 0) {
			page = pageableParam.getFirst();
		}

		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, pageableParam.getSortBy()));
		return pageRequest;
	}
}
