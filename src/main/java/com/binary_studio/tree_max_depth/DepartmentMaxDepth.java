package com.binary_studio.tree_max_depth;

import java.util.*;

public final class DepartmentMaxDepth {

	private DepartmentMaxDepth() {
	}

	public static Integer calculateMaxDepth(Department rootDepartment) {
		int allMaxDepth = 0;
		Deque<Optional<Department>> deq = new ArrayDeque<>();
		deq.push(Optional.ofNullable(rootDepartment));
		while (!deq.isEmpty()) {
			if (deq.peek().isPresent()) {
				if (!deq.peek().get().subDepartments.isEmpty()) {
					deq.push(Optional.ofNullable(deq.peek().get().subDepartments.remove(0)));
				}
				else {
					if (deq.size() > allMaxDepth) {
						allMaxDepth = deq.size();
					}
					deq.pollFirst();
				}
			}
			else {
				deq.pollFirst();
			}
		}
		return allMaxDepth;
	}

}
