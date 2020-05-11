package com.binary_studio.tree_max_depth;

import java.util.*;

public final class DepartmentMaxDepth {

	private DepartmentMaxDepth() {
	}

	public static Integer calculateMaxDepth(Department rootDepartment) {
		int maxDepth = 0;
		int allMaxDepth = maxDepth;
		Deque<Optional<Department>> deq = new ArrayDeque<>();
		deq.push(Optional.ofNullable(rootDepartment));
		while (!deq.isEmpty()) {
			if (deq.peek().isPresent()) {
				maxDepth++;
				if (!deq.peek().get().subDepartments.isEmpty()) {
					deq.push(Optional.ofNullable(deq.peek().get().subDepartments.remove(0)));
				}
				else {
					if (maxDepth > allMaxDepth) {
						allMaxDepth = maxDepth;
					}
					maxDepth -= 2;
					deq.pollFirst();
				}
			}
			else {
				maxDepth--;
				deq.pollFirst();
			}
		}
		return allMaxDepth;
	}

}
